package calcmalc.logic;

import org.junit.Test;

import calcmalc.exceptions.EvaluatorException;
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
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:log(x)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 0.6931471805599453, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateLogBase2Function() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=2:log(x)/log(2)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double) 1.0, evaluator.evaluate(nodes.pop()));
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
    public void testEvaluateCustomFunctionWithCustomFunctionInBody() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x)=2*x:fun(x)=x*custom(x):fun(2)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals("<assignment:fun>", evaluator.evaluateAssignment(variables.dequeue()));
        assertEquals((Double)8.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateCustomFunctionFailsWithIncorrectAmountOfArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y,z)=y+x+z:custom(2,9)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodes.pop());
        }); 
    }

    @Test
    public void testEvaluateCustomFunctionFailsWithIncorrectAmountOfArguments2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y,z)=y+x+z:custom(2,9,10,4)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodes.pop());
        }); 
    }

    @Test
    public void testEvaluateCustomFunctionFailsWithIncorrectAmountOfArgumentsButSucceedsAfter() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("custom(x,y,z)=y+x+z:custom(2,9)"));
        Queue<ASTNode> variables = parser.variables();
        assertEquals("<assignment:custom>", evaluator.evaluateAssignment(variables.dequeue()));
        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodes.pop());
        }); 
        Stack<ASTNode> nodesAgain = parser.parse(lexer.lex("custom(2,9,9)"));
        assertEquals(20.0, evaluator.evaluate(nodesAgain.pop()));
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
    public void testEvaluateFailsOnEmptyAssignment2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("x=:"));
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
        Stack<ASTNode> nodes = parser.parse(lexer.lex("-1*(-1)/2*(max(2,3,4,5)+(-abs(-5)*sqrt(2%(5*5*4))))"));
        assertEquals((Double) (-1.0355339059327378), evaluator.evaluate(nodes.pop()));
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
    public void testEvaluateExponent4() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2^(-2)"));
        assertEquals((Double) (0.25), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateExponent5() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex("2^(-2.5)"));
        assertEquals(Double.NaN, evaluator.evaluate(nodes.pop()));
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
    public void testEvaluateExressions() throws Exception {
        String expr = new StringBuilder()
            .append("y = 10^6:\n") // 1000000
            .append("x = -20+100*log(50):\n") // 371.202300542814
            .append("very_stupidly_long_variable_name = sqrt(2000)+max(2,6,200,x,y)*(5+(2-5)*2^2+(10*(16+1000))):\n") // 1.015300004472135954999579
            .append("function_eval_all_sum_what_even_is_this = y+x+very_stupidly_long_variable_name:\n") // 1.01540004159236600928103997900585521656807094214826514819595
            .append("__lets_call_this__double__EVEN___THOUG_WE_DONT_DOUBLE(x) = 10*(40-25*(-(x+2)*2)):\n") // 51400
            .append("function_eval_all_sum_what_even_is_this + __lets_call_this__double__EVEN___THOUG_WE_DONT_DOUBLE(10^2)") // 51400+(10^6+-20+100*log(50)+sqrt(2000)+10^6*(5+(2-5)*2^2+(10*(16+1000))))
            .toString();

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex(expr));
        while(!parser.variables().isEmpty()) {
            evaluator.evaluateAssignment(parser.variables().dequeue());
        }
        assertEquals((Double) 1.015405181592366E10, evaluator.evaluate(nodes.pop())); // verified with wolfram alpha
    }

    @Test
    public void testEvaluateExressions2() throws Exception {
        String expr = new StringBuilder()
            .append("_((((y))),(x))   = (((((x)))+((((((y)))))))):")
            .append("_(((((2)+(2)))),((((sqrt(2))))))/(((max(((2+3),(((log(20)))))))))")
            .toString();

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex(expr));
        while(!parser.variables().isEmpty()) {
            evaluator.evaluateAssignment(parser.variables().dequeue());
        }
        assertEquals((Double) 1.082842712474619, evaluator.evaluate(nodes.pop())); // verified with wolfram alpha
    }

    @Test
    public void testEvaluateExressions3() throws Exception {
        String expr = new StringBuilder()
            .append("___(__X,__HOW__) = ((((20000+__X)))^2)%__HOW__:")
            .append("double(some__number) = (some__number)*(2):")
            .append("triple(_param) = _param+double(_param):")
            .append("eleganto(positiveNumber, negativeNumber) = triple((max(2,3,4,5,6,7,10^2)+positiveNumber)+double(negativeNumber)):")
            .append("2+(2*(-2))+abs(eleganto(___(min(1,1.5), ((1/16))), log(2+2+2+2+2*(2+2+2+2))))") // 2+(2*(-2))+3*(10^2+0+(2*3.17))
            .toString();

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex(expr));
        while(!parser.variables().isEmpty()) {
            evaluator.evaluateAssignment(parser.variables().dequeue());
        }
        assertEquals((Double) 317.0683229820877, evaluator.evaluate(nodes.pop())); // verified with wolfram alpha
    }

    @Test
    public void testEvaluateExressions5() throws Exception {
        String expr = new StringBuilder()
            .append("addX(y) = y+x:")
            .append("x = 2000:")
            .append("double(sum_param) = sum_param+sum_param:")
            .append("addX(double(-abs(sqrt(2))))")
            .toString();

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex(expr));
        while(!parser.variables().isEmpty()) {
            evaluator.evaluateAssignment(parser.variables().dequeue());
        }
        assertEquals((Double) 1997.1715728752538, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateExressions6() throws Exception {
        String expr = new StringBuilder()
            .append("addX(y) = y+x:")
            .append("x = 2000:")
            .append("sum(a,b,c,d,e,f,g) = a+b+c+d+e+f+g:")
            .append("addX(sum(1,2,3,4,5,6,7)+sum(2,4,6,8,10,12,14))") // 2084
            .toString();

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex(expr));
        while(!parser.variables().isEmpty()) {
            evaluator.evaluateAssignment(parser.variables().dequeue());
        }
        assertEquals(2084.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testCantReasignStlFunctions() throws Exception {
        String expr = new StringBuilder()
            .append("sqrt = 1000:")
            .append("sqrt")
            .toString();

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex(expr));
        while(!parser.variables().isEmpty()) {
            evaluator.evaluateAssignment(parser.variables().dequeue());
        }

        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodes.pop());
        });
    }

    @Test
    public void testEvaluateThrowsOnUnknownFunctionCall() throws Exception {
        String expr = new StringBuilder()
            .append("funfunfun(x) = 2^x:")
            .append("funfun(2)")
            .toString();

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse(lexer.lex(expr));
        while(!parser.variables().isEmpty()) {
            evaluator.evaluateAssignment(parser.variables().dequeue());
        }

        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodes.pop());
        });
    }
    
}
