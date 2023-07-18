package tree;

import visitor.NodeVisitor;

public class Read extends Statement {

    private final VariableRef variable;

    public Read(VariableRef var) {
        variable = var;
    }

    public VariableRef getVariable() {
        return variable;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitRead(this);
    }
}
