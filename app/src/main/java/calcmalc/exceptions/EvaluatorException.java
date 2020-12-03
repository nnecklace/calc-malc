package calcmalc.exceptions;

/**
 * @author nnecklace
 */
public class EvaluatorException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for EvaluatorException
     * @param message the exception message
     */
    public EvaluatorException(String message) {
        super(message);
    }
}
