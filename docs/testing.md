# Testing

## What has been tested

In my definitions document is showed a context free language that CalcMalc should recognize and throughout this course I've been trying to make the algorithm accept that context free grammar.
This includes inputs like:

```
2+2
2*(2+2+6+8-10)/3*2
2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3))))
x=2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3)))):
x=2:y=2:z=4
sin(max(min(2,5),sqrt(1))+2+2)
-100+(-100)
```
And many more.

The context free grammar looks something like this.

Capital letter are excluded from the context free grammar, but they can be used just like lower case letters.

```
S        => EXPR | (EXPR) | VAR | e
VAR      => VAR_NAME=EXPR_OR_VALUE:VAR | e
VAR_NAME => VAR_NAME | (VAR_NAME) | SYMBOL() | SYMBOL(SYMBOL) | SYMBOL(SYMBOL,SYMBOL)
SYMBOL => _LETTER | aLETTER | bLETTER | cLETTER | dLETTER | eLETTER | fLETTER | gLETTER | hLETTER | iLETTER | jLETTER | kLETTER | lLETTER | mLETTER | nLETTER | oLETTER | pLETTER | qLETTER | rLETTER | sLETTER | tLETTER | uLETTER | vLETTER | wLETTER | xLETTER | yLETTER | zLETTER 
LETTER => _SYMBOL | aSYMBOL | bSYMBOL | cSYMBOL | dSYMBOL | eSYMBOL | fSYMBOL | gSYMBOL | hSYMBOL | iSYMBOL | jSYMBOL | kSYMBOL | lSYMBOL | mSYMBOL | nSYMBOL | oSYMBOL | pSYMBOL | qSYMBOL | rSYMBOL | sSYMBOL | tSYMBOL | uSYMBOL | vSYMBOL | wSYMBOL | xSYMBOL | ySYMBOL | zSYMBOL | e
EXPR_OR_VALUE => SYMBOL | EXPR
EXPR => FUNCTION(EXPR) | (EXPR) | NUBER | -EXPR | EXPR + EXPR | EXPR - EXPR | EXPR * EXPR | EXPR / EXPR | EXPR^EXPR | EXPR % EXPR
FUNCTION => log | abs | min | max | sqrt | sin | cos | tan
NUMBER => 0DIGIT | 1DIGIT | 2DIGIT | 3DIGIT | 4DIGIT | 5DIGIT | 6DIGIT | 7DIGIT | 8DIGIT | 9DIGIT
DIGIT => NUMBER | e
```

Inputs can be found with the following cfg tool.
https://web.stanford.edu/class/archive/cs/cs103/cs103.1156/tools/cfg/

Valid inputs are, e.g., 
```
x = 2:y = 2:z= x^y:
(5+x)+y*z
```

## Manual testing

Manual tests can be made easily. First start CalcMalc `./gradlew repl` and wait for the greeting message. Then input any of the above mentioned inputs and see the results. I really hope someone finds inputs that break CalcMalc :).

## Unit Testing

The project currently has about 140 unit tests for different kinds of inputs. All unit tests can be run with gradle `./gradlew test` (unix systems).
All inputs haven't been tested for each section (Lexer, Parser, Evaluator). Test coverage should be 100%.

## Performance Testing

App folder contains a folder called inputs which includes several testing files, all named `test_input_{x}.txt`. All include inputs of different sizes and some contain variables an some not. 

```
test_input_2.txt    (n = 30)    = 14
test_input_1.txt    (n = 429)   = 215
test_input_3.txt    (n = 1000)  = 22.130913118333183
test_input_6.txt    (n = 20000) = -66963.60130479863
test_input_7.txt    (n = 20063) = -95926.6552149921
test_input_11.txt   (n = 50000)  = 23200.447662919054
test_input_5.txt    (n = 100000) = -461536.8189290817
test_input_13.txt   (n = 500000) = 58476770.37377888
test_input_4.txt    (n = 1000000) = 5.491284883131212E8
test_input_9.txt    (n = 2000000) = 2.7280722156884734E7
test_input_8.txt    (n = 3000000) = 4061403.6528164987
test_input_12.txt   (n = 5000000) = 5741399.3087914875
test_input_10.txt   (n = 10000000) = -2.960803788820491E8
```

The input files can be tested with the command ./gradlew run --args='_some file location_', where _some file location_ should be a valid file location for calcmalc. E.g., 
`./gradlew run --args='src/inputs/test_input_2.txt'`. The larger files will probably fail without configuring gradle's inner jvm stacksize. Add `jvmArgs '-Xss512m'` should be enough for all the test files.

Test files were generated using python scripts.

```python
import random

output = ""

operators = ["+", "-", "*", "/"] # add more operators if needed

for i in range(1, 100): # some range
    if i % 2 == 0:
        output += random.choice(operators)
    else:
        output += str(random.randint(1, 9))

print(output,  file=open("test_filename.txt", "a"))
```

The files were verified by a python script
```python
import os

files = os.listdir("app/src/inputs/")

for file in files:
    if file == "test_input_7.txt" or file == "test_input_2.txt": # python can evaluate my program
        continue
    print("file " + file)
    st = open("app/src/inputs/" + file, 'r').read()
    print("Result " + str(eval(st)))
```

Some larger files may require configuring the process stack size.

## Results

Run the performance test suite with `./gradlew performance`. Test suite can be found in `app/src/main/java/calcmalc/performance/PerformanceTest.java`. Test suite reads and evaluates each generated test file for 40 runs. Below are some benchmark results.

```
Results: 
test_input_2.txt ran on average 0.0012706414 on runs of 10
test_input_1.txt ran on average 0.0027720661 on runs of 10
test_input_3.txt ran on average 0.0029877456 on runs of 10
test_input_6.txt ran on average 0.0300822358 on runs of 10
test_input_7.txt ran on average 0.052239819 on runs of 10
test_input_11.txt ran on average 0.07462040140000001 on runs of 10
test_input_5.txt ran on average 0.1583822914 on runs of 10
test_input_13.txt ran on average 0.8369532523 on runs of 10
test_input_4.txt ran on average 1.214685729 on runs of 10
test_input_9.txt ran on average 2.6815009426999996 on runs of 10
test_input_8.txt ran on average 4.0054051211 on runs of 10
test_input_12.txt ran on average 6.2718223313 on runs of 10
test_input_10.txt ran on average 12.5357777728 on runs of 10
```

## Graph

<img src="https://raw.githubusercontent.com/nnecklace/calc-malc/main/docs/images/graph.png" width="800px"/>

As we can see from the graph the algorithm increases linearly up until n = 2*10^6 after which it starts to increase exponentially.
