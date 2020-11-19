package calcmalc.logic;

import org.junit.Test;
import calcmalc.logic.types.Token;
import calcmalc.structures.ASTNode;
import calcmalc.structures.Queue;
import calcmalc.structures.Stack;

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
}
