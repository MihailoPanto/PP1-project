// generated with ast extension for cup
// version 0.8
// 16/0/2024 16:37:5


package rs.ac.bg.etf.pp1.ast;

public class NoStmtList extends StatementList {

    public NoStmtList () {
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
        buffer.append("NoStmtList(\n");

        buffer.append(tab);
        buffer.append(") [NoStmtList]");
        return buffer.toString();
    }
}
