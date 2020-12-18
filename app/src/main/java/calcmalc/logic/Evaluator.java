package calcmalc.logic;

import calcmalc.structures.ASTNode;
import calcmalc.structures.HashTable;
import calcmalc.structures.Queue;
import calcmalc.structures.Stack;
import calcmalc.exceptions.EvaluatorException;

/**
 * Evaluator for the program. 
 * The evaluator evaluates AST (abstract syntax trees) using dfs (depth first search) and post order traversal
 * The class will walk (traverse) through the tree and return some value
 * The evaluation happens bottom up
 * Time complexity for the evaluator is O(V + E) where V is the number of nodes in the tree and E is the number of edges. All nodes and edges will be visited exactly once.
 * Evaluator uses more memory than the any other class
 * Space complexity is O(n^2) where n is the number of custom functions
 * In the average case space complexity is θ(n)
 * Examples
 * <pre>
 *      +
 *     / \   => 4
 *    2   2
 * 
 *     max
 *    / | \  => 5
 *   2  4  5
 * 
 *     abs
 *      |
 *      +
 *     / \      =>  1
 *    *   1
 *   / \
 *  2   $
 *      |
 *      1
 * </pre>
 * @author nnecklace
 */
public class Evaluator {
    /**
     * function arity HashTable contains all the operators, and functions encountered so far,
     * the count of arguments the operator, or functions, is expected to be given
     * <pre>
     * <abs, 1>
     * <max, -1> -1 means unlimited
     * <+, 2>
     * </pre>
     */
    private HashTable<Integer> functionArity = new HashTable<>();
    /**
     * Symbol table, also known as variable table, contains
     * all variables and their values. Variables are always
     * evaluated immediatly and their value is stored in this table
     * Future reuse will just lookup the variable value instead of evaluating it again
     */
    private HashTable<Double> symbolTable = new HashTable<>(); 
    /**
     * Custom function bodies table works like symbol table. Custom functions have a body.
     * Functions can't be evaluated immediately, unlike variables, so the function body will be stored in this table
     * Every time a custom function is called, its body will be retrieved and evaluated with the parameter the function was given
     * <pre>
     * 
     * fn(x) = 1+x:
     *          ^   this is the body of the custom function
     * 
     * </pre>
     */
    private HashTable<ASTNode> customFunctionBodies = new HashTable<>();
    /**
     * Custom function arguments table contains the arguments (list of symbols given to the function).
     * Custom functions are given some arguments, as symbols, and these symbols are stored in this hashtable.
     * The symbols are used when custom functions are evaluated. We need to know what value corresponds to what symbol
     * <pre>
     * add(x,y) = x + y:
     *     ^ ^  these are the symbol arguments for the custom function
     * 
     * </pre>
     * 
     * Ideally there would be only one hashtable that contains both informations that customFunctionBodies and customFunctionArguments contain
     */
    private HashTable<Queue<String>> customFunctionArguments = new HashTable<>();
    /**
     * Hashtable contains all the standard library functions, there is no hashset class
     * so we use hashtable as a hashset. 
     * Table is meant to quickly check that the standard library functions are not overwritten by the user
     */
    private HashTable<Boolean> standardLibraryFunctions = new HashTable<>();

    /**
     * Hashtable contains all the custom arguments (symbols) and their values
     * This is needed since when custom functions are evaluated they will have some context to what the symbols mean
     * 
     * Example
     * <pre>
     * x = 2:
     * 
     * fn(val) = 1+val:
     * 
     * double(x) = 1 + val(x):
     * 
     * double(1)
     * 
     * </pre>
     * 
     * In this case the val function will first lookup the current context, which is double
     * <pre>double -> x -> 1</pre>
     * In the context of double, x refers to 1, in the global context x refers to 2
     * Every time a custom function is called, a new entry (or updated) will be made in the contextSymbolTable
     *<pre> 
     * double(2)+fn(3)
     * Two entries will be made
     * 
     * double -> x = 2
     * fn -> val = 3
     * </pre>
     */
    private HashTable<HashTable<Double>> contextSymbolTable = new HashTable<>();
    /**
     * A stack which contains the current context the function is in
     * if the stack is empty every symbol lookup will be in the global context
     * Otherwise it will first lookup the symbol from some function context and if that fails
     * It will then look it up in the global context
     */
    private Stack<String> contexts = new Stack<>();

    private static final double NAN = 0.0d / 0.0;

    /**
     * Basic string concat function
     * Concatenates three strings togerther, ideally would use string builder or some such
     * But that is not allowed some we do this instead
     * @param start first string
     * @param middle second string
     * @param end thrid string
     * @return the three strings concatenated together
     */
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
     * Constructor initializes the function arity table with built in functions and operators and their argument count
     * Also initializes the standard library functions table with all the built in functions
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

        standardLibraryFunctions.placeOrUpdate("sqrt", true);
        standardLibraryFunctions.placeOrUpdate("log", true);
        standardLibraryFunctions.placeOrUpdate("abs", true);
        standardLibraryFunctions.placeOrUpdate("cos", true);
        standardLibraryFunctions.placeOrUpdate("sin", true);
        standardLibraryFunctions.placeOrUpdate("tan", true);
        standardLibraryFunctions.placeOrUpdate("max", true);
        standardLibraryFunctions.placeOrUpdate("min", true);
    }

    /**
     * Method checks that the given function or operator has the correct amount of arguments
     * @param function the function or operator to check
     * @param argumentsCount the given number of arguments given to the symbol
     * @throws EvaluatorException if wrong number of arguments
     */
    private void checkArguments(String function, int argumentsCount) throws EvaluatorException {
        Integer value = functionArity.get(function);
        if (value != null && value != argumentsCount && value != -1) {
            // in exceptions we use the concat operator for strings
            throw new EvaluatorException("Wrong number of arguments for " + function);
        }
    }

    /**
     * Checks if the given symbol (variable) has been defined during runtime
     * @param token The token to check for
     * @return the value associated with the symbol or null if the token is unknown
     */
    private Double checkSymbolAndContextTable(String token) {
        Double symbolValue = null;
        if (!contexts.isEmpty()) {
            HashTable<Double> contextTable = contextSymbolTable.get(contexts.peek());
            symbolValue = contextTable.get(token);
        }

        if (symbolValue == null) {
            symbolValue = symbolTable.get(token);
        }

        return symbolValue;
    }

    /**
     * Method tries to evaluate the given token. If this method is called the token has to be an Stl function or it is unknown
     * @param <N> Let n be any java Number type
     * @param token the token to be evaluated
     * @param arguments the queue of arguments for the token
     * @return Whatever result the symbol represents with the given arguments
     * @throws EvaluatorException if symbol is unknown or a function was given an incorrect number of arguments
     */
    public <N extends Number> double evaluateStlFunction(String token, Queue<N> arguments) throws EvaluatorException {
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
                return pow(arguments.dequeue().doubleValue(), arguments.dequeue().doubleValue());
            case "sqrt":
                return Math.sqrt(arguments.dequeue().doubleValue()); // check custom square root function for reason why we use this
            case "log":
                return Math.log(arguments.dequeue().doubleValue()); // check custom log function for reason of why we use this
            case "abs":
                return abs(arguments.dequeue().doubleValue());
            case "max":
                return minOrMax("max", arguments);
            case "min":
                return minOrMax("min", arguments);
            default:
                throw new EvaluatorException("Unknown Symbol " + token);
        }
    }

    /**
     * Method is helper function to determine of or max value of the arguments
     * @param <N> Let N be any java number type
     * @param minOrMax string to determine if the method should evaluate min or max of the arguments
     * @param arguments the arguments queue, at this point the arguments queue will only contain at most two parameters
     *                  check evaluate method for explanation
     * @return the max or min of the arguments
     */
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
     * A function for calculating the squre root of a positive number
     * currently not in use since it is much less accurate than the java's standard sqrt
     * testing and verifying correct results become difficult. Performance is not an issue, but accuracy is
     * @param n the number to find the squre root of
     * @return the square root of the param number
     */
    private double sqrt(double n) {
        if (n < 0.0) {
            return NAN;
        }

        if (n == 0.0) {
            return 0.0;
        }

        double x = n;
        double root;
        // newtons method
        while (true) {  
            root = 0.5 * (x + n / x);  
            if (abs(root - x) < 1) {
                break;
            }
            x = root;  
        }  
        
        return root;
    }

    /**
     * Highly inaccurate implementation of logarithm function. 
     * Function uses Taylor series expansion to converge the natural logarithm. Very slow
     * For log(100) takes about 42 seconds to compute. Practically unsuable and also very inaccurate.
     * https://www.efunda.com/math/taylor_series/logarithmic.cfm 
     * @param n the logarithm of the value
     * @return the natural log of param value
     */
    private double log(double n) {
        int precision = 1000;
        double ln = 0.0;
        for (int i = 1; i <= precision; i = i + 2) {
            ln += ((1.0 / i) * pow((n - 1) / (n + 1), i));
        }

        return 2 * ln;
    }

    /**
     * Calculates the exponential value of a number. Doesn't calculate fractional exponenets.
     * @param n base value
     * @param e the exponent value
     * @return n^e
     */
    private double pow(double n, double e) {
        // we don't handle fractional exponents :(
        if (e % 1 != 0) {
            return NAN;
        }

        double result = 1.0;

        if (e == 0.0) {
            return result;
        }

        for (int i = 0; i < abs(e); ++i) {
            result *= n;
        }

        if (e < 0.0) {
            return 1.0 / result;
        } 

        return result;
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
     * Method evaluates an assignment ASTNode and returns a string representation of the variable or custom functio that was created
     * 
     * Examples:
     * <pre>
     * 
     *           =
     *         /   \
     *       sum      +
     *      / | \    / \
     *     x  y  z   x  +
     *                 / \
     *                x   z
     * 
     *         =
     *        / \
     *       x   2
     * 
     * </pre>
     * @param node any assignment node
     * @return string representation of the variable. E.g., x=2 => {@literal <}assignment:x{@literal >}
     * @throws EvaluatorException if assignment cannot be evaluated or the custom function was given illegal arguments
     */
    public String evaluateAssignment(ASTNode node) throws EvaluatorException {
        if (node.children().size() < 2) {
            throw new EvaluatorException("Assignment error: Assignment operator should have a symbol to assign and a value to assign too");
        }

        ASTNode symbol = node.children().get(0);
        String symbolName = symbol.token().getKey();

        if (symbol.token().isFunction()) {
            Queue<String> argumentSymbols = new Queue<>();

            int size = symbol.children().size();

            for (int i = 0; i < size; ++i) {
                ASTNode child = symbol.children().get(i);
                if (!child.token().isSymbol()) {
                    throw new EvaluatorException("Custom function arguments have to be symbols");
                }
                argumentSymbols.enqueue(child.token().getKey());
            }

            functionArity.placeOrUpdate(symbolName, size);
            customFunctionBodies.placeOrUpdate(symbolName, node.children().get(1));
            customFunctionArguments.placeOrUpdate(symbolName, argumentSymbols);

        } else {
            symbolTable.placeOrUpdate(
                symbolName,
                evaluate(node.children().get(1)).doubleValue()
            );
        }


        return concat("<assignment:", symbolName, ">");
    }

    /**
     * Method tries to evaluate custom fuction calls. Custom functions are user made functions and don't exist in the stl hashtable.
     * 
     * Example
     * <pre>
     * custom funiction node:
     * 
     * =>     double
     *          |
     *          5 
     * 
     * FunctionBodyTable double -> [x*2]
     * Node children 5
     * Function Arguments table double -> [x]
     * context table double -> x = 2
     * 
     * Function body  =>   *
     *                    / \   =>  2*2 => 4
     *                   x   2                 
     * </pre>
     * @param node custom function node
     * @return result of the evaluated body of the custom function
     * @throws EvaluatorException if the function is unknown, i.e., doesn't have a function body
     */
    private Number evaluateCustomFunction(ASTNode node) throws EvaluatorException {
        String functionName = node.token().getKey();

        checkArguments(functionName, node.children().size());

        ASTNode body = customFunctionBodies.get(functionName);

        if (body == null) {
            throw new EvaluatorException("Unknown function " + functionName);
        }

        // This hack is made because java always retrieves class by reference so when we dequeue
        // from argumentSymbols we actually change the queue that is stored inside the Hashtable
        // we solve this by making a new queue and adding each symbol to it so it can be reused later
        // we add this new queue argumentSymbolsAgain to the Hashtable.
        // argumentSymbols and arguments will always be the same length.
        // checkArguments function will detect if the function was given incorrect amount of arguments
        Queue<String> argumentSymbolsAgain = new Queue<>();
        Queue<String> argumentSymbols = customFunctionArguments.get(functionName);
        HashTable<Double> contextTableEntry = new HashTable<>();

        for (int i = 0; i < node.children().size(); ++i) {
            String argumentSymbol = argumentSymbols.dequeue();
            contextTableEntry.placeOrUpdate(argumentSymbol, evaluate(node.children().get(i)).doubleValue());
            argumentSymbolsAgain.enqueue(argumentSymbol);
        }

        contextSymbolTable.placeOrUpdate(functionName, contextTableEntry);
        customFunctionArguments.placeOrUpdate(functionName, argumentSymbolsAgain);

        contexts.push(functionName);
        Double result = (double) evaluate(body);
        contexts.pop();

        return result;
    }

    /**
     * Method evaluates a given AST tree structure and returns the result of the expression the tree represents.
     * <pre>
     *      +
     *     / \    ==>   5
     *    2   3
     * 
     * </pre>
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

        String nodeTokenKey = node.token().getKey();

        if (node.token().isNumber()) {
            return Double.parseDouble(nodeTokenKey);
        }

        // Node is a leaf symbol
        if (node.token().isSymbol() && standardLibraryFunctions.get(nodeTokenKey) == null) {
            Double possibleValue = checkSymbolAndContextTable(nodeTokenKey);
            if (possibleValue != null) {
                return possibleValue;
            }
        }

        if (node.token().isFunction() && standardLibraryFunctions.get(nodeTokenKey) == null) {
            return evaluateCustomFunction(node);
        }

        Queue<Number> arguments = new Queue<>();

        for (int i = 0; i < node.children().size(); ++i) {
            arguments.enqueue(evaluate(node.children().get(i)));
            if (arguments.size() == 2 && i < (node.children().size() - 1)) {
                // evaluate max and min two arguments at a time
                // in this case the function takes more than two arguments
                arguments.enqueue(
                    evaluateStlFunction(node.token().getKey(), arguments)
                );
            }
        }

        return evaluateStlFunction(node.token().getKey(), arguments);
    }
}
