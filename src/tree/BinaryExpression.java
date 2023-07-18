package tree;

import visitor.NodeVisitor;

public class BinaryExpression extends Expression {

    public String operator;
    public Expression left, right;

    public BinaryExpression(String op, Expression lhs, Expression rhs) {
        operator = op;
        left = lhs;
        right = rhs;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitBinaryExpression(this);
    }
}
