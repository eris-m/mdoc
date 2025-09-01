package code.eris.mdoc.frontend.ast;

import java.util.List;

public class MacroExpr extends Expr {
    public MacroExpr(String name, List<Expr> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<Expr> getArgs() {
        return args;
    }

    private String name;
    private List<Expr> args;
}
