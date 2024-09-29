package no.adrsolheim.lit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private static final char NULL_CHARACTER = '\0';
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("if",     TokenType.IF);
        keywords.put("else",   TokenType.ELSE);
        keywords.put("and",    TokenType.AND);
        keywords.put("or",     TokenType.OR);
        keywords.put("fun",    TokenType.FUN);
        keywords.put("return", TokenType.RETURN);
        keywords.put("var",    TokenType.VAR);
        keywords.put("nil",    TokenType.NIL);
        keywords.put("while",  TokenType.WHILE);
        keywords.put("for",    TokenType.FOR);
        keywords.put("this",   TokenType.THIS);
        keywords.put("print",  TokenType.PRINT);
        keywords.put("class",  TokenType.CLASS);
        keywords.put("super",  TokenType.SUPER);
    }

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
            case '\n' -> { line++; yield TokenType.SKIP; }
            case '"' -> string();
            default -> {
                if (isDigit(c)) {
                    yield number();
                } else if (isAlpha(c)) {
                    yield identifierOrKeyword();
                }
                yield TokenType.INVALID;
            }
        };
        addToken(type);
    }

    private void addToken(TokenType tokenType) {
        if (tokenType == null) {
            Lit.lexerError(line, "Unrecognized symbol found.");
            return;
        }
        if (tokenType == TokenType.SKIP) {
            return;
        }
        if (tokenType == TokenType.STRING) {
            addToken(tokenType, source.substring(start + 1, current - 1));
        } else if (tokenType == TokenType.IDENTIFIER) {
            addToken(tokenType, source.substring(start, current));
        } else if (tokenType == TokenType.NUMBER) {
            addToken(tokenType, Double.parseDouble(source.substring(start, current)));
        } else {
            addToken(tokenType, null);
        }
    }

    private void addToken(TokenType tokenType, Object literal) {
        String lexeme = source.substring(start, current);
        tokens.add(new Token(tokenType, lexeme, literal, line));
    }

    private TokenType string() {
        while(!atEndOfSource() && peek() != '"') {
            if (peek() == '\n') { // newlines within strings
                line++;
            }
            advance();
        }
        if (peek() != '"') {
            Lit.lexerError(line, "String not closed");
        }

        advance(); // close string

        return TokenType.STRING;
    }

    private TokenType number() {
        // integer part
        while(!atEndOfSource() && isDigit(peek())) {
            advance();
        }
        // decimal point
        if (!atEndOfSource() && peek() == '.' && isDigit(doublePeek())) {
            advance();
        }
        // decimal part
        while(!atEndOfSource() && isDigit(peek())) {
            advance();
        }
        return TokenType.NUMBER;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private TokenType identifierOrKeyword() {
        while(!atEndOfSource() && isAlphaNumeric(peek())) {
            advance();
        }
        String lexeme = source.substring(start, current);
        return keywords.containsKey(lexeme) ? keywords.get(lexeme) : TokenType.IDENTIFIER;
    }
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
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

    private char peek() {
        return atEndOfSource() ? NULL_CHARACTER : source.charAt(current);
    }
    private char doublePeek() {
        return current+1 >= source.length() ? NULL_CHARACTER : source.charAt(current+1);
    }

    public boolean atEndOfSource() {
        return current >= source.length();
    }
}
