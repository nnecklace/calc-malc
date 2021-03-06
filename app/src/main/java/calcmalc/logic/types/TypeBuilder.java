package calcmalc.logic.types;

/**
 * The type builder class builds tokens 
 * @see calcmalc.logic.types.Token
 * And gives them correct precedence levels
 * @author nnecklace
 */
public class TypeBuilder {
    /**
     * Class is used as static so the constructor is set to private
     */
    private TypeBuilder() {}

    /**
     * Builds tokens representing the key that was read by the lexer
     * @param type the token type
     * @param key the key that was read by the lexer
     * @return Token matching the key
     */
    public static Token buildToken(Types type, String key) {
        Token token = new Token(type, key);

        switch (type) {
            case ASSIGNMENT:
                token.setPrecedence(0);
                break;
            case SYMBOL:
                token.setPrecedence(4);
                break;
            case OPERATOR:
                if (key.equals("^")) {
                    token.setPrecedence(4);
                } else if (key.equals("$")) {
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
