package calcmalc.logic;

import org.junit.Test;
import calcmalc.logic.types.Token;
import calcmalc.structures.Queue;
import static org.junit.Assert.*;
import java.text.ParseException;

public class ParserTest {
    @Test
    public void testParseSimpleInput() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("1+1")));
        parser.parse();

        assertEquals("11+", parser.printTree());
    }

    @Test
    public void testParseShortMultipleOperators() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*2+6+4-10/2")));

        parser.parse();

        assertEquals("22*6+4+102/-", parser.printTree());
    }

    @Test
    public void testParseSimpleWithParens() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2+2)")));
        parser.parse();

        assertEquals("222+*", parser.printTree());
    }

    @Test
    public void testParseManyOperatorsAndOneParen() throws ParseException{
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2+2+6+8-10)/3*2")));

        parser.parse();
        assertEquals("222+6+8+10-*3/2*", parser.printTree());
    }

    @Test
    public void testDeeplyNestedParens() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3))))")));
        parser.parse();
        assertEquals("2222+*144101+*++*10523++*+*", parser.printTree());
    }

    @Test
    public void testFunctionParsing() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sin(2+2)")));
        parser.parse();
        assertEquals("22+sin", parser.printTree());
    }

    @Test
    public void testFunctionWithInnerFunctionsParsing() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sin(max(min(2,5),sqrt(1))+2+2)")));
        parser.parse();
        assertEquals("1sqrt52minmax2+2+sin", parser.printTree());
    }

    @Test
    public void testParseSimpleParens() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(2+2)")));
        parser.parse();
        assertEquals("22+", parser.printTree());
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*((2+2)")));
        parser.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression2() throws ParseException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(2+2))")));
        parser.parse();
    }

}
