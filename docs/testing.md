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

Some of the above shown inputs are not recognized by the context free language, e.g., x=2:.  

## Manual testing

Manual tests can be made easily. First start CalcMalc `./gradlew run` and wait for the greeting message. Then input any of the above mentioned inputs and see the results. I really hope someone finds inputs that break CalcMalc :). Point to remember, variable declaration will currently return 0.0 as a result, this is to be expected, not a bug.

## Unit Testing

The project currently has about 100 unit tests for different kinds of inputs. All unit tests can be run with gradle `./gradlew test` (unix systems).
All inputs haven't been tested for each section (Lexer, Parser, Evaluator).

## Performance Testing

Performance testing has been limited to writing manually large and long expressions, e.g., `1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1` and so on. Performance testing should ideally be made on the jar file, currently performance testing has been done with gradle and gradle seems to lie quite much, currently gradle says it takes 12 seconds to run the small test input file in the root directory.

There should ideally be 2-3 large files (currently only one small one) with complicated inputs that could easily be fed to CalcMalc. Scripts could also be added to make it easier to run performance tests. 