package no.adrsolheim.littl;

public class Token {
    private final TokenType tokenType;
    private final String lexeme;
    private final Object literal;
    private final int line;

    public Token(TokenType tokenType, String lexeme, Object literal, int line) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return STR."\{tokenType} \{lexeme} \{literal}";
    }
}
