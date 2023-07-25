package visitor;

import tree.*;
import java.util.HashMap;
import java.util.Scanner;

public class Interpreter implements NodeVisitor {

    private final HashMap<String, Integer> symbolTable;
    private int integerValue;
    private boolean booleanValue;
    private String identifier;
    private boolean identifierNotKnown;
    private final Scanner stdin;
    private String stringValue;
    private boolean booleanV;


    public Interpreter() {
        symbolTable = new HashMap<>();
        stdin = new Scanner(System.in);
    }

    @Override
    public void visitBinaryExpression(BinaryExpression node) {
        int left, right;

        node.left.traverse(this);
        left = integerValue;
        node.right.traverse(this);
        right = integerValue;
        if (node.operator.equals("+")) {
            integerValue = left + right;
        } else if (node.operator.equals("-")) {
            integerValue = left - right;
        } else if (node.operator.equals("*")) {
            integerValue = left * right;
        } else if (node.operator.equals("/")) {
            integerValue = left / right;
        } else if (node.operator.equals("%")) {
            integerValue = left % right;
        } else if (node.operator.equals("<")) {
            booleanValue = left < right;
        } else if (node.operator.equals(">")) {
            booleanValue = left > right;
        } else if (node.operator.equals("==")) {
            booleanValue = left == right;
        }
    }

    @Override
    public void visitVariableRef(VariableRef node) {
        identifier = node.getName();
        if (symbolTable.containsKey(identifier)) {
            integerValue = symbolTable.get(identifier);
            identifierNotKnown = false;
        } else {
            //System.out.println("Error: " + identifier + " is not in symbol table!");
            identifierNotKnown = true;
            integerValue = 0;
            symbolTable.put(identifier, integerValue);
        }
    }

    @Override
    public void visitIntegerValue(IntegerValue node) {
        integerValue = node.getValue();
    }

    @Override
    public void visitBooleanValue(BooleanValue node){
        booleanV = (node.getValue());
    }

    @Override
    public void visitStringValue(StringValue node){
        stringValue = (node.getValue());
    }

    @Override
    public void visitAssignment(Assignment node) {
        String id;
        node.getVariable().traverse(this);
        id = identifier;
        node.getRightHandSide().traverse(this);
        symbolTable.put(id, integerValue);
    }

    @Override
    public void visitPrint(Print node) {
        node.getExpression().traverse(this);
        System.out.println(integerValue);
    }

    @Override
    public void visitRead(Read node) {
        node.getVariable().traverse(this);
        System.out.print(identifier + " = ? ");
        integerValue = stdin.nextInt();
        symbolTable.put(identifier, integerValue);
    }

    @Override
    public void visitConditional(Conditional node) {
        node.getCondition().traverse(this);
        if (booleanValue) {
            node.getThenPart().traverse(this);
        } else {
            node.getElsePart().traverse(this);
        }
    }

    @Override
    public void visitWhile(While node) {
        node.getCondition().traverse(this);
        while (booleanValue) {
            node.getLoopBody().traverse(this);
            node.getCondition().traverse(this);
        }
    }

    @Override
    public void visitBlock(Block node) {
        for (int i = 0; i < node.getStatements().size(); i++) {
            node.getStatements().get(i).traverse(this);
        }
    }

    @Override
    public void visitFunction(Function node){

    }
}
