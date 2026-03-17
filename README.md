# Compiler-Principal
# Overview

This repository contains three core laboratory projects from the Compiler Construction course. Together, they demonstrate the fundamental stages of a compiler's front-end: converting raw source code into token streams, and subsequently parsing those streams using two distinct syntax analysis algorithms (Top-Down Recursive Descent and LL(1) Table-Driven Parsing).

# Project 1: Lexical Analyzer (Scanner)
Overview
The Lexical Analyzer is designed to process a simplified programming language called "CP" (Compiler Principle). It scans the raw source code string character by character and groups them into meaningful sequences called tokens.

Implementation Details
Keywords Recognition: Identifies standard lowercase keywords such as begin, end, if, then, else, for, while, do, and, or, and not.

Operators & Delimiters: Supports a comprehensive set of symbols including mathematical operators (+, -, *, /), relational operators (>, <, =, >=, <=, <>), assignment (:=), and delimiters ((, ), ;).

Whitespace & Comment Handling: Automatically ignores spaces, tabs, and newlines. It also skips single-line comments starting with the # symbol.

Output: Generates a stream of tuples (Token ID, String Value) to be fed into the syntax analyzer.

# Project 2: Recursive Descent Parser
Overview
This project implements a Top-Down Recursive Descent Parsing subroutine to validate simple arithmetic expressions based on a specific context-free grammar G[E].

Target Grammar G[E]
The parser eliminates left-recursion and utilizes the following grammar:

E → TE'

E' → ATE' | ε

T → FT'

T' → MFT' | ε

F → (E) | i (where i represents an identifier/variable)

A → + | -

M → * | /

Implementation Details
Language: C++ (utilizing std::vector and std::string manipulation).

Functionality: It features recursive functions for each non-terminal (E, T, F, etc.). It evaluates whether an input token sequence is a valid arithmetic expression.

Leftmost Derivation Generation: Beyond simple validation, the program uniquely records and prints the Leftmost Derivation (最左推导过程) of the expression step-by-step (e.g., E => TE' => FTE' => ...) using string injection techniques.

# Project 3: LL(1) Syntax Analyzer
Overview
Unlike the hard-coded recursive descent parser, this project implements a dynamic, table-driven LL(1) Syntax Analyzer. It automatically processes grammar rules to build a prediction table and analyzes expressions.

Implementation Details
Language: Java (utilizing Collections like HashSet and HashMap for set operations).

Algorithmic Core: 1. Automatically computes the FIRST set for all non-terminals.
2. Automatically computes the FOLLOW set for all non-terminals.
3. Constructs the LL(1) Prediction Table based on the computed sets.

Output: Displays the complete FIRST sets, FOLLOW sets, the generated 2D LL(1) analysis table, and the parsing steps for test sentences.

Environment & Usage
Lexer & Recursive Descent Parser: Requires a standard C++11 compiler (e.g., g++).

LL(1) Parser: Requires Java Development Kit (JDK 8 or higher).

Execution: Each project is standalone. Feed the output tuples of the Lexical Analyzer into the syntax parsers to simulate a complete compiler front-end pipeline.
