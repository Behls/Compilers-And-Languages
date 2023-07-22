package parser;

import tree.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeBuilder {

    private ProgramNode rootNode;

    public ProgramNode getRootNode() {

        return rootNode;
    }

    public void makeExpression(String operator, Expression left, Expression right) {
        rootNode = new BinaryExpression(operator, left, right);
    }

    public void makeIntegerValue(Integer value) {
        rootNode = new IntegerValue(value);
    }

    public void makeStringValue(String value) {
        rootNode = new StringValue(value);
    }

    public void makeBooleanValue(Boolean value) {
        rootNode = new BooleanValue(value);
    }

    public void makeVariableReference(String name) {
        if (ProgramNode.varTable == null) {
            ProgramNode.varTable = new HashMap<>();
        }
        if (ProgramNode.varTable.containsKey(name)) {
            rootNode = ProgramNode.varTable.get(name);
        } else {
            rootNode = new VariableRef(name);
            ProgramNode.varTable.put(name, (VariableRef) rootNode);
        }
    }

    public void makePrintStatement(Expression argument) {
        rootNode = new Print(argument);
    }

    public void makeReadStatement(VariableRef variable) {
        rootNode = new Read(variable);
    }

    public void makeAssignmentStatement(VariableRef variable, Expression rhs) {
        rootNode = new Assignment(variable, rhs);
    }

    public void makeConditionalStatement(Expression condition, Block thenPart, Block elsePart) {
        rootNode = new Conditional(condition, thenPart, elsePart);
    }

    public void makeWhileStatement(Expression condition, Block body) {
        rootNode = new While(condition, body);
    }

    public void makeBlock(ArrayList<Statement> statements) {
        rootNode = new Block(statements);
    }

}
