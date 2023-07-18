package tree;

import visitor.NodeVisitor;
import java.util.HashMap;

public abstract class ProgramNode {

    public static HashMap<String, VariableRef> varTable;

    private boolean topLevel = false;

    public void setTopLevel() {

        topLevel = true;
    }

    public boolean isTopLevel() {

        return topLevel;
    }

    public abstract void traverse(NodeVisitor v);
}
