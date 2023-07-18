package tree;

import visitor.NodeVisitor;

public class Print extends Statement {

    private final Expression expression;

    public Print(Expression e) {
        expression = e;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitPrint(this);
    }
}
