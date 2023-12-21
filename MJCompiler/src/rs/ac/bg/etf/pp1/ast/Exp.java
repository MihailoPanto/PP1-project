// generated with ast extension for cup
// version 0.8
// 21/11/2023 15:19:45


package rs.ac.bg.etf.pp1.ast;

public class Exp extends Expr {

    private MinusOptional MinusOptional;
    private ExprPart ExprPart;

    public Exp (MinusOptional MinusOptional, ExprPart ExprPart) {
        this.MinusOptional=MinusOptional;
        if(MinusOptional!=null) MinusOptional.setParent(this);
        this.ExprPart=ExprPart;
        if(ExprPart!=null) ExprPart.setParent(this);
    }

    public MinusOptional getMinusOptional() {
        return MinusOptional;
    }

    public void setMinusOptional(MinusOptional MinusOptional) {
        this.MinusOptional=MinusOptional;
    }

    public ExprPart getExprPart() {
        return ExprPart;
    }

    public void setExprPart(ExprPart ExprPart) {
        this.ExprPart=ExprPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MinusOptional!=null) MinusOptional.accept(visitor);
        if(ExprPart!=null) ExprPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MinusOptional!=null) MinusOptional.traverseTopDown(visitor);
        if(ExprPart!=null) ExprPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MinusOptional!=null) MinusOptional.traverseBottomUp(visitor);
        if(ExprPart!=null) ExprPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Exp(\n");

        if(MinusOptional!=null)
            buffer.append(MinusOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprPart!=null)
            buffer.append(ExprPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Exp]");
        return buffer.toString();
    }
}
