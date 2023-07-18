package visitor;

import tree.*;
import java.io.PrintStream;
import java.util.ArrayList;

public class RALCodeGenerator implements NodeVisitor {

    private PrintStream out = null;
    private String progName = "";
    
    // These array lists store the RAM setup
    public ArrayList<Integer> memoryValues;
    public ArrayList<String> memoryComments;
    
    // These store the RAL code
    public ArrayList<String> instructionCodes;
    public ArrayList<Integer> instructionArgs;
    public ArrayList<String> instructionComments;

    // This field is for passing information about a logical expression
    // back to the calling code, so it knows what sort of jump to use.
    public String logicalComparison = "";

    // This constructor makes a new RALCodeGenerator for a named program and
    // writes it to the given output stream.
    public RALCodeGenerator(PrintStream outputstream, String name) {
        progName = name;
        out = outputstream;
        memoryValues = new ArrayList<>();
        memoryComments = new ArrayList<>();
        instructionCodes = new ArrayList<>();
        instructionArgs = new ArrayList<>();
        instructionComments = new ArrayList<>();
    }

    // This constructor is for when one RALCodeGenerator needs to make another
    // to delegate some of its work. The new one shares the same memory setup
    // but gets its own code fragment. The caller can then splice code
    // fragments together (but will need to adjust the target instruction
    // numbers for any jumps).
    public RALCodeGenerator(ArrayList<Integer> mv, ArrayList<String> mc) {
        memoryValues = mv;
        memoryComments = mc;
        instructionCodes = new ArrayList<>();
        instructionArgs = new ArrayList<>();
        instructionComments = new ArrayList<>();
    }

    @Override
    public void visitBinaryExpression(BinaryExpression node) {
        // New code generators to process the left and right subexpressions
        RALCodeGenerator leftGenerator = new RALCodeGenerator(memoryValues,
                memoryComments);
        RALCodeGenerator rightGenerator = new RALCodeGenerator(memoryValues,
                memoryComments);

        Expression left, right;

        if (node.operator.equals(">")) {
            // Swap left and right sides because RAL only has a JMN (jump if
            // negative), but no jump if positive. So a > b just gets
            // processed as b < a.
            left = node.right;
            right = node.left;
        } else {
            left = node.left;
            right = node.right;
        }

        // Do the recursive calls
        left.traverse(leftGenerator);
        right.traverse(rightGenerator);

        // Now splice the code together
        
        // First the code to evaluate the right hand side
        instructionCodes.addAll(rightGenerator.instructionCodes);
        instructionArgs.addAll(rightGenerator.instructionArgs);
        instructionComments.addAll(rightGenerator.instructionComments);

        // Create a temporary variable to store the result of that
        memoryValues.add(0);
        memoryComments.add("Value of right subexpression");
        int location = memoryValues.size() - 1;

        // Store the result of the right hand side in the temporary variable
        instructionCodes.add("STA");
        instructionArgs.add(location);
        instructionComments.add("Store value of right subexpression");

        // Now the code to evaluate the left hand side
        instructionCodes.addAll(leftGenerator.instructionCodes);
        instructionArgs.addAll(leftGenerator.instructionArgs);
        instructionComments.addAll(leftGenerator.instructionComments);

        // Finally code to apply the appropriate operation to the LHS (in
        // the accumulator) and the RHS (in the temporary variable).
        
        // Arithmetic operations (+, -, *, /, %) leave their result in the 
        // accumulator. Logical operations (<, >, ==) leave the difference
        // between the two sides in the accumulator. How do IF or WHILE
        // statements know whether to do JMZ or JMN??? Use the
        // logicalComparison field to pass this information back to the
        // caller.
        
        if (node.operator.equals("+")) {
            instructionCodes.add("ADD");
        } else if (node.operator.equals("-")) {
            instructionCodes.add("SUB");
        } else if (node.operator.equals("*")) {
            instructionCodes.add("MUL");
        } else if (node.operator.equals("/")) {
            instructionCodes.add("DIV");
        } else if (node.operator.equals("%")) {
            instructionCodes.add("MOD");
        } else if (node.operator.equals("<")) {
            instructionCodes.add("SUB");
            logicalComparison = "JMN";
        } else if (node.operator.equals(">")) {
            instructionCodes.add("SUB");
            logicalComparison = "JMN";
        } else if (node.operator.equals("==")) {
            instructionCodes.add("SUB");
            logicalComparison = "JMZ";
        }
        instructionArgs.add(location);
        instructionComments.add("Apply binary operation");
    }

    @Override
    public void visitVariableRef(VariableRef node) {
        // What to do with a variable reference depends on whether the
        // parent is an expression (in which case load the value of the
        // variable into the accumulator, or if it's the LHS of an
        // assignment statement or the argument of a read statement, in
        // which case the parent just needs to know the location so it can
        // store something there.

        // OK, so assume this method ONLY gets called in the first case, i.e.
        // evaluate this as an expression by loading its value into the 
        // accumulator.
        String name = node.getName();
        int location = memoryComments.indexOf(name);
        instructionCodes.add("LDA");
        instructionArgs.add(location);
        instructionComments.add("Value of the variable " + node.getName());
    }

    @Override
    public void visitIntegerValue(IntegerValue node) {
        // Set it up as a constant loaded into memory at the start
        memoryValues.add(node.getValue());
        memoryComments.add("The constant " + node.getValue());
        
        // Now this is a type of expression, so load that into memory
        int location = memoryValues.size() - 1;
        instructionCodes.add("LDA");
        instructionArgs.add(location);
        instructionComments.add("The constant " + node.getValue());
    }

    @Override
    public void visitAssignment(Assignment node) {
        // Don't "visit" the variable. We know what type it is, so grab its
        // location directly from here without doing the Visitor thing.
        String name = node.getVariable().getName();
        int location = memoryComments.indexOf(name);
        node.getRightHandSide().traverse(this);
        
        // Value of RHS is now in accumulator, so store it at location
        instructionCodes.add("STA");
        instructionArgs.add(location);
        instructionComments.add("Store in variable " + name);
    }

    @Override
    public void visitPrint(Print node) {
        // Evaluate the expression
        node.getExpression().traverse(this);
        
        // Create a temporary variable to store the result
        memoryValues.add(0);
        memoryComments.add("Value of expression to print");
        int location = memoryValues.size() - 1;
        
        // Store the value of the expression in this location
        instructionCodes.add("STA");
        instructionArgs.add(location);
        instructionComments.add("Store value of expression");
        
        // And finally print it
        instructionCodes.add("PRN");
        instructionArgs.add(location);
        instructionComments.add("Print value of expression");
    }

    @Override
    public void visitRead(Read node) {
        // As for assignment, don't visit the variable, just get its
        // location directly from here.
        String name = node.getVariable().getName();
        int location = memoryComments.indexOf(name);

        instructionCodes.add("INP");
        instructionArgs.add(location);
        instructionComments.add("Read " + name);
    }

    @Override
    public void visitConditional(Conditional node) {
        // NOT IMPLEMENTED YET
        // Will certainly involve recursive calls to traverse the three
        // subtrees (for the boolean expression, the THEN part and the ELSE
        // part). This might be with this same RALCodeGenerator, or it
        // might be with new ones created specially for that purpose, as
        // with subexpressions. The code fragments and memory allocations
        // will then need to be patched together to make valid code for the
        // whole if-then-else statement.
        
        //node.getCondition().traverse(this);
        //node.getThenPart().traverse(this);
        //node.getElsePart().traverse(this);
    }

    @Override
    public void visitWhile(While node) {
        // NOT IMPLEMENTED YET
        // As for conditionals, this will certainly involve recursive calls
        // to traverse the two subtrees (for the boolean expression and the
        // loop body). These code fragments will then need to be patched
        // together to create valid code for the whole loop.
        
        //node.getCondition().traverse(this);
        //node.getLoopBody().traverse(this);
    }

    @Override
    public void visitBlock(Block node) {
        if (node.isTopLevel()) {
            // If this block is actually the whole program, set up memory
            // for all variables in the symbol table.
            for (String key : ProgramNode.varTable.keySet()) {
                memoryValues.add(0);
                String name = ProgramNode.varTable.get(key).getName();
                memoryComments.add(name);
            }
        }

        // Process the statements in this block, one after the other, in
        // order. There's no fancy splicing needs to be done, unlike for
        // conditionals or while loops.
        for (int i = 0; i < node.getStatements().size(); i++) {
            node.getStatements().get(i).traverse(this);
        }

        if (node.isTopLevel()) {
            // If this block is actually the whole program, write the finished
            // code to the output. (Note that none of the other routines
            // generate any output. They just save instructions and memory
            // descriptions into the different arrays.)
            
            // First the header
            out.println(progName + ".ral");
            out.println("Generated from " + progName + ".toy by the Toy Compiler.");
            out.println("---");
            
            // Then the memory setup
            for (int i = 0; i < memoryValues.size(); i++) {
                out.print(i);
                out.println(" " + memoryValues.get(i) + " # "
                        + memoryComments.get(i));
            }
            out.println("---");
            
            // Then the instructions
            for (int i = 0; i < instructionCodes.size(); i++) {
                out.print(i);
                out.print(" " + instructionCodes.get(i));
                if (instructionArgs.get(i) != null) {
                    out.print(" " + instructionArgs.get(i));
                }
                out.println("\t# " + instructionComments.get(i));
            }
            
            // Don't forget to put a HALT instruction at the end.
            out.print(instructionCodes.size());
            out.println(" HLT\t# Halt");
        }
    }
}
