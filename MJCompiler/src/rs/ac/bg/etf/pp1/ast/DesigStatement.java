// generated with ast extension for cup
// version 0.8
// 21/11/2023 15:19:45


package rs.ac.bg.etf.pp1.ast;

public class DesigStatement extends DesignatorStatement {

    private Designator Designator;
    private DesignatorStatementPart DesignatorStatementPart;

    public DesigStatement (Designator Designator, DesignatorStatementPart DesignatorStatementPart) {
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
        buffer.append("DesigStatement(\n");

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
        buffer.append(") [DesigStatement]");
        return buffer.toString();
    }
}
