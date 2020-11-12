package calcmalc.logic;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;

public class ParserTest {
    @Test
    public void testParseSimpleInput() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("1+1"));

        assertEquals("11+", parser.printTree());
    }

    @Test
    public void testParseShortMultipleOperators() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();

        parser.parse(lexer.lex("2*2+6+4-10/2"));

        assertEquals("22*6+4+102/-", parser.printTree());
    }

    @Test
    public void testParseSimpleWithParens() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("2*(2+2)"));

        assertEquals("222+*", parser.printTree());
    }

    @Test
    public void testParseManyOperatorsAndOneParen() throws ParseException{
        Parser parser = new Parser();
        Lexer lexer = new Lexer();

        parser.parse(lexer.lex("2*(2+2+6+8-10)/3*2"));
        assertEquals("222+6+8+10-*3/2*", parser.printTree());
    }

    @Test
    public void testDeeplyNestedParens() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3))))"));
        assertEquals("2222+*144101+*++*10523++*+*", parser.printTree());
    }

    @Test
    public void testFunctionParsing() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("sin(2+2)"));
        assertEquals("22+sin", parser.printTree());
    }

    @Test
    public void testFunctionWithInnerFunctionsParsing() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("sin(max(min(2,5),sqrt(1))+2+2)"));
        assertEquals("1sqrt52minmax2+2+sin", parser.printTree());
    }

    @Test
    public void testParseSimpleParens() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("(2+2)"));
        assertEquals("22+", parser.printTree());
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("2*((2+2)"));
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression2() throws ParseException {
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        parser.parse(lexer.lex("(2+2))"));
    }

}
