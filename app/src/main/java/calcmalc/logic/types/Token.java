package calcmalc.logic.types;

/**
 * @author nnecklace
 */
public class Token {
    private Types type;
    private int precedence;
    private String key;

    /**
     * Constructor for Token class
     * @param type an enum type of Types representing the class type
     * @param key String key to tokenize
     */
    public Token(Types type, String key) {
        this.type = type;
        this.key = key;
    }

    /**
     * Check if token is a type operator
     * @return true if token is operator else false
     */
    public boolean isOperator() {
        return type == Types.OPERATOR;
    }

    /**
     * Check if token is a type numeric 
     * @return true if token is numeric else false
     */
    public boolean isNumber() {
        return type == Types.NUMERIC;
    }

    /**
     * Check if token is a type symbol 
     * @return true if token is symbol else false
     */
    public boolean isSymbol() {
        return type == Types.SYMBOL;
    }

    /**
     * Check if token is a type assignment 
     * @return true if token is assignment else false
     */
    public boolean isAssignment() {
        return type == Types.ASSIGNMENT;
    }

    /**
     * Check if token is a type empty 
     * @return true if token is empty else false
     */
    public boolean isEmpty() {
        return type == Types.EMPTY;
    }

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

    public void setType(Types type) {
        this.type = type;
    }

    /**
     * Getter for class key property
     * @return the key property
     */
    public String getKey() {
        return key;
    }
}
