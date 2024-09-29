package no.adrsolheim.lit;

public abstract class Expr {

    abstract <R> R accept(Visitor<R> visitor);

    public static class Literal extends Expr {

        final Object value;

        public Literal (Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Binary extends Expr {

        final Expr left;
        final Token operator;
        final Expr right;

        public Binary (Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Unary extends Expr {

        final Token operator;
        final Expr expr;

        public Unary (Token operator, Expr expr) {
            this.operator = operator;
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Grouping extends Expr {

        final Expr expr;

        public Grouping (Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }


    interface Visitor<R> {
        <R> R visit(Literal expr);
        <R> R visit(Binary expr);
        <R> R visit(Unary expr);
        <R> R visit(Grouping expr);
    }

}
