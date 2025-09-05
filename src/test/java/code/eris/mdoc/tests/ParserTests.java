package code.eris.mdoc.tests;

import code.eris.mdoc.frontend.Parser;
import code.eris.mdoc.frontend.Token;
import code.eris.mdoc.frontend.ast.BooleanValueExpr;
import code.eris.mdoc.frontend.ast.Expr;
import code.eris.mdoc.frontend.ast.NumberValueExpr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.stream.Stream;

public class ParserTests {
    static Stream<Arguments> testNumberArguments() {
        return Stream.of(
                Arguments.of(
                        List.of(new Token(Token.Kind.Digit, "12345")),
                        1,
                        0,
                        12345.0
                ),
                Arguments.of(
                        List.of(
                                new Token(Token.Kind.Digit, "12345"),
                                new Token(Token.Kind.Period, "."),
                                new Token(Token.Kind.Digit, "51423")
                        ),
                        1,
                        0,
                        12345.51423
                ),
                Arguments.of(
                        List.of(
                                new Token(Token.Kind.Alpha, "Hello!"),
                                new Token(Token.Kind.Digit, "5")
                        ),
                        2,
                        1,
                        5.0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testNumberArguments")
    void testNumber(List<Token> tokens, int size, int pos, double expected) {
        List<Expr> exprs = Parser.parse(tokens);

        assertAll(
                () -> assertEquals(size, exprs.size()),
                () -> assertEquals(expected, ((NumberValueExpr)exprs.get(pos)).getValue(), 0.005)
        );
    }

    @Test
    void testBooleanTrue() {
        List<Token> tokens = List.of(
                new Token(Token.Kind.True, "true")
        );
        List<Expr> exprs = Parser.parse(tokens);

        assertAll(
                () -> assertEquals(1, exprs.size()),
                () -> assertEquals(new BooleanValueExpr(true), exprs.getFirst())
        );
    }

    @Test
    void testBooleanFalse() {
        List<Token> tokens = List.of(
                new Token(Token.Kind.False, "false")
        );
        List<Expr> exprs = Parser.parse(tokens);

        assertAll(
                () -> assertEquals(1, exprs.size()),
                () -> assertEquals(new BooleanValueExpr(false), exprs.getFirst())
        );
    }
}
