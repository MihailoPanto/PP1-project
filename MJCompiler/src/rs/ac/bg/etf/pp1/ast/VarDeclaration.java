// generated with ast extension for cup
// version 0.8
// 20/11/2023 22:3:6


package rs.ac.bg.etf.pp1.ast;

public class VarDeclaration implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private OptionalSquareBrackets OptionalSquareBrackets;

    public VarDeclaration (String I1, OptionalSquareBrackets OptionalSquareBrackets) {
        this.I1=I1;
        this.OptionalSquareBrackets=OptionalSquareBrackets;
        if(OptionalSquareBrackets!=null) OptionalSquareBrackets.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public OptionalSquareBrackets getOptionalSquareBrackets() {
        return OptionalSquareBrackets;
    }

    public void setOptionalSquareBrackets(OptionalSquareBrackets OptionalSquareBrackets) {
        this.OptionalSquareBrackets=OptionalSquareBrackets;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalSquareBrackets!=null) OptionalSquareBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalSquareBrackets!=null) OptionalSquareBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalSquareBrackets!=null) OptionalSquareBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclaration(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(OptionalSquareBrackets!=null)
            buffer.append(OptionalSquareBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclaration]");
        return buffer.toString();
    }
}
