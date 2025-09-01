package code.eris.mdoc.macro;

//import code.eris.mdoc.fron

import code.eris.mdoc.frontend.ast.Expr;

import java.util.List;

public interface FrontendMacro {
    Expr transform(List<Expr> arguments);
}
