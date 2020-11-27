
## Week 5

Hours spent: 26 - 29h

This week I started fixing bugs in the project, and also implmenting correct data structures. I created a hashtable structure, also fixed my list implementation. I had tried to make list remove `O(1)` operation, but this resulted in many hidden bugs. Had refactor _all_ my data structures because of this. Stack was rewritten, as was Queue. Queue will probably still be fixed more. Will make it into a linkedlist.

Found a handful of small hidden bugs that resulted in the code actually becoming more readable in my opinion. Lots of edge cases were found, mostly resulting from poorly implemented data structures.
Also generated some nice inputs for performance testing purposes. Unfortunately testing failed somewhat, there doesn't seem to be any good way of increasing the stack size or the heap size of the jvm in gradle. DFS runs out of memory on inputs n > 20000. Maybe if I just compile to a jar I can increase memory size. Gradle really annoyed me this week. Haven't had time to benchmark the performance testing yet, will do it next. 

As mentioned, next up will be benchmarking, queue refactor, and GUI development.

This week I was reminded that list remove is actually a `O(n)` operation. Also learned that Java saves memory on objects. Two Integer classes can have the same value even though they are different integers, really weird but this is not a bug apparently.

What is still unclear for me is how should I estimate the space complexity for the Parser. There are some recursive calls and each recusive call creates three new stacks (size n). Would it be `O(2^(3*n))` <=> `O(2^n)` ?

The workload for this project is getting out of hands. Hopefully next week will be less stressful :) 
