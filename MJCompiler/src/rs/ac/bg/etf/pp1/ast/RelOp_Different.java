// generated with ast extension for cup
// version 0.8
// 29/11/2023 16:45:26


package rs.ac.bg.etf.pp1.ast;

public class RelOp_Different extends RelOp {

    public RelOp_Different () {
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
        buffer.append("RelOp_Different(\n");

        buffer.append(tab);
        buffer.append(") [RelOp_Different]");
        return buffer.toString();
    }
}
