package code.eris.mdoc.frontend.ast;

import java.util.Objects;

public class NumberValueExpr extends ValueExpr {
    public NumberValueExpr(double value) {
        this.value = value;
    }

    public NumberValueExpr(long l) {
        this((double)l);
    }

    @Override
    public boolean isTruthy() {
        return Math.abs(value) >= 1.0;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NumberValueExpr that)) return false;
        return Double.compare(value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    private final double value;
}
