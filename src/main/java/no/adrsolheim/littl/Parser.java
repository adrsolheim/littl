package no.adrsolheim.littl;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        return atEnd() ? false : type == peek();
    }

    private boolean atEnd() {
        return tokens.get(current).tokenType == TokenType.EOF;
    }

    private TokenType peek() {
        return tokens.get(current).tokenType;
    }

    private Token previous() {
        return tokens.get(current-1);
    }

    private TokenType advance() {
        return tokens.get(current++).tokenType;
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr expr = comparison();
        while(match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while(match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while(match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while(match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.BANG,TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return literal();
    }

    private Expr literal() {
        if (match(TokenType.TRUE)) return new Expr.Literal(true);
        if (match(TokenType.FALSE)) return new Expr.Literal(false);
        if (match(TokenType.NIL)) return new Expr.Literal(null);
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expr.Literal(previous().literal);
        }
        if(match(TokenType.LEFT_PAREN)) {
            consume(TokenType.LEFT_PAREN, "Expected closing ')'");
            return new Expr.Grouping(expression());
        }
        return null;
    }

    private void consume(TokenType expected, String message) {
        if (expected == TokenType.LEFT_PAREN) {
            List<TokenType> illegalTypes = List.of(TokenType.EOF, TokenType.RIGHT_BRACE, TokenType.LEFT_PAREN, TokenType.LEFT_BRACE);
            for (int i = current;;i++) {
                if (illegalTypes.contains(tokens.get(i))){
                    System.out.println(message);
                    break;
                }
            }
        }
    }
}
