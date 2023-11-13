package no.adrsolheim.littl;

public class AstPrinter implements Expr.Visitor<String> {

    public void print(Expr expr) {
        System.out.println(expr.accept(this));
    }

    @Override
    public String visit(Expr.Literal expr) {
        return expr.value == null ? "nil" : String.valueOf(expr.value);
    }

    @Override
    public String visit(Expr.Binary expr) {
        return parensWrap(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visit(Expr.Unary expr) {
        return expr.operator.lexeme + expr.expr.accept(this);
    }

    @Override
    public String visit(Expr.Grouping expr) {
        return parensWrap("grouping", expr.expr);
    }

    String parensWrap(String name, Expr... exprs) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(name);
        for (Expr expr : exprs) {
            sb.append(" ");
            sb.append(expr.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }
}
