// generated with ast extension for cup
// version 0.8
// 20/11/2023 16:20:56


package rs.ac.bg.etf.pp1.ast;

public class Namespace implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private ConstVarDeclList ConstVarDeclList;

    public Namespace (String I1, ConstVarDeclList ConstVarDeclList) {
        this.I1=I1;
        this.ConstVarDeclList=ConstVarDeclList;
        if(ConstVarDeclList!=null) ConstVarDeclList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public ConstVarDeclList getConstVarDeclList() {
        return ConstVarDeclList;
    }

    public void setConstVarDeclList(ConstVarDeclList ConstVarDeclList) {
        this.ConstVarDeclList=ConstVarDeclList;
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
        if(ConstVarDeclList!=null) ConstVarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstVarDeclList!=null) ConstVarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstVarDeclList!=null) ConstVarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Namespace(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ConstVarDeclList!=null)
            buffer.append(ConstVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Namespace]");
        return buffer.toString();
    }
}
