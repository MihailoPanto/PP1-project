// generated with ast extension for cup
// version 0.8
// 16/0/2024 16:37:5


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Designator Designator);
    public void visit(Factor Factor);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(ConstDecl ConstDecl);
    public void visit(AssignOp AssignOp);
    public void visit(MulOp MulOp);
    public void visit(NamespaceList NamespaceList);
    public void visit(Expr Expr);
    public void visit(VarDeclList VarDeclList);
    public void visit(ConstValue ConstValue);
    public void visit(VarDecl VarDecl);
    public void visit(VarDeclaration VarDeclaration);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(ConstVarDecl ConstVarDecl);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(Statement Statement);
    public void visit(ConstVarDeclList ConstVarDeclList);
    public void visit(AddOp AddOp);
    public void visit(Term Term);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(StatementList StatementList);
    public void visit(LocalVarDeclList LocalVarDeclList);
    public void visit(Mod Mod);
    public void visit(Div Div);
    public void visit(Mul Mul);
    public void visit(Minus Minus);
    public void visit(Plus Plus);
    public void visit(Equal Equal);
    public void visit(DesignatorIdent DesignatorIdent);
    public void visit(DesignatorNamespace DesignatorNamespace);
    public void visit(DesignatorNamespaceBraces DesignatorNamespaceBraces);
    public void visit(DesignatorIdentBraces DesignatorIdentBraces);
    public void visit(FactorDesignator FactorDesignator);
    public void visit(FactorParenExpr FactorParenExpr);
    public void visit(FactorNew FactorNew);
    public void visit(FactorConstValueNum FactorConstValueNum);
    public void visit(FactorConstValueChar FactorConstValueChar);
    public void visit(FactorConstValueBool FactorConstValueBool);
    public void visit(TermFactor TermFactor);
    public void visit(TermMulOp TermMulOp);
    public void visit(ExprAddop ExprAddop);
    public void visit(ExprTerm ExprTerm);
    public void visit(ExprMinus ExprMinus);
    public void visit(DesignatorMM DesignatorMM);
    public void visit(DesignatorPP DesignatorPP);
    public void visit(DesignatorAssign DesignatorAssign);
    public void visit(PrintNumStmt PrintNumStmt);
    public void visit(PrintStmt PrintStmt);
    public void visit(ReadStmt ReadStmt);
    public void visit(ErrorStmt ErrorStmt);
    public void visit(DesignatorStatementStmt DesignatorStatementStmt);
    public void visit(NoStmtList NoStmtList);
    public void visit(StmtList StmtList);
    public void visit(Type Type);
    public void visit(VDeclaration VDeclaration);
    public void visit(VArrayDeclaration VArrayDeclaration);
    public void visit(ErrorCommaVList ErrorCommaVList);
    public void visit(NoCommaVarDeclList NoCommaVarDeclList);
    public void visit(CommaVarDeclList CommaVarDeclList);
    public void visit(ErrorVarDecl ErrorVarDecl);
    public void visit(TypeSemiVarDecl TypeSemiVarDecl);
    public void visit(NumberConst NumberConst);
    public void visit(CharConst CharConst);
    public void visit(BoolConst BoolConst);
    public void visit(ConstDeclaration ConstDeclaration);
    public void visit(OneConstDecl OneConstDecl);
    public void visit(CommaConstDeclList CommaConstDeclList);
    public void visit(TypeSemiConstDecl TypeSemiConstDecl);
    public void visit(VarCVDeclaration VarCVDeclaration);
    public void visit(ConstCVDeclaration ConstCVDeclaration);
    public void visit(NoConstVarDeclarationList NoConstVarDeclarationList);
    public void visit(ConstVarDeclarationList ConstVarDeclarationList);
    public void visit(NoLocalVarDeclarationList NoLocalVarDeclarationList);
    public void visit(LocalVarDeclarationList LocalVarDeclarationList);
    public void visit(MethodTypeNameVoid MethodTypeNameVoid);
    public void visit(MethodTypeNameType MethodTypeNameType);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDeclarationList NoMethodDeclarationList);
    public void visit(MethodDeclarationList MethodDeclarationList);
    public void visit(NamespaceName NamespaceName);
    public void visit(Namespace Namespace);
    public void visit(NoNameSpacesList NoNameSpacesList);
    public void visit(NameSpacesList NameSpacesList);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
