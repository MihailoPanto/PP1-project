// generated with ast extension for cup
// version 0.8
// 20/11/2023 15:46:35


package rs.ac.bg.etf.pp1.ast;

public class NoOSBrackets extends OptionalSquareBrackets {

    public NoOSBrackets () {
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
        buffer.append("NoOSBrackets(\n");

        buffer.append(tab);
        buffer.append(") [NoOSBrackets]");
        return buffer.toString();
    }
}
