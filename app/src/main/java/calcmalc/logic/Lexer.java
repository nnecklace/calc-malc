package calcmalc.logic;

import calcmalc.structures.List;
import calcmalc.structures.Listable;
import calcmalc.logic.types.*;
import java.text.ParseException;

public class Lexer {
    /**
     * Function lexes, gives each character a token, the given arthemetic expression
     * @param expression the string representing the arthemetic expression we want to lex
     * @return List of lexical tokens representing the given symbols in the input
     * @throws ParseException if expression contains invalid characters
     */
    public Listable<Token> lex(String expression) throws ParseException {
        // TODO: Throw lexer exception
        Listable<Token> tokens = new List<>();
        for (int i = 0; i < expression.length(); ++i) {
            String c = Character.toString(expression.charAt(i));
            if (c.matches("\\+|\\*|/|\\-")) {
                tokens.push(new Operator(c));
            }
            else if (c.matches("[0-9]")) {
                StringBuilder number = new StringBuilder();
                number.append(c);
                i = scan(expression, number, i, "[0-9]");
                tokens.push(new Numeric(number.toString()));
            }
            else if (c.matches("[_a-zA-Z]")) {
                StringBuilder symbol = new StringBuilder();
                symbol.append(c);
                i = scan(expression, symbol, i, "[_a-zA-Z]");
                tokens.push(new Symbol(symbol.toString()));
            }
            else if (c.matches("\\(") || c.matches("\\)") || c.matches(",")) {
                tokens.push(new Empty(c));
            }
            else {
                throw new ParseException("Unknown character", 0);
            }
        }

        return tokens;
    }

    private int scan(String expression, StringBuilder tokenName, int i, String pattern) {
        int pos = i;
        while (pos+1 < expression.length() && Character.toString(expression.charAt(pos+1)).matches(pattern)) {
            tokenName.append(expression.charAt(pos+1));
            pos++;
        }
        return pos;
    }
}