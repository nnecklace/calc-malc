package calcmalc.structures;

import calcmalc.logic.types.Token;

public class ASTNode {
    private Token token;
    private Stack<ASTNode> children;

    public ASTNode(Token token) {
        this.token = token;
        this.children = new Stack<>(new List<>());
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
        this.children.push(child);
    }

    /**
     * Setter for children property
     * @param children the children of this node
     */
    public void setChildren(Stack<ASTNode> children) {
        this.children = children;
    }

    /**
     * Getter for the children property
     * @return the nodes current children
     */
    public Stack<ASTNode> getChildren() {
        return children;
    }
}
