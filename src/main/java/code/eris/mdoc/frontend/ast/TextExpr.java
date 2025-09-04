package code.eris.mdoc.frontend.ast;

public class TextExpr extends Expr {
    public TextExpr(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    private String text;
}
