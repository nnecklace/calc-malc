package calcmalc.logic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeBuilderTest {
    @Test
    public void testTypeBuilderReturnsCorrectPrecedence() {
        Token actual1 = TypeBuilder.buildToken(Types.OPERATOR, "*");
        Token actual2 = TypeBuilder.buildToken(Types.OPERATOR, "+");
        Token actual3 = TypeBuilder.buildToken(Types.OPERATOR, "$");
        Token actual4 = TypeBuilder.buildToken(Types.OPERATOR, "^");
        assertEquals(2, actual1.getPrecedence());
        assertEquals(1, actual2.getPrecedence());
        assertEquals(3, actual3.getPrecedence());
        assertEquals(4, actual4.getPrecedence());
    }

    @Test
    public void testTypeBuilderReturnsCorrectTokenType() {
        Token actual1 = TypeBuilder.buildToken(Types.OPERATOR, "*");
        Token actual2 = TypeBuilder.buildToken(Types.ASSIGNMENT, "=");
        Token actual3 = TypeBuilder.buildToken(Types.VARIABLE_DELIMITER, ":");
        Token actual4 = TypeBuilder.buildToken(Types.CLOSING_PARENTHESIS, "(");
        assertTrue(actual1.isOperator() && !actual1.isVariableDelimiter()); // second part is only for a jacoco bug
        assertTrue(actual2.isAssignment() && !actual2.isClosingParenthesis());
        assertTrue(actual3.isVariableDelimiter());
        assertTrue(actual4.isClosingParenthesis());
    }
}
