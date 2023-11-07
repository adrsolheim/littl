package no.adrsolheim.littl;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
       if (obj == null) return false;
       if (!(obj instanceof Token)) return false;
       Token tokenObj = (Token) obj;
       if (tokenType != tokenObj.tokenType) return false;
       if (!Objects.equals(lexeme, tokenObj.lexeme)) return false;
       if (!Objects.equals(literal, tokenObj.literal)) return false;
       if (line != tokenObj.line) return false;

       return true;
    }
}
