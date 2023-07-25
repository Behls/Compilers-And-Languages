package tree;


import visitor.NodeVisitor;

public class Function extends Expression{

    private String value;
    private Expression expression;


    public Function(String name, Expression e){
        value = name;
        expression = e;
    }

    @Override
    public void traverse(NodeVisitor v) {
        v.visitFunction(this);
    }

}
