// generated with ast extension for cup
// version 0.8
// 25/11/2023 23:23:42


package rs.ac.bg.etf.pp1.ast;

public class DesignatorIdentBraces extends Designator {

    private String desigName;
    private Expr Expr;

    public DesignatorIdentBraces (String desigName, Expr Expr) {
        this.desigName=desigName;
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public String getDesigName() {
        return desigName;
    }

    public void setDesigName(String desigName) {
        this.desigName=desigName;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorIdentBraces(\n");

        buffer.append(" "+tab+desigName);
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorIdentBraces]");
        return buffer.toString();
    }
}
