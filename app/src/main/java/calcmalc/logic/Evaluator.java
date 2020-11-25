package calcmalc.logic;

import calcmalc.structures.ASTNode;
import calcmalc.structures.HashTable;
import calcmalc.structures.List;
import calcmalc.structures.Queue;
import calcmalc.exceptions.EvaluatorException;

public class Evaluator {
    private HashTable<String, Integer> config = new HashTable<>();
    private HashTable<String, Double> symbolTable = new HashTable<>();

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

    private void checkArguments(String symbol, int argumentsCount) {
        Integer value = config.get(symbol);
        if (value != null && value != argumentsCount && value != -1) {
            throw new IllegalArgumentException("Wrong number of arguments " + symbol);
        }
    }

    private Double checkSymbolTable(String token) throws EvaluatorException {
        Double symbol = symbolTable.get(token);
        if (symbol != null) {
            return symbol;
        }

        throw new EvaluatorException("Unknown Symbol " + token);
    }

    public <N extends Number> double evaluateFunction(String token, Queue<N> arguments) throws EvaluatorException {
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
                return arguments.size() > 1 ? 
                    max(arguments.dequeue().doubleValue(), evaluateFunction("max", arguments)) :
                    max(arguments.dequeue().doubleValue(), 0.0);
            case "min":
                return arguments.size() > 1 ?
                    min(arguments.dequeue().doubleValue(), evaluateFunction("min", arguments)) :
                    min(arguments.dequeue().doubleValue(), Double.MAX_VALUE);
            default:
                return checkSymbolTable(token);
        }
    }

    private double max(double n, double m) {
        return n > m ? n : m;
    }

    private double min(double n, double m) {
        return n < m ? n : m;
    }

    private double abs(double n) {
        return n > 0 ? n : -n;
    }

    public String evaluateAssignment(ASTNode node) throws EvaluatorException {
        ASTNode symbol = node.getChildren().getLast();

        node.getChildren().remove(node.getChildren().size());
        Number value = evaluate(node.getChildren().get(0));
        symbolTable.placeOrUpdate(symbol.token().getKey(), value.doubleValue());

        return "<assignment:" + symbol.token().getKey() + ">";
    }

    public Number evaluate(ASTNode node) throws EvaluatorException {
        if (node.token().isAssignment()) {
            throw new EvaluatorException("Can't assign values in expressions, values must be assigned before or after expressions");
        }

        if (node.token().isNumber()) {
            return Double.parseDouble(node.token().getKey());
        }

        Queue<Number> arguments = new Queue<>(new List<>());
        
        while (!node.getChildren().isEmpty()) {
            arguments.enqueue(evaluate(node.getChildren().getLast()));
            node.getChildren().remove(node.getChildren().size());
        }

        return evaluateFunction(node.token().getKey(), arguments);
    }
}
