package calcmalc.logic.types;

public class Numeric implements Token {
    private String key;

    public Numeric(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getPrecedence() {
        return 100;
    }

    public boolean isOperator() {
        return false;
    }

    public boolean isNumber() {
        return true;
    }

    public boolean isSymbol() {
        return false;
    }

    public boolean isEmpty() {
        return false;
    } 

    public boolean isAssignment() {
        return false;
    }
}