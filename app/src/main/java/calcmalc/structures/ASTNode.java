package calcmalc.structures;

import calcmalc.logic.types.Token;

public class ASTNode {
   private Token token;
   private Listable<ASTNode> children;

   public ASTNode(Token token) {
       this.token = token;
       this.children = new List<>();
   }

   public Token token() {
       return token;
   }

   public void addChild(ASTNode child) {
       this.children.push(child);
   }

   public void setChildren(Listable<ASTNode> children) {
       this.children = children;
   }

   public Listable<ASTNode> getChildren() {
       return children;
   }
}
