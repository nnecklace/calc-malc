# Implementation

CalcMalc is both a project and an algorithm. The project itself is a REPL (Read, Eval, Print, Loop) program that also comes with a custom GUI, while the algorithm is an interpreter for the programming language calcmalc recognises. Although extremely basic, a programming language non the less. The interpreter should recognise any arthemetic expression that matches the syntax of the programming languages context free grammar. The interpreter realies heavily on Dijkstra's _shunting yard algorithm_, with slight modifications to make variables and functions work. 

## Interpreter Structure

The interpreter is actually 3 algorithms combined into one: Lexer, Parser, Evaluator. The algorithm starts at the lexer (_lexing phase_), and moves on to the parser (_parsing phase_ shunting yard is implemented here) and finally to the evaluator.

### Lexer

Time complexity: `O(n)` where n is the size of the input.
Space complexity: `O(n)` where n is the size of the input.

The lexer is a basic linear algorithm, each character in the input is read at once and its respective token is stored in a token queue.
Each character, or group of characters, is given a token that represents that character, or group of characters.

Pseudo code example of the lexer algorithm:

```
Queue tokens = []

    for character in expression
        if character is minus_operand
            if last_token(tokens) is any (Assignment, OpenParenthesis, Comma, VariableDelimiter)
                tokens.que(new UnaryOperand(character))
            else
                tokens.que(new Operand(character))
        else if character is operand
            tokens.que(new Operand(character))
        else if character is Number
            numberToken <- readUntilNoLongerNumber(expression)
            tokens.que(new Number(numberToken))
        else if character is Assignment
            tokens.que(new Assignment(character))
        else if character is Symbol
            symbolToken <- readUntilNoLongerSymbol(expression)
            tokens.que(new Symbol(symbolToken))
        else if character is OpenParenthesis
            if last_token(tokens) is symbol
                tokens.que(new Function(pop_last(tokens)))
            tokens.que(new OpenParenthesis(character))
        else if character is ClosingParenthesis
            tokens.que(new ClosingParenthesis(character))
        else if character is Comma
            tokens.que(new Comma(character))
        else if character is VariableDelimiter
            tokens.que(new VariableDelimiter(character))
        else
            Print -> "Unknown Character " + character

return tokens
```

Step by step example of input `1+4+min(-1,0)`


| Character     | TokenQueue                                                                                | Description                                                     |
| ------------- |-------------------------------------------------------------------------------------------|-----------------------------------------------------------------| 
| 1             | []                                                                                        | add 1 to queue as number                                        |
| +             | [NUM(1)]                                                                                  | add + to queue as operator                                      |
| 4             | [NUM(1), OPER(+)]                                                                         | add 4 to queue as number                                        |
| +             | [NUM(1), OPER(+), NUM(4)]                                                                 | add + to queue as operator                                      |
| m             | [NUM(1), OPER(+), NUM(4), OPER(+)]                                                        | read following token                                            |
| ma            | [NUM(1), OPER(+), NUM(4), OPER(+)]                                                        | read following token                                            |
| max           | [NUM(1), OPER(+), NUM(4), OPER(+)]                                                        | read following token                                            |
| max(          | [NUM(1), OPER(+), NUM(4), OPER(+)]                                                        | read fail token is not alphabetical, add max to queue as symbol |
| (             | [NUM(1), OPER(+), NUM(4), OPER(+), SYM(MAX)]                                              | change max to type function and add paren to queu               |
| -             | [NUM(1), OPER(+), NUM(4), OPER(+), FUN(MAX), PAREN(()]                                    | add unary minus to queue                                        |
| 1             | [NUM(1), OPER(+), NUM(4), OPER(+), FUN(MAX), PAREN(()), OPER($),]                         | add 1 to queue as number                                        |
|,              | [NUM(1), OPER(+), NUM(4), OPER(+), FUN(MAX), PAREN((), OPER($), NUM(1)]                   | add comma to queue                                              |
| 0             | [NUM(1), OPER(+), NUM(4), OPER(+), FUN(MAX), PAREN((), OPER($), NUM(1), COMMA(,)]         | add 0 to queue as number                                        |
| )             | [NUM(1), OPER(+), NUM(4), OPER(+), FUN(MAX), PAREN((), OPER($), NUM(1), COMMA(,), NUM(0)] | add ) to queue as closing parethesis                            |

Result is a queue that looks like this `[NUM(1), OPER(+), NUM(4), OPER(+), FUN(MAX), PAREN((), OPER($), NUM(1), COMMA(,), NUM(0), PAREN())]`

The Lexer is hopefully clear and easy to understand. One thing to notice in the lexer is the use of the $ symbol. In mathematics some operations can be made unary meaning there is only one argumnent. In this case CalcMalc recognises inputs like `-1+1` where `-1` is a unary. Unary operator (-) will be turned into a $ symbol. When readuntil, or scan as it is called in the projecta, fails it rereads the failed character, so this edge cases causes a reread of a token. In this case the token is read twice. 

### Parser

Time complexity: `O(n)` where n is the size of the input.
Space complexity: `O(n)` where n is the size of the input.

The parser is basically a modification of Dijkstra's shunting yard algorithm. Normally the shunting yard returns the input in (RPN) Reverse Polish notation, but it can be modified to return Abstract syntax tree instead (more on this later) as is the case in CalcMalc. The parser reads the queue of tokens made by the lexer and converts them tree nodes. The parser will also work in linear time. 
The parser includes 3 stacks and 1 queue. The Queue will contain all variables, as they will be evaluated separately from expressions. The stacks contain either the tree nodes, operators, or the count of arguments for a function.

Pseudo code example:

```
Stack operators     = []
Stack functionArity = [] 
Stack nodes         = []
Queue varirables    = []

ShuntingYard(tokens)
    for each token in tokens
        checkIfFunctionArityCanBeIncreased(token)
        if token is Number
            nodes.push(Node(token))
        else if token is Symbol
            nodes.push(Node(token))
        else if token is Function
            operators.push(token)
        else if token is Assignment
            if operator.isNotEmpty() or nodes.top() is not any(Function, Symbol)
                Print <- "Illegal assignment operator
                exit(1)
            operators.push(token)
        else if token is Operator
            while (operators.isNotEmpty() and operators.top() != Openparenthesis and token.precedence < operators.top().precedence) 
                createTreeNode(operators.pop())
            operators.push(token)
        else if token is Openparenthesis
            if operators.top() is Function
                functionArity.push(0)
            operators.push(token)
        else if token is ClosingParenthesis
            while (operators.isNotEmpty() and operators.top() != Openparenthesis)
                createTreeNode(operators.pop())

            if operators.isEmpty()
                Print <- "Missing parenthesis"
                exit(1)

            operators.pop() # discard open parenthesis

            if operators.top() is Function
                createTreeNode(operators.pop())
        else if token is Comma
            while (operators.isNotEmpty() and operators.top() != Openparenthesis)
                createTreeNode(operators.pop()) 

            if functionArity.isEmpty() or functionArity.top() == 0
                Print <- "Illegal Comma"
                exit(1)
            
            functionArity.push(functionArity.pop() + 1)
        else if token is VariableDelimiter
            while (operators.isNotEmpty() and operators.top() != Assignment)
                createTreeNode(operators.pop())
            
            if operators.isEmpty()
                Print <- "Assignment missing"
                exit(1)
            
            variables.queue(createTreeNode(operators.pop()))

    while (operators.isNotEmpty())
        if operators.top() == Openparenthesis
            Print -> "Unmatched parenthesis"

        createTreeNode(operators.pop())

    return nodes
```

Now understandable that this section can be really difficult to understand, it is a complicated algorithm. I advise everyone to read the wikipedia article: https://en.wikipedia.org/wiki/Shunting-yard_algorithm Really good explanation on the algorithm and its details. The modifications are in the section when the token is either a symbol or an assignment. In short, this algorithm creates nodes (Abstract syntax nodes) for a tree structure and returns the stack of nodes for a given input.

Some examples:

Input `max(2,2)`

| Token         | Node stack                | Operators stack | functionArity | Description                                                                                                       |
| ------------- |:-------------------------:|:---------------:|:-------------:|-------------------------------------------------------------------------------------------------------------------|
| FUN(max)      | []                        | []              | []            | add function max to operator stack                                                                                |
| PAREN(()      | []                        | [max]           | []            | set function arity to 0 and add paren to operators stack                                                          |
| NUM(2)        | []                        | [max (]         | [0]           | increase function arity to 1 and create tree node out of 2                                                        |
| COMMA(,)      | [Node(2)]                 | [max (]         | [1]           | pop operators stack until top is an open paren and increase function arity to 2                                   |
| NUM(2)        | [Node(2)]                 | [max (]         | [2]           | create tree node out of 2                                                                                         |
| PAREN())      | [Node(2), Node(2)]        | [max (]         | [2]           | pop operators stack until top is an open paren and discard open paren and create tree node out of max             |
|               | [Node(2), Node(2)]        | []              | [2]           | pop function arity value from stack and pop arity amount of nodes from node stack and add as children to max node |
|               | [Node(max)]               | []              | []            |                                                                                                                   |

`Result =>`

```
    max
   /   \
  2     2
```

Input `x = 2: 2^x`

| Token         | Node stack                | Operators stack | variable queue| Description                                                                                                       |
| ------------- |:-------------------------:|:---------------:|:-------------:|-------------------------------------------------------------------------------------------------------------------|
| SYM(x)        | []                        | []              | []            | create tree node for symbol x                                                                                     |
| ASSIGNMENT    | [Node(x)]                 | []              | []            | push assignment to operator stack                                                                                 |
| NUM(2)        | [Node(x)]                 | [=]             | []            | create tree node out of 2                                                                                         |
| DELIMITER(:)  | [Node(x),Node(2)]         | []              | []            | pop until top of operator stack is assignment and create node for assignment                                      |
|               | [Node(x),Node(2)]         | []              | []            | pop exactly 2 nodes from the node stack an add them to assignment node as children and add node to variable queue | 
| NUM(2)        | []                        | []              | [Node(=)]     | create tree node out of 2                                                                                         |
| OPERATOR(^)   | [Node(2)]                 | []              | [Node(=)]     | pop operators stack until ( or stack empty and add to operators stack                                             |
| SYM(x)        | [Node(2)]                 | [^]             | [Node(=)]     | create tree node for symbol x                                                                                     |
|               | [Node(2). Node(x)]        | [^]             | [Node(=)]     | pop operators stack until empty and create tree nodes from the remaing operators                                  |
|               | [Node(^)]                 | []              | [Node(=)]     |                                                                                                                   |

`Result =>`

```
    =
  /   \
 x     2

   ^
 /   \
2     x
```

Input `(2+2)*2`

| Token         | Node stack                | Operators stack | Description                                                                                 |
| ------------- |:-------------------------:|:---------------:|---------------------------------------------------------------------------------------------|
| PAREN(()      | []                        | []              | add paren to operator stack                                                                 |
| NUM(2)        | []                        | [(]             | create tree node for 2                                                                      |
| OPERATOR(+)   | [Node(2)]                 | [(]             | pop operator stack until top of stack is ( and add operator + to stack                      |
| NUM(2)        | [Node(2)]                 | [(,+]           | create tree node for 2                                                                      |
| PAREN())      | [Node(2),Node(2)]         | [(,+]           | pop operators stack until top is ( and discard ( create tree nodes for the popped operators |
|               | [Node(2),Node(2)]         | []              | pop exactly two nodes from node stack and add them to + node as children                    |
| OPERATOR(*)   | [Node(+)]                 | []              | pop operator stack until top of stack is ( and add operator * to stack                      |
| NUM(2)        | [Node(+)]                 | [*]             | create tree node for 2                                                                      |
|               | [Node(+),Node(2)]         | [*]             | pop operators stack until empty and create tree nodes from the remaining operators          |
|               | [Node(+),Node(2)]         | []              | pop exactly two nodes from the node stack and add them to * node as children                |
|               | [Node(*)]                 | []              |                                                                                             |

`Result =>`

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
Space complexity: `O(n^2)` where n is the number of custom functions
Average space complexity: In the average case space complexity is θ(n) where n is the number of nodes in the tree

The evaluator is the part that processess the nodes that were created by the parser. This is the part where actual values are made. In some earlier reports I claimed that the tree is traversed in inorder while it is infact traversed in postorder depth first search. All leaf nodes have to be numbers, or in some cases symbols. Each time we reach a leaf we will stop the recusive decent and go up to the parent and evaluate the parent operator.

Pseudo code example:

```
HashTable FunctionArity     <>
HashTable SymbolTable       <>
HashTable FunctionBodies    <>
HashTable FunctionArguments <>
HashTable StlFunctions      <>
HashTable ContextSymbolTable<>
Stack Context               []

EvaluateAssignment(node)
    if node is Function
        Queue argumentSymbols = []
        for each child in node.children
            argumentSymbols.add(child.symbol())
        FunctionArity.add(<node, node.children.size()>)
        FunctionBodies.add(<node, node.body>)
        FunctionArguments.add(<node,argumentSymbol>)
    else
        SymbolTable.add(<node.symbol(), Evaluate(node.body)>)
        
Evaluate(node)
    if node is number 
        return node

    if node is symbol
        return symbol

    if node is customFunction
        return EvaluateCustomFunction(node)

    # node is apparently operator or standard library function

    arguments = []

    for each child in node.children
        if node is max or min and arguments.size() = 2
            # eval max and min two arguments at a time
            arguments.add(EvaluateStlFunction(node, arugments))
        arguments.add(Evaluate(node))

    return EvaluateStlFunction(node, arguments)

EvaluateCustomFunction(node)
    FunctionBody <- FunctionBodies.get(node)
    FunctionArgumentSymbols <- FunctionArguments.get(node)
    HashTable FunctionContext = <>

    for each child in node.children
        symbol <- FcuntionArgumentSymbols.dequeue()
        FunctionContext.add(<symbol, Evaluate(child)>)
        
    ContextSymbolTable.add(<node, FunctionContext>)

    Context.push(node)
    Result <- Evaluate(FunctionBody)
    Context.pop(node)
    Return Result

EvaluateStlFunction(node, arguments)
    checkArgumentsAreCorrect(node, arguments)
    switch (node)
        * return multiply arguments
        + return add arguments
        - return subtract arguments
        $ return subtract argument
        ^ return pow arguments
        % return modulo arguments
        / return divide arguments
        sqrt return sqrt arguments
        ...
        default => Print <-- "Unknown Symbol " + node.token()
```

This section is hopefully not so difficult to understand. Tree traversals and recusive functions. As mentioned earlier the tree is evaluated from the bottom up and from left to right.
Some examples with intermediate stages:

```
        *            *          8
       / \          / \        / \
      +   2   =>   4   2  =>  4   2
     / \          / \        / \
    2   2        2   2      2   2 
```

Some examples:

```
          =
       /     \
     fn       *
    /  \     / \
   x    y   y   x

        fn
       /  \
      2    8
```

| Node          | FunctionArity  | FunctionBodies | FuncArguments | FuncContextTable        | Context Stack | Description                                                                                                      |
| ------------- |:--------------:|:--------------:|:-------------:|:-----------------------:|:-------------:|------------------------------------------------------------------------------------------------------------------|
| Node(=)       |                |                |               |                         |               | Read Nodes first child                                                                                           |
| Node(fn)      |                |                |               |                         |               | DFS over the nodes arguments and create queue for functions arguments                                            |
| Node(x)       |                |                |               |                         |               | Check that x is type symbol and add to queue                                                                     |
| Node(y)       |                |                |               |                         |               | Check that y is type symbol and add to queue                                                                     |
| Node(fn)      |                |                |               |                         |               | Set functionArity for function to 2 and add arguments queue to Functions arguments hashtable                     |
| Node(*)       | [<fn, 2>]      |                | [<fn, [x,y]>] |                         |               | Add function body to FunctionBody HashTable                                                                      |
|               | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] |                         |               | Assignment Node DFS Finished                                                                                     |
| Node(fn)      | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] |                         |               | Evaluate custom function get argument symbols from Function arguments hashtable                                  |
| Node(2)       | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] |                         |               | Evaluate node and map node to first symbol in fn's Function arguments hashtable entry                            |
| Node(8)       | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>]>]         |               | Evaluate node and map node to second symbol in fn's Function arguments hashtable entry                           |
| Node(fn)      | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |               | Check that fn's function arity is equal 2 and grab fn's body from FunctionBodies hashtable and add fn to context |
|               | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |[fn]           | fn node DFS Done                                                                                                 |
| Node(*)       | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |[fn]           | Create Queue for node's children's value DFS over node's children                                                |
| Node(y)       | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |[fn]           | Peek context stack and check peeked context's context hashtable and give node value of 8                         |
| Node(x)       | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |[fn]           | Peek context stack and check peeked context's context hashtable and give node value of 2                         |
| Node(*)       | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |[fn]           | Evaluate `8*2`                                                                                                   |
|               | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |[fn]           | Return result 16 and pop element from context stack                                                              |
| 8             | [<fn, 2>]      | [<fn, Node(*)>]| [<fn, [x,y]>] | [<fn, [<x,2>. <y,8>]>]  |[]             | Finished                                                                                                         |
 
```

    =
   / \
  x   2

  abs
   |
   x
```

| Node          | Symbol table   | Description                                                                          |
| ------------- |:--------------:|--------------------------------------------------------------------------------------|
| Node(=)       |                | Read Nodes firs and second child simultaneously and add to symbol table              |
|               | [<x.2>]        | Assignment Node DFS done                                                             |
| Node(abs)     | [<x.2>]        | Create Queue for node's children's values and DFS over node's children               |
| Node(x)       | [<x.2>]        | Context stack is empty, check value for Node from symbol table and give node value 2 |
| Node(abs)     | [<x,2>]        | Evaluate Stl function with argument 2                                                |
| 2             | [<x,2>]        | Finished                                                                             |


## Improvements

### Loops
Initially there was a plan to implement loops into CalcMalc, but was quickly left out of the implementation phase, mostly due to the fact that I had no idea how to implement it.

Loops would have looked something like this, e.g.,

```
x = 5:

sum = 0:

loop x  > 0:
    sum = sum + sqrt(x):
    x = x - 1:

sum
```

### Lexer Code

Currently the lexer rereads character from the input string. This happens when reading numbers or continuous character. When the character no longer matches either number or alphabetical letter the lexer has to reread the failed character. In the next version the lexer will be updated to read each character once, and only once.

### Parse double

In the evaluator there is one place where Double.Parse is used. This might not be allowed, but writing a custom working parseDouble function was actually kinda impossible.

### String concat

In some cases the project uses string concat operator +. The operation is really slow, but didn't have time to implement a custom concat function, apart from one place in the evaluator.
In the future there could be a faster string concat function in the project.

### Better Square root, pow and logarithm functions

Made three custom math functions. 2 of which were almost useless. If I would have had more time I would have tried to make much much better square root function and logarithm function.
Pow function worked nicely, only problem is that it doesn't support fractional exponents.

### Better Error Messages

Currently the error messages tell the user quite little about what went wrong. Missing colon error and pointing out where the code failed could be nice features to add.

## References

[Tirakirja](https://raw.githubusercontent.com/pllk/tirakirja/master/tirakirja.pdf)

[JDK Soure code](https://hg.openjdk.java.net/jdk8/jdk8/jdk)

[Shunting Yard](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)

[Extending the shunting yard algorithm](https://blog.kallisti.net.nz/2008/02/extension-to-the-shunting-yard-algorithm-to-allow-variable-numbers-of-arguments-to-functions/)

[P3G Shunting Yard Algorithm](http://wcipeg.com/wiki/Shunting_yard_algorithm)

[Evaluating Infix Expressions](http://www.neocomputer.org/projects/lang/infix.html)

[Writing a programming language](https://www.youtube.com/watch?v=9-EYWLbmiG0&t=2052s)