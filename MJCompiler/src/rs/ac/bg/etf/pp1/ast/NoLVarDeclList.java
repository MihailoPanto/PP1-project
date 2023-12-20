// generated with ast extension for cup
// version 0.8
// 20/11/2023 22:3:6


package rs.ac.bg.etf.pp1.ast;

public class NoLVarDeclList extends LocalVarDeclList {

    public NoLVarDeclList () {
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
        buffer.append("NoLVarDeclList(\n");

        buffer.append(tab);
        buffer.append(") [NoLVarDeclList]");
        return buffer.toString();
    }
}
