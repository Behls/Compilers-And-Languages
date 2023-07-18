package main;

import java.io.File;

public class Main {

    public enum Action {

        STATS, TREE, CCODE, JAVACODE, PYTHONCODE, INTERPRET, RALCODE, NONE
    }

    public static void main(String[] args) {
        Action action = Action.NONE;
        boolean debug = false;
        String path = "";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s")) {
                action = Action.STATS;
            } else if (args[i].equals("-t")) {
                action = Action.TREE;
            } else if (args[i].equals("-c")) {
                action = Action.CCODE;
            } else if (args[i].equals("-j")) {
                action = Action.JAVACODE;
            } else if (args[i].equals("-p")) {
                action = Action.PYTHONCODE;
            } else if (args[i].equals("-i")) {
                action = Action.INTERPRET;
            } else if (args[i].equals("-r")) {
                action = Action.RALCODE;
            } else if (args[i].equals("-d")) {
                debug = true;
            } else {
                path = args[i];
            }
        }

        if (path.equals("")) {
            System.err.println("Error: You must specify an input file.");
            System.exit(1);
        }

        if (action == Action.NONE) {
            System.err.println("Error: You must specify an action.");
            System.err.println("-s = Print program statistics");
            System.err.println("-t = Display abstract syntax tree");
            System.err.println("-c = Generate C code");
            System.err.println("-j = Generate Java code");
            System.err.println("-p = Generate Python code");
            System.err.println("-i = Run interpreter");
            System.err.println("-r = Generate RAM/RAL code");
            System.exit(1);
        }

        Compiler compiler = new Compiler(debug);
        compiler.buildAbstractSyntaxTree(path);
        
        File f = new File(path);
        String progName = f.getName().substring(0, f.getName().lastIndexOf("."));

        if (action == Action.STATS) {
            compiler.printStatistics();
        } else if (action == Action.TREE) {
            compiler.printAST();
        } else if (action == Action.CCODE) {
            compiler.generateCCode(System.out);
        } else if (action == Action.JAVACODE) {
            compiler.generateJavaCode(System.out, path);
        } else if (action == Action.PYTHONCODE) {
            compiler.generatePythonCode(System.out);
        } else if (action == Action.INTERPRET) {
            compiler.interpret();
        } else if (action == Action.RALCODE) {
            compiler.generateRALCode(System.out, progName);
        }
    }
}
