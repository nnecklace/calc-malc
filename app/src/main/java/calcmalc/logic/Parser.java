package calcmalc.logic;

import calcmalc.structures.Stack;
import calcmalc.structures.Queue;
import calcmalc.structures.ASTNode;
import calcmalc.structures.List;
import calcmalc.logic.types.Token;
import java.text.ParseException;

/**
 * Parser for the program. The parser is implmented with the Shunting Yard algoritm. 
 @see <a href="https://en.wikipedia.org/wiki/Shunting-yard_algorithm">Shunting yard</a>
 * Main objective of the class is to convert expressions into an AST (abstract syntax tree)
 * The Parser has some added functionality that the regular shunting yard algorithm does not support
 * This includes variables and custom functions
 * The parser runs in O(n) time, where n is the number of tokens in the queue
 * The parser uses an O(n) amount of space, where n is the number of tokens in the queue.
 * 
 * Examples:
 * <pre>
 *              +
 * 2+4*6 =>    / \ 
 *            *   2
 *           / \
 *          4   6
 *                            = 
 * double(x) = 2*x:  =>     /   \
 *                       double  * 
 *                       /      / \
 *                      x      2   x
 * </pre>
 * @author nnecklace
 */
public class Parser {
    /**
     * Operator stack will contain operators or functions
     * Operators include: = + - / * ^ $ % 
     * And all functions
     */
    private Stack<Token> operators = new Stack<>();
    /**
     * Nodes stack contains all the ASTNodes created by the parser
     * Ideally this should be a Queue but our queue is not a doubly linked list
     * So we cannot access the last element in the queue in constant time O(1)
     * Ideally the root node should be on top of the stack at the end
     */
    private Stack<ASTNode> nodes = new Stack<>();
    /**
     * When the parser has parsed an assignment can created an assignment node
     * it will add it to the variables queue
     * All variables will be evaluated first, and separetly from the actual expression nodes
     */
    private Queue<ASTNode> variables = new Queue<>();
    /**
     * When the parser enccouters a function, it will count the amount of arguments for the function
     * This way the parser knows how many child nodes to add the a function node
     * The argument amount will be stored in this stack
     */
    private Stack<Integer> functionArity = new Stack<>();

    /**
     * Method accepts queue of tokens from the lexer and runs the shunting yard algorithm
     * until the queue is empty
     * After which is it will emnpty any remaing operators in the operator queue
     * and add the remining operators to the syntax tree
     * @param tokens the queue of tokens made by the lexer
     * @return stack of nodes with the top of the stack being root node
     * @throws ParseException if illegal input 
     * see documentation for legal input
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

    /**
     * Method tries to increase the argument count for a function to 1
     * It will only ever increase it to 1 if current token is not a 
     * closing parenthesis or a variable delimiter or a comma and the top of the function arity stack is 0
     * @param token the token to check
     */
    private void tryIncFunctionArity(Token token) {
        if ((token.isNumber() || 
             token.isSymbol() || 
             token.isOperator() || 
             token.isFunction() || 
             token.isOpenParenthesis()) && 
             !functionArity.isEmpty() && 
             functionArity.peek() == 0) {
            functionArity.push(functionArity.pop() + 1);
        }
    }

    /**
     * Method pops operators from the operator stack until the top of the operator stack is the tokenKey
     * or until a lower precedence operator is found.
     * @param tokenKey the token to pop until
     * @param precedence the precedence to pop until
     * @throws ParseException if there is an error while creating a tree node out of the popped operator
     */
    private void popUntilTokenOrPrecedence(String tokenKey, int precedence) throws ParseException {
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
     * @see <a href="https://en.wikipedia.org/wiki/Shunting-yard_algorithm">Shunting yard</a>
     * We have modified the algorithm to accept variables and functions with parameters and custom functions
     * @see documentation for step by step explanation of the algorithm and all its intermediate stages
     * @param token The token to parse
     * @throws ParseException if the token causes the input to be malformed
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
                    !nodes.peek().token().isSymbol()) {
                        // This catches cases where the assignment is used in a really weird way
                        // 2 = x:
                        // abs(x=2:)
                        // = 2.5:
                        // etc
                    throw new ParseException("Syntax error: Tried to assign value to non assignable or empty", 0);
                }

                operators.push(token);
                break;
            case OPERATOR:
                popUntilTokenOrPrecedence("(", token.getPrecedence());
                operators.push(token);
                break;
            case OPEN_PARENTHESIS:
                if (!operators.isEmpty() && operators.peek().isFunction()) {
                    functionArity.push(0);
                }

                operators.push(token);
                break;
            case CLOSING_PARENTHESIS:
                popUntilTokenOrPrecedence("(", 0);

                if (operators.isEmpty()) {
                    throw new ParseException("Syntax error missing parenthesis! 2", 0);
                } 
                
                operators.pop();

                if (!operators.isEmpty() && operators.peek().isFunction()) {
                    addOperatorNode(operators.pop());
                } 
                break;
            case COMMA:
                popUntilTokenOrPrecedence("(", 0);

                if (functionArity.isEmpty() || functionArity.peek() == 0) {
                    throw new ParseException("Syntax error: Illegal use of comma", 0);
                }

                functionArity.push(functionArity.pop() + 1); 
                break;
            case VARIABLE_DELIMITER:
                popUntilTokenOrPrecedence("=", 0);

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
     *                 pop the previously two added nodes since they will be the 
     *                 arguments for the operator. The operator can also be unary, in which case only one node is popped
     *                 If the param is a function
     *                 we pop nodes and decrease the number of argument count, we pop until
     *                 there are no more nodes or argument count is 0
     * @throws ParseException if there is an incorrect amount of arguments for a function
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

            List<ASTNode> children = new List<>(argCount);

            while (argCount > 0 && !nodes.isEmpty()) {
                children.set(--argCount, nodes.pop());
            }

            node.setChildren(children);

            if (argCount > 0) {
                // this catches cases where there is a dangling comma
                // e.g., max(2,3,)
                throw new ParseException("Parse error: function was expected to have more arguments than available", 1);
            }

        } else {
            ASTNode secondChild = nodes.pop();
            ASTNode firstChild = nodes.pop();

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
     * The method should only be used in tests and debugging
     * Using the string concat + operator is really slow
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
     * Should only be used in tests and debugging
     * @param node root node of the tree
     * @return String representing the node and its position in RPN normal form
     */
    private String depthFirstSearch(ASTNode node) {
        if (node.token().isNumber()) {
            return node.token().getKey();
        }

        String branches = "";
        
        for (int i = 0; i < node.children().size(); ++i) {
            branches += depthFirstSearch(node.children().get(i));
        }

        return (branches += node.token().getKey());
    }

    /**
     * Getter for the variable queue
     * @return the current queue of variables, variables include custom functions and "constants"
     */
    public Queue<ASTNode> variables() {
        return this.variables;
    }
}
