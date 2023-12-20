// generated with ast extension for cup
// version 0.8
// 20/11/2023 22:3:6


package rs.ac.bg.etf.pp1.ast;

public class NoCommaConstDeclList extends ConstDeclList {

    private ConstDeclaration ConstDeclaration;

    public NoCommaConstDeclList (ConstDeclaration ConstDeclaration) {
        this.ConstDeclaration=ConstDeclaration;
        if(ConstDeclaration!=null) ConstDeclaration.setParent(this);
    }

    public ConstDeclaration getConstDeclaration() {
        return ConstDeclaration;
    }

    public void setConstDeclaration(ConstDeclaration ConstDeclaration) {
        this.ConstDeclaration=ConstDeclaration;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclaration!=null) ConstDeclaration.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclaration!=null) ConstDeclaration.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclaration!=null) ConstDeclaration.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoCommaConstDeclList(\n");

        if(ConstDeclaration!=null)
            buffer.append(ConstDeclaration.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NoCommaConstDeclList]");
        return buffer.toString();
    }
}
