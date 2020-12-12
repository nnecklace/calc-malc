package calcmalc.logic.types;

/**
 * @author nnecklace
 */
public enum Types {
    /**
     * Assignment type, e.g. {@code =}
     */
    ASSIGNMENT,
    /**
     * Empty type, e.g. {@code , () :}
     */
    EMPTY,
    /**
     * Numeric type, e.g. {@code 1 2 3 4 5 6}
     */
    NUMERIC,
    /**
     * Operator type, e.g. {@code * + - / ^ $}
     */
    OPERATOR,
    /**
     * Symbol type, e.g. {@code max() min() log() etc.}
     */
    SYMBOL,
    FUNCTION
}
