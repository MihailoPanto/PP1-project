// generated with ast extension for cup
// version 0.8
// 24/11/2023 23:9:20


package rs.ac.bg.etf.pp1.ast;

public class MinusMinus extends DesignatorStatementPart {

    public MinusMinus () {
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
        buffer.append("MinusMinus(\n");

        buffer.append(tab);
        buffer.append(") [MinusMinus]");
        return buffer.toString();
    }
}
