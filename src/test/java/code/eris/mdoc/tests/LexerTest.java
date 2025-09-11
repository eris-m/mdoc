package code.eris.mdoc.tests;

import code.eris.mdoc.frontend.Lexer;
import code.eris.mdoc.frontend.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    static Stream<Arguments> testSingleSource() {
        return Stream.of(
                Arguments.of("$", Token.Kind.Dollar),
                Arguments.of("(", Token.Kind.OpenParenthesis),
                Arguments.of(")", Token.Kind.CloseParenthesis),
                Arguments.of("123456789", Token.Kind.Digit),
                Arguments.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", Token.Kind.Alpha),
                Arguments.of("\\", Token.Kind.Unknown),
                Arguments.of("\t \n\r\n  \t\t  ", Token.Kind.Whitespace),
                Arguments.of(".", Token.Kind.Period),
                Arguments.of(",", Token.Kind.Comma),
                Arguments.of("true", Token.Kind.True),
                Arguments.of("false", Token.Kind.False)
        );
    }

    @ParameterizedTest
    @MethodSource("testSingleSource")
    void testSingle(String input, Token.Kind token) {
        var lexer = new Lexer(input);
        List<Token> tokens = lexer.lexAll();

        assertAll(
                () -> assertEquals(1, tokens.size()),
                () -> assertEquals(new Token(token, input), tokens.getFirst())
        );
    }

    @Test
    public void testFloat() {
        var lexer = new Lexer("12345.54312");
        List<Token> tokens = lexer.lexAll();
        
        assertAll(
                () -> assertEquals(3, tokens.size()),
                () -> assertEquals(new Token(Token.Kind.Digit, "12345"), tokens.getFirst()),
                () -> assertEquals(new Token(Token.Kind.Period, "."), tokens.get(1)),
                () -> assertEquals(new Token(Token.Kind.Digit, "54312"), tokens.get(2))
        );
    }

    @Test
    public void testWords() {
        var lexer = new Lexer("Hello world\n\r\nline");
        List<Token> tokens = lexer.lexAll();

        assertAll(
                () -> assertEquals(5, tokens.size()),
                () -> assertEquals(new Token(Token.Kind.Alpha, "Hello"), tokens.getFirst()),
                () -> assertEquals(new Token(Token.Kind.Whitespace, " "), tokens.get(1)),
                () -> assertEquals(new Token(Token.Kind.Alpha, "world"), tokens.get(2)),
                () -> assertEquals(new Token(Token.Kind.Whitespace, "\n\r\n"), tokens.get(3)),
                () -> assertEquals(new Token(Token.Kind.Alpha, "line"), tokens.get(4))
        );
    }

    @Test
    public void testTrueFalse() {
        var lexer = new Lexer("truefalse");
        List<Token> tk = lexer.lexAll();
        assertAll(
                () -> assertEquals(1, tk.size()),
                () -> assertEquals(new Token(Token.Kind.Alpha, "truefalse"), tk.getFirst())
        );
    }
}
