package calcmalc.logic;

import org.junit.Test;
import org.junit.ComparisonFailure;
import static org.junit.Assert.*;
import calcmalc.structures.Stack;
import calcmalc.structures.List;
import calcmalc.structures.Queue;
import calcmalc.logic.Parser;
import java.text.ParseException;

public class ParserTest {
    @Test
    public void testParseSimpleInput() throws ParseException {
        Stack<String> stack = new Stack<>(new List<String>());
        Queue<String> queue = new Queue<>(new List<String>());
        Parser parser = new Parser(stack, queue);

        parser.parse("1 + 1");

        assertEquals(parser.show(), "11+");
    }

    @Test
    public void testParseShortMultipleOperators() throws ParseException {
        Stack<String> stack = new Stack<>(new List<String>());
        Queue<String> queue = new Queue<>(new List<String>());
        Parser parser = new Parser(stack, queue);

        parser.parse("2 * 2 + 6 + 4 - 10 / 2");

        assertEquals(parser.show(), "22*6+4+102/-");
    }

    @Test
    public void testParseSimpleWithParens() throws ParseException {
        Stack<String> stack = new Stack<>(new List<String>());
        Queue<String> queue = new Queue<>(new List<String>());
        Parser parser = new Parser(stack, queue);

        parser.parse("2 * ( 2 + 2 )");

        assertEquals(parser.show(), "222+*");
    }

    @Test
    public void testParseManyOperatorsAndOneParen() throws ParseException{
        Stack<String> stack = new Stack<>(new List<String>());
        Queue<String> queue = new Queue<>(new List<String>());
        Parser parser = new Parser(stack, queue);

        parser.parse("2 * ( 2 + 2 + 6 + 8 - 10 ) / 3 * 2");

        assertEquals(parser.show(), "222+6+8+10-*3/2*");
    }

    @Test
    public void testDeeplyNestedParens() throws ParseException {
        Stack<String> stack = new Stack<>(new List<String>());
        Queue<String> queue = new Queue<>(new List<String>());
        Parser parser = new Parser(stack, queue);

        parser.parse("2 * ( 2 * ( 2 + 2 ) * ( 1 + ( 4 + 4 * ( 10 + 1 ) ) ) + ( 10 * ( 5 + ( 2 + 3 ) ) ) )");
        assertEquals(parser.show(), "2222+*144101+*++*10523++*+*");
    }

    @Test
    public void testParseExceptionWhenIllegalExpression() {
        Stack<String> stack = new Stack<>(new List<String>());
        Queue<String> queue = new Queue<>(new List<String>());
        Parser parser = new Parser(stack, queue);
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse("2 * ( ( 2 + 2 )");
        });
    }

}
