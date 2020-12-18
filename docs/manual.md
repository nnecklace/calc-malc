
# Manual

## Requirements

Java 15 or higher

gradle 6.7

## Commands

### Start the repl

```./gradlew repl```

Once the welcoming message has been shown you can start using the repl.

### Pass file to calcmalc

`./gradlew run --args='src/inputs/test_input_2.txt'`

Make sure all the test files are located in the app directory!

### Start the UI

```./gradlew ui```

### Generate javadocs

```./gradlew javadoc```

### Run the unit tests

```./gradlew test```

### Build the project

```./gradlew build```

### Run checkstyle

```./gradlew checkstyleMain```

### Run jacoco test coverage report

```./gradlew jacocoReport```
(make sure you run the unit tests first)

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
FUNCTION      => log | abs | min | max | sqrt | sin | cos | tan
NUMBER        => 0DIGIT | 1DIGIT | 2DIGIT | 3DIGIT | 4DIGIT | 5DIGIT | 6DIGIT | 7DIGIT | 8DIGIT | 9DIGIT
DIGIT         => NUMBER | e
```
