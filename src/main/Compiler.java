package main;

import scanner.*;
import parser.*;
import tree.*;
import visitor.*;
import java.io.PrintStream;

class Compiler {

    private Lexer lexer;
    private Parser parser;
    private NodeBuilder builder;

    private final boolean debug;

    public Compiler(boolean debug) {
        this.debug = debug;
    }

    private ProgramNode abstractSyntaxTree() {
        if (builder != null) {
            builder.getRootNode().setTopLevel();
            return builder.getRootNode();
        }
        return null;
    }

    public boolean hasValidSyntaxTree() {
        return abstractSyntaxTree() != null;
    }

    public void buildAbstractSyntaxTree(String path) {
        lexer = new Lexer(path, debug);
        builder = new NodeBuilder();
        parser = new Parser(lexer, builder, debug);
    }

    public void generateCCode(PrintStream outputStream) {
        CCodeGenerator codeGenerator = new CCodeGenerator(outputStream);
        abstractSyntaxTree().traverse(codeGenerator);
    }

    public void generateJavaCode(PrintStream outputStream, String name) {
        JavaCodeGenerator codeGenerator = new JavaCodeGenerator(outputStream, name);
        abstractSyntaxTree().traverse(codeGenerator);
    }

    public void generatePythonCode(PrintStream outputStream) {
        PythonCodeGenerator codeGenerator = new PythonCodeGenerator(outputStream);
        abstractSyntaxTree().traverse(codeGenerator);
    }
    
    public void generateRALCode(PrintStream outputStream, String name) {
        RALCodeGenerator codeGenerator = new RALCodeGenerator(outputStream, name);
        abstractSyntaxTree().traverse(codeGenerator);
    }

    public void printStatistics() {
        StatGenerator statGenerator = new StatGenerator();
        abstractSyntaxTree().traverse(statGenerator);
    }

    public void printAST() {
        ASTPrinter astPrinter = new ASTPrinter();
        abstractSyntaxTree().traverse(astPrinter);
    }

    public void interpret() {
        Interpreter interpreter = new Interpreter();
        abstractSyntaxTree().traverse(interpreter);
    }
}
