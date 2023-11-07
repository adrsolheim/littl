package no.adrsolheim.littl;

public enum TokenType {
    // single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    PLUS, MINUS, SLASH, STAR,
    COMMA, DOT,  SEMICOLON,

    // one or two character tokens
    BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

    // literals
    IDENTIFIER, STRING, NUMBER,

    // keywords
    IF, ELSE, AND, OR,
    FUN, RETURN, VAR, NIL,
    WHILE, FOR, THIS, PRINT,
    CLASS, SUPER,

    SKIP, INVALID, EOF
}
