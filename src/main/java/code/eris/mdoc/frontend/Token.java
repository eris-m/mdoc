package code.eris.mdoc.frontend;

public record Token(Kind kind, String contents) {
    public enum Kind {
        Unknown,
        Alpha,
        Digit,
        Period,
        Dollar,
        OpenParenthesis,
        CloseParenthesis
    }

}
