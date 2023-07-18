package visitor;

import tree.*;
import java.io.PrintStream;
import java.io.File;

public class JavaCodeGenerator implements NodeVisitor {

    private final PrintStream out;
    private final String className;

    public JavaCodeGenerator(PrintStream outputstream, String name) {
        File f = new File(name);
        className = f.getName().substring(0, f.getName().lastIndexOf("."));
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
    public void visitAssignment(Assignment node) {
        indentation();
        node.getVariable().traverse(this);
        out.print(" = ");
        node.getRightHandSide().traverse(this);
        out.println(";");
    }

    @Override
    public void visitPrint(Print node) {
        indentation();
        out.print("System.out.println(");
        node.getExpression().traverse(this);
        out.println(");");
    }

    @Override
    public void visitRead(Read node) {
        indentation();
        out.print("System.out.print(\"");
        node.getVariable().traverse(this);
        out.println(" = ? \");");
        indentation();
        node.getVariable().traverse(this);
        out.println(" = input.nextInt();");
    }

    @Override
    public void visitConditional(Conditional node) {
        indentation();
        out.print("if (");
        node.getCondition().traverse(this);
        out.println(")");
        node.getThenPart().traverse(this);
        indentation();
        out.println("else");
        node.getElsePart().traverse(this);
    }

    @Override
    public void visitWhile(While node) {
        indentation();
        out.print("while (");
        node.getCondition().traverse(this);
        out.println(")");
        node.getLoopBody().traverse(this);
    }

    @Override
    public void visitBlock(Block node) {
        if (node.isTopLevel()) {
            out.println("import java.util.Scanner;");
            out.println();
            out.println("public class " + className);
            out.println("{");
            indent();
            indentation();
            out.println("public static void main(String[] args)");
        }
        indentation();
        out.println("{");
        indent();
        if (node.isTopLevel()) {
            indentation();
            out.print("int ");
            String separator = "";
            for (String key : ProgramNode.varTable.keySet()) {
                out.print(separator);
                separator = ", ";
                visitVariableRef(ProgramNode.varTable.get(key));
            }
            out.println(";");
            indentation();
            out.println("Scanner input = new Scanner(System.in);");
            out.println("");
        }
        for (int i = 0; i < node.getStatements().size(); i++) {
            node.getStatements().get(i).traverse(this);
        }
        outdent();
        indentation();
        out.println("}");
        if (node.isTopLevel()) {
            outdent();
            indentation();
            out.println("}");
        }
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
