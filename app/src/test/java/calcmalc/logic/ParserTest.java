package calcmalc.logic;

import org.junit.Test;

import calcmalc.exceptions.LexerException;
import calcmalc.logic.types.Token;
import calcmalc.structures.Queue;
import static org.junit.Assert.*;
import java.text.ParseException;

public class ParserTest {
    @Test
    public void testParseSimpleInput() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("1+1")));
        parser.parse();

        assertEquals("11+", parser.printTree());
    }

    @Test
    public void testParseShortMultipleOperators() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*2+6+4-10/2")));

        parser.parse();

        assertEquals("22*6+4+102/-", parser.printTree());
    }

    @Test
    public void testParseSimpleWithParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2+2)")));
        parser.parse();

        assertEquals("222+*", parser.printTree());
    }

    @Test
    public void testParseManyOperatorsAndOneParen() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2+2+6+8-10)/3*2")));

        parser.parse();
        assertEquals("222+6+8+10-*3/2*", parser.printTree());
    }

    @Test
    public void testDeeplyNestedParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3))))")));
        parser.parse();
        assertEquals("2222+*144101+*++*10523++*+*", parser.printTree());
    }

    @Test
    public void testFunctionParsing() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sin(2+2)")));
        parser.parse();
        assertEquals("22+sin", parser.printTree());
    }

    @Test
    public void testFunctionWithInnerFunctionsParsing() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sin(max(min(2,5),sqrt(1))+2+2)")));
        parser.parse();
        assertEquals("1sqrt52minmax2+2+sin", parser.printTree());
    }

    @Test
    public void testParseSimpleParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(2+2)")));
        parser.parse();
        assertEquals("22+", parser.printTree());
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*((2+2)")));
        parser.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(2+2))")));
        parser.parse();
    }

    @Test
    public void testParseNegativeNumbersSimple() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("-2")));
        parser.parse();
        assertEquals("2$", parser.printTree());
    }

    @Test
    public void testParseNegativeNumbersExpression() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("-2+4")));
        parser.parse();
        assertEquals("2$4+", parser.printTree());
    }

    @Test
    public void testParseNegativeNumbersExpressionWithParenthesis() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(-10)")));
        parser.parse();
        assertEquals("210$*", parser.printTree());
    }

    @Test
    public void testParseNegativeNumbersExpressionWithParenthesisComplicated() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(-10)/4")));
        parser.parse();
        assertEquals("210$*4/", parser.printTree());
    }
}
