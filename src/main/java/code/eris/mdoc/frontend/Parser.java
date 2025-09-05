package code.eris.mdoc.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import code.eris.mdoc.frontend.ast.BooleanValueExpr;
import code.eris.mdoc.frontend.ast.Expr;
import code.eris.mdoc.frontend.ast.NumberValueExpr;
import code.eris.mdoc.frontend.ast.TextExpr;

import java.util.function.Function;

import static code.eris.mdoc.frontend.Combinators.*;
import static java.lang.Math.*;

record ParserOutput<T>(List<Token> rest, T expr) {
}

interface ParserF<T> {
    ParserOutput<T> parse(List<Token> tokens) throws ParserException;
}

class Combinators {
    @SafeVarargs
    public static ParserF<Expr> parseOr(ParserF<Expr> ...parsers) {
        return (tks) -> {
            var orException = new ParserOrException();

            for (var parser : parsers) {
                try {
                    return  parser.parse(tks);
                } catch (ParserException ex) {
                    orException.addException(ex);
                }
            }

            throw orException;
        };
    }

    public static <T extends Expr> ParserF<Expr> exprParser(ParserF<T> parser) {
        return (tks) -> {
            ParserOutput<T> out = parser.parse(tks);
            return new ParserOutput<>(out.rest(), out.expr());
        };
    }

    static ParserF<Token> take(Predicate<Token> predicate, Function<Token, ParserException> error) {
        return (tks) -> {
            if (tks.isEmpty()) {
                throw new EndOfInputException();
            }

            Token tk = tks.getFirst();

            if (!predicate.test(tk)) {
                throw error.apply(tk);
            }

            return new ParserOutput<>(tks.subList(1, tks.size()), tk);
        };
    }

    static ParserF<List<Token>> takeWhile(Predicate<Token> predicate) {
        return (tks) -> {
            int i;
            for (i = 0; i < tks.size(); i++) {
                Token tk = tks.get(i);
                if (!predicate.test(tk))
                    break;
            }

            List<Token> taken = tks.subList(0, i);
            List<Token> rest = tks.subList(i, tks.size());

            return new ParserOutput<>(rest, taken);
        };
    }

    private static List<Token> skipWhitespace(List<Token> tks) {
        return takeWhile((tk) -> tk.kind() == Token.Kind.Whitespace).parse(tks).rest();
    }

    static <T> ParserF<T> whitespaceAround(ParserF<T> parser) throws ParserException {
        return (tks) -> {
            List<Token> tksWs = skipWhitespace(tks);
            ParserOutput<T> output = parser.parse(tksWs);

            List<Token> rest = skipWhitespace(output.rest());
            return new ParserOutput<>(rest, output.expr());
        };
    }
}

class ValueParsers {
    public static ParserOutput<TextExpr> parseText(List<Token> tokens) throws ParserException {
        if (tokens.isEmpty())
            throw new EndOfInputException();

        return new ParserOutput<>(tokens.subList(1, tokens.size()), new TextExpr(tokens.getFirst().toString()));
    }

    public static ParserF<Expr> textExprParser() {
        return exprParser(ValueParsers::parseText);
    }

    public static ParserOutput<NumberValueExpr> parseNumber(List<Token> tokens) throws ParserException {
        ParserF<Integer> digitsParser = (tks) -> {
            ParserOutput<List<Token>> digits =
                    whitespaceAround(takeWhile((tk) -> tk.kind() == Token.Kind.Digit))
                    .parse(tks);

            var builder = new StringBuilder();
            for (Token digit : digits.expr()) {
                builder.append(digit.contents());
            }

            try {
                int digit = Integer.parseInt(builder.toString());
                return new ParserOutput<>(digits.rest(), digit);
            } catch (NumberFormatException e) {
                throw new ParserException(e.getLocalizedMessage());
            }
        };

        ParserOutput<Integer> digitA = digitsParser.parse(tokens);
        try {
            tokens = Combinators.take(
                    (tk) -> tk.kind() == Token.Kind.Period,
                    (tk) -> new ExpectedTokensException(List.of(Token.Kind.Period), tk)
            ).parse(digitA.rest()).rest();
        } catch (ParserException ex) {
            var number = new NumberValueExpr(digitA.expr());
            return new ParserOutput<>(digitA.rest(), number);
        }

        try {
            ParserOutput<Integer> digitB = digitsParser.parse(tokens);
            tokens = digitB.rest();

            double bLength = ceil(log10(digitB.expr()));
            double b = (double)digitB.expr() / pow(10, bLength);

            var number = new NumberValueExpr(digitA.expr() + b);
            return new ParserOutput<>(tokens, number);
        } catch (ParserException _) {
            var number = new NumberValueExpr(digitA.expr());
            return new ParserOutput<>(digitA.rest(), number);
        }
    }

    public static ParserF<Expr> numberExprParser() {
        return exprParser(ValueParsers::parseNumber);
    }

    public static ParserOutput<BooleanValueExpr> parseBoolean(List<Token> tokens) throws ParserException {
        if (tokens.isEmpty())
            throw new EndOfInputException();

        Token.Kind kind = tokens.getFirst().kind();
        List<Token> rest = tokens.subList(1, tokens.size());

        if (kind == Token.Kind.True) {
            return new ParserOutput<>(rest, new BooleanValueExpr(true));
        }

        if (kind == Token.Kind.False) {
            return new ParserOutput<>(rest, new BooleanValueExpr(false));
        }

        throw new ExpectedTokensException(List.of(Token.Kind.True, Token.Kind.False), tokens.getFirst());
    }

    public static ParserF<Expr> booleanExprParser() {
        return exprParser(ValueParsers::parseBoolean);
    }
}

public class Parser {
    public static List<Expr> parse(Lexer lexer) throws ParserException {
        return parse(lexer.lexAll());
    }

    public static List<Expr> parse(List<Token> tokens) throws ParserException {
        var exprs = new ArrayList<Expr>();

        while(!tokens.isEmpty()) {
            ParserOutput<Expr> expr = parseNext(tokens);

            exprs.add(expr.expr());
            tokens = expr.rest();
        }

        return exprs;
    }

    private static ParserOutput<Expr> parseNext(List<Token> tokens) throws ParserException {
        return parseOr(
                ValueParsers.booleanExprParser(),
                ValueParsers.numberExprParser(),
                ValueParsers.textExprParser()
        ).parse(tokens);
    }
}
