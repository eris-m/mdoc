package code.eris.mdoc.frontend.ast;

import java.util.Objects;

public class StringValueExpr extends ValueExpr {
    public StringValueExpr(String value) {
        this.value = value;
    }

    @Override
    public boolean isTruthy() {
        return value.isBlank();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringValueExpr that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    private final String value;
}
