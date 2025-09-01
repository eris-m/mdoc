package code.eris.mdoc.frontend.ast;

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

    private final double value;
}
