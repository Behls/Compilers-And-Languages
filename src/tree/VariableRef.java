package tree;

import visitor.NodeVisitor;

public class VariableRef extends Expression {

    private final String name;

    public String getName() {
        return name;
    }

    public VariableRef(String n) {
        name = n;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitVariableRef(this);
    }
}
