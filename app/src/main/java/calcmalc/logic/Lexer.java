package calcmalc.logic;

import calcmalc.structures.List;
import calcmalc.structures.Queue;
import calcmalc.exceptions.LexerException;
import calcmalc.logic.types.*;

/**
 * Lexical analysis class. Class reads character from the input and gives them  values (tokens)
 * For the parser
 * @author nnecklace
 */
public class Lexer {
    /**
     * Function lexes, gives each character a token, the given arthemetic expression
     * @param expression the string representing the arthemetic expression we want to lex
     * @return List of lexical tokens representing the given symbols in the input
     * @throws LexerException if expression contains invalid characters
     */
    public Queue<Token> lex(String expression) throws LexerException {
        Queue<Token> tokens = new Queue<>();
        for (int i = 0; i < expression.length(); ++i) {
            String c = Character.toString(expression.charAt(i));
            if (c.matches("\\+|\\*|/|\\-|\\^|\\%")) {
                // check if operator is unary - operator
                // ,-100
                // -x
                // (-100)
                if ("-".equals(c) && (tokens.isEmpty() || tokens.peekLast().getKey().equals("(") || tokens.peekLast().getKey().equals(","))) {
                    // unary minus operator
                    tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, "$"));
                } else {
                    tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, c));
                }
            } else if (c.matches("[.0-9]")) {
                StringBuilder number = new StringBuilder();
                number.append(c);
                i = scan(expression, number, i, "[.0-9]");
                tokens.enqueue(TypeBuilder.buildToken(Types.NUMERIC, number.toString()));
            } else if (c.matches("=")) {
                tokens.enqueue(TypeBuilder.buildToken(Types.ASSIGNMENT, "="));
            } else if (c.matches("[_a-zA-Z]")) {
                StringBuilder symbol = new StringBuilder();
                symbol.append(c);
                i = scan(expression, symbol, i, "[_a-zA-Z]");
                tokens.enqueue(TypeBuilder.buildToken(Types.SYMBOL, symbol.toString()));
            } else if (c.matches("\\(") || c.matches("\\)") || c.matches(",") || c.matches(":")) {
                if (c.matches("\\(") && !tokens.isEmpty() && tokens.peekLast().isSymbol()) {
                    tokens.peekLast().setType(Types.FUNCTION);
                }
                tokens.enqueue(TypeBuilder.buildToken(Types.EMPTY, c));
            } else {
                String errorAt = String.format("%" + (i + 1) + "s", "^");
                throw new LexerException("Unknown character " + c + " at position " + (i + 1) + " in expression " + expression + "\n" + expression + "\n" + errorAt);
            }
        }

        return tokens;
    }

    /**
     * Method reads all characters until it can no longer match the following character with the givin regex.
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
