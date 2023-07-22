package tree;

//imports
import visitor.NodeVisitor;

public class BooleanValue extends Expression{

    private final boolean value;

    public BooleanValue(boolean v) {
        value = v;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public void traverse(NodeVisitor v) {
        v.visitBooleanValue(this);
    }

}
