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
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2+2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)4.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleMultiplication() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)4.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleSubtraction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2-2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)0.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleDivision() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2/2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)1.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionMultiplication() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2+2*5")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)12.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionSubtraction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("10+2-5")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)7.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionSubtractionMirrored() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("-5+10+2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)7.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleSubtractionMultiplication() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("5-5*2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)(-5.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionDivision() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("12+6/2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)15.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateSimpleAddtionDivisionOrderDoesntMatter() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("10/2+22")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)27.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesis() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(2+2)*2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)8.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesisOrderDoesntMatter() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(2+2)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)8.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesisExpression() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(10+2)/4")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)6.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateParenthesisNegativeExpression() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2*(-10)/4")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)(-5.0), evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateComplicatedExpressions() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("(2+2)*(5-1+8)/(2*2*2)+15*5")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals((Double)81.0, evaluator.evaluate(nodes.pop()));
    }

    @Test
    public void testEvaluateVariableAssignment() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodes.pop()));
    }

    @Test
    public void testEvaluateVariableAssignmentAndCalculation() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:2+x")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)4.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateTwoVariableAssignmentAndCalculation() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:y=5:2+x+y")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals("<assignment:y>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)9.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateVariableAsFunctionParam() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=4:sqrt(x)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)2.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateNaturalLogFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:ln(x)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)0.6931471805599453, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateLogBase2Function() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:log(x)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)1.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateSinFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:sin(x)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)0.9092974268256817, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateCosFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:cos(x)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)(-0.4161468365471424), evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateTanFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("x=2:tan(x)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals("<assignment:x>", evaluator.evaluateAssignment(nodeQue.dequeue()));
        assertEquals((Double)(-2.185039863261519), evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluatePowOperator() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2^8")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)(256.0), evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateModuloOperator() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("8%2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)0.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateMaxFunctionWithTwoArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("max(10,50)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)50.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateMaxFunctionWithManyArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("max(10,50,100,250,2,1,-100)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)250.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateMinFunctionWithTwoArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("min(2,7)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)2.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateMinFunctionWithManyArguments() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("min(2,7,20,100,2,1,-100,0,200)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)(-100.0), evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateAbsFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("abs(-100+(-100))")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)200.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateAbsWithPositiveFunction() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("abs(100)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)100.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluatePrecedence() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("-2%2*2+2^2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)4.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluatePrecedence2() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("3*2^2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)12.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateNestedUnaryParams() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("abs(-100+(-100))")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        assertEquals((Double)200.0, evaluator.evaluate(nodeQue.dequeue()));
    }

    @Test
    public void testEvaluateThrowsExceptionOnUnknownSymbol() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("abs(x)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodeQue.dequeue());
        });
    }

    @Test
    public void testEvaluateThrowsExceptionOnWrongNumberOfArgs() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("abs(2,2)")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        Exception exception = assertThrows(Exception.class, () -> {
            evaluator.evaluate(nodeQue.dequeue());
        });
    }

    @Test
    public void testEvaluateThrowsExceptionIfNonSymbolAssignment() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2=4:")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        Exception exception = assertThrows(EvaluatorException.class, () -> {
            evaluator.evaluateAssignment(nodeQue.dequeue());
        });
    }

    @Test
    public void testEvaluateThrowsExceptionIfNonAssignmentInExpression() throws Exception {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(new Queue<Token>(lexer.lex("2+x=4*2")));
        Evaluator evaluator = new Evaluator();
        Stack<ASTNode> nodes = parser.parse();
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        Exception exception = assertThrows(EvaluatorException.class, () -> {
            evaluator.evaluate(nodeQue.dequeue());
        });
    }
}
