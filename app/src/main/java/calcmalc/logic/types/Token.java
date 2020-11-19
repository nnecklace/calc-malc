package calcmalc.logic.types;

public interface Token {
    public boolean isOperator();
    public boolean isNumber();
    public boolean isFunction();
    public boolean isAssignment();
    public boolean isEmpty();
    public int getPrecedence();
    public String getKey();
}
