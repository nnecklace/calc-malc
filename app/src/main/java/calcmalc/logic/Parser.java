package calcmalc.logic;

import calcmalc.structures.Stack;
import calcmalc.structures.Queue;
import java.text.ParseException;

/**
 * Parser for the Shunting Yard algoritm. Main objective of the class is to convert expressions into RPN (reverse polish notation)
 */
public class Parser {
    private Stack<String> operators;
    private Queue<String> output;

    public Parser(Stack<String> operators, Queue<String> output) {
        this.operators = operators;
        this.output = output;
    }

    /**
     * Method checks if the token is a type of number.
     * @param token String token to be checked
     * @return true if token is a type of number else false
     */
    private boolean isNumber(String token) {
        return token != null && token.matches("[0-9]+");
    }

    /**
     * Method checks if the token is a type of operator.
     * @param token String token to be checked
     * @return true if token is a type of operator else false
     */
    private boolean isOperator(String token) {
        return token != null && token.matches("\\+|\\*|/|\\-");
    }

    /**
     * Method checks if the token is a type of open parenthesis.
     * @param token String token to be checked
     * @return true if token is a type of open parenthesis else false
     */
    private boolean isOpenParenthesis(String token) {
        return token != null && token.matches("\\(");
    }

    /**
     * Method checks if the token is a type of closing parenthesis.
     * @param token String token to be checked
     * @return true if token is a type of closing parenthesis else false
     */
    private boolean isCloseParenthesis(String token) {
        return token != null && token.matches("\\)");
    }

     // TODO: Remove and create a data type for this precedence levels
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

    /**
     * Method accepts arthemetic expressions and converts them to RPN
     * Currently method assumes that inputs in the expression are separated by space
     * @param input the expression to be converted
     * @throws ParseException if illegal input
     */
    public void parse(String input) throws ParseException {
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

    /**
     * Method shows the RPN form of the expression that was parsed
     * @return the RPN form of the expression
     */
    public String show() {
        String o = "";
        while (!output.isEmpty()) {
            o += output.dequeue();
        }

        return o;
    }
}
