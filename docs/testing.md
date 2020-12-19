# Testing

## What has been tested

In my definitions document is showed a context free language that CalcMalc should recognise and throughout this course I've been trying to make the algorithm accept that context free grammar.
This includes inputs like:

```
2+2
2*(2+2+6+8-10)/3*2
2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3))))
x=2*(2*(2+2)*(1+(4+4*(10+1)))+(10*(5+(2+3)))):
x=2:y=2:z=4
log(max(min(2,5),sqrt(1))+2+2)
-100+(-100)
```
And many more.

The context free grammar looks something like this.

Capital letter are excluded from the context free grammar, but they can be used just like lower case letters.

```
S             => EXPR | (EXPR) | VAR | e
VAR           => VAR_NAME=EXPR_OR_VALUE:VAR | e
VAR_NAME      => VAR_NAME | (VAR_NAME) | SYMBOL(PARAM)
PARAM         => SYMBOL,PARAM | SYMBOL | (SYMBOL) | (SYMBOL,PARAM)
SYMBOL        => _LETTER | aLETTER | bLETTER | cLETTER | dLETTER | eLETTER | fLETTER | gLETTER | hLETTER | iLETTER | jLETTER | kLETTER | lLETTER | mLETTER | nLETTER | oLETTER | pLETTER | qLETTER | rLETTER | sLETTER | tLETTER | uLETTER | vLETTER | wLETTER | xLETTER | yLETTER | zLETTER 
LETTER        => _SYMBOL | aSYMBOL | bSYMBOL | cSYMBOL | dSYMBOL | eSYMBOL | fSYMBOL | gSYMBOL | hSYMBOL | iSYMBOL | jSYMBOL | kSYMBOL | lSYMBOL | mSYMBOL | nSYMBOL | oSYMBOL | pSYMBOL | qSYMBOL | rSYMBOL | sSYMBOL | tSYMBOL | uSYMBOL | vSYMBOL | wSYMBOL | xSYMBOL | ySYMBOL | zSYMBOL | e
EXPR_OR_VALUE => SYMBOL | EXPR
EXPR          => FUNCTION(EXPR) | (EXPR) | NUMBER | -EXPR | EXPR + EXPR | EXPR - EXPR | EXPR * EXPR | EXPR / EXPR | EXPR^EXPR | EXPR % EXPR
FUNCTION      => log | abs | min | max | sqrt 
NUMBER        => 0DIGIT | 1DIGIT | 2DIGIT | 3DIGIT | 4DIGIT | 5DIGIT | 6DIGIT | 7DIGIT | 8DIGIT | 9DIGIT
DIGIT         => NUMBER | e
```

Inputs can be found with the following cfg tool.
https://web.stanford.edu/class/archive/cs/cs103/cs103.1156/tools/cfg/

Valid inputs are, e.g., 
```
x = 2:y = 2:z= x^y:
(5+x)+y*z
```

## Manual testing

Manual tests can be made easily. First start CalcMalc `./gradlew repl` or `./gradlew ui` and wait for the greeting message. Then input any of the above mentioned inputs and see the results. I really hope someone finds inputs that break CalcMalc :).

## Unit Testing

The project currently has about 220 unit tests for different kinds of inputs. All unit tests can be run with gradle `./gradlew test` (unix systems).
All inputs haven't been tested for each section (Lexer, Parser, Evaluator). Test coverage should be close 100%. Test coverage can be viewed by generating jacoco test report coverage `./gradlew jacocoReport`

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

All test files were verified with https://api.mathjs.org/ . However, test_input_10.txt remains unverified since https://api.mathjs.org/ takes too long to respond. This can happen with all test files larger than 10^6. In this case the following python code can be used to verify all but test_input_10.txt.

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
The result for test_input_10.txt is honestly not that important. More important was to see how the performance behaves on very large inputs.


The input files can be tested with the command ./gradlew run --args='_some file location_', where _some file location_ should be a valid file location for calcmalc. E.g., 
`./gradlew run --args='src/inputs/test_input_2.txt'`. The larger files will probably fail without configuring the heap size for the jvm. Add `jvmArgs '-Xss512m'` when running performance tests with jar file, this should be enough for all the test files, with gradle nothing has to be added.

Test files were generated using python script.

The file is located in `scripts/generate.py`. The script will first generate a random input can call calcmalc with it, and then verify the result with a api request to https://api.mathjs.org/.
There might be some precision errors from time to time. Mathjs is built with Javascript and might give different precision than CalcMalc.

## Performance Test Results

Run the performance test suite with `./gradlew performance`. Test suite can be found in `app/src/main/java/calcmalc/performance/PerformanceTest.java`. Test suite reads and evaluates each generated test file for 40 runs. Below are some benchmark results. Not all test files are included in the results, since some of them are only meant for unit tests.

Results are displayed in seconds
```
test_input_7.txt ran on average 0.0068723878 on runs of 30
test_input_6.txt ran on average 0.0067285579666666664 on runs of 30
test_input_4.txt ran on average 0.5972191891333333 on runs of 30
test_input_5.txt ran on average 0.04518566373333333 on runs of 30
test_input_1.txt ran on average 7.81546E-4 on runs of 30
test_input_2.txt ran on average 5.512952666666668E-4 on runs of 30
test_input_3.txt ran on average 6.937549333333333E-4 on runs of 30
test_input_12.txt ran on average 4.3993488048 on runs of 30
test_input_13.txt ran on average 0.2565695782 on runs of 30
test_input_11.txt ran on average 0.027049322733333336 on runs of 30
test_input_10.txt ran on average 8.0342533581 on runs of 30
test_input_8.txt ran on average 2.0447751723333334 on runs of 30
test_input_9.txt ran on average 1.3458993782666666 on runs of 30
```

### Difference Between Custom Math Functions

I used 3 custom math functions in calcmalc. All were significantly slower, and more inaccurate than Java Math methods. Below are some comparisons of the implementations.

```
Comparing Sqrt
Custom Sqrt ran on average in 9.936466E-8
Math Sqrt ran on average in 4.319516E-8

Comparing Log
Custom Log ran on average 2.7860173684999997E-4
Math Log ran on average 8.180576E-8
```

The tests were performed with 10^5 runs. Although Java Math implementation's were significantly faster, the speed was not the worst part, the inaccuracy was. Custom sqrt uses the newton method to count the square root and it turned out to be much less accurate than Java's native implementation. The custom log was implemented with Taylor's series and it also was highly inaccurate, and in some cases wrong results e.g., `log(e) =/= e`.

Because of the inaccuracy the veryification became impossible, so decided to go with Java's native solutions instead.

## Results as Graph

<img src="https://raw.githubusercontent.com/nnecklace/calc-malc/main/docs/images/graph.png" width="800px"/>

Right column is seconds, while horizontal axis is input size.

As we can see from the graph the algorithm increases linearly up until n = 2*10^6 after which it starts to increase exponentially.
