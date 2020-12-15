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
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isClosingParenthesis());
    }

    @Test
    public void testLexingSymbolsWithFloatArgs() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("sqrt(1.2123)");
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isClosingParenthesis());
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
    public void testLexingWhiteSpaceText() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("   100+    100*    1560\n\r    \n");
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
    }

    @Test
    public void testLexingComplicatedSymbolNames() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("_a_A__MAMW_AA_X__clz=2:");
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isAssignment());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isVariableDelimiter());
    }

    @Test
    public void testLexingComplicatedSymbolNamesIsCorrect() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("_a_A__MAMW_AA_X__clz");
        assertEquals("_a_A__MAMW_AA_X__clz", tokens.dequeue().getKey());
    }

    @Test 
    public void testLexingIncorrectNumbers() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("A__.__B");
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isSymbol());
    }

    @Test 
    public void testLexingUnaryMinus() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("x=-10:-2+(-4)+x");
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isAssignment());
        assertEquals("$", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isVariableDelimiter());
        assertEquals("$", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertEquals("$", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isClosingParenthesis());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isSymbol());
    }

    @Test 
    public void testLexingOperators() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("2+2*5/5-1^2%10");
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isOperator());
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
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isComma());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isClosingParenthesis());
    }

    @Test 
    public void testLexingVariableAssignments() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("x=2:");
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isAssignment());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isVariableDelimiter());
    }

    @Test
    public void testLexingFunctionsAndOperators() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("max(2,8)-min(8,12)");
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isComma());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isClosingParenthesis());
        assertTrue(tokens.dequeue().isOperator());
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isComma());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isClosingParenthesis());
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
    public void testLexingAndChaniningTypes() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("x=2:(max(x,5))");
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isAssignment());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isVariableDelimiter());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertTrue(tokens.dequeue().isFunction());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertTrue(tokens.dequeue().isSymbol());
        assertTrue(tokens.dequeue().isComma());
        assertTrue(tokens.dequeue().isNumber());
        assertTrue(tokens.dequeue().isClosingParenthesis());
        assertTrue(tokens.dequeue().isClosingParenthesis());
    }

    @Test
    public void testLexingAndChaniningTypes2() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("__xYh=2:(max(x__X,5.123123))");
        assertEquals("__xYh", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isAssignment());
        assertEquals("2", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isVariableDelimiter());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertEquals("max", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isOpenParenthesis());
        assertEquals("x__X", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isComma());
        assertEquals("5.123123", tokens.dequeue().getKey());
        assertTrue(tokens.dequeue().isClosingParenthesis());
        assertTrue(tokens.dequeue().isClosingParenthesis());
    }

    @Test
    public void testLexThrowsOnUnknownCharacter() {
        Lexer lexer = new Lexer();
        Exception exception = assertThrows(LexerException.class, () -> {
            Queue<Token> tokens = lexer.lex("2@2");
        });
    }

    @Test
    public void testLexThrowsOnTooLongContinousSequence() {
        Lexer lexer = new Lexer();
        Exception exception = assertThrows(LexerException.class, () -> {
            Queue<Token> tokens = lexer.lex("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        });
    }

    @Test
    public void testLexAllows64CharactersInAContinuousSequence() throws LexerException {
        Lexer lexer = new Lexer();
        Queue<Token> tokens = lexer.lex("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", tokens.dequeue().getKey());
    }
}
