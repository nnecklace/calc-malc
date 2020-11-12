package calcmalc.logic;

import org.junit.Test;
import static org.junit.Assert.*;
import calcmalc.structures.Listable;
import calcmalc.logic.types.Token;
import java.text.ParseException;

public class LexerTest {
    @Test
    public void testLexingForSimpleInput() throws ParseException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("1+1");
        assertTrue(tokens.get(0).isNumber());
        assertTrue(tokens.get(1).isOperator());
        assertTrue(tokens.get(2).isNumber());
    }

    @Test
    public void testLexingSymbols() throws ParseException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("sqrt(1)");
        assertTrue(tokens.get(0).isFunction());
        assertTrue(tokens.get(1).isEmpty());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isEmpty());
    }

    @Test 
    public void testLexingMultiDigitNumber() throws ParseException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("100+100*1560");
        assertTrue(tokens.get(0).isNumber());
        assertTrue(tokens.get(1).isOperator());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isOperator());
        assertTrue(tokens.get(4).isNumber());
    }

    @Test 
    public void testLexingOperators() throws ParseException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("2+2*5/5-1");
        assertTrue(tokens.get(0).isNumber());
        assertTrue(tokens.get(1).isOperator());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isOperator());
        assertTrue(tokens.get(4).isNumber());
        assertTrue(tokens.get(5).isOperator());
        assertTrue(tokens.get(6).isNumber());
        assertTrue(tokens.get(7).isOperator());
        assertTrue(tokens.get(8).isNumber());
    }

    @Test 
    public void testLexingFunctionArguments() throws ParseException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("max(2,3)");
        assertTrue(tokens.get(0).isFunction());
        assertTrue(tokens.get(1).isEmpty());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isEmpty());
        assertTrue(tokens.get(4).isNumber());
        assertTrue(tokens.get(5).isEmpty());
    }

    @Test
    public void testLexThrowsOnUnknownCharacter() {
        Lexer lexer = new Lexer();
        Exception exception = assertThrows(ParseException.class, () -> {
            Listable<Token> tokens = lexer.lex("2:2");
        });
    }
}
