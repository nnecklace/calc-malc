package calcmalc.logic;

import java.util.HashMap;

import calcmalc.structures.ASTNode;
import calcmalc.structures.List;
import calcmalc.structures.Queue;
import calcmalc.exceptions.EvaluatorException;

public class Evaluator {
    private HashMap<String, Integer> config = new HashMap<>();
    private HashMap<String, Double> symbolTable = new HashMap<>();

    public Evaluator() { 
        config.put("+", 2);
        config.put("-", 2);
        config.put("*", 2);
        config.put("/", 2);
        config.put("^", 2);
        config.put("%", 2);
        config.put("$", 1);
        config.put("sqrt", 1);
        config.put("log", 1);
        config.put("ln", 1);
        config.put("abs", 1);
        config.put("cos", 1);
        config.put("sin", 1);
        config.put("tan", 1);
        config.put("max", -1);
        config.put("min", -1);
    }

    private void checkArguments(String symbol, int argumentsCount) {
        if (config.containsKey(symbol) && config.get(symbol) != argumentsCount && config.get(symbol) != -1) {
            throw new IllegalArgumentException("Wrong number of arguments " + symbol);
        }
    }

    private Double checkSymbolTable(String token) throws EvaluatorException {
        if (symbolTable.containsKey(token)) {
            return symbolTable.get(token);
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
                return Math.abs(arguments.dequeue().doubleValue());
            case "cos":
                return Math.cos(arguments.dequeue().doubleValue());
            case "sin":
                return Math.sin(arguments.dequeue().doubleValue());
            case "tan":
                return Math.tan(arguments.dequeue().doubleValue());
            case "max":
                return arguments.size() > 1 ? 
                    Math.max(arguments.dequeue().doubleValue(), evaluateFunction("max", arguments)) :
                    Math.max(arguments.dequeue().doubleValue(), 0.0);
            case "min":
                return arguments.size() > 1 ?
                    Math.min(arguments.dequeue().doubleValue(), evaluateFunction("min", arguments)) :
                    Math.min(arguments.dequeue().doubleValue(), Double.MAX_VALUE);
            default:
                return checkSymbolTable(token);
        }
    }

    public Number evaluate(ASTNode node) throws Exception {
        if (node.token().isNumber()) {
            return Double.parseDouble(node.token().getKey());
        }

        if (node.token().isAssignment()) {
            ASTNode symbol = node.getChildren().getLast();
            node.getChildren().remove(node.getChildren().size());
            Number value = evaluate(node.getChildren().get(0));
            symbolTable.put(symbol.token().getKey(), value.doubleValue());
            return 0.0;
        }

        Queue<Number> arguments = new Queue<>(new List<>());
        
        while (!node.getChildren().isEmpty()) {
            arguments.enqueue(evaluate(node.getChildren().getLast()));
            node.getChildren().remove(node.getChildren().size());
        }

        return evaluateFunction(node.token().getKey(), arguments);
    }
}
