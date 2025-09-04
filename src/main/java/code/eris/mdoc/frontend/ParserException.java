package code.eris.mdoc.frontend;

import java.util.ArrayList;
import java.util.List;

public class ParserException extends RuntimeException {
    public ParserException(String message) {
        super(message);
    }
}

class ExpectedTokensException extends ParserException {
    public ExpectedTokensException(List<Token.Kind> expected, Token got) {
        super(formatMessage(expected, got));

        this.expected = expected;
        this.got = got;
    }

    private static String formatMessage(List<Token.Kind> expected, Token got) {
        return switch (expected.size()) {
            case 0 -> String.format("Unexpected token %s", got);
            case 1 -> String.format("Expected token %s, got %s", expected.getFirst(), got);
            default -> String.format("Expected one of %s, got token %s", expected, got);
        };
    }

    private List<Token.Kind> expected;
    private Token got;
}

class EndOfInputException extends ParserException {
    public EndOfInputException() {
        super("Unexpected end of input");
    }
}

class ParserOrException extends ParserException {
    public ParserOrException() {
        super("");
        this.exceptions = new ArrayList<>();
    }

    public void addException(ParserException ex) {
        exceptions.add(ex);
    }

    @Override
    public String getMessage() {
        var builder = new StringBuilder();
        for (var ex : exceptions) {
            builder.append(ex.getMessage());
        }

        return String.format("Failed to parse rules: %s", builder);
    }

    private ArrayList<ParserException> exceptions;
}
