package calcmalc.logic;

import calcmalc.structures.Stack;
import calcmalc.structures.Queue;
import java.text.ParseException;

public class Parser {
    private Stack<String> operators;
    private Queue<String> output;

    public Parser(Stack<String> operators, Queue<String> output) {
        this.operators = operators;
        this.output = output;
    }

    private boolean isNumber(String token) {
        return token != null && token.matches("[0-9]+");
    }

    private boolean isOperator(String token) {
        return token != null && token.matches("\\+|\\*|/|\\-");
    }

    private boolean isOpenParenthesis(String token) {
        return token != null && token.matches("\\(");
    }

    private boolean isCloseParenthesis(String token) {
        return token != null && token.matches("\\)");
    }

    private int precedenceLevel(String token) {
        if (token == null) {
            return -1;
        }

        if (token.equals("*") || token.equals("/")) {
            return 2;
        }

        if (token.equals("+") || token.equals("-")) {
            return 1;
        }

        return 0;
    }

    public void parse(String input) throws ParseException {
        // 2 * 2 + 6 + 4 - 10 / 2
        for (String token : input.split(" ")) {
            if (isNumber(token)) {
                output.enqueue(token);
                continue;
            }
            
            if (isOperator(token)) {
                while (!operators.isEmpty() && !isOpenParenthesis(operators.peek()) && precedenceLevel(operators.peek()) >= precedenceLevel(token)) {
                    String operator = operators.pop();
                    output.enqueue(operator);
                }
                operators.push(token);
                continue;
            }

            if (isOpenParenthesis(token)) {
                operators.push(token);
                continue;
            }

            if (isCloseParenthesis(token)) {
                while (!isOpenParenthesis(operators.peek())) {
                    String operator = operators.pop();
                    output.enqueue(operator);
                }

                if (!isOpenParenthesis(operators.peek())) {
                    throw new ParseException("Syntax error missing parenthesis!", 0);
                }

                operators.pop();
            }
        }

        while (!operators.isEmpty()) {
            String operator = operators.pop();

            if (isOpenParenthesis(operator)) {
                throw new ParseException("Syntax error missing parenthesis", 0);
            }

            output.enqueue(operator);
        }
    }

    public String show() {
        String o = "";
        while (!output.isEmpty()) {
            o += output.dequeue();
        }

        return o;
    }
}
