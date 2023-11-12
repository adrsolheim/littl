package no.adrsolheim.littl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AstPrinterTest {


    @Test
    void expressions_to_string_representation() {
        AstPrinter visitor = new AstPrinter();
        Expr expressions = new Expr.Binary(
                new Expr.Binary(
                        new Expr.Literal(7),
                        new Token(TokenType.PLUS, "+", null, 1),
                        new Expr.Literal(0)
                ),
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Grouping(
                        new Expr.Unary(
                                new Token(TokenType.MINUS, "-", null, 1),
                                new Expr.Literal(55)
                        )
                )
        );
        String result = expressions.accept(visitor);
        String expected = "-(+(70) grouping(-55) ) ";

        Assertions.assertEquals(expected, result);
    }
}