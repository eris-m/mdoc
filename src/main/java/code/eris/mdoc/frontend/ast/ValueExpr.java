package code.eris.mdoc.frontend.ast;

public abstract class ValueExpr extends Expr {
    public abstract boolean isTruthy();
    public boolean isFalsey() {
        return !isTruthy();
    }
}
