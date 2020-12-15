package calcmalc.structures;

import calcmalc.logic.types.Token;

/**
 * @author nnecklace
 */
public class ASTNode {
    private Token token;
    private List<ASTNode> children;

    /**
     * Constructor for ASTNode
     * @param token the token the node contains
     */
    public ASTNode(Token token) {
        this.token = token;
        this.children = new List<>();
    }

    /**
     * Getter for the token inside the node
     * @return token
     */
    public Token token() {
        return token;
    }

    /**
     * Adds a child node for this node
     * @param child to be added to this node
     */
    public void addChild(ASTNode child) {
        this.children.append(child);
    }

    /**
     * Getter for the children property
     * @return the nodes current children
     */
    public List<ASTNode> children() {
        return children;
    }

    public void setChildren(List<ASTNode> children) {
        this.children = children;
    }
}
