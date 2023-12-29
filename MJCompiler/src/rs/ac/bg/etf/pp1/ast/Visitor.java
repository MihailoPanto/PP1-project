// generated with ast extension for cup
// version 0.8
// 29/11/2023 16:45:26


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(VarDeclaration VarDeclaration);
    public void visit(FormParsOpt FormParsOpt);
    public void visit(ConditionList ConditionList);
    public void visit(MethodType MethodType);
    public void visit(CondTermList CondTermList);
    public void visit(StatementList StatementList);
    public void visit(NamespaceList NamespaceList);
    public void visit(ConstVarDeclList ConstVarDeclList);
    public void visit(Factor Factor);
    public void visit(ConstVarDecl ConstVarDecl);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(OptionalSquareBrackets OptionalSquareBrackets);
    public void visit(ConstValue ConstValue);
    public void visit(MulOp MulOp);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(RelOp RelOp);
    public void visit(AssignOp AssignOp);
    public void visit(VarDeclList VarDeclList);
    public void visit(Expr Expr);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(AddOp AddOp);
    public void visit(NamespaceAccess NamespaceAccess);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(Statement Statement);
    public void visit(VarDecl VarDecl);
    public void visit(ConstDecl ConstDecl);
    public void visit(CondFact CondFact);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(OptPrint OptPrint);
    public void visit(FormPars FormPars);
    public void visit(LocalVarDeclList LocalVarDeclList);
    public void visit(Mod Mod);
    public void visit(Div Div);
    public void visit(Mul Mul);
    public void visit(AddOpMinus AddOpMinus);
    public void visit(AddOpPlus AddOpPlus);
    public void visit(AssignOpEqual AssignOpEqual);
    public void visit(RelOp_Less_Equal RelOp_Less_Equal);
    public void visit(RelOp_Less RelOp_Less);
    public void visit(RelOp_Greater_Equal RelOp_Greater_Equal);
    public void visit(RelOp_Greater RelOp_Greater);
    public void visit(RelOp_Different RelOp_Different);
    public void visit(RelOp_EqualEqual RelOp_EqualEqual);
    public void visit(CondFactNoRelOp CondFactNoRelOp);
    public void visit(CondFactRelOp CondFactRelOp);
    public void visit(CondTermListNoAnd CondTermListNoAnd);
    public void visit(CondTermListAnd CondTermListAnd);
    public void visit(CondListNoOr CondListNoOr);
    public void visit(CondListOr CondListOr);
    public void visit(DesignatorIdent DesignatorIdent);
    public void visit(DesignatorNamespace DesignatorNamespace);
    public void visit(DesignatorNamespaceBraces DesignatorNamespaceBraces);
    public void visit(DesignatorIdentBraces DesignatorIdentBraces);
    public void visit(FactorDesignator FactorDesignator);
    public void visit(FactorExpr FactorExpr);
    public void visit(FactorNew FactorNew);
    public void visit(FactorConstValueBool FactorConstValueBool);
    public void visit(FactorConstValueChar FactorConstValueChar);
    public void visit(FactorConstValueNum FactorConstValueNum);
    public void visit(TermFactor TermFactor);
    public void visit(TermMulOp TermMulOp);
    public void visit(ExprTerm ExprTerm);
    public void visit(ExprAddop ExprAddop);
    public void visit(ExprMinus ExprMinus);
    public void visit(DesignatorMinusMinus DesignatorMinusMinus);
    public void visit(DesignatorPlusPlus DesignatorPlusPlus);
    public void visit(DesignatorAssign DesignatorAssign);
    public void visit(NoOptPrint NoOptPrint);
    public void visit(CommaOptPrint CommaOptPrint);
    public void visit(StmtPrint StmtPrint);
    public void visit(StmtRead StmtRead);
    public void visit(StmtError StmtError);
    public void visit(StmtDesignatorStatement StmtDesignatorStatement);
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
    public void visit(BoolConst BoolConst);
    public void visit(CharConst CharConst);
    public void visit(NumberConst NumberConst);
    public void visit(ConstDeclaration ConstDeclaration);
    public void visit(SingleConstDecl SingleConstDecl);
    public void visit(CommaConstDeclList CommaConstDeclList);
    public void visit(TypeSemiConstDecl TypeSemiConstDecl);
    public void visit(VarCVDeclaration VarCVDeclaration);
    public void visit(ConstCVDeclaration ConstCVDeclaration);
    public void visit(NoConstVarDeclarationList NoConstVarDeclarationList);
    public void visit(ConstVarDeclarationList ConstVarDeclarationList);
    public void visit(NoLocalVarDeclarationList NoLocalVarDeclarationList);
    public void visit(LocalVarDeclarationList LocalVarDeclarationList);
    public void visit(FormPar FormPar);
    public void visit(FPar FPar);
    public void visit(FPars FPars);
    public void visit(NoFormParsOptional NoFormParsOptional);
    public void visit(FormParsOptional FormParsOptional);
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
