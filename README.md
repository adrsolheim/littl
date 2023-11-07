
# (Litt)le (L)anguage
A little basic and primitive language

Build and run
```
mvn clean install

java --enable-preview -jar target/littl-1.0-SNAPSHOT.jar
```

# Compilers and Interpreters
A compiler *translates* source code to another -- usually low-level -- form. Source code -> bytecode, or source code -> native code. 

An interpreter reads source code *and executes it* immediately.

### Frontend
- Lexing -- convert raw text to a series of tokens.
- Parsing -- convert token sequence into an AST (introduces grammar)
- Static Analysis (binding) -- resolve what entities *identifiers* refer to by looking up *scope*
- Static Analysis (storing information) -- store semantic information in AST, symbol table or another data structure

### "Middle end"
- Intermediate representations (IR) -- frontend language that produce IR. Backend translate IR to target architecture.
- Optimization -- a known user program can be swapped with a more efficient implementation with the *same semantics*

### Backend
- Code Generation -- convert the program to something the computer can understand
  - Native -- compiler targets chip architectures directly. Faster & more complex
  - Bytecode -- compiler outputs virtual machine code. Then requires bytecode to native code, or bytecode to virtual machine.

### Runtime
- Native -- the OS simply runs the executable
- Bytecode -- start VM, load program and execute

### Implementations
- Single-pass compilers -- interleave parsing, analysis, and code generation to produce output code directly in the parser. No AST or IRs
- Tree-walk interpreters -- traverse and execute code one branch and leaf at a time, right after parsing to AST.
- Transpilers -- produce a string of valid source code for another language, e.g., C or Haskell.
- Just-in-time compilation -- compile source code or bytecode to native code for the architecture the current machine supports. 

### Language Features

- Dynamic typing - Store values of any type in a variable. Store different types in a variable at different times.
- Automatic memory management - Reference counting is easier to implement but is limited. Garbage collection more powerful and more complex. 
- Data types - Booleans, Numbers (int, decimal), Strings & NiL (null)
- Expressions - Produce a value; Arithmetic, Comparison & equality, Logical operators, and Precedence & grouping.
- Statements - Produce an effect (control flow operation) -> Print statements. Variable declarations
- Variables - Defaults to `nil`
- Control Flow - if statements, while- and for-loops
- Functions - Function call expression with parameter list. Closures hold on to references to variables after the outer scope (holding those variables) has returned.
- Classes - Classes contain methods and inheritance chain. Objects contain state. 
- Prototypes - Objects only, and they contain methods, inheritance chain and state.


