package no.adrsolheim.littl;

public enum TokenType {
    // single-character tokens
    LEFT_PAREN(false), RIGHT_PAREN(false), LEFT_BRACE(false), RIGHT_BRACE(false),
    PLUS(false), MINUS(false), SLASH(false), STAR(false),
    COMMA(false), DOT(false),  SEMICOLON(false),

    // one or two character tokens
    BANG(false), BANG_EQUAL(false), EQUAL(false), EQUAL_EQUAL(false),
    GREATER(false), GREATER_EQUAL(false), LESS(false), LESS_EQUAL(false),

    // literals
    IDENTIFIER(false), STRING(false), NUMBER(false),

    // keywords
    IF(true), ELSE(false), AND(false), OR(false),
    FUN(true), RETURN(true), VAR(true), NIL(false),
    WHILE(true), FOR(true), THIS(false), PRINT(true),
    CLASS(true), SUPER(true), TRUE(false), FALSE(false),

    SKIP(false), INVALID(false), EOF(false);

    boolean statementStart;

    TokenType(boolean statementStart) {
        this.statementStart = statementStart;
    }

    boolean isStartOfStatement() {
        return statementStart;
    }
}
