// generated with ast extension for cup
// version 0.8
// 22/11/2023 22:51:19


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStmt extends DesignatorStatement {

    private Designator Designator;
    private DesignatorStatementPart DesignatorStatementPart;

    public DesignatorStmt (Designator Designator, DesignatorStatementPart DesignatorStatementPart) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorStatementPart=DesignatorStatementPart;
        if(DesignatorStatementPart!=null) DesignatorStatementPart.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorStatementPart getDesignatorStatementPart() {
        return DesignatorStatementPart;
    }

    public void setDesignatorStatementPart(DesignatorStatementPart DesignatorStatementPart) {
        this.DesignatorStatementPart=DesignatorStatementPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorStatementPart!=null) DesignatorStatementPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorStatementPart!=null) DesignatorStatementPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorStatementPart!=null) DesignatorStatementPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmt(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementPart!=null)
            buffer.append(DesignatorStatementPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmt]");
        return buffer.toString();
    }
}
