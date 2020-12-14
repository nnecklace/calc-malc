package calcmalc.logic;

import calcmalc.structures.Stack;
import calcmalc.structures.Queue;
import calcmalc.structures.ASTNode;
import calcmalc.logic.types.Token;
import java.text.ParseException;

/**
 * Parser for the Shunting Yard algoritm. Main objective of the class is to convert expressions into RPN (reverse polish notation)
 */
public class Parser {
    private Stack<Token> operators = new Stack<>();
    private Stack<ASTNode> nodes = new Stack<>();
    private Queue<ASTNode> variables = new Queue<>();
    private Stack<Integer> functionArity = new Stack<>();

    /**
     * Method accepts list of tokens for the arthemetic expression
     * @return stack of nodes with the top of the stack being root node
     * @throws ParseException if illegal input
     */
    public Stack<ASTNode> parse(Queue<Token> tokens) throws ParseException {
        while (!tokens.isEmpty()) {
            shuntingYardParse(tokens.dequeue());
        }

        while (!operators.isEmpty()) {
            Token operator = operators.pop();

            if (operator.isOpenParenthesis()) {
                throw new ParseException("Syntax error: Missing parenthesis 1", 0);
            }

            addOperatorNode(operator);
        }

        return nodes;
    }

    private void tryIncFunctionArity(Token token) {
        if ((token.isNumber() || 
             token.isSymbol() || 
             token.isOperator() || 
             token.isFunction() || 
             token.isOpenParenthesis()) && 
             !functionArity.isEmpty() && 
             functionArity.peek() == 0) 
        {
            functionArity.push(functionArity.pop() + 1);
        }
    }

    private void popUntil(String tokenKey, int precedence) throws ParseException {
        while (
                !operators.isEmpty() && 
                !operators.peek().getKey().equals(tokenKey) &&
                operators.peek().getPrecedence() >= precedence
        ) {
            addOperatorNode(operators.pop());
        }
    }

    /**
     * Parses each token at a time and performs the given action per token
     * This the main algorithm of the program, explaining it is rather difficult
     * Wikipedia however has a very good and simple explanation of the algorithm
     * https://en.wikipedia.org/wiki/Shunting-yard_algorithm
     * We have modified the algorithm to accept variables and functions with parameters
     * @param token The token to parse
     * @throws ParseException if parenthesis don't match
     */
    private void shuntingYardParse(Token token) throws ParseException { 
        tryIncFunctionArity(token);

        switch (token.getType()) {
            case NUMERIC:
            case SYMBOL:
                nodes.push(new ASTNode(token)); 
                break;
            case FUNCTION:
                operators.push(token);
                break;
            case ASSIGNMENT:
                if (!operators.isEmpty() || 
                    nodes.isEmpty() || 
                    !nodes.peek().token().isFunction() && 
                    !nodes.peek().token().isSymbol()) 
                {
                    throw new ParseException("Syntax error: Tried to assign value to non assignable or empty", 0);
                }

                operators.push(token);
                break;
            case OPERATOR:
                popUntil("(", token.getPrecedence());
                operators.push(token);
                break;
            case OPEN_PARENTHESIS:
                if (!operators.isEmpty() && operators.peek().isFunction()) {
                    functionArity.push(0);
                }

                operators.push(token);
                break;
            case CLOSING_PARENTHESIS:
                popUntil("(", 0);

                if (operators.isEmpty()) {
                    throw new ParseException("Syntax error missing parenthesis! 2", 0);
                } 
                
                operators.pop();

                if (!operators.isEmpty() && operators.peek().isFunction()) {
                    addOperatorNode(operators.pop());
                } 
                break;
            case COMMA:
                popUntil("(", 0);

                if (functionArity.isEmpty() || functionArity.peek() == 0) {
                    throw new ParseException("Syntax error: Illegal use of comma", 0);
                }

                functionArity.push(functionArity.pop() + 1); 
                break;
            case VARIABLE_DELIMITER:
                popUntil("=", 0);

                if (operators.isEmpty()) {
                    throw new ParseException("Syntax error: Assignment for variable delimitter missing", 0);
                } 

                addOperatorNode(operators.pop());
                variables.enqueue(nodes.pop());
                break;
        }
    }

    /**
     * Add nodes to the abstract syntax tree
     * 
     * @param operator to add to the tree, if operator is a mathematical operator we
     *                 pop the previously added nodes since they will be the numbers
     *                 for the operator If token is a function we pop the top
     *                 function from the function stack and add it to the tree
     * @throws ParseException
     */
    private void addOperatorNode(Token operator) throws ParseException {
        ASTNode node = new ASTNode(operator);
        if (operator.getKey().equals("$") && !nodes.isEmpty()) {
            node.addChild(nodes.pop());

        } else if (operator.isFunction()) {
            Integer argCount = functionArity.pop();

            if (argCount == 0) {
                throw new ParseException("Parse error: functions must have arguments, use variables instead in cases where no arguments are needed", 1);
            }

            while (argCount > 0 && !nodes.isEmpty()) {
                node.addChild(nodes.pop());
                argCount--;
            }

            if (argCount > 0) {
                throw new ParseException("Parse error: function was expected to have more arguments than available", 1);
            }

        } else {
            ASTNode firstChild = nodes.pop();
            ASTNode secondChild = nodes.pop();

            if (firstChild != null) {
                node.addChild(firstChild);
            }

            if (secondChild != null) {
                node.addChild(secondChild);
            }

        }

        nodes.push(node);
    }

    /**
     * Prints the abstract syntax tree in RPN normal form
     * @return The RPN form of the tree structure
     */
    public String printTree() {
        String output = "";

        while (!variables.isEmpty()) {
            output += depthFirstSearch(variables.dequeue());
        }

        while (!nodes.isEmpty()) {
            output += depthFirstSearch(nodes.pop());
        }

        return output;
    } 

    /**
     * Normal depth first search traversal of the abstract syntax tree the parser created
     * @param node root node of the tree
     * @return String representing the node and its position in RPN normal form
     */
    private String depthFirstSearch(ASTNode node) {
        if (node.token().isNumber()) {
            return node.token().getKey();
        }

        String branches = "";
        
        for (int i = node.children().size() - 1; i >= 0; --i) {
            branches += depthFirstSearch(node.children().get(i));
        }

        return (branches += node.token().getKey());
    }

    public Queue<ASTNode> variables() {
        return this.variables;
    }
}
