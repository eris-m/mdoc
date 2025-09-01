package code.eris.mdoc.tests;

import code.eris.mdoc.frontend.ast.BooleanValueExpr;
import code.eris.mdoc.frontend.ast.NumberValueExpr;
import code.eris.mdoc.frontend.ast.ValueExpr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExprTests {
    static Stream<Arguments> truthyProvider() {
        return Stream.of(
                Arguments.of(new BooleanValueExpr(false), false),
                Arguments.of(new BooleanValueExpr(true), true),
                Arguments.of(new NumberValueExpr(0), false),
                Arguments.of(new NumberValueExpr(1), true),
                Arguments.of(new NumberValueExpr(100_000), true)
        );
    }

    @ParameterizedTest
    @MethodSource("truthyProvider")
    void testTruthy(ValueExpr expr, boolean expected) {
        boolean isTruthy = expr.isTruthy();

        assertEquals(expected, isTruthy);
    }
}
