// generated with ast extension for cup
// version 0.8
// 26/11/2023 21:41:36


package rs.ac.bg.etf.pp1.ast;

public class RelOp_Less_Equal extends RelOp {

    public RelOp_Less_Equal () {
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
        buffer.append("RelOp_Less_Equal(\n");

        buffer.append(tab);
        buffer.append(") [RelOp_Less_Equal]");
        return buffer.toString();
    }
}
