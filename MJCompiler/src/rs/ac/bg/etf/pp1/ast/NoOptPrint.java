// generated with ast extension for cup
// version 0.8
// 1/0/2024 21:11:56


package rs.ac.bg.etf.pp1.ast;

public class NoOptPrint extends OptPrint {

    public NoOptPrint () {
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
        buffer.append("NoOptPrint(\n");

        buffer.append(tab);
        buffer.append(") [NoOptPrint]");
        return buffer.toString();
    }
}
