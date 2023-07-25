package visitor;

import tree.*;

public class ASTPrinter implements NodeVisitor {

    private int depth = 0;

    private void indent() {
        depth++;
    }

    private void outdent() {
        depth--;
    }

    private void indentation() {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
    }

    @Override
    public void visitBinaryExpression(BinaryExpression node) {
        indentation();
        System.out.println(node.operator);
        indent();
        node.left.traverse(this);
        node.right.traverse(this);
        outdent();
    }

    @Override
    public void visitVariableRef(VariableRef node) {
        indentation();
        System.out.println(node.getName());
    }

    @Override
    public void visitIntegerValue(IntegerValue node) {
        indentation();
        System.out.println(node.getValue());
    }

    @Override
    public void visitBooleanValue(BooleanValue node){
        indentation();
        System.out.println(node.getValue());
    }

    @Override
    public void visitStringValue(StringValue node){
        indentation();
        System.out.println(node.getValue());
    }

    @Override
    public void visitAssignment(Assignment node) {
        indentation();
        System.out.println("Assignment");
        indent();
        node.getVariable().traverse(this);
        node.getRightHandSide().traverse(this);
        outdent();
    }

    @Override
    public void visitPrint(Print node) {
        indentation();
        System.out.println("Print");
        indent();
        node.getExpression().traverse(this);
        outdent();
    }

    @Override
    public void visitRead(Read node) {
        indentation();
        System.out.println("Read");
        indent();
        node.getVariable().traverse(this);
        outdent();
    }

    @Override
    public void visitConditional(Conditional node) {
        indentation();
        System.out.println("If");
        indent();
        node.getCondition().traverse(this);
        node.getThenPart().traverse(this);
        node.getElsePart().traverse(this);
        outdent();
    }

    @Override
    public void visitWhile(While node) {
        indentation();
        System.out.println("WHILE");
        indent();
        node.getCondition().traverse(this);
        node.getLoopBody().traverse(this);
        outdent();
    }

    @Override
    public void visitBlock(Block node) {
        indentation();
        System.out.println("Block");
        indent();
        for (int i = 0; i < node.getStatements().size(); i++) {
            node.getStatements().get(i).traverse(this);
        }
        outdent();
    }

    public void visitFunction(Function node){

    }
}
