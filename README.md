# VBNConcolicGen: Java Automatic Unit Test Generation
Unit testing provides the foundation of the Testing pyramid in
traditional approaches and requires a lot of resources to create
and maintain the test suite. We aim to automate the process to
save resources (and avoid tedious work) concolic execution.

Concolic execution (**conc**rete + symb**olic**) has been used by
Sen et al. to generate unit tests for both C and Java programs
through CUTE and jCUTE respectively. Those libraries were
written almost 20 years ago, with older dependencies. This
project aims to recreate portions of the CUTE library using
dependencies and versions of Java. This project recreation
will be substituting Sen et al.’s jCUTE usage of JDK 1.4 with
JDK 11, and will use Z3 as the SMT solver, offered by
Microsoft Research, rather than lp_solve.

The project implements Java unit test generation capabilities 
by performing basic instrumentation on Java code, running the 
instrumented program multiple times, and generating solved inputs 
such that every branch of a Java program is explored. 
The project uses Soot to instrument the code, 
and the Z3 SMT solver to solve constraints that allow us to 
re-execute programs and reach alternative branches.

More information about VBNConcolicGen can be found in the 
final report: [CMPT 479 - Final Report](CMPT%20479%20-%20Final%20Report.pdf).

This project was created as a final project for CMPT 479/745 (Special Topics in Software Engineering) 
at Simon Fraser University, supervised by Dr. Nick Sumner.

## Components
- Instrumentation
- Runtime Library
- Concolic Runner
- Constraint Solver

## Dependencies
- Java 1.11
- Maven
- JUnit
- Z3
- Soot

## Contributors
- [Viet-Hung Nguyen](https://github.com/viethung7899) (vhn@sfu.ca)
- [Brendan Saw](https://github.com/brendansaw) (bsaw@sfu.ca)
- [Nathan Tsai](https://github.com/chrishappy) (nta41@sfu.ca)
