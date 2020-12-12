package calcmalc.logic;

import org.junit.Test;
import static org.junit.Assert.*;
import calcmalc.structures.Queue;
import calcmalc.exceptions.LexerException;
import calcmalc.logic.types.Token;

public class LexerTest {
    @Test
    public void testLexingForSimpleInput() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("1+1");
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
    }

    @Test
    public void testLexingFloatValues() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("1.5+1.2525");
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
        assertNull(tokens.dequeue());
    }

    @Test
    public void testLexingSymbols() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("sqrt(1)");
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
    }

    @Test
    public void testLexingSymbolsWithFloatArgs() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("sqrt(1.2123)");
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
    }

    @Test 
    public void testLexingMultiDigitNumber() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("100+100*1560");
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
    }

    @Test 
    public void testLexingOperators() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("2+2*5/5-1");
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
    }

    @Test 
    public void testLexingFunctionArguments() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("max(2,3)");
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
    }

    @Test 
    public void testLexingVariableAssignments() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("x=2:");
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isAssignment());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
    }

    @Test
    public void testLexingFunctionsAndOperators() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("max(2,8)-min(8,12)");
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
    }

    @Test 
    public void testLexingLongVariableChain() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("a=1:b=2:c=3:d=4:e=5:f=6:g=7:h=8:i=9:");
        assertEquals("a", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("b", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("c", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("d", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("e", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("f", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("g", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("h", tokens.dequeue().getKey());
        tokens.dequeue();
        tokens.dequeue();
        tokens.dequeue();
        assertEquals("i", tokens.dequeue().getKey());
    }

    @Test
    public void testLexingAndChangingTypes() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("x=2:(max(x,5))");
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isAssignment());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isEmpty());
        assertTrue(tokens.dequeue().isEmpty());
    }

    @Test
    public void testLexThrowsOnUnknownCharacter() {
        Lexer lexer = new Lexer();
        Exception exception = assertThrows(LexerException.class, () -> {
            Queue<Token> tokens = lexer.lex("2@2");
        });
    }
}
