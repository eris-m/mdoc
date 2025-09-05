package code.eris.mdoc.frontend.ast;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BooleanValueExpr that)) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    private final boolean value;
}
