package code.eris.mdoc.frontend;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A lexer, turns a string into a list of tokens.

 * If you just need to get the token list, use {@link #lexString(String)}
 */
public class Lexer {
    public Lexer() {
        this("");
    }

    public Lexer(String source) {
        this.source = source.toCharArray();
    }

    /**
     * Lexes a string and returns a list of tokens.

     * Shorthand for {@code new Lexer(str).lexAll()}.

     * @param str The string to lex.
     * @return List of tokens.
     */
    public static List<Token> lexString(String str) {
        return new Lexer(str).lexAll();
    }

    /**
     * Lexes the next token in the lexer's source.

     * Advances the lexer's index so that the next call to {@code lexNextToken}
     * returns the token after that.
     */
    public Token lexNextToken() {
        if (isFinished())
            return null;

        switch (peekChar()) {
            case '$':
                return nextCharToken(Token.Kind.Dollar);
            case '(':
                return nextCharToken(Token.Kind.OpenParenthesis);
            case ')':
                return nextCharToken(Token.Kind.CloseParenthesis);
            case '.':
                return nextCharToken(Token.Kind.Period);
            default:
                Token tk = lexBoolean();
                if (tk != null)
                    return tk;

                tk = lexAlpha();
                if (tk != null)
                    return tk;

                tk = lexWhitespace();
                if (tk != null)
                    return tk;

                tk = lexDigits();
                if (tk != null)
                    return tk;

                return nextCharToken(Token.Kind.Unknown);
        }
    }

    /**
     * Lexes all the tokens remaining in the lexer's source.
     * @return A list of the remaining tokens.
     */
    public List<Token> lexAll() {
        var tokens = new ArrayList<Token>();

        while (index < source.length) {
            Token tk = lexNextToken();
            if (tk == null)
                break;

            tokens.add(tk);
        }

        return tokens;
    }

    public boolean isFinished() {
        return index >= source.length;
    }

    private Token lexBoolean() {
        Token tk = lexKeyword(Token.Kind.True, "true");
        if (tk != null) {
            return tk;
        }

        tk = lexKeyword(Token.Kind.False, "false");
        return tk;
    }

    private Token lexAlpha() {
        return lexWhile(Token.Kind.Alpha, Character::isAlphabetic);
    }

    private Token lexWhitespace() {
        return lexWhile(Token.Kind.Whitespace, Character::isWhitespace);
    }

    private Token lexDigits() {
        return lexWhile(Token.Kind.Digit, Character::isDigit);
    }

    private Token lexKeyword(Token.Kind kind, String keyword) {
        if (!isLastSeparating()) {
            return null;
        }

        if (index + keyword.length() > source.length) {
            return null;
        }

        int i;
        for (i = 0; i < keyword.length(); i++) {
            if (source[i + index] != keyword.charAt(i)) {
                return null;
            }
        }

        index += i;
        if (!isCurrentSeparating()) {
            index -= i;
            return null;
        }

        return new Token(kind, keyword);
    }

    private Token lexWhile(Token.Kind kind, Predicate<Character> predicate) {
        int i;

        for (i = index; i < source.length; i++) {
            char curr = source[i];
            if (!predicate.test(curr)) {
                break;
            }
        }

        if (i == index) {
            return null;
        }

        String substr = new String(source, index, i - index);
        index = i;

        return new Token(kind, substr);
    }

    private char peekChar() {
        return source[index];
    }

    private char nextChar() {
        return source[index++];
    }

    private Token nextCharToken(Token.Kind kind) {
        char ch = nextChar();
        return new Token(kind, Character.toString(ch));
    }

    private boolean isCurrentSeparating() {
        if (isFinished())
            return true;

        char ch = source[index];
        return switch (ch) {
            case '(', ')' -> true;
            default -> Character.isWhitespace(ch);
        };
    }

    private boolean isLastSeparating() {
        if (index == 0)
            return true;

        char ch = source[index - 1];
        return switch (ch) {
            case '(', ')' -> true;
            default -> Character.isWhitespace(ch);
        };
    }

    private int index = 0;
    private char[] source;
}