package tree;

import visitor.NodeVisitor;

public class Assignment extends Statement {

    private final VariableRef variable;
    private final Expression rightHandSide;

    public VariableRef getVariable() {
        return variable;
    }

    public Expression getRightHandSide() {
        return rightHandSide;
    }

    public Assignment(VariableRef var, Expression rhs) {
        variable = var;
        rightHandSide = rhs;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitAssignment(this);
    }
}
