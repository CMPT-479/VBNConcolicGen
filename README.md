# VBNConcolicGen: Java Automatic Unit Test Generation
Unit testing provides the foundation of the Testing pyramid in
traditional approaches and requires a lot of resources to create
and maintain the test suite. We aim to automate the process to
save resources (and avoid tedious work) concolic execution.

Concolic execution (concrete + symbolic) has been used by
Sen et al. to generate unit tests for both C and Java programs
through CUTE and jCUTE respectively. Those libraries were
written almost 20 years ago, with older dependencies. This
project aims to recreate portions of the CUTE library using
dependencies and versions of Java. This project recreation
will be substituting Sen et al.’s jCUTE usage of JDK 1.4 with
JDK 1.11, and will use Z3 as the SMT solver, offered by
Microsoft Research, rather than lp_solve.

Heavily inspired by CUTE and jCUTE.

## Components
- Instrumentation
- Runtime Library
- Concolic Runner
- Constraint Solver

## Dependencies
- Java 1.11
- Maven
- JUnit
- Z3, used by many symbolic execution modules
- Soot

## Contributors
- [Viet-Hung Nguyen](https://github.com/viethung7899) (vhn@sfu.ca)
- [Brendan Saw](https://github.com/brendansaw) (bsaw@sfu.ca)
- [Nathan Tsai](https://github.com/chrishappy) (nta41@sfu.ca)
