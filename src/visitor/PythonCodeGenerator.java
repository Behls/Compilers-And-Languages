package visitor;

import tree.*;
import java.io.PrintStream;

public class PythonCodeGenerator implements NodeVisitor {

    private final PrintStream out;

    public PythonCodeGenerator(PrintStream outputstream) {
        out = outputstream;
    }

    @Override
    public void visitBinaryExpression(BinaryExpression node) {
        out.print("(");
        node.left.traverse(this);
        out.print(")" + node.operator + "(");
        node.right.traverse(this);
        out.print(")");
    }

    @Override
    public void visitVariableRef(VariableRef node) {
        out.print(node.getName());
    }

    @Override
    public void visitIntegerValue(IntegerValue node) {
        out.print(node.getValue());
    }

    @Override
    public void visitBooleanValue(BooleanValue node){
        out.print(node.getValue());
    }

    @Override
    public void visitStringValue(StringValue node){
        out.print(node.getValue());
    }

    @Override
    public void visitAssignment(Assignment node) {
        indentation();
        node.getVariable().traverse(this);
        out.print(" = ");
        node.getRightHandSide().traverse(this);
        out.println();
    }

    @Override
    public void visitPrint(Print node) {
        indentation();
        out.print("print ");
        node.getExpression().traverse(this);
        out.println();
    }

    @Override
    public void visitRead(Read node) {
        indentation();
        node.getVariable().traverse(this);
        out.print(" = int(raw_input(\"");
        node.getVariable().traverse(this);
        out.println(" = ? \"))");
    }

    @Override
    public void visitConditional(Conditional node) {
        indentation();
        out.print("if ");
        node.getCondition().traverse(this);
        out.println(":");
        indent();
        node.getThenPart().traverse(this);
        outdent();
        indentation();
        out.println("else:");
        indent();
        node.getElsePart().traverse(this);
        outdent();
    }

    @Override
    public void visitWhile(While node) {
        indentation();
        out.print("while ");
        node.getCondition().traverse(this);
        out.println(":");
        indent();
        node.getLoopBody().traverse(this);
        outdent();
    }

    @Override
    public void visitBlock(Block node) {
        for (int i = 0; i < node.getStatements().size(); i++) {
            node.getStatements().get(i).traverse(this);
        }
    }

    public void visitFunction(Function node){

    }

    private int depth;

    public void indent() {
        depth++;
    }

    public void outdent() {
        depth--;
    }

    public void indentation() {
        for (int i = 0; i < depth; i++) {
            out.print("    ");
        }
    }
}
