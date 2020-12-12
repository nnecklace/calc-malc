package calcmalc.logic.types;

import static org.junit.Assert.assertEquals;

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
}
