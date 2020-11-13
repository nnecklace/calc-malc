/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package calcmalc;

import calcmalc.structures.ASTNode;
import calcmalc.structures.Listable;
import calcmalc.structures.Stack;
import calcmalc.structures.Queue;
import calcmalc.logic.types.Token;
import calcmalc.logic.Lexer;
import calcmalc.logic.Parser;
import java.text.ParseException;
import java.util.Scanner;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        Scanner input = new Scanner(System.in);
        Lexer lexer = new Lexer();

        while (input.hasNextLine()) {
            String line = input.nextLine().replaceAll("\\s", "");

            try {
                Listable<Token> tokens = lexer.lex(line);
                Parser parser = new Parser(new Queue<Token>(tokens));
                Stack<ASTNode> nodes = parser.parse();
                System.out.println(parser.printTree());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }

    public static int dfs(ASTNode node) {
        if (node.token().isNumber()) {
            return Integer.parseInt(node.token().getKey());
        }
        int child = 0;
        int value = 0;
        int prod = 1;
        while (node.getChildren().get(child) != null) {
            if (node.token().getKey().equals("+")) {
                value += dfs(node.getChildren().get(child));
            }
            else if (node.token().getKey().equals("*")) {
                prod *= dfs(node.getChildren().get(child));
            }
            child++;
        }

        if (node.token().getKey().equals("+")) {
            return value;
        }

        return prod;
    }
}
