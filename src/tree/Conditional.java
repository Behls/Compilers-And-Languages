package tree;

import visitor.NodeVisitor;

public class Conditional extends Statement {

    private final Expression condition;
    private final Block thenPart;
    private final Block elsePart;

    public Expression getCondition() {
        return condition;
    }

    public Block getThenPart() {
        return thenPart;
    }

    public Block getElsePart() {
        return elsePart;
    }

    public Conditional(Expression c, Block tp, Block ep) {
        condition = c;
        thenPart = tp;
        elsePart = ep;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitConditional(this);
    }
}
