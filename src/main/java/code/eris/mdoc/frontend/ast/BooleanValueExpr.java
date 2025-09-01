package code.eris.mdoc.frontend.ast;

public class BooleanValueExpr extends ValueExpr {
    public BooleanValueExpr(boolean value) {
        this.value = value;
    }

    @Override
    public boolean isTruthy() {
        return value;
    }

    public boolean isValue() {
        return value;
    }

    private final boolean value;
}
