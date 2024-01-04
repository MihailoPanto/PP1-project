// generated with ast extension for cup
// version 0.8
// 4/0/2024 17:14:54


package rs.ac.bg.etf.pp1.ast;

public class NameSpacesList extends NamespaceList {

    private NamespaceList NamespaceList;
    private Namespace Namespace;

    public NameSpacesList (NamespaceList NamespaceList, Namespace Namespace) {
        this.NamespaceList=NamespaceList;
        if(NamespaceList!=null) NamespaceList.setParent(this);
        this.Namespace=Namespace;
        if(Namespace!=null) Namespace.setParent(this);
    }

    public NamespaceList getNamespaceList() {
        return NamespaceList;
    }

    public void setNamespaceList(NamespaceList NamespaceList) {
        this.NamespaceList=NamespaceList;
    }

    public Namespace getNamespace() {
        return Namespace;
    }

    public void setNamespace(Namespace Namespace) {
        this.Namespace=Namespace;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NamespaceList!=null) NamespaceList.accept(visitor);
        if(Namespace!=null) Namespace.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NamespaceList!=null) NamespaceList.traverseTopDown(visitor);
        if(Namespace!=null) Namespace.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NamespaceList!=null) NamespaceList.traverseBottomUp(visitor);
        if(Namespace!=null) Namespace.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NameSpacesList(\n");

        if(NamespaceList!=null)
            buffer.append(NamespaceList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Namespace!=null)
            buffer.append(Namespace.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NameSpacesList]");
        return buffer.toString();
    }
}
