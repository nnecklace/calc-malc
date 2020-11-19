package calcmalc.logic.types;

public class Assignment implements Token {
    private String key;

    public Assignment(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getPrecedence() {
        return 0;
    }

    public boolean isOperator() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isFunction() {
        return false;
    }

    public boolean isEmpty() {
        return false;
    } 

    public boolean isAssignment() {
        return true;
    }
}
