package calcmalc.logic.types;

public class Operator implements Token {
    private String key;

    public Operator(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getPrecedence() {
        if (key.equals("$")) {
            return 4;
        }

        if (key.equals("^")) {
            return 3;
        }

        if (key.equals("*") || key.equals("/") || key.equals("%")) {
            return 2;
        }

        return 1;
    }

    public boolean isOperator() {
        return true;
    }

    public boolean isNumber() {
        return false;
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
