package calcmalc.logic;

import org.junit.Test;

import calcmalc.exceptions.LexerException;
import static org.junit.Assert.*;
import java.text.ParseException;

public class ParserTest {
    @Test
    public void testParseSimpleInput() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("1+1"));

        assertEquals("11+", parser.printTree());
    }

    @Test
    public void testParseSimpleInputFloats() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("1.123+1.999"));

        assertEquals("1.1231.999+", parser.printTree());
    }    
    
    @Test
    public void testParseShortMultipleOperators() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        parser.parse(lexer.lex("2*2+6+4-10/2"));

        assertEquals("22*6+4+102/-", parser.printTree());
    }

    @Test
    public void testParseSimpleWithParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("2*(2+2)"));

        assertEquals("222+*", parser.printTree());
    }

    @Test
    public void testParseSimpleWithParensAndFloats() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("2.9*(2.9999+2.123123)"));

        assertEquals("2.92.99992.123123+*", parser.printTree());
    }

    @Test
    public void testFunctionsResultsAndOperatorMinus() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("max(2,8)-min(8,12)"));

        assertEquals("28max812min-", parser.printTree());
    }

    @Test
    public void testFunctionsResultsAndOperatorPlus() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("max(2,8)+min(8,12)"));

        assertEquals("28max812min+", parser.printTree());
    }

    @Test
    public void testParseManyOperatorsAndOneParen() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        parser.parse(lexer.lex("2*(2+2+6+8-10)/3*2"));
        assertEquals("222+6+8+10-*3/2*", parser.printTree());
    }

    @Test
    public void testDeeplyNestedParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("2*(2*(2+2)*(1+(4+4*(10.919+1)))+(10*(5+(2+3))))"));
        assertEquals("2222+*14410.9191+*++*10523++*+*", parser.printTree());
    }

    @Test
    public void testFunctionParsing() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("sin(2+2)"));
        assertEquals("22+sin", parser.printTree());
    }

    @Test
    public void testFunctionWithInnerFunctionsParsingSimple() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("sin(max(2,4))"));
        assertEquals("24maxsin", parser.printTree());
    }

    @Test
    public void testFunctionWithInnerFunctionsParsing() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("sin(max(min(2,5),sqrt(1))+2+2)"));
        assertEquals("25min1sqrtmax2+2+sin", parser.printTree());
    }

    @Test
    public void testParseSimpleParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("(2+2)"));
        assertEquals("22+", parser.printTree());
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("2*((2+2)"));
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenIllegalExpression2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("(2+2))"));
    }

    @Test
    public void testParseNegativeNumbersSimple() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("-2"));
        assertEquals("2$", parser.printTree());
    }

    @Test
    public void testParseNegativeNumbersExpression() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("-2+4"));
        assertEquals("2$4+", parser.printTree());
    }

    @Test
    public void testParseNegativeNumbersExpressionWithParenthesis() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("2*(-10)"));
        assertEquals("210$*", parser.printTree());
    }

    @Test
    public void testParseNegativeNumbersExpressionWithParenthesisComplicated() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("2*(-10)/4"));
        assertEquals("210$*4/", parser.printTree());
    }

    @Test
    public void testParseNegativeNumberSum() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("-100+(-100)"));
        assertEquals("100$100$+", parser.printTree());
    }

    @Test
    public void testParseVariableAssignment() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=2:"));
        assertEquals("x2=", parser.printTree());
    }

    @Test
    public void testParseVariableAssignmentAndExpression() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=2:2+2"));
        assertEquals("x2=22+", parser.printTree());
    }

    @Test
    public void testParseExpressionInVariable() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=2+2:"));
        assertEquals("x22+=", parser.printTree());
    }

    @Test
    public void testParseComplicatedExpressionInVariable() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3)))):"));
        assertEquals("x2222+*144101+*++*10523++*+*=", parser.printTree());
    }

    @Test
    public void testParseFunctionExpressionInVariable() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=sin(2+2):"));
        assertEquals("x22+sin=", parser.printTree());
    }

    @Test
    public void testParseMultipleVariables() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=2:y=2:z=4:"));
        assertEquals("x2=y2=z4=", parser.printTree());
    }

    @Test
    public void testParseMultipleVariablesAndUse() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=2:y=2:2+x+y"));
        assertEquals("x2=y2=2x+y+", parser.printTree());
    }

    @Test
    public void testParseMultipleVariablesAndUse2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("a=1:b=2:c=3:d=4:e=5:f=6:g=7:a+b+c+d+e+f+g"));
        assertEquals("a1=b2=c3=d4=e5=f6=g7=ab+c+d+e+f+g+", parser.printTree());
    }

    @Test
    public void testParseMultipleVariablesAndUse3() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("a=1:b=2:c=5:d=8:(b+b)*(c-a+d)"));
        assertEquals("a1=b2=c5=d8=bb+ca-d+*", parser.printTree());
    }

    @Test
    public void testParseVariableAsFunctionParameter() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("x=2:sqrt(x)"));
        assertEquals("x2=xsqrt", parser.printTree());
    }

    @Test
    public void testParseAbsFunctionWithNegativeSum() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("abs(-100+(-100))"));
        assertEquals("100$100$+abs", parser.printTree());
    }

    @Test
    public void testParseAbsFunctionWithNegativeUnary() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("-abs(-10)"));
        assertEquals("10$abs$", parser.printTree());
    }

    @Test
    public void testParseSingleOperator() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("-"));
        assertEquals("$", parser.printTree());
    }

    @Test
    public void testParseCustomFunction() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("fn_abs(x)=x:"));
        assertEquals("xfn_absx=", parser.printTree());
    }

    @Test
    public void testParseCustomFunctionWithTwoArgs() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("fn_abs(x,y)=x+y:"));
        assertEquals("xyfn_absxy+=", parser.printTree());
    }

    @Test
    public void testParseCustomFunctionWithCallToStandardFunction() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("fn_abs(x,y)=max(x,y):"));
        assertEquals("xyfn_absxymax=", parser.printTree());
    }

    @Test
    public void testParseCustomFunctionWithCallToCustomFunction() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("fx(x)=x+2:fn_abs(x,y)=fx(x)+y:"));
        assertEquals("xfxx2+=xyfn_absxfxy+=", parser.printTree());
    }

    @Test
    public void testParseCustomFunctionWithFunctionComposition() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("fn_abs(x,y)=x+y:fn_abs(max(2,3),5)"));
        assertEquals("xyfn_absxy+=23max5fn_abs", parser.printTree());
    }

    @Test
    public void testParseCustomFunctionWithVariablesInBody() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("g=5*5:fn_abs(x)=x+g:"));
        assertEquals("g55*=xfn_absxg+=", parser.printTree());
    }

    @Test
    public void testParseCustomFunctionWithCustomFunctionComposition() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("fn_abs(x,y)=x+y:fn(x)=x+2:fn_abs(fn(0),1)"));
        assertEquals("xyfn_absxy+=xfnx2+=0fn1fn_abs", parser.printTree());
    }

    @Test
    public void testParseThrowsExceptionOnWrongArgumentCountCustomFunction() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("custom_fn()=2+2:"));
        });
    }

    @Test
    public void testEvaluateMaxWithNoArguments() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("max()"));
        });
    }

    @Test
    public void testEvaluateMinWithNoArguments() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("min()"));
        });
    }
    @Test
    public void testParseThrowsExceptionOnMisMatchedParenthesis() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("())"));
        });
    }

    @Test
    public void testParseThrowsExceptionOnIllegalVariableTerminator() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("2:"));
        });
    }

    @Test
    public void testParseThrowsExceptionOnIllegalVariableAssignment() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("x=2+y=4:"));
        });
    }

    @Test
    public void testParseThrowsExceptionOnIllegalVariableAssignment2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("x=2+y=4::"));
        });
    }

    @Test
    public void testParseThrowsExceptionOnMisMatchedParenthesis2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("(,):)"));
        });
    }

    @Test
    public void testParseThrowsExceptionOnMisMatchedParenthesis3() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("+,:)"));
        });
    }

    @Test
    public void testParseInputOfParens() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("()"));
        assertEquals("", parser.printTree());
    }

    @Test
    public void testThrowsOnInputOfComas() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex(",,,"));
        });
    }

    @Test
    public void testParseInputWithSingleOperator() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        parser.parse(lexer.lex("+"));
        assertEquals("+", parser.printTree());
    }

    @Test
    public void testThrowsOnInputWithParenAndComas() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("(,)"));
        });
    }

    @Test
    public void testThrowsOnInputWithFunctionAndDanglingComma() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("max(,)"));
        });
    }

    @Test
    public void testThrowsOnInputWithFunctionAndDanglingComma2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("max(1,)"));
        });
    }

    @Test
    public void testThrowsOnInputWithFunctionAndDanglingColon() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("sqrt():"));
        });
    }

    @Test
    public void testParseThrowsOnSingleClosingParen() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex(")"));
        });
    }

    @Test
    public void testParseThrowsOnMisMatchedParensInFunctions() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("sqrt())"));
        });
    }

    @Test
    public void testParseThrowsOnMisMatchedParensInFunctions2() throws ParseException, LexerException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(lexer.lex("sqrt(()"));
        });
    }
}
