// generated with ast extension for cup
// version 0.8
// 22/11/2023 22:51:19


package rs.ac.bg.etf.pp1.ast;

public class Designator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private NamespaceAccess NamespaceAccess;
    private DesignatorOptPart DesignatorOptPart;

    public Designator (NamespaceAccess NamespaceAccess, DesignatorOptPart DesignatorOptPart) {
        this.NamespaceAccess=NamespaceAccess;
        if(NamespaceAccess!=null) NamespaceAccess.setParent(this);
        this.DesignatorOptPart=DesignatorOptPart;
        if(DesignatorOptPart!=null) DesignatorOptPart.setParent(this);
    }

    public NamespaceAccess getNamespaceAccess() {
        return NamespaceAccess;
    }

    public void setNamespaceAccess(NamespaceAccess NamespaceAccess) {
        this.NamespaceAccess=NamespaceAccess;
    }

    public DesignatorOptPart getDesignatorOptPart() {
        return DesignatorOptPart;
    }

    public void setDesignatorOptPart(DesignatorOptPart DesignatorOptPart) {
        this.DesignatorOptPart=DesignatorOptPart;
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
        if(NamespaceAccess!=null) NamespaceAccess.accept(visitor);
        if(DesignatorOptPart!=null) DesignatorOptPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NamespaceAccess!=null) NamespaceAccess.traverseTopDown(visitor);
        if(DesignatorOptPart!=null) DesignatorOptPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NamespaceAccess!=null) NamespaceAccess.traverseBottomUp(visitor);
        if(DesignatorOptPart!=null) DesignatorOptPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator(\n");

        if(NamespaceAccess!=null)
            buffer.append(NamespaceAccess.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorOptPart!=null)
            buffer.append(DesignatorOptPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator]");
        return buffer.toString();
    }
}
