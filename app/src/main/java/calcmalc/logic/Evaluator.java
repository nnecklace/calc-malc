package calcmalc.logic;

import calcmalc.structures.ASTNode;
import calcmalc.structures.HashTable;
import calcmalc.structures.Queue;
import calcmalc.structures.Stack;
import calcmalc.exceptions.EvaluatorException;

/**
 * @author nnecklace
 */
public class Evaluator {
    private HashTable<String, Integer> functionArity = new HashTable<>();
    private HashTable<String, Double> symbolTable = new HashTable<>();
    private HashTable<String, ASTNode> customFunctionBodies = new HashTable<>();
    private HashTable<String, FunctionArgumentTuple> customFunctionArguments = new HashTable<>();
    private Stack<String> contexts = new Stack<>();

    private class FunctionArgumentTuple {
        String first;
        String second;
        public void setNext(String next) {
            if (first == null) {
                first = next;
            } else {
                second = next;
            }
        }
    }

    private String concat(String start, String middle, String end) {
        int size = start.length() + middle.length() + end.length();
        char[] combined = new char[size];
        int i = 0;
        for (Character c : start.toCharArray()) {
            combined[i++] = c;
        }
        for (Character c : middle.toCharArray()) {
            combined[i++] = c;
        }
        for (Character c : end.toCharArray()) {
            combined[i++] = c;
        }

        return new String(combined, 0, size);
    }

    /**
     * Constructor for the Evaluator
     * Constructor initializes the config table with built in functions and operators
     */
    public Evaluator() { 
        functionArity.placeOrUpdate("+", 2);
        functionArity.placeOrUpdate("-", 2);
        functionArity.placeOrUpdate("*", 2);
        functionArity.placeOrUpdate("/", 2);
        functionArity.placeOrUpdate("^", 2);
        functionArity.placeOrUpdate("%", 2);
        functionArity.placeOrUpdate("$", 1);
        functionArity.placeOrUpdate("sqrt", 1);
        functionArity.placeOrUpdate("log", 1);
        functionArity.placeOrUpdate("abs", 1);
        functionArity.placeOrUpdate("cos", 1);
        functionArity.placeOrUpdate("sin", 1);
        functionArity.placeOrUpdate("tan", 1);
        functionArity.placeOrUpdate("max", -1);
        functionArity.placeOrUpdate("min", -1);
    }

    /**
     * Method checks that the given symbol (function) has the correct amount of arguments
     * @param symbol the symbol to check
     * @param argumentsCount the given number of arguments given to the symbol
     * @throws EvaluatorException if wrong number of arguments
     */
    private void checkArguments(String symbol, int argumentsCount) throws EvaluatorException {
        Integer value = functionArity.get(symbol);
        if (value != null && value != argumentsCount && value != -1) {
            // in exceptions we use the concat operator for strings
            throw new EvaluatorException("Wrong number of arguments for " + symbol);
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
        if (!contexts.isEmpty()) {
            symbolValue = symbolTable.get(concat(contexts.peek(), "@", token));
        }

        if (symbolValue == null) {
            symbolValue = symbolTable.get(token);
        }

        if (symbolValue != null) {
            return symbolValue;
        }

        ASTNode node = customFunctionBodies.get(token);

        if (node != null) {
            FunctionArgumentTuple argumentPair = customFunctionArguments.get(token);

            symbolTable.placeOrUpdate(concat(token, "@", argumentPair.first), arguments.dequeue().doubleValue());

            if (argumentPair.second != null) {
                symbolTable.placeOrUpdate(concat(token, "@", argumentPair.second), arguments.dequeue().doubleValue());
            }

            contexts.push(token);
            Double result = (double) evaluate(node);
            contexts.pop();

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
            case "log":
                return Math.log(arguments.dequeue().doubleValue());
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

    private <N extends Number> double minOrMax(String minOrMax, Queue<N> arguments) {
        if (arguments.size() == 1) {
            return arguments.dequeue().doubleValue();
        } else {
            if (minOrMax.equals("min")) {
                return min(arguments.dequeue().doubleValue(), arguments.dequeue().doubleValue());
            }
            return max(arguments.dequeue().doubleValue(), arguments.dequeue().doubleValue());
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
        if (node.children().size() < 2) {
            throw new EvaluatorException("Assignment error: Assignment operator should have a symbol to assign and a value to assign too");
        }

        ASTNode symbol = node.children().get(0);

        if (symbol.token().isFunction()) {
            FunctionArgumentTuple argumentTuple = new FunctionArgumentTuple();

            int size = symbol.children().size();

            if (size > 2) {
                throw new EvaluatorException("Custom function can only be given max two arguments");
            }

            // dfs will run max 2 times
            for (int i = 0; i < size; ++i) {
                ASTNode child = symbol.children().get(i);
                if (!child.token().isSymbol()) {
                    throw new EvaluatorException("Custom function arguments have to be symbols");
                }
                argumentTuple.setNext(child.token().getKey());
            }

            functionArity.placeOrUpdate(symbol.token().getKey(), size); 
            customFunctionBodies.placeOrUpdate(symbol.token().getKey(), node.children().get(1));
            customFunctionArguments.placeOrUpdate(symbol.token().getKey(), argumentTuple);

        } else {
            symbolTable.placeOrUpdate(
                symbol.token().getKey(),
                evaluate(node.children().get(1)).doubleValue()
            );
        }


        return concat("<assignment:", symbol.token().getKey(), ">");
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

        for (int i = 0; i < node.children().size(); ++i) {
            arguments.enqueue(evaluate(node.children().get(i)));
            if (arguments.size() == 2 && i < (node.children().size() - 1)) {
                // in this case the function takes more than two arguments
                arguments.enqueue(
                    evaluateFunction(node.token().getKey(), arguments)
                );
            }
        }

        return evaluateFunction(node.token().getKey(), arguments);
    }
}
