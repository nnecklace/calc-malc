
# Manual

## Requirements

Java 15 or higher

gradle 6.7

Optional: Python 3

## Commands

### Start the repl

```./gradlew repl```

Once the welcoming message has been shown you can start using the repl.

### Pass file to calcmalc

`./gradlew run --args='src/inputs/test_input_2.txt'`

Make sure all the test files are located in the `app/src/inputs/` directory!
The compiled jar doesn't need the --args argument. 

```java -jar <jar-file> <path to file>``` 

`app/src/inputs/` contains a sandbox file `calc_malc_test.txt`. This file is meant to be a playground where you can write different programs and test calcmalc's features.

### Start the UI

```./gradlew ui```

### Generate javadocs

```./gradlew javadoc```

### Run the unit tests

```./gradlew test```

### Build the project

```./gradlew build```

The compiled jar will be in `app/build/libs` folder. 

### Run checkstyle

```./gradlew checkstyleMain```

### Run jacoco test coverage report

```./gradlew jacocoReport```
(make sure you run the unit tests first)

### Run performance test

```./gradlew performance```

### Run outcome verifyer

Make sure you are in the root directory.

```python3 scripts/generate.py```

Make sure the jar file is in the same directory.

## Usage of functions

```
abs(x) Returns the absolute value of a number. Takes exactly one parameter;

sqrt(x) Returns the square root value of number. Takes exactly one parameter;

log(x) Returns the logarithmic value of a number. Takes exactly one parameter;

max(...) Returns the maximum value of the given parameters. Takes an n amount of parameters, were n is any number.

min(...) Returns the minimum value of the given parameteres. Takes an n amount of parameter, were n is any number.

x + y Operator adds both parameters and returns their sum.

x - y Operator subtracts both parameteres and return their differance.

x * y Operator multiplies both parameters and returns their product.

x / y Operator divides left parameter by the left and returns their quotient.

x ^ y Operator multiples the base x y amount of times to itself. 

x % y Operator returns the modulo of x mod y.

-x Operator is unary and returns the negative of its parameter.
```

## Examples 

```
1.
2+2

2.
3*3

3.
(1+1)^2

4.
sqrt(2)

5.
abs(2/((2+2)*(5-2)))

6.
max(1,2,3,4,5,6,7,8,9,10)

7.
PI = 3.14:

calculate_circle_area(radius) = RI*radius^2:

calculate_circle_area(sqrt(2))
```

## Basic Programs

Programs in CalcMalc are usually written in a way where variables and custom functions are declared first and the last line is an expression to evaluate.
Variables are not constants, they can be reassigned at any point, same with functions. The last assignment of the variable or function is aka the source of truth for that variable of function.

```
add(x,y) = x+y:

add(x,y) = x*y:

add(2,3)
```
The result of this program will be 6 and not 5, since the source of truth for add is `x*y`.

Variables and functions are declared on their own line. Variables or functions can't be created inside other variables or functions. Variables and functions also can't be created inside math expressions.
The following are illegal

```
x = (y = 4)y+2:

func(x) = (
    y = 2:
    x * y
):

sqrt(x)+y=2*4
```

Functions and variables also have to end with the delimiter symbol `:`.

One thing to notice is that variables and functions are evaluated separetly from expressions. This means that variables and functions are visible to each other at all times. 
Example
```
calc_circle_area(radius) = PI*radius^2:

PI = 3.14:

calc_circle_area(2)
```

## Context Free Grammar

All syntax that calcmalc supports can be described by the following context free grammar.

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
