package tree;

//imports
import visitor.NodeVisitor;

public class StringValue extends Expression{

    private final String value;

    public StringValue(String v){
        value = v;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void traverse(NodeVisitor v) {
        v.visitStringValue(this);
    }
}
