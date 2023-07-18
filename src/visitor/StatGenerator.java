package visitor;

import tree.*;

public class StatGenerator implements NodeVisitor {

    private int operations = 0, ifs = 0, whiles = 0, assignments = 0,
            others = 0, vars = 0, ints = 0, blocks = 0;

    @Override
    public void visitBinaryExpression(BinaryExpression node) {
        operations++;
        node.left.traverse(this);
        node.right.traverse(this);
    }

    @Override
    public void visitVariableRef(VariableRef node) {
        vars++;
    }

    @Override
    public void visitIntegerValue(IntegerValue node) {
        ints++;
    }

    @Override
    public void visitAssignment(Assignment node) {
        assignments++;
        node.getVariable().traverse(this);
        node.getRightHandSide().traverse(this);
    }

    @Override
    public void visitPrint(Print node) {
        others++;
        node.getExpression().traverse(this);
    }

    @Override
    public void visitRead(Read node) {
        others++;
        node.getVariable().traverse(this);
    }

    @Override
    public void visitConditional(Conditional node) {
        ifs++;
        node.getCondition().traverse(this);
        node.getThenPart().traverse(this);
        node.getElsePart().traverse(this);
    }

    @Override
    public void visitWhile(While node) {
        whiles++;
        node.getCondition().traverse(this);
        node.getLoopBody().traverse(this);
    }

    @Override
    public void visitBlock(Block node) {
        blocks++;
        for (int i = 0; i < node.getStatements().size(); i++) {
            node.getStatements().get(i).traverse(this);
        }
        if (node.isTopLevel()) {
            System.out.println("Number of code blocks = " + blocks);
            System.out.println("Number of IF statements = " + ifs);
            System.out.println("Number of WHILE statements = " + whiles);
            System.out.println("Number of := statements = " + assignments);
            System.out.println("Number of I/O statements = " + others);
            System.out.println("Number of binary operations = " + operations);
            System.out.println("Number of variable references = " + vars);
            System.out.println("Number of integer constants = " + ints);
        }
    }
}
