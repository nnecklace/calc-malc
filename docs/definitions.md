# Definitions

## Background

I belong to the tietojenkäsittelytieteen kandidaattiohjelma (TKT). My project will be in English, as will all documents, code and comments.


## Algorithms & Data structures

Calc-Malc will use a handful of data structures and algorithms to fullfill it's purpose. The algorithm that will be in the most significant role of the project is the [Shunting Yard](https://en.wikipedia.org/wiki/Shunting-yard_algorithm) algortim. The algorithm will be used to create an abstract syntax tree, for analysis custom functions, and convert a infix notation input string to postfix notation for ease of evaluating the mathematical input expressions given to the program. Data structures that will be used with the Shunting yard algorithm are, lists, stacks, and queues. List will be used to contain the actual input for the program, the program will read symbol at a time and add each symbol to the end of the list. Stacks, or stack, is the one of the main data structures of the Shunting Yard algorithm. The stack will contain all the operators of the mathematical expression. The operators will be poped from the stack according to their precedence level. The queue will contain the final postfix notation form of the expression.

Abstract syntax trees (AST) will also be used. AST will be used at the syntax analysis phase for custom functions. User will have the ability to create custom functions, during runtime these functions need to be analyzed and validated. AST will be traversed with preorder traversal and at the same time the expressions will again be converted into postfix notaiton.

The project will also use Hashmaps to store assignments, like variables and functions. 


## Problems Solved

Calc-Malc is intended to read and evaluate arthemetic expressions and let en users create basic assignments for contiunous use functions and values. I chose Shunting Yard since it's a famous algorithm that solves pricesly the problem I am trying to solve with converting infix expressions to postfix expressions. Shunting Yard makes use of stacks and queues, that is the reason I chose to go with those data structures. I also really wanted to try out parsing and lexing. That is why I want to give users the ability to assign variables and functions. Functions and variables also make for a better user experience. Abstract syntax trees are usually used when analysing and evaluating expressions, or context free grammars in this case. There are many alternatives to AST, but as of right now it seems that AST is the best choice for analysis and evaluating. 


## Input

<img src="https://raw.githubusercontent.com/nnecklace/calc-malc/main/docs/images/a1.png" width="800px"/>

The above explains what language is recognized by Calc-Malc. All the arthemetic expressions that Calc-Malc recognizes are in infix notation, e.g. `2 + 2 * 2`. During evaluation of the expressions, the expression will be transfored from infix to postfix notation, e.g `2 + 2 * 2 ==> 222*+`. Inputs in the form of assigments will be transformed to AST. E.g. `fn f(x) = 2 + 2 * x`.

``` 
        fn
      /    \
     f      =
    /        \
   x          +
             / \
            2   * 
               / \
              2   x 
```

Example of what an input that Calc-Malc would accept:

```
fn f(x) = 2^x+4*x

var x = f(5)
var c = 5
var y = 0

loop 5 = y = c*f(10)

sqrt((2+2)*2+c*log(y))
```

## Time and space complexity

It is difficult to estimate the time and space complexity for this project. Ideal, and realistic, time complexity at all stages should be `O(n)`. Shunting Yard is `O(n)` algorithm and tree traversals are `O(|V|+|E|)` which we can reduce to `O(n)`. Space complexity is tough to estimate, realistically the space complexity will be `O(n^4)`. `O(n)` for the stack, `O(n)` for the queue, `O(n)` for the AST, and `O(n)` for the input list. `O(n)*O(n)*O(n)*O(n) = O(n^4)`


## References

1. [Shunting Yard](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)

2. [Reverse Polish Notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation)

3. [AST](https://en.wikipedia.org/wiki/Abstract_syntax_tree)

4. [Parsing Algorithms](https://tomassetti.me/guide-parsing-algorithms-terminology/)

5. [Context Free Grammars](https://www.cs.rochester.edu/~nelson/courses/csc_173/grammars/cfg.html)
