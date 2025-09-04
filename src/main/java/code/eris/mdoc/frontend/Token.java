package code.eris.mdoc.frontend;

public record Token(Kind kind, String contents) {
    public enum Kind {
        Unknown,
        Whitespace,
        Alpha,
        Digit,
        Period,
        Dollar,
        OpenParenthesis,
        CloseParenthesis,
        True,
        False,
    }

}
