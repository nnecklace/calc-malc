package calcmalc.logic.types;

public class Token {
    private Types type;
    private int precedence;
    private String key;

    public Token(Types type, String key) {
        this.type = type;
        this.key = key;
    }

    public boolean isOperator() {
        return type == Types.OPERATOR;
    }

    public boolean isNumber() {
        return type == Types.NUMERIC;
    }

    public boolean isSymbol() {
        return type == Types.SYMBOL;
    }

    public boolean isAssignment() {
        return type == Types.ASSIGNMENT;
    }

    public boolean isEmpty() {
        return type == Types.EMPTY;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public String getKey() {
        return key;
    }
}
