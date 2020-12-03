package calcmalc.logic.types;

public class TypeBuilder {
    private TypeBuilder() {}

    public static Token buildToken(Types type, String key) {
        Token token = new Token(type, key);

        switch (type) {
            case SYMBOL:
                token.setPrecedence(4);
                break;
            case OPERATOR:
                if (key.equals("$")) {
                    token.setPrecedence(4);
                } else if (key.equals("^")) {
                    token.setPrecedence(3);
                } else if (key.equals("*") || key.equals("/") || key.equals("%")) {
                    token.setPrecedence(2);
                } else {
                    token.setPrecedence(1);
                }
                break;
            default:
                break;
        }
        return token;
    }
}
