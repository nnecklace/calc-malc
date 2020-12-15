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
    HashTable<Character, Boolean> alphabet = new HashTable<>();
    HashTable<Character, Boolean> numbers = new HashTable<>();

    public Lexer() {
        // ideally we would regex match in this case but since it is not allowed in this course we will resort to doing it this way
        alphabet.placeOrUpdate('_', true);
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
                case ' ': 
                case '\n':
                case '\r': // scanner handles these cases for us, but just incase something weird happens with scanner we ignore these manually
                    break;
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
                    tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, valueOf(new char[]{c}, 1)));
                    break;
                case '-':
                    // check if operator is unary - operator ,-100 -x (-100) unary minus operator
                    if (tokens.isEmpty() || tokens.peekLast().isOpenParenthesis() || tokens.peekLast().isComma()) {
                        tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, "$"));
                    } else {
                        tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, valueOf(new char[]{c}, 1)));
                    }
                    break;
                default:
                    if (alphabet.get(c) != null) {
                        tokens.enqueue(TypeBuilder.buildToken(Types.SYMBOL, scan(expression, c, i, alphabet)));
                    } else if (numbers.get(c) != null) {
                        tokens.enqueue(TypeBuilder.buildToken(Types.NUMERIC, scan(expression, c, i, numbers)));
                    } else {
                        throw new LexerException("Unknown character " + c + " at position " + (i + 1));
                    }
                    i += tokens.peekLast().getKey().length() - 1;
            }
        }

        return tokens;
    }

    private String valueOf(char[] charSequence, int size) {
        return new String(charSequence, 0, size);
    }

    private String scan(String expression, char start, int i, HashTable<Character, Boolean> lookup) throws LexerException {
        int maxLength = 64;
        char[] tokens = new char[maxLength];
        tokens[0] = start;
        int offSet;
    
        for (offSet = i + 1; offSet < expression.length(); ++offSet) {
            char current = expression.charAt(offSet);
            if (lookup.get(current) == null) {
                break;
            }

            if ((offSet - i) == maxLength) {
                throw new LexerException("token " + valueOf(tokens, (offSet - i)) +  " too long. Max length is 64");
            }

            tokens[offSet - i] = current;
        }

        return valueOf(tokens, (offSet - i));
    }
}
