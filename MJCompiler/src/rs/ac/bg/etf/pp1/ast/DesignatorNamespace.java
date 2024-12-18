// generated with ast extension for cup
// version 0.8
// 16/0/2024 16:37:5


package rs.ac.bg.etf.pp1.ast;

public class DesignatorNamespace extends Designator {

    private String namespaceName;
    private String desigName;

    public DesignatorNamespace (String namespaceName, String desigName) {
        this.namespaceName=namespaceName;
        this.desigName=desigName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName=namespaceName;
    }

    public String getDesigName() {
        return desigName;
    }

    public void setDesigName(String desigName) {
        this.desigName=desigName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorNamespace(\n");

        buffer.append(" "+tab+namespaceName);
        buffer.append("\n");

        buffer.append(" "+tab+desigName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorNamespace]");
        return buffer.toString();
    }
}
