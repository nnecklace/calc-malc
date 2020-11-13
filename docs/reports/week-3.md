
# Week Report 3

Hours spent: 24 - 27

This week I put a lot of hours into this project, much fun. I finally got a clear picture of how I want to structure this project. 
I created a Lexer for lexically analysising all the characters in the expression, updated the parser and the shunting yard algorithm to recognizes functions and its arguments. Also made the parser return an abstract syntax tree. The abstract syntax tree can now be traversed in preorder/postorder and can even return all the parsed tokens into reverse postfix notation. This will make parsing an evaluating variables much easier in the future. 

So the plan is the have a lexer (done) which passes the lexical tokens to the parser (done) which then creates an abstract syntax tree which will be given to evaluator (ongoing).

LEX -> PARSE -> EVAL -> LOOP

Also created better tests for everything. Last week I got feedback that data structures can work incorrectly without me knowing, and of course it so happend that my list and queue had bugs which I didn't notce before. Test coverage should be at 98% now. Skipped the main App.java file since it's only a sandbox atm and nothing in there will stay the same.

This week has been really interesting, learned a lot about designing programming languages, and they are really difficult to make. Really surprising how difficult it has been to create something that takes an input like: `2+2*(max(2,3))` and creates a syntax tree out of that.

```
            +
          /   \
        *      2
      /   \    
    max    2
  /     \
 2       3
```

What was really difficult this week was to find a good solution for parsing functions with an unknown amount of arguments. All the material I found had incorrect algorithms, and some were really bad algorithms, and some didn't use shunting yard as their parsing algorithm. Wikipedia had nice solution although it didn't translate well with me. In the end, I made my own solution and I think it works well.

Next thing I will do is implment an evaluator for the algorithm, so we can finally get some concrete results, also will start to create variables. Don't think I will have time to implement custom functions, like `f(x) = 2*x`, but if there will be enough time, I will see what I can do.
