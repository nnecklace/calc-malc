package calcmalc.logic;

import calcmalc.structures.List;
import calcmalc.structures.Listable;
import calcmalc.exceptions.LexerException;
import calcmalc.logic.types.*;

public class Lexer {
    /**
     * Function lexes, gives each character a token, the given arthemetic expression
     * @param expression the string representing the arthemetic expression we want to lex
     * @return List of lexical tokens representing the given symbols in the input
     * @throws ParseException if expression contains invalid characters
     */
    public Listable<Token> lex(String expression) throws LexerException {
        Listable<Token> tokens = new List<>();
        for (int i = 0; i < expression.length(); ++i) {
            String c = Character.toString(expression.charAt(i));
            if (c.matches("\\+|\\*|/|\\-|\\^|\\%")) {
                // check if operator is unary - operator
                if ("-".equals(c) && (tokens.isEmpty() || !tokens.getLast().isNumber())) {
                    // unary minus operator
                    tokens.push(new Operator("$"));
                } else {
                    tokens.push(new Operator(c));
                }
            } else if (c.matches("[0-9]")) {
                StringBuilder number = new StringBuilder();
                number.append(c);
                i = scan(expression, number, i, "[0-9]");
                tokens.push(new Numeric(number.toString()));
            } else if (c.matches("[_a-zA-Z]")) {
                StringBuilder symbol = new StringBuilder();
                symbol.append(c);
                i = scan(expression, symbol, i, "[_a-zA-Z]");
                tokens.push(new Symbol(symbol.toString()));
            } else if (c.matches("\\(") || c.matches("\\)") || c.matches(",")) {
                tokens.push(new Empty(c));
            } else {
                throw new LexerException("Unknown character");
            }
        }

        return tokens;
    }

    /**
     * Method reads all character until it can no longer match the following character with the givin regex.
     * Perhaps a bit needlessly complicated function
     * @param expression the String expression the algoritm is going to evaluate
     * @param tokenName the token name, all the characters that match the regex will be append to this token name
     * @param i the position in the expression we are currently in
     * @param pattern the regex to match
     * @return the position in the expression where we no longer match with the regex
     */
    private int scan(String expression, StringBuilder tokenName, int i, String pattern) {
        int pos = i;
        while (pos + 1 < expression.length() && Character.toString(expression.charAt(pos + 1)).matches(pattern)) {
            tokenName.append(expression.charAt(pos + 1));
            pos++;
        }
        return pos;
    }
}
