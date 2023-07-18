package tree;

import visitor.NodeVisitor;

public class While extends Statement {

    private final Expression condition;
    private final Block loopBody;

    public Expression getCondition() {
        return condition;
    }

    public Block getLoopBody() {
        return loopBody;
    }

    public While(Expression c, Block b) {
        condition = c;
        loopBody = b;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitWhile(this);
    }
}
