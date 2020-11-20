
# Week Report 4

Hours spent: 15 - 18

This week was much more incoherent than expected. I wish I had more time for this project, much much fun. This week finished the evaluator and also implemented variable assignments in the project `x = 2:`.
Lot of testing and fixing bugs. Much of this week was realizing that the much of my code was unnecessary. Started cleaning up after I implemented the variables assignments, still feel there is much to cleanup.
I just wanted to make sure my parser and evaluator work as expected, and that's why I spent so much time writing tests and testing the program. 100 unit tests currently and looking to add more. I also want to make sure my program accepts the inputs I mentioned in my context free language in the defintions document. There are still probably some bugs in the code, e.g., something weird might happend with inputs like `max(x=2:,2)`.

Also started writing the testing and implemenation documents. Still work in progres. I need to do better performance testing and acutally make some graph for the performance testing results.

The main part of the project is about done at this point. Need to fix some issues, like the about mentioned things.

Next I will fix bugs and cleanup the code a little bit, also need to create a hashtable data structure. Need to create a simple UI for the project and do proper testing. I should somehow figure out how to generate 10^6 inputs document to feed to my program. Ah! Also need to remove that dumb manual size limit for my lists. And I also need to make my project accept floating point numbers, currently only integers are supported.

Unfortunately, I don't think I will have enough time to implement functions for my project. I do have a vague idea how it could be done, but time is limited and need to focus on more important things, like testing and documentation, and UI development.

I'm still learning a lot about designing programming languages, I way over confident in my abilities :) This has been really fun to make. I also learned why, e.g., considers all numbers to be floating point numbers, it actually makes designing the programming language much easier. CalcMalc also interprets all numbers as floating point numbers.

Difficulties that I've encountered throughout this project have mostly been related to lack of material, no one seems to have done something similair (Shunting Yard -> AST). Lot of the material don't use abstract syntax tree's or their algorithms don't accept all inputs that I want my project to accept. E.g., many solutions don't accept `-100+(-100)` because they don't support unary minus operator. Best material so far has been shunting yard's wikipedia page, and my whiteboard at home. Also found some youtube videos on desiging programming languages, they have had minor impact on my project, but some help still.
