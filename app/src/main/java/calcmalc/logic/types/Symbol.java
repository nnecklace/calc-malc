package calcmalc.logic.types;

public class Symbol implements Token {
    private String key;

    public Symbol(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getPrecedence() {
        return 4;
    }

    public boolean isOperator() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isFunction() {
        return true;
    }

    public boolean isEmpty() {
        return false;
    } 

    public boolean isAssignment() {
        return false;
    }
}
