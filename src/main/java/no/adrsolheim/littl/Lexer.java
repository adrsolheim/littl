package no.adrsolheim.littl;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final char NULL_CHARACTER = '\0';
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
            case  '(' -> TokenType.LEFT_PAREN;
            case  ')' -> TokenType.RIGHT_PAREN;
            case  '{' -> TokenType.LEFT_BRACE;
            case  '}' -> TokenType.RIGHT_BRACE;
            case  ',' -> TokenType.COMMA;
            case  '.' -> TokenType.DOT;
            case  '-' -> TokenType.MINUS;
            case  '+' -> TokenType.PLUS;
            case  ';' -> TokenType.SEMICOLON;
            case  '*' -> TokenType.STAR;
            case  '!' -> nextSymbol('=') ?    TokenType.BANG_EQUAL : TokenType.BANG;
            case  '=' -> nextSymbol('=') ?   TokenType.EQUAL_EQUAL : TokenType.EQUAL;
            case  '<' -> nextSymbol('=') ?    TokenType.LESS_EQUAL : TokenType.LESS;
            case  '>' -> nextSymbol('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER;
            case  '/' -> nextSymbol('/') ?    skipUntil('\n') : TokenType.SLASH;
            case  ' ' -> TokenType.SKIP;
            case '\r' -> TokenType.SKIP;
            case '\t' -> TokenType.SKIP;
            case '\n' -> {
                line++;
                yield TokenType.SKIP;
            }
            case '"' -> string();
            default -> null;
        };
        addToken(type);
    }

    private TokenType string() {
        while(!atEndOfSource() && peek() != '"') {
            if (peek() == '\n') { // newlines within strings
                line++;
            }
            advance();
        }
        if (peek() != '"') {
            Littl.error(line, "String not closed");
        }
        return TokenType.STRING;
    }

    private TokenType skipUntil(char end) {
        while(!atEndOfSource() && peek() != '\n') {
            advance();
        }
        return TokenType.SKIP;
    }

    private boolean nextSymbol(char symbol) {
        if (atEndOfSource()) return false;
        if (source.charAt(current) != symbol) return false;
        current++;
        return true;
    }

    private void addToken(TokenType tokenType) {
        if (tokenType == null) {
            Littl.error(line, "Unrecognized symbol found.");
            return;
        }
        if (tokenType == TokenType.SKIP) {
            return;
        }
        if (tokenType == TokenType.STRING) {
            addToken(tokenType, source.substring(start+1, current-1));
        }
        addToken(tokenType, null);
    }

    private void addToken(TokenType tokenType, Object literal) {
        String lexeme = source.substring(start, current);
        tokens.add(new Token(tokenType, lexeme, literal, line));
    }

    private char peek() {
        return atEndOfSource() ? NULL_CHARACTER : source.charAt(current);
    }

    public boolean atEndOfSource() {
        return current >= source.length();
    }
}
