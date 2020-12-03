package calcmalc.logic;

import calcmalc.structures.ASTNode;
import calcmalc.structures.HashTable;
import calcmalc.structures.List;
import calcmalc.structures.Queue;
import calcmalc.exceptions.EvaluatorException;

/**
 * @author nnecklace
 */
public class Evaluator {
    private HashTable<String, Integer> config = new HashTable<>();
    private HashTable<String, Double> symbolTable = new HashTable<>();

    /**
     * Constructor for the Evaluator
     * Constructor initializes the config table with built in functions and operators
     */
    public Evaluator() { 
        config.placeOrUpdate("+", 2);
        config.placeOrUpdate("-", 2);
        config.placeOrUpdate("*", 2);
        config.placeOrUpdate("/", 2);
        config.placeOrUpdate("^", 2);
        config.placeOrUpdate("%", 2);
        config.placeOrUpdate("$", 1);
        config.placeOrUpdate("sqrt", 1);
        config.placeOrUpdate("log", 1);
        config.placeOrUpdate("ln", 1);
        config.placeOrUpdate("abs", 1);
        config.placeOrUpdate("cos", 1);
        config.placeOrUpdate("sin", 1);
        config.placeOrUpdate("tan", 1);
        config.placeOrUpdate("max", -1);
        config.placeOrUpdate("min", -1);
    }

    /**
     * Method checks that the given symbol (function) has the correct amount of arguments
     * @param symbol the symbol to check
     * @param argumentsCount the given number of arguments given to the symbol
     * @throws EvaluatorException if wrong number of arguments
     */
    private void checkArguments(String symbol, int argumentsCount) throws EvaluatorException {
        Integer value = config.get(symbol);
        if (value != null && value != argumentsCount && value != -1) {
            throw new EvaluatorException("Wrong number of arguments " + symbol);
        }
    }

    /**
     * Checks if the given symbol has been defined during runtime
     * @param token The token to check for
     * @return the value associated with the symbol
     * @throws EvaluatorException if symbol is unknown
     */
    private Double checkSymbolTable(String token) throws EvaluatorException {
        Double symbol = symbolTable.get(token);
        if (symbol != null) {
            return symbol;
        }

        throw new EvaluatorException("Unknown Symbol " + token);
    }

    /**
     * Method tries to evaluate the given token, be it a function or variable symbol
     * @param <N> Let n be any java Number type
     * @param token the token to be evaluated
     * @param arguments the queue of arguments for the token
     * @return Whatever result the symbol represents wuth the given arguments
     * @throws EvaluatorException if symbol is unknown or a function was given an incorrect number of arguments
     */
    public <N extends Number> double evaluateFunction(String token, Queue<N> arguments) throws EvaluatorException {
        // TODO: Catch divide by zero
        checkArguments(token, arguments.size());
        switch (token) {
            case "*":
                return arguments.dequeue().doubleValue() * arguments.dequeue().doubleValue();
            case "+":
                return arguments.dequeue().doubleValue() + arguments.dequeue().doubleValue();
            case "/":
                return arguments.dequeue().doubleValue() / arguments.dequeue().doubleValue(); // java will throw divide by zero exception, no need to do it ourselves
            case "-":
                return arguments.dequeue().doubleValue() - arguments.dequeue().doubleValue();
            case "$":
                return -arguments.dequeue().doubleValue();
            case "%":
                return arguments.dequeue().doubleValue() % arguments.dequeue().doubleValue();
            case "^":
                return Math.pow(arguments.dequeue().doubleValue(), arguments.dequeue().doubleValue());
            case "sqrt":
                return Math.sqrt(arguments.dequeue().doubleValue());
            case "ln":
                return Math.log(arguments.dequeue().doubleValue());
            case "log":
                return Math.log(arguments.dequeue().doubleValue()) / Math.log(2);
            case "abs":
                return abs(arguments.dequeue().doubleValue());
            case "cos":
                return Math.cos(arguments.dequeue().doubleValue());
            case "sin":
                return Math.sin(arguments.dequeue().doubleValue());
            case "tan":
                return Math.tan(arguments.dequeue().doubleValue());
            case "max":
                if (arguments.size() == 0) {
                    return 0.0;
                } else if (arguments.size() == 1) {
                    return arguments.dequeue().doubleValue();
                } else {
                    return max(arguments.dequeue().doubleValue(), evaluateFunction("max", arguments));
                }
            case "min":
                if (arguments.size() == 0) {
                    return 0.0;
                } else if (arguments.size() == 1) {
                    return arguments.dequeue().doubleValue();
                } else {
                    return min(arguments.dequeue().doubleValue(), evaluateFunction("min", arguments));
                }
            default:
                return checkSymbolTable(token);
        }
    }

    /**
     * A basic max function that returns the maximum value of the arguments
     * @param n left number
     * @param m right number
     * @return the greater of the two
     */
    private double max(double n, double m) {
        return n > m ? n : m;
    }

    /**
     * A basic min function that returns the minimum value of the arguments
     * @param n left number
     * @param m right number
     * @return the lesser of the two
     */
    private double min(double n, double m) {
        return n < m ? n : m;
    }

    /**
     * A method that returns the absolute value of the argument passed
     * @param n the number to absolute
     * @return the absolute value of the argument
     */
    private double abs(double n) {
        return n > 0 ? n : -n;
    }

    /**
     * Method evaluates an assignment ASTNode and returns a string representation of the variable that was created
     * @param node any assignment node
     * @return string representation of the variable. E.g., x=2 => {@literal <}assignment:x{@literal >}
     * @throws EvaluatorException if assignment cannot be evaluated
     */
    public String evaluateAssignment(ASTNode node) throws EvaluatorException {
        ASTNode symbol = node.getChildren().pop();

        Number value = evaluate(node.getChildren().pop());
        symbolTable.placeOrUpdate(symbol.token().getKey(), value.doubleValue());

        return "<assignment:" + symbol.token().getKey() + ">";
    }

    /**
     * Method evaluates a given AST tree structure and returns the result of the expression the tree represents.
     *      +
     *     / \    ==>   5
     *    2   3
     * All leaves in the tree should, and must, be a value. Meaning that all leaves are either variables or numbers.
     * Post order traversal is used when traversing the tree.
     * @param node the root node of the AST tree
     * @return The result of the expression the AST tree represents.
     * @throws EvaluatorException if evaluation fails
     */
    public Number evaluate(ASTNode node) throws EvaluatorException {
        if (node.token().isAssignment()) {
            throw new EvaluatorException("Can't assign values in expressions, values must be assigned before or after expressions");
        }

        if (node.token().isNumber()) {
            return Double.parseDouble(node.token().getKey());
        }

        Queue<Number> arguments = new Queue<>(new List<>());
        
        while (!node.getChildren().isEmpty()) {
            arguments.enqueue(evaluate(node.getChildren().pop()));
        }

        return evaluateFunction(node.token().getKey(), arguments);
    }
}
