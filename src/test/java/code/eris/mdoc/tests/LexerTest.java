package code.eris.mdoc.tests;

import code.eris.mdoc.frontend.Lexer;
import code.eris.mdoc.frontend.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    @Test
    public void testDollar() {
        var lexer = new Lexer("$hello");
        Token tk = lexer.lexNextToken();
        assertEquals(new Token(Token.Kind.Dollar, "$"), tk);
    }

    @Test
    public void testParenthesis() {
        var lexer = new Lexer("()");
        List<Token> tokens = lexer.lexAll();

        assertAll(
                () -> assertEquals(2, tokens.size()),
                () -> assertEquals(new Token(Token.Kind.OpenParenthesis, "("), tokens.getFirst()),
                () -> assertEquals(new Token(Token.Kind.CloseParenthesis, ")"), tokens.get(1)
        )
        );
        
    }
    
    @Test
    public void testInteger() {
        var lexer = new Lexer("12345");
        Token token = lexer.lexNextToken();
        
        assertEquals(new Token(Token.Kind.Digit, "12345"), token);
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
    public void testAlpha() {
        var lexer = new Lexer("abc");
        Token token = lexer.lexNextToken();
        
        assertEquals(new Token(Token.Kind.Alpha, "abc"), token);
    }
    
    @Test
    public void testUnknown() {
        var lexer = new Lexer("::<><>''_'-'_");
        
        List<Token> tks = lexer.lexAll();
        
        for (var tk : tks) {
            assertEquals(Token.Kind.Unknown, tk.kind());
        }
    }

    @Test
    public void testWhitespace() {
        String str = "\t  \t\n\r\n";
        var lexer = new Lexer(str);
        List<Token> tokens = lexer.lexAll();

        assertAll(
                () -> assertEquals(1, tokens.size()),
                () -> assertEquals(new Token(Token.Kind.Whitespace, str), tokens.getFirst())
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
    public void testTrueSolo() {
        var trueLexer = new Lexer("true");
        Token tk = trueLexer.lexNextToken();

        assertEquals(new Token(Token.Kind.True, "true"), tk);
    }

    @Test
    public void testFalseSolo() {
        var falseLexer = new Lexer("false");
        Token tk = falseLexer.lexNextToken();

        assertEquals(new Token(Token.Kind.False, "false"), tk);
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
