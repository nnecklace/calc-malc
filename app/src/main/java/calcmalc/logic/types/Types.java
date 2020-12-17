package calcmalc.logic.types;

/**
 * Enum list for all types of tokens that the program supports and uses
 * @author nnecklace
 */
public enum Types {
    /**
     * Assignment type, e.g. {@code =}
     */
    ASSIGNMENT,
    /**
     * Numeric type, e.g. {@code 1 2 3 4 5 6}
     */
    NUMERIC,
    /**
     * Operator type, e.g. {@code * + - / ^ $}
     */
    OPERATOR,
    /**
     * Symbol type, e.g. {@code x y z}
     */
    SYMBOL,
    /**
     * Function type, e.g. {@code max() min() log() etc.}
     */
    FUNCTION,
    /**
     * Open parenthesis type, e.g. {@code (}
     */
    OPEN_PARENTHESIS,
    /**
     * Closing parenthesis type, e.g. {@code )}
     */
    CLOSING_PARENTHESIS,
    /**
     * Variable delimiter type, e.g. {@code : }
     */
    VARIABLE_DELIMITER,
    /**
     * Comma type, e.g. {@code , }
     */
    COMMA
}
