package calcmalc.logic;

import org.junit.Test;
import static org.junit.Assert.*;
import calcmalc.structures.Listable;
import calcmalc.exceptions.LexerException;
import calcmalc.logic.types.Token;
import java.text.ParseException;

public class LexerTest {
    @Test
    public void testLexingForSimpleInput() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("1+1");
        assertTrue(tokens.get(0).isNumber());
        assertTrue(tokens.get(1).isOperator());
        assertTrue(tokens.get(2).isNumber());
    }

    @Test
    public void testLexingSymbols() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("sqrt(1)");
        assertTrue(tokens.get(0).isSymbol());
        assertTrue(tokens.get(1).isEmpty());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isEmpty());
    }

    @Test 
    public void testLexingMultiDigitNumber() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("100+100*1560");
        assertTrue(tokens.get(0).isNumber());
        assertTrue(tokens.get(1).isOperator());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isOperator());
        assertTrue(tokens.get(4).isNumber());
    }

    @Test 
    public void testLexingOperators() throws ParseException, LexerException {
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
    public void testLexingFunctionArguments() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("max(2,3)");
        assertTrue(tokens.get(0).isSymbol());
        assertTrue(tokens.get(1).isEmpty());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isEmpty());
        assertTrue(tokens.get(4).isNumber());
        assertTrue(tokens.get(5).isEmpty());
    }

    @Test 
    public void testLexingVariableAssignments() throws LexerException {
        Lexer lexer = new Lexer();
        Listable<Token> tokens = lexer.lex("x=2:");
        assertTrue(tokens.get(0).isSymbol());
        assertTrue(tokens.get(1).isAssignment());
        assertTrue(tokens.get(2).isNumber());
        assertTrue(tokens.get(3).isEmpty());
    }

    @Test
    public void testLexThrowsOnUnknownCharacter() {
        Lexer lexer = new Lexer();
        Exception exception = assertThrows(LexerException.class, () -> {
            Listable<Token> tokens = lexer.lex("2@2");
        });
    }
}
