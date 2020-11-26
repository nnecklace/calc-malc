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
    public void testParseSimpleInputFloats() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("1.123+1.999")));
        parser.parse();

        assertEquals("1.1231.999+", parser.printTree());
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
    public void testParseSimpleWithParensAndFloats() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2.9*(2.9999+2.123123)")));
        parser.parse();

        assertEquals("2.92.99992.123123+*", parser.printTree());
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
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2*(2+2)*(1+(4+4*(10.919+1)))+(10*(5+(2+3))))")));
        parser.parse();
        assertEquals("2222+*14410.9191+*++*10523++*+*", parser.printTree());
    }

    @Test
    public void testFunctionParsing() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sin(2+2)")));
        parser.parse();
        assertEquals("22+sin", parser.printTree());
    }

    @Test
    public void testFunctionWithInnerFunctionsParsingSimple() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sin(max(2,4))")));
        parser.parse();
        assertEquals("42maxsin", parser.printTree());
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

    @Test
    public void testParseNegativeNumberSum() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("-100+(-100)")));
        parser.parse();
        assertEquals("100$100$+", parser.printTree());
    }

    @Test
    public void testParseVariableAssignment() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:")));
        parser.parse();
        assertEquals("x2=", parser.printTree());
    }

    @Test
    public void testParseVariableAssignmentAndExpression() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:2+2")));
        parser.parse();
        assertEquals("x2=22+", parser.printTree());
    }

    @Test
    public void testParseExpressionInVariable() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2+2:")));
        parser.parse();
        assertEquals("x22+=", parser.printTree());
    }

    @Test
    public void testParseComplicatedExpressionInVariable() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3)))):")));
        parser.parse();
        assertEquals("x2222+*144101+*++*10523++*+*=", parser.printTree());
    }

    @Test
    public void testParseFunctionExpressionInVariable() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=sin(2+2):")));
        parser.parse();
        assertEquals("x22+sin=", parser.printTree());
    }

    @Test
    public void testParseMultipleVariables() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:y=2:z=4")));
        parser.parse();
        assertEquals("x2=y2=z4=", parser.printTree());
    }

    @Test
    public void testParseMultipleVariablesAndUse() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:y=2:2+x+y")));
        parser.parse();
        assertEquals("x2=y2=2x+y+", parser.printTree());
    }

    @Test
    public void testParseMultipleVariablesAndUse2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("a=1:b=2:c=3:d=4:e=5:f=6:g=7:a+b+c+d+e+f+g")));
        parser.parse();
        assertEquals("a1=b2=c3=d4=e5=f6=g7=ab+c+d+e+f+g+", parser.printTree());
    }

    @Test
    public void testParseMultipleVariablesAndUse3() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("a=1:b=2:c=5:d=8:(b+b)*(c-a+d)")));
        parser.parse();
        assertEquals("a1=b2=c5=d8=bb+ca-d+*", parser.printTree());
    }

    @Test
    public void testParseVariableAsFunctionParameter() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:sqrt(x)")));
        parser.parse();
        assertEquals("x2=xsqrt", parser.printTree());
    }

    @Test
    public void testParseAbsFunctionWithNegativeSum() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("abs(-100+(-100))")));
        parser.parse();
        assertEquals("100$100$+abs", parser.printTree());
    }

    @Test
    public void testParseThrowsExceptionOnMisMatchedParenthesis() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("())")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseThrowsExceptionOnMisMatchedParenthesis2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(,):)")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseThrowsExceptionOnMisMatchedParenthesis3() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("+,:)")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseThrowsExceptionOnIllegalAssignment() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2=5:")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseThrowsExceptionOnIllegalAssignment2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("+=5:")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseInputOfParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("()")));
        parser.parse();
        assertEquals("", parser.printTree());
    }

    @Test
    public void testParseInputOfComaAndStop() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex(",:")));
        parser.parse();
        assertEquals("", parser.printTree());
    }

    @Test
    public void testParseInputOfOnlyEmptyTokens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(,):")));
        parser.parse();
        assertEquals("", parser.printTree());
    }

    @Test
    public void testParseInputWithSingleOperator() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("+")));
        parser.parse();
        assertEquals("+", parser.printTree());
    }

    @Test
    public void testParseInputWithSingleOperatorAndEmptyTokens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("+,:")));
        parser.parse();
        assertEquals("+", parser.printTree());
    }

    @Test
    public void testParseInputWithParenAndEmptyTokens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(,)")));
        parser.parse();
        assertEquals("", parser.printTree());
    }

    @Test
    public void testParseThrowsOnSingleClosingParen() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex(")")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseThrowsOnMisMatchedParensInFunctions() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sqrt())")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseThrowsOnMisMatchedParensInFunctions2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("sqrt(()")));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse();
        });
    }
}
