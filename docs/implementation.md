# Implementation

CalcMalc is both a project and an algorithm. The project itself is a REPL (Read, Eval, Print, Loop) program, while the algorithm is a programming language. Although extremely basic, a programming language non the less. The programming language should recognize any arthemetic expression. The algorithm realies heavily on Dijkstra's _shunting yard algorithm_, with slight modifications to make variables and functions work. 

## Algorithm Structure

The algorithm is actually 3 smaller algorithms combined into one: Lexer, Parser, Evaluator. The algorithm starts at the lexer (_lexing phase_), and moves on to the parser (_parsing phase_ shunting yard is implemented here) and finally to the evaluator.

### Lexer

Time complexity: `O(n)` where n is the size of the input.
Space complexity: `O(n)` where n is the size of the input.

The lexer is a basic linear algorithm, each character in the input is read at most once and its respective token is stored in a list.

Pseudo code example of the lexer algorithm:

```
tokens = []

    for character in expression
        if character is operand
            tokens.add(new Operand(character))
        else if character is Number
            numberToken <- readUntilNoLongerNumber(expression)
            tokens.add(new Number(numberToken))
        else if character is Assignment
            tokens.add(new Assignment(character))
        else if character is Symbol
            symbolToken <- readUntilNoLongerSymbol(expression)
            tokens.add(new Symbol(symbolToken))
        else if character is Parenthesis or Coma or Colon
            tokens.add(new Empty(character))
        else
            Print -> "Unknown Character " + character

return tokens
```

The code is a bit ugly looking at the moment, the `readUntil`, or `scan` as it is called in the project, is a bit ulgy and probably needs to be refactored at some point. Other than that, the lexer should be quite clear and easy to understand. One thing to notice in the lexer is the use of the $ symbol. In mathematics some operations can be made unarymeaning there is only one argumnent. In this case CalcMalc recognises inputs like `-1+1` where `-1` is a ununaryrator. unarunary will be turned into a $ symbol.


### Parser

Time complexity: `O(n)` where n is the size of the input.
Space complexity: `O(n)` where n is the size of the input.

The parser is basically a modification of Dijkstra's shunting yard algorithm. Normally the shunting yard returns the input in (RPN) Reverse Polish notation, but it can be modified to return Abstract syntax nodes instead (more on this later) as is the case in CalcMalc. The parser reads the list of tokens made by the lexer and converts them tree nodes. The parser will also work in linear time, each token is processed exactly once.

Pseudo code example:

```
Stack operators = []
Stack functions = [] // store all symbols in this stack
Stack nodes = []

ShuntingYard(tokens)
    for each token in tokens
        if token is Number
            nodes.push(Node(token))
        else if token is Symbol
            operators.push(token)
            Node root = Node(token)
            if next(tokens) is OpenParenthesis
                root.children <- ShuntingYardUntilCharacter())
            else 
                root.children <- ShuntingYardUntilNoLongerSymbolToken()
            
            functions.push(root)

        else if token is Assignment
            operators.pop() // top operator is expected to be a symbol
            operators.push(token)

            Node root = Node(token)
            root.children <- ShuntingYardUntil(:)
            root.addChild(functions.pop()) // previous function is expected to be a symbol
            functions.push(root)

        else if token is Operator
            while (operators.isNotEmpty() and operators.top() != Openparenthesis and token.precedence < operators.top().precedence) 
                createOperatorNode(operators.pop())
            operators.push(token)
        else
            if token is Openparenthesis
                operators.push(token)
            else
                token is Coma or Colon
                    ignore

                while (operators.isNotEmpty() and operators.top() != Openparenthesis)
                    createOperatorNode(operators.pop())
                
                if operators.isEmtpy()
                    Print -> "Unmatched parenthesis"

                operators.push()

    while (operators.isNotEmpty())
        if operators.top() == Openparenthesis
            Print -> "Unmatched parenthesis"

        createOperatorNode(operators.pop())

    return nodes
```

Now understandable that this section can be really difficult to understand, it is a complicated algorithm. I advise everyone to read the wikipedia article: https://en.wikipedia.org/wiki/Shunting-yard_algorithm Really good explanation on the algorithm and its details. The modifications are in the section when the token is either a symbol or an assignment. In short, this algorithm creates nodes (Abstract syntax nodes) for a tree structure and returns the stack of nodes for a given input.

Some examples:

Input `max(2,2)`

```
    max
   /   \
  2     2
```

Input `x = 2: 2^x`

```
    =
  /   \
 x     2

   ^
 /   \
2     x
```

Input `(2+2)*2`

```
        *
      /   \
    +      2
  /   \
 2     2
```

The result of the last tree is always value of the calculated expression.

## Evaluator

Time complexity: `O(|V|+|E|)` where V is the number of nodes to process and E are the edges for each node.
Space complexity: `O(n)` where n is the number of nodes in the tree.

The evaluator is the part that process the nodes that were created by the parser. This is the part where actual values are made. In some earlier reports I claimed that the tree is traversed in inorder while it is infact traversed in postorder. All leaf nodes have to be numbers, or in some cases symbols. Each time we reach a leaf we will stop the recusive decent and go up to the parent and evaluate the parent operator.

Pseudo code example:

```
Traverse(node)
    if node is number 
        return node
    
    if node is assignment
        symbol = children.pop()
        value = Traverse(children.first())
        symbolTable <- symbol, value
        return 0.0

    arguments = []

    for each child in node.children
        arguments.add(Traverse(node))

    return EvaluateFunction(node, arguments)

EvaluateFunction(node, arguments)
    checkArgumentsAreCorrect(node, arguments)
    switch (node)
        * return multiply arguments
        + return add arguments
        - return subtract arguments
        $ return subtract argument
        ^ return pow arguments
        % return modulo arguments
        / return divide arguments
        ...
        default => checkSymbolTable(node) // incase node is some variable
```

This section shouldn't be too difficult to understand. Tree traversals and recusive functions. As mentioned earlier the tree is evaluated from the bottom up and from left to right.
Some examples with intermediate stages:

```
        *            *          8
       / \          / \        / \
      +   2   =>   4   2  =>  4   2
     / \          / \        / \
    2   2        2   2      2   2 
```

## References

[Tirakirja](https://raw.githubusercontent.com/pllk/tirakirja/master/tirakirja.pdf)

[JDK Soure code](https://hg.openjdk.java.net/jdk8/jdk8/jdk)

[Shunting Yard](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)

[Writing a programming language](https://www.youtube.com/watch?v=9-EYWLbmiG0&t=2052s)