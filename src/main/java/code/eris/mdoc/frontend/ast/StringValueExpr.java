package code.eris.mdoc.frontend.ast;

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

    private final String value;
}
