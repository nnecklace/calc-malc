package calcmalc.logic;

import calcmalc.structures.Stack;
import calcmalc.structures.ASTNode;
import calcmalc.structures.Listable;
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
    private Listable<Token> tokens;
    private int position = 0;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTokens(Listable<Token> tokens) {
        this.tokens = tokens;
    }

    public Listable<Token> getTokens() {
        return tokens;
    }

    /**
     * Method accepts list of tokens for the arthemetic expression
     * @param input the expression to be converted
     * @return stack of nodes with the top of the stack being root node
     * @throws ParseException if illegal input
     */
    public Stack<ASTNode> parse(Listable<Token> tokens) throws ParseException {
        setTokens(tokens);
        while (getPosition() < tokens.size()) {
            Token token = tokens.get(getPosition());
            shuntingYardParse(token);
            setPosition(getPosition()+1);
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
    public Stack<ASTNode> parseFunction(Listable<Token> tokens) throws ParseException {
        setTokens(tokens);
        Token token = tokens.get(getPosition());
        while (token != null) {
            shuntingYardParse(token);
            setPosition(getPosition()+1);
            if (token.isEmpty() && token.getKey().equals(")")) {
                break;
            }
            token = tokens.get(getPosition());
        }
        return popRemainingTokens();
    }

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

    private void parseFunctionArguments(ASTNode root) throws ParseException {
        Parser parser = new Parser();
        parser.setPosition(getPosition()+1);
        Stack<ASTNode> children = parser.parseFunction(getTokens());
        setPosition(parser.getPosition()-1);
        root.setChildren(children.asList());
        functions.push(root);
    }

    // This the main algorithm of the program, explaining it is rather difficult
    // Wikipedia however has a very good and simple explanation of the algorithm
    // https://en.wikipedia.org/wiki/Shunting-yard_algorithm
    private void shuntingYardParse(Token token) throws ParseException {
        if (token.isNumber()) {
            nodes.push(new ASTNode(token));
        } 
        else if (token.isFunction()) {
            // This is currently where we diverge from the standard algorithm
            // Here we recursively parse the function and its arguments
            operators.push(token);
            ASTNode root = new ASTNode(token);
            parseFunctionArguments(root);
        }
        else if (token.isOperator()) {
            while (
                !operators.isEmpty() && 
                !operators.peek().getKey().equals("(") && 
                operators.peek().getPrecedence() >= token.getPrecedence()
            ) {
                Token operator = operators.pop();
                addOperatorNode(operator);
            }
            operators.push(token);
        } 
        else if (token.isEmpty()) {
            if (token.getKey().equals("(")) {
                operators.push(token);
            } 
            else {
                while (!operators.isEmpty() && !operators.peek().getKey().equals("(")) {
                    Token operator = operators.pop();
                    addOperatorNode(operator);
                }

                if (operators.isEmpty() || !operators.peek().getKey().equals("(")) {
                    throw new ParseException("Syntax error missing parenthesis! 2", 0);
                }

                if (!token.getKey().equals(",")) {
                    operators.pop();
                }
            }
        }
    }

    private void addOperatorNode(Token operator) {
        if (operator.isFunction()) {
            nodes.push(functions.pop());
        } 
        else {
            ASTNode node = new ASTNode(operator);
            node.addChild(nodes.pop());
            node.addChild(nodes.pop());
            nodes.push(node);
        }
    }

    /**
     * Prints the abstract syntax tree in RPN normal form
     * @return The RPN form of the tree structure
     */
    public String printTree() {
        ASTNode root = nodes.pop();
        return dfs(root);
    } 

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
