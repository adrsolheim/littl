package no.adrsolheim.littl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void assign_decimal_number_to_idenfitier() {
        String source = "var n = 123.5";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        List<Token> expected = List.of(
                new Token(TokenType.VAR, "var", null, 1),
                new Token(TokenType.IDENTIFIER, "n", "n", 1),
                new Token(TokenType.EQUAL, "=", null, 1),
                new Token(TokenType.NUMBER, "123.5", 123.5, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        assertIterableEquals(expected, tokens);
    }

}