package calcmalc.logic;

import calcmalc.structures.ASTNode;
import calcmalc.structures.HashTable;
import calcmalc.structures.Queue;
import calcmalc.exceptions.EvaluatorException;
import calcmalc.logic.types.Token;

/**
 * @author nnecklace
 */
public class Evaluator {
    private HashTable<String, Integer> config = new HashTable<>();
    private HashTable<String, Double> symbolTable = new HashTable<>();
    private HashTable<String, ASTNode> customFunctionTable = new HashTable<>();
    private HashTable<String, FunctionArgumentPair> customFunctionArguments = new HashTable<>();
    private String context;

    private class FunctionArgumentPair {
        String first;
        String second;
        int argumentCount() {
            if (second != null) {
                return 2;
            }

            return 1;
        }
    }

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
        if ((value != null && value != argumentsCount && value != -1)) {
            throw new EvaluatorException("Wrong number of arguments " + symbol);
        }
    }

    /**
     * Checks if the given symbol (variable) has been defined during runtime
     * @param token The token to check for
     * @return the value associated with the symbol
     * @throws EvaluatorException if symbol is unknown
     */
    private <N extends Number> double checkSymbolAndFunctionTable(String token, Queue<N> arguments) throws EvaluatorException {
        Double symbolValue = null;
        if (context != null) {
            symbolValue = symbolTable.get(context + "_" + token);
        }

        if (symbolValue == null) {
            symbolValue = symbolTable.get(token);
        }

        if (symbolValue != null) {
            return symbolValue;
        }

        ASTNode node = customFunctionTable.get(token);

        if (node != null) {
            FunctionArgumentPair argumentPair = customFunctionArguments.get(token);

            symbolTable.placeOrUpdate(token + "_" + argumentPair.first, arguments.dequeue().doubleValue());

            if (argumentPair.second != null) {
                symbolTable.placeOrUpdate(token + "_" + argumentPair.second, arguments.dequeue().doubleValue());
            }

            context = token;
            Double result = (double) evaluate(node);
            context = null;

            return result;
        }


        throw new EvaluatorException("Unknown Symbol " + token);
    }

    /**
     * Method tries to evaluate the given token, be it a function or variable symbol
     * @param <N> Let n be any java Number type
     * @param token the token to be evaluated
     * @param arguments the queue of arguments for the token
     * @return Whatever result the symbol represents with the given arguments
     * @throws EvaluatorException if symbol is unknown or a function was given an incorrect number of arguments
     */
    public <N extends Number> double evaluateFunction(String token, Queue<N> arguments) throws EvaluatorException {
        checkArguments(token, arguments.size());
        switch (token) {
            case "*":
                return arguments.dequeue().doubleValue() * arguments.dequeue().doubleValue();
            case "+":
                return arguments.dequeue().doubleValue() + arguments.dequeue().doubleValue();
            case "/":
                return arguments.dequeue().doubleValue() / arguments.dequeue().doubleValue(); 
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
                return minOrMax("max", arguments);
            case "min":
                return minOrMax("min", arguments);
            default:
                return checkSymbolAndFunctionTable(token, arguments);
        }
    }

    private <N extends Number> double minOrMax(String minOrMax, Queue<N> arguments) throws EvaluatorException {
        if (arguments.size() == 1) {
            return arguments.dequeue().doubleValue();
        } else {
            if (minOrMax.equals("min")) {
                return min(arguments.dequeue().doubleValue(), minOrMax("min", arguments));
            }
            return max(arguments.dequeue().doubleValue(), minOrMax("max", arguments));
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
        ASTNode symbol = node.children().get(node.children().size() - 1);

        if (!symbol.token().isFunction() && !symbol.token().isSymbol()) {
            throw new EvaluatorException("Assignment error: Can't assign variable to non-symbol or non-function");
        }

        if (symbol.token().isFunction()) {
            FunctionArgumentPair argumentPair = new FunctionArgumentPair();

            int size = symbol.children().size();

            if (size > 2) {
                throw new EvaluatorException("Custom function can only be given max two arguments");
            }

            Token firstArg = symbol.children().get(size - 1).token();
            argumentPair.first = firstArg.getKey();

            Token secondArg = null;

            if (size - 2 == 0) {
                secondArg = symbol.children().get(size - 2).token(); 
                argumentPair.second = secondArg.getKey();
            }

            if (!firstArg.isSymbol() || (secondArg != null && !secondArg.isSymbol())) {
                throw new EvaluatorException("Custom function arguments have to be symbols");
            }

            config.placeOrUpdate(symbol.token().getKey(), argumentPair.argumentCount()); 
            customFunctionTable.placeOrUpdate(symbol.token().getKey(), node.children().get(node.children().size() - 2));
            customFunctionArguments.placeOrUpdate(symbol.token().getKey(), argumentPair);

        } else {
            Number value = evaluate(node.children().get(node.children().size() - 2));
            symbolTable.placeOrUpdate(symbol.token().getKey(), value.doubleValue());
        }


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

        Queue<Number> arguments = new Queue<>();
        
        for (int i = node.children().size() - 1; i >= 0; --i) {
            arguments.enqueue(evaluate(node.children().get(i))); // O(1) since functions or operators can have 1 or 2 arguments
        }

        return evaluateFunction(node.token().getKey(), arguments);
    }
}
