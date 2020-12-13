package calcmalc.logic;

import calcmalc.structures.HashTable;
import calcmalc.structures.Queue;
import calcmalc.exceptions.LexerException;
import calcmalc.logic.types.*;

/**
 * Lexical analysis class. Class reads character from the input and gives them  values (tokens)
 * For the parser
 * @author nnecklace
 */
public class Lexer {
    HashTable<Character,Boolean> alphabet = new HashTable<>();
    HashTable<Character,Boolean> numbers = new HashTable<>();

    public Lexer() {
        // ideally we would regex match in this case but since it is not allowed in this course we will resort to doing it this way
        alphabet.placeOrUpdate('_', true);
       // size         abcdefghijklmnopqrstuvwxyz 
        for (char ch: "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            alphabet.placeOrUpdate(ch, true);
            ch -= 32; // make uppercase
            alphabet.placeOrUpdate(ch, true);
        }

        for (char ch: ".0123456789".toCharArray()) {
            numbers.placeOrUpdate(ch, true);
        }
    }

    /**
     * Function lexes, gives each character a token, the given arthemetic expression
     * @param expression the string representing the arthemetic expression we want to lex
     * @return List of lexical tokens representing the given symbols in the input
     * @throws LexerException if expression contains invalid characters
     */
    public Queue<Token> lex(String expression) throws LexerException {
        Queue<Token> tokens = new Queue<>();
        for (int i = 0; i < expression.length(); ++i) {
            char  c = expression.charAt(i);
            switch (c) {
                case '=':
                    tokens.enqueue(TypeBuilder.buildToken(Types.ASSIGNMENT, "="));
                    break;
                case ')':
                    tokens.enqueue(TypeBuilder.buildToken(Types.CLOSING_PARENTHESIS, ")"));
                    break;
                case ':':
                    tokens.enqueue(TypeBuilder.buildToken(Types.VARIABLE_DELIMITER, ":"));
                    break;
                case ',':
                    tokens.enqueue(TypeBuilder.buildToken(Types.COMMA, ","));
                    break;
                case '(':
                    if (!tokens.isEmpty() && tokens.peekLast().isSymbol()) {
                        tokens.peekLast().setType(Types.FUNCTION);
                    }
                    tokens.enqueue(TypeBuilder.buildToken(Types.OPEN_PARENTHESIS, "("));
                    break;
                case '+':
                case '*':
                case '/':
                case '^':
                case '%':
                    tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, String.valueOf(c)));
                    break;
                case '-':
                    // check if operator is unary - operator ,-100 -x (-100) unary minus operator
                    if (tokens.isEmpty() || tokens.peekLast().isOpenParenthesis() || tokens.peekLast().isComma()) {
                        tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, "$"));
                    } else {
                        tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, String.valueOf(c)));
                    }
                    break;
                default:
                    if (alphabet.get(c) != null) {
                        tokens.enqueue(TypeBuilder.buildToken(Types.SYMBOL, scan(expression, c, i + 1, alphabet)));
                    } else if (numbers.get(c) != null) {
                        tokens.enqueue(TypeBuilder.buildToken(Types.NUMERIC, scan(expression, c, i + 1, numbers)));
                    } else {
                        // TODO: should use something else since this is not allowed
                        String errorAt = String.format("%" + (i + 1) + "s", "^");
                        throw new LexerException("Unknown character " + c + " at position " + (i + 1) + " in expression " + expression + "\n" + expression + "\n" + errorAt);
                    }
                    i += tokens.peekLast().getKey().length() - 1;
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
    private String scan(String expression, char start, int i, HashTable<Character, Boolean> lookup) {
        String token = String.valueOf(start);
        while (i < expression.length() && lookup.get(expression.charAt(i)) != null) {
            token += (expression.charAt(i));
            i++;
        }

        return token;
    }
}
