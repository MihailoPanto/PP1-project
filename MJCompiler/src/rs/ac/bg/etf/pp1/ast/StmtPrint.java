// generated with ast extension for cup
// version 0.8
// 25/11/2023 23:23:42


package rs.ac.bg.etf.pp1.ast;

public class StmtPrint extends Statement {

    private Expr Expr;
    private OptPrint OptPrint;

    public StmtPrint (Expr Expr, OptPrint OptPrint) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.OptPrint=OptPrint;
        if(OptPrint!=null) OptPrint.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public OptPrint getOptPrint() {
        return OptPrint;
    }

    public void setOptPrint(OptPrint OptPrint) {
        this.OptPrint=OptPrint;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(OptPrint!=null) OptPrint.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(OptPrint!=null) OptPrint.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(OptPrint!=null) OptPrint.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtPrint(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptPrint!=null)
            buffer.append(OptPrint.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtPrint]");
        return buffer.toString();
    }
}
