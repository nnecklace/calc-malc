package calcmalc.logic;

import calcmalc.structures.HashTable;
import calcmalc.structures.Queue;
import calcmalc.exceptions.LexerException;
import calcmalc.logic.types.*;

/**
 * Lexical analysis class. 
 * Class reads character from the input and gives them values (tokens 
 * @see calcmalc.logic.types.Types
 * )
 * for the parser to parse
 * Unknown tokens will result in exception being thrown
 * <pre> 
 * 
 * 2+2 => [NUMBER, OPERATOR, NUMBER]
 * 
 * </pre>
 * 
 * The above example shows how the lexer would give the characters in the input tokens
 * @author nnecklace
 */
public class Lexer {
    /**
     * Alphabet Hashtable contains underscore and all letters of the english alphabet
     * This property is used to lookup it the input is alphabetical in nature
     * Normally it would be done with regex, but regex is not allowed
     */
    private HashTable<Boolean> alphabet = new HashTable<>();
    /**
     * Same as alphabet but for numbers
     */
    private HashTable<Boolean> numbers = new HashTable<>();

    /**
     * Constructor for lexer. 
     * The constructor sets up the alphabet and numbers tables with their values
     */
    public Lexer() {
        // ideally we would regex match in this case but since it is not allowed in this course we will resort to doing it this way
        alphabet.placeOrUpdate("_", true);
        for (char ch: "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            alphabet.placeOrUpdate(valueOf(ch), true);
            ch -= 32; // make uppercase
            alphabet.placeOrUpdate(valueOf(ch), true);
        }

        for (char ch: ".0123456789".toCharArray()) {
            numbers.placeOrUpdate(valueOf(ch), true);
        }
    }

    /**
     * Function lexes, gives each character a token, the given input stream of characters 
     * @param expression the string representing the input stream or expression we want to lex
     * @return Queue of lexical tokens representing the given symbols in the input
     * @throws LexerException if expression contains invalid characters
     */
    public Queue<Token> lex(String expression) throws LexerException {
        Queue<Token> tokens = new Queue<>();
        for (int i = 0; i < expression.length(); ++i) {
            String c = valueOf(expression.charAt(i));
            switch (c) {
                case " ":
                case "\n":
                case "\r": // scanner handles these cases for us, but just incase something weird happens with scanner we ignore these manually
                    break;
                case "=":
                    tokens.enqueue(TypeBuilder.buildToken(Types.ASSIGNMENT, "="));
                    break;
                case ")":
                    tokens.enqueue(TypeBuilder.buildToken(Types.CLOSING_PARENTHESIS, ")"));
                    break;
                case ":":
                    tokens.enqueue(TypeBuilder.buildToken(Types.VARIABLE_DELIMITER, ":"));
                    break;
                case ",":
                    tokens.enqueue(TypeBuilder.buildToken(Types.COMMA, ","));
                    break;
                case "(":
                    // if if passes, the previous token must have been a function
                    // cos(x)
                    // cos <-- this point it is symbol
                    // cos( <-- open paren means it was actually a function
                    if (!tokens.isEmpty() && tokens.peekLast().isSymbol()) {
                        tokens.peekLast().setType(Types.FUNCTION);
                    }
                    tokens.enqueue(TypeBuilder.buildToken(Types.OPEN_PARENTHESIS, "("));
                    break;
                case "+":
                case "*":
                case "/":
                case "^":
                case "%":
                    tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, c));
                    break;
                case "-":
                    // check if operator is unary - operator ,-100 -x (-100), x = -1 unary minus operator
                    if (tokens.isEmpty() ||
                        tokens.peekLast().isOpenParenthesis() ||
                        tokens.peekLast().isComma() ||
                        tokens.peekLast().isAssignment() ||
                        tokens.peekLast().isVariableDelimiter()) {
                        tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, "$"));
                    } else {
                        tokens.enqueue(TypeBuilder.buildToken(Types.OPERATOR, c));
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
                    // skip all scanned tokens and continue right after where scanner failed
                    i += tokens.peekLast().getKey().length() - 1;
            }
        }

        return tokens;
    }

    /**
     * Helper function to convert a single char into a string
     * Normally we would use something like String.valueOf but this is not allowed
     * So we made our own implementation
     * @param ch single character to make into a string
     * @return the character as a String
     */
    private String valueOf(char ch) {
        return valueOf(new char[]{ch}, 1);
    }

    /**
     * Helper function to convert a sequence of chars into a string
     * Normally we would use something like String.valueOf but this is not allowed
     * So we made our own implementation
     * @param charSequqnce the char array to convert to a string
     * @param size the size fo the string to create
     * @return the character array as a String
     */
    private String valueOf(char[] charSequence, int size) {
        return new String(charSequence, 0, size);
    }

    /**
     * Method is called when the current character is alphabetical or a number.
     * Scans forward from the current poisiton as long as the characters are a type that is contained the lookup table
     * <pre>
     * abs(1)
     * </pre>
     * 
     * Scan would start from s and continue all the way until it reaches to opening parenthesis, after which it retrurns the scaned char sequence
     * @param expression the input string stream that was given to the lexer
     * @param start the first character in the char sequence
     * @param i the starting position from which the scanning starts
     * @param lookup the lookup hashtable, will be either alphabet or numbers
     * @return the scanned char sequence as a string
     * @throws LexerException if the char sequence is longer than 64 characters which is the maximum for any continuous sequence of numbers or letters
     */
    private String scan(String expression, String start, int i, HashTable<Boolean> lookup) throws LexerException {
        int maxLength = 64;
        char[] tokens = new char[maxLength];
        tokens[0] = start.charAt(0); // start will always be a 1 character string
        int offSet;
    
        for (offSet = i + 1; offSet < expression.length(); ++offSet) {
            char current = expression.charAt(offSet);
            if (lookup.get(valueOf(current)) == null) {
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
