# CalcMalc

## Week reports
[Week report 1](https://github.com/nnecklace/calc-malc/blob/main/docs/reports/week-1.md)

[Week report 2](https://github.com/nnecklace/calc-malc/blob/main/docs/reports/week-2.md)

[Week report 3](https://github.com/nnecklace/calc-malc/blob/main/docs/reports/week-3.md)

[Week report 4](https://github.com/nnecklace/calc-malc/blob/main/docs/reports/week-4.md)

[Week report 5](https://github.com/nnecklace/calc-malc/blob/main/docs/reports/week-5.md)

[Week report 6](https://github.com/nnecklace/calc-malc/blob/main/docs/reports/week-6.md)

## Documents

[Definitions](https://github.com/nnecklace/calc-malc/blob/main/docs/definitions.md)

[Testing](https://github.com/nnecklace/calc-malc/blob/main/docs/testing.md)

[Implementation](https://github.com/nnecklace/calc-malc/blob/main/docs/implementation.md)

[Manual](https://github.com/nnecklace/calc-malc/blob/main/docs/manual.md)

## Overview 

Faculty: TKT

Language: English

Programming language: Java

CalcMalc is simple arthemetic REPL (Read, Evaluate, Parse, Loop). CalcMalc can read any normal arthemetic expression, such as `2+2`, and it should also support a handful of other mathematical functions: `log, ln, sqrt, max, min, abs`. You should also be able to create your own temporary values, variables and functions, and be able to reuse them thourghout the process's lifetime. The project is written in Java and all documents and code will be written in English.

## Requirements

Java 15 or higher

gradle 6.7

## Commands

Run the project with a test file `./gradlew run --args='src/inputs/test_input_2.txt'`

Make sure all the test files are located in the app directory!

Run the repl, i.e., interactive environment `./gradlew repl`

Run unit tests `./gradlew test`

Run jacoco test coverage `./gradlew jacocoTestReport` (make sure you run the unit tests first)

Run checkstyle `./gradlew checkstyleMain`
