package calcmalc.logic.types;

import calcmalc.logic.types.Token;

public class Empty implements Token {
    private String key;

    public Empty(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getPrecedence() {
        return -1;
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
        return true;
    }
}
