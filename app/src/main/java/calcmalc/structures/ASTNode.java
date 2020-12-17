package calcmalc.structures;

import calcmalc.logic.types.Token;

/**
 * ASTNode (stands for; abstract syntax tree node) is node datastructure in an AST
 * The Node can have n- amount of child nodes contained in the children list
 * Ideally, if the node contains only a symbol or a number the node should be a leaf
 * @author nnecklace
 */
public class ASTNode {
    /**
     * The token contained within the node of the tree
     * {@see calcmalc.logic.types.Token}
     */
    private Token token;
    /**
     * The list of children associated with the node
     * The first child in the list is the left most node
     *      +
     *     / \
     *    2   3 [2, 3] children list 
     * {@see calcmalc.structures.List}
     */
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
     * @return the node's current children list
     */
    public List<ASTNode> children() {
        return children;
    }

    /**
     * Setter for the children property 
     * Used in cases where the children are added to some other list or before the node has been created
     * @param children the list of children for this node, overwrites the previous list of children the node had
     */
    public void setChildren(List<ASTNode> children) {
        this.children = children;
    }
}
