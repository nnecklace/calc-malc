package calcmalc.logic;

import calcmalc.structures.Stack;
import calcmalc.structures.Queue;
import calcmalc.structures.ASTNode;
import calcmalc.structures.List;
import calcmalc.logic.types.Token;
import java.text.ParseException;

/**
 * Parser for the Shunting Yard algoritm. Main objective of the class is to convert expressions into RPN (reverse polish notation)
 */
public class Parser {
    private Stack<Token> operators = new Stack<>(new List<>());
    private Stack<ASTNode> nodes = new Stack<>(new List<>());
    private Stack<ASTNode> functions = new Stack<>(new List<>());
    private Queue<Token> tokens;

    public Parser(Queue<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Method accepts list of tokens for the arthemetic expression
     * @param input the expression to be converted
     * @return stack of nodes with the top of the stack being root node
     * @throws ParseException if illegal input
     */
    public Stack<ASTNode> parse() throws ParseException {
        while (!tokens.isEmpty()) {
            Token token = tokens.dequeue();
            shuntingYardParse(token);
        }
        return popRemainingTokens();
    }

    /**
     * Parses functions and its arguments
     * @param tokens List of tokens, ideally this should contain arguments for the function
     * @param from indicating where to start parsing from
     * @return stack of nodes that represent the functions arguments
     * @throws ParseException on illegal input
     */
    public Stack<ASTNode> parseUntil(String end) throws ParseException {
        int openingParenthesisCounter = 0;
        while (!tokens.isEmpty()) {
            Token token = tokens.peek();
            
            if (token.getKey().equals("(")) {
                openingParenthesisCounter++;
            }

            if (token.getKey().matches(end) && !end.equals("\\)")) {
                break;
            }

            tokens.dequeue();
            shuntingYardParse(token);

            // make sure to process the last closing parenthesis.
            // when processing function parameters, we process up until and including the last parenthesis
            // normally we only process until the last token that matches the given terminator (end) string
            if (token.getKey().matches(end) && end.equals("\\)")) {
                if (openingParenthesisCounter <= 1) {
                    break;
                } else {
                    openingParenthesisCounter--;
                }
            }
        }

        return popRemainingTokens();
    }

    /**
     * Pops all the remaining operators from the operator stack and creates ast nodes of them
     * @return stack of ast nodes, ideally should only return one root node but incase of function arguments or variables it will return many nodes
     * @throws ParseException
     */
    private Stack<ASTNode> popRemainingTokens() throws ParseException {
        while (!operators.isEmpty()) {
            Token operator = operators.pop();

            if (operator.getKey().equals("(")) {
                throw new ParseException("Syntax error missing parenthesis 1", 0);
            }

            addOperatorNode(operator);
        }

        return nodes;
    }

    // This the main algorithm of the program, explaining it is rather difficult
    // Wikipedia however has a very good and simple explanation of the algorithm
    // https://en.wikipedia.org/wiki/Shunting-yard_algorithm
    private void shuntingYardParse(Token token) throws ParseException {
        if (token.isNumber()) {
            nodes.push(new ASTNode(token));
        } else if (token.isSymbol()) {
            // This is currently where we diverge from the standard algorithm
            // Here we recursively parse the function and its arguments
            operators.push(token);
            ASTNode root = new ASTNode(token);
            Parser parser = new Parser(tokens);
            Stack<ASTNode> children = new Stack<>(new List<>()); 

            if (!tokens.isEmpty()) {
                if (tokens.peek().isAssignment()) {
                    children = parser.parseUntil("\\=");
                } else if (tokens.peek().isEmpty() && tokens.peek().getKey().equals("(")) {
                    children = parser.parseUntil("\\)");
                } else {
                    children = parser.parseUntil("[^_a-zA-Z]");
                }
            }

            root.setChildren(children.asList());
            functions.push(root);
        } else if (token.isAssignment()) {
            operators.pop();
            operators.push(token);

            ASTNode root = new ASTNode(token);
            Parser parser = new Parser(tokens);
            Stack<ASTNode> children = parser.parseUntil(":");

            root.setChildren(children.asList());
            root.addChild(functions.pop());

            functions.push(root);
        } else if (token.isOperator()) {
            while (
                    !operators.isEmpty() && 
                    !operators.peek().getKey().equals("(") && 
                    operators.peek().getPrecedence() >= token.getPrecedence()
            ) {
                Token operator = operators.pop();
                addOperatorNode(operator);
            }
            operators.push(token);
        } else {
            if (token.getKey().equals("(")) {
                operators.push(token);
            } else {
                while (!operators.isEmpty() && !operators.peek().getKey().equals("(")) {
                    Token operator = operators.pop();
                    addOperatorNode(operator);
                }

                if (!token.getKey().equals(",") && !token.getKey().equals(":") && operators.isEmpty()) {
                    throw new ParseException("Syntax error missing parenthesis! 2", 0);
                }

                if (!token.getKey().equals(",") && !token.getKey().equals(":")) {
                    operators.pop();
                }
            }
        }
    }

    /**
     * Add nodes to the abstract syntax tree
     * @param operator to add to the tree, 
     * if operator is a mathematical operator we pop the previously added nodes since they will be the numbers for the operator
     * If token is a function we pop the top function from the function stack and add it to the tree
     */
    private void addOperatorNode(Token operator) {
        if (operator.isSymbol() || operator.isAssignment()) {
            nodes.push(functions.pop());
        } else {
            ASTNode node = new ASTNode(operator);
            if ("$".equals(operator.getKey())) {
                node.addChild(nodes.pop());
            } else {
                if (!nodes.isEmpty()) {
                    node.addChild(nodes.pop());
                }

                if (!nodes.isEmpty()) {
                    node.addChild(nodes.pop());
                }
            }

            nodes.push(node);
        }
    }

    /**
     * Prints the abstract syntax tree in RPN normal form
     * @return The RPN form of the tree structure
     */
    public String printTree() {
        String output = "";
        Queue<ASTNode> nodeQue = new Queue<>(nodes.asList());
        while (!nodeQue.isEmpty()) {
            ASTNode root = nodeQue.dequeue();
            output += dfs(root);
            System.out.println();
        }
        return output;
    } 

    /**
     * Normal depth first search traversal of the abstract syntax tree the parser created
     * @param node root node of the tree
     * @return String representing the node and its position in RPN normal form
     */
    private String dfs(ASTNode node) {
        if (node.token().isNumber()) {
            return node.token().getKey();
        }

        String branches = "";
        
        while (!node.getChildren().isEmpty()) {
            branches += dfs(node.getChildren().getLast());
            node.getChildren().remove(node.getChildren().size());
        }

        return (branches += node.token().getKey());
    }
}
