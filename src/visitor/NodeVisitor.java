package visitor;

import tree.*;

public interface NodeVisitor {

    public abstract void visitBinaryExpression(BinaryExpression node);

    public abstract void visitVariableRef(VariableRef node);

    public abstract void visitIntegerValue(IntegerValue node);

    public abstract void visitAssignment(Assignment node);

    public abstract void visitPrint(Print node);

    public abstract void visitRead(Read node);

    public abstract void visitConditional(Conditional node);

    public abstract void visitWhile(While node);

    public abstract void visitBlock(Block node);
}
