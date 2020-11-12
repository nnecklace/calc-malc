package calcmalc.logic.types;

import calcmalc.logic.types.Token;

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

    public boolean isFunction() {
        return false;
    }

    public boolean isEmpty() {
        return false;
    } 
}