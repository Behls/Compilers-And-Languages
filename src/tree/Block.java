package tree;

import visitor.NodeVisitor;
import java.util.ArrayList;

public class Block extends Statement {

    private final ArrayList<Statement> statements;

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    public Block(ArrayList<Statement> list) {
        statements = list;
    }

    @Override
    public void traverse(NodeVisitor visitor) {
        visitor.visitBlock(this);
    }
}
