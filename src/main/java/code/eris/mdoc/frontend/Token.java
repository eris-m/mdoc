package code.eris.mdoc.frontend;

public record Token(Kind kind, String contents) {
    public enum Kind {
        Unknown,
        Whitespace,
        Alpha,
        Digit,
        Period,
        Comma,
        Dollar,
        OpenParenthesis,
        CloseParenthesis,
        True,
        False,
    }

}
