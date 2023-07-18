package tree;

import visitor.NodeVisitor;

public class IntegerValue extends Expression {

    private final int value;

    public IntegerValue(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void traverse(NodeVisitor v) {

        v.visitIntegerValue(this);
    }
}
