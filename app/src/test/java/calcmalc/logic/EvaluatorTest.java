package calcmalc.logic;

import org.junit.Test;

import calcmalc.exceptions.EvaluatorException;
import calcmalc.logic.types.Token;
import calcmalc.structures.ASTNode;
import calcmalc.structures.Stack;
import calcmalc.structures.Queue;

import static org.junit.Assert.*;

public class EvaluatorTest {
    @Test
    public void testEvaluateSimpleAddition() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2+2"));
        assertEquals((Double) 4.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleMultiplication() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2*2"));
        assertEquals((Double) 4.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleSubtraction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2-2"));
        assertEquals((Double) 0.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleDivision() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2/2"));
        assertEquals((Double) 1.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionMultiplication() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2+2*5"));
        assertEquals((Double) 12.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionSubtraction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("10+2-5"));
        assertEquals((Double) 7.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionSubtractionMirrored() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-5+10+2"));
        assertEquals((Double) 7.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleSubtractionMultiplication() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("5-5*2"));
        assertEquals((Double) (-5.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionDivision() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("12+6/2"));
        assertEquals((Double) 15.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionDivisionOrderDoesntMatter() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("10/2+22"));
        assertEquals((Double) 27.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesis() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("(2+2)*2"));
        assertEquals((Double) 8.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesisOrderDoesntMatter() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2*(2+2)"));
        assertEquals((Double) 8.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesisExpression() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2*(10+2)/4"));
        assertEquals((Double) 6.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesisNegativeExpression() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2*(-10)/4"));
        assertEquals((Double) (-5.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateComplicatedExpressions() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("(2+2)*(5-1+8)/(2*2*2)+15*5"));
        assertEquals((Double) 81.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateComplicatedExpressionsWithVariables() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("a=1:b=2:c=5:d=8:e=15:(b+b)*(c-a+d)/(b*b*b)+e*c"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:a>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals("<assignment:b>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals("<assignment:c>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals("<assignment:d>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals("<assignment:e>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 81.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateVariableAssignment() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
    }

    @Test
    public void testEvaluateVariableAssignmentAndCalculation() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:2+x"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 4.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateTwoVariableAssignmentAndCalculation() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:y=5:2+x+y"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals("<assignment:y>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 9.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateVariableAsFunctionParam() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=4:sqrt(x)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 2.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateNaturalLogFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:ln(x)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 0.6931471805599453, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateLogBase2Function() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:log(x)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 1.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSinFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:sin(x)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 0.9092974268256817, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCosFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:cos(x)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) (-0.4161468365471424), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateTanFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:tan(x)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) (-2.185039863261519), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCustomFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x)=2+x:custom(2)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double)4.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCustomFunctionWithTwoArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y)=y+x:custom(2,9)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double)11.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCustomFunctionFailsWithMoreThanTwoArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y,z)=y+x+z:custom(2,9,11)"));
        Queue<ASTNode> variables = parser.variables();
        Exception exception = assertThrows(Exception.class, () -> {
            assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        }); 
    }

    @Test
    public void testEvaluateCustomFunctionFailsIfAllArgumentsAreNotSymbols() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(2,y)=y+2:custom(2,2)"));
        Queue<ASTNode> variables = parser.variables();
        Exception exception = assertThrows(Exception.class, () -> {
            assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        }); 
    }

    @Test
    public void testEvaluateCustomFunctionFailsIfAllArgumentsAreNotSymbols2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,sqrt(2))=2+x:custom(2,2)"));
        Queue<ASTNode> variables = parser.variables();
        Exception exception = assertThrows(Exception.class, () -> {
            assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        }); 
    }

    @Test
    public void testEvaluateCustomFunctionComposition() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y)=x+y:custom(abs(-1),1)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double)2.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCustomFunctionComposition2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y)=x+y:custom(abs(-1),sqrt(4))"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double)3.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCustomFunctionCompositionWithMaxAndMin() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y)=x+y:custom(min(-1,2,3,0),max(-1,-2,-3,-10,4))"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double)3.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCustomFunctionCompositionWithCustomFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y)=x+y:fn(x)=x+2:custom(fn(0),1)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals("<assignment:fn>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double)3.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateFailsOnEmptyAssignment() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        // custom(x,+)=2+x:custom(2,2) send to parser
        // custom(x,())=2+x:custom(2,2) send to parser
        // test composition
        Stack<ASTNode> nodes = parser.parse(lexer.lex("=:"));
        Exception exception = assertThrows(EvaluatorException.class, () -> {
            evaluator.evaluate(parser.variables().dequeue());
        });
    }

    @Test
    public void testEvaluatePowOperator() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2^8"));
        assertEquals((Double) (256.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateFloatAddition() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2+2.2"));
        assertEquals((Double) (4.2), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateFloatSubtraction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("6.2-2.2"));
        assertEquals((Double) (4.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMaxWithFloats() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("max(6.2,2.2,1.999,.25,0.1,10.10)"));
        assertEquals((Double) (10.10), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMaxWithFloatsAndInts() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("max(6.2,2,1.999,.25,100,12,100.1)"));
        assertEquals((Double) (100.1), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMinWithFloatsAndInts() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("min(.25,100)"));
        assertEquals((Double) (0.25), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMinWithNegative() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("min(-100,0)"));
        assertEquals((Double) (-100.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMaxWithOneArgument() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("max(1)"));
        assertEquals((Double) 1.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMinWithOneArgument() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("min(1)"));
        assertEquals((Double) 1.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateModuloOperator() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("8%2"));
        assertEquals((Double) 0.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testFunctionResultsWithOperatorMinus() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("min(8,2)-min(2,14)"));
        assertEquals((Double) 0.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testFunctionResultsWithOperatorPlus() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("max(8,2)+min(2,14)"));
        assertEquals((Double) 10.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMaxFunctionWithTwoArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("max(10,50)"));
        assertEquals((Double) 50.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMaxFunctionWithManyArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("max(10,50,100,250,max(2,1,-100))"));
        assertEquals((Double) 250.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMinFunctionWithTwoArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("min(2,7)"));
        assertEquals((Double) 2.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateMinFunctionWithManyArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("min(10,50,100,250,min(2,1,-100))"));
        assertEquals((Double) (-100.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateComplicatedExpressions2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("1+2*(sqrt(2)*max(2,(2+3)))"));
        assertEquals((Double) (15.142135623730951), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateComplicatedExpressions3() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-1*(-1)/2*(max(2,3,4,5)+(-abs(-5)^cos(2%(5*5*4))))"));
        assertEquals((Double) (2.244084937441898), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateExponent() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-2^4"));
        assertEquals((Double) (-16.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateExponent2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-(2)^4"));
        assertEquals((Double) (-16.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateExponent3() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("(-2)^4"));
        assertEquals((Double) (16.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateAbsFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("abs(-100+(-100))"));
        assertEquals((Double) 200.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateAbsFunction2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-abs(-5)"));
        assertEquals((Double) (-5.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateAbsFunction3() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("abs(-5)"));
        assertEquals((Double) (5.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateFunctionWithUnaryMinus() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-max(2,6)"));
        assertEquals((Double) (-6.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateAbsWithPositiveFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("abs(100)"));
        assertEquals((Double) 100.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluatePrecedence() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-2%2*2+2^2"));
        assertEquals((Double) 4.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluatePrecedence2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("3*2^2"));
        assertEquals((Double) 12.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateNestedUnaryParams() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("abs(-100+(-100))"));
        assertEquals((Double) 200.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateThrowsExceptionOnUnknownSymbol() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("abs(x)"));
        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodes.pop());
        });
    }

    @Test
    public void testEvaluateThrowsExceptionOnWrongNumberOfArgs() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("abs(2,2)"));
        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodes.pop());
        });
    }

    @Test
    public void testEvaluateThrowsExceptionIfNonAssignmentInExpression() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2+x=4*2:"));
        Exception exception = assertThrows(EvaluatorException.class, () -> {
            evaluator.evaluate(parser.variables().dequeue());
        });
    }

    @Test
    public void testParseThrowsExceptionOnIllegalAssignment() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2=5:"));
        Exception exception = assertThrows(EvaluatorException.class, () -> {
            evaluator.evaluate(parser.variables().dequeue());
        });
    }

    @Test
    public void testParseThrowsExceptionOnIllegalAssignment2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("+=5:"));
        Exception exception = assertThrows(EvaluatorException.class, () -> {
            evaluator.evaluate(parser.variables().dequeue());
        });
    }
}
