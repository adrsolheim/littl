package no.adrsolheim.littl;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Lexer(String sourceCode) {
        this.source = sourceCode;
    }

    public List<Token> scanTokens() {
        while(!atEndOfSource()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void scanToken() {
        char c = advance();
        TokenType type = switch (c) {
            case '(' -> TokenType.LEFT_PAREN;
            case ')' -> TokenType.RIGHT_PAREN;
            case '{' -> TokenType.LEFT_BRACE;
            case '}' -> TokenType.RIGHT_BRACE;
            case ',' -> TokenType.COMMA;
            case '.' -> TokenType.DOT;
            case '-' -> TokenType.MINUS;
            case '+' -> TokenType.PLUS;
            case ';' -> TokenType.SEMICOLON;
            case '*' -> TokenType.STAR;
            default -> null;
        };
        addToken(type);
    }

    private void addToken(TokenType tokenType) {
        if (tokenType == null) return;
        addToken(tokenType, null);
    }

    private void addToken(TokenType tokenType, Object literal) {
        String lexeme = source.substring(start, current);
        tokens.add(new Token(tokenType, lexeme, literal, line));
    }

    public boolean atEndOfSource() {
        return current >= source.length();
    }
}
