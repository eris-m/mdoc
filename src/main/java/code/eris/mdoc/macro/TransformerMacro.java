package code.eris.mdoc.macro;

import code.eris.mdoc.backend.Element;
import code.eris.mdoc.frontend.ast.Expr;

import java.util.List;

public interface TransformerMacro {
    Element transform(List<Expr> arguments);
}
