package calcmalc.logic.types;

/**
 * The token class represents a value for one of character (or group of characters)
 * in the input. The token is created by the lexer 
 * @see calcmalc.logic.Lexer
 * 
 * 2 + 2 
 * 
 * In this example the tokens are two numbers and an operator
 * Tokens contain a string key, the actualy character or group of characters the token represents
 * And a type for the token. As already mentioned numbers and operators are types 
 * @see calcmalc.logic.types.Types
 * for list of all token types
 * Token's also contain precedence value, this is important for the parser to understand in what order the token should be placed in the abstract syntax tree
 * @see calcmalc.logic.types.TypeBuilder
 * for precedence value for each token
 * @author nnecklace
 */
public class Token {
    /**
     * The type of the token
     * @see calcmalc.logic.types.Types 
     * for all possible types
     */
    private Types type;
    /**
     * Precedence value for the token
     * @see calcmalc.logic.types.TypeBuilder 
     * for precedence values for each token
     */
    private int precedence;
    /**
     * The key contains the character, or group of characters, that the token represents
     * Keys can be at maximum 64 characters long, this is validated be the lexer and not be this class
     */
    private String key;

    /**
     * Constructor for Token class
     * @param type an enum type of Types representing the class type 
     * @see calcmalc.logic.types.Types
     * @param key String key to tokenize
     */
    public Token(Types type, String key) {
        this.type = type;
        this.key = key;
    }

    /**
     * Check if token is a type operator
     * + - * / ^ %
     * @return true if token is operator else false
     */
    public boolean isOperator() {
        return type == Types.OPERATOR;
    }

    /**
     * Check if token is a type numeric 
     * 1 2 3
     * @return true if token is numeric else false
     */
    public boolean isNumber() {
        return type == Types.NUMERIC;
    }

    /**
     * Check if token is a type symbol 
     * x y z
     * @return true if token is symbol else false
     */
    public boolean isSymbol() {
        return type == Types.SYMBOL;
    }

    /**
     * Check if token is a type assignment 
     * =
     * @return true if token is assignment else false
     */
    public boolean isAssignment() {
        return type == Types.ASSIGNMENT;
    }

    /**
     * Check if the token is a type open parenthesis
     * (
     * @return true if token is open parenthesis else false
     */
    public boolean isOpenParenthesis() {
        return type == Types.OPEN_PARENTHESIS;
    }

    /**
     * Check if the token is close parenthesis
     * )
     * @return true if token is close parenthesis else false
     */
    public boolean isClosingParenthesis() {
        return type == Types.CLOSING_PARENTHESIS;
    }

    /**
     * Check if the token is a type comma
     * ,
     * @return true if token is comma else false
     */
    public boolean isComma() {
        return type == Types.COMMA;
    }

    /**
     * Check if the token is a type variable delimiter
     * :
     * @return true if token is delimiter else false
     */
    public boolean isVariableDelimiter() {
        return type == Types.VARIABLE_DELIMITER;
    }

    /**
     * Check if token is a type function
     * abs()
     * @return true if token is a type function else false
     */
    public boolean isFunction() {
        return type == Types.FUNCTION;
    }

    /**
     * Getter for class precedence level
     * @return precedence level
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * Setter for class precedence level
     * @param precedence the new precedence level
     */
    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    /**
     * Setter for class type value
     * @param type the type of the token
     */
    public void setType(Types type) {
        this.type = type;
    }

    /**
     * Getter for the class type value
     * @return the type the token is
     */
    public Types getType() {
        return type;
    }

    /**
     * Getter for class key property
     * @return the key property
     */
    public String getKey() {
        return key;
    }
}
