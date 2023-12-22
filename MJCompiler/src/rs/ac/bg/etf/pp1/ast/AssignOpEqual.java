// generated with ast extension for cup
// version 0.8
// 22/11/2023 22:51:19


package rs.ac.bg.etf.pp1.ast;

public class AssignOpEqual extends AssignOp {

    public AssignOpEqual () {
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
        buffer.append("AssignOpEqual(\n");

        buffer.append(tab);
        buffer.append(") [AssignOpEqual]");
        return buffer.toString();
    }
}
