// generated with ast extension for cup
// version 0.8
// 6/0/2024 21:29:33


package rs.ac.bg.etf.pp1.ast;

public class NoConstVarDeclarationList extends ConstVarDeclList {

    public NoConstVarDeclarationList () {
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
        buffer.append("NoConstVarDeclarationList(\n");

        buffer.append(tab);
        buffer.append(") [NoConstVarDeclarationList]");
        return buffer.toString();
    }
}
