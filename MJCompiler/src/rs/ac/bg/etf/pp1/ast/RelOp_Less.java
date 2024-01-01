// generated with ast extension for cup
// version 0.8
// 1/0/2024 21:11:56


package rs.ac.bg.etf.pp1.ast;

public class RelOp_Less extends RelOp {

    public RelOp_Less () {
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
        buffer.append("RelOp_Less(\n");

        buffer.append(tab);
        buffer.append(") [RelOp_Less]");
        return buffer.toString();
    }
}
