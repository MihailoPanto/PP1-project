// generated with ast extension for cup
// version 0.8
// 24/11/2023 23:9:20


package rs.ac.bg.etf.pp1.ast;

public class NoFormParsOptional extends FormParsOpt {

    public NoFormParsOptional () {
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
        buffer.append("NoFormParsOptional(\n");

        buffer.append(tab);
        buffer.append(") [NoFormParsOptional]");
        return buffer.toString();
    }
}
