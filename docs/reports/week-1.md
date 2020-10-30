
# Week Report 1

Hours spent: 8-10

This week I have mostly done research on types of solutions to syntax analysing, parsing and evalution. Knew that I wanted to do something with the Shunting Yard algorithm, but I feel that it in it self is too lightweight for this course. I've also been keen on trying to make a parser for a simple arthemetic inputs for sometime now. I felt that this was a good opportunity to try it out. There are many solutions and algorithms for parsing and analysing inputs, really overwhelming (CYK, LL, LLR etc). Haven't yet have time to settle for one specific algorithm, besides the Shunting Yard algorithm. 

This week I've also written the definitions document, and generated a skeleton application for the project.

This week I learned that parsing inputs and creating some tree structure of out it is not really that simple, infact it seems to be difficult. Also, analysing the tree structure and determining if there are syntax errors can also be quite hard. I did -- however -- learn that the Shunting Yard algorithm can be used to create an AST so maybe I can tweak the algorithm a little to make it parse inputs like `fn f(x,y)= 2*x+4+y`.

What is still unclear is what is the best solution to this problem. Basic inputs, like `2+2+(2*4+8+15)+log(12)*max(5,5)` are easy to solve, but the more complicated inputs, like `fn f(x,y) = x+y` are still somewhat unclear. Also when reading the theories on parsing and analysing, I get the feeling that I might not necessarily need any tree structures.

Next I will start the coding phase. I will create the necessary data structures that are used in the Shunting Yard algorithm (stacks, queues and lists), and get it to accept simple inputs. Also need to continue to do some research on the more complicated inputs and how to parse and analyse them.I am also excited to get to use java 15.