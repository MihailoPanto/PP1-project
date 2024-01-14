package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	Obj currentNamespace = null;
	Obj mainScope = null;
	Obj mainFunction = null;

	Logger log = Logger.getLogger(getClass());

	// Greske i informacije

	public Obj getMainScope() {
		return mainScope;
	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean passed() {
		return !errorDetected;
	}

	// Program
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}

	public void visit(Program program) {
		if (mainFunction == null) {
			report_error("GRESKA: funkcija main mora postojati!", program);
		}
		mainScope = program.getProgName().obj;
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(Type type) {
		Obj oType = Tab.find(type.getTypeName());
		if (oType.getKind() == Obj.Type) {
			type.struct = oType.getType();
		} else {
			type.struct = Tab.noType;
			report_error("Greska: nije validan tip " + type.getTypeName() + " na liniji " + type.getLine(), null);
		}
	}

	// Namespace

	public void visit(NamespaceName namespaceName) {
		currentNamespace = Tab.insert(NewObj.Namespace, namespaceName.getNamespaceName(), Tab.noType);
		namespaceName.obj = currentNamespace;
		report_info("INFO: obradjuje se namespace " + currentNamespace.getName(), namespaceName);
	}

	public void visit(Namespace namespace) {
		currentNamespace = null;
	}

	// Deklaracija promenljivih

	public void visit(VDeclaration vDeclaration) {
		String name = "";
		if (currentNamespace != null) {
			name = currentNamespace.getName() + "::" + vDeclaration.getVarName();
		} else {
			name = vDeclaration.getVarName();
		}
		Obj var = Tab.currentScope.findSymbol(name);
		if (var != null) {
			report_error("GRESKA: ime promenljive " + name + " na liniji " + vDeclaration.getLine() + " je zauzeto!",
					null);
		}

		SyntaxNode par = vDeclaration.getParent();

		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		if (currentNamespace == null) {
			report_info("INFO: deklarisana promenljiva " + name, vDeclaration);
		} else {
			report_info("INFO: deklarisana namespace promenljiva " + name, vDeclaration);
		}
		vDeclaration.obj = Tab.insert(Obj.Var, name, ((TypeSemiVarDecl) par).getType().struct);
	}

	public void visit(VArrayDeclaration vArrayDeclaration) {
		String name = "";
		if (currentNamespace != null) {
			name = currentNamespace.getName() + "::" + vArrayDeclaration.getVarName();
		} else {
			name = vArrayDeclaration.getVarName();
		}
		Obj var = Tab.currentScope.findSymbol(name);
		if (var != null) {
			report_error("GRESKA: ime niza " + name + " na liniji " + vArrayDeclaration.getLine() + " je zauzeto!",
					null);
		}

		SyntaxNode par = vArrayDeclaration.getParent();

		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		if (currentNamespace == null) {
			report_info("INFO: deklarisana promenljiva niza " + name, vArrayDeclaration);
		} else {
			report_info("INFO: deklarisana namespace promenljiva niza " + name, vArrayDeclaration);
		}
		vArrayDeclaration.obj = Tab.insert(Obj.Var, name,
				new Struct(Struct.Array, ((TypeSemiVarDecl) par).getType().struct));
	}

	// Deklaracija konstanti

	public void visit(NumberConst numberConst) {
		numberConst.struct = Tab.intType;
	}

	public void visit(CharConst charConst) {
		charConst.struct = Tab.charType;
	}

	public void visit(BoolConst boolConst) {
		boolConst.struct = NewTab.boolType;
	}

	public void visit(ConstDeclaration constDeclaration) {
		String name = "";
		if (currentNamespace != null) {
			name = currentNamespace.getName() + "::" + constDeclaration.getConstName();
		} else {
			name = constDeclaration.getConstName();
		}
		Obj con = Tab.currentScope.findSymbol(name);
		if (con != null) {
			report_error("GRESKA: ime konstante " + name + " na liniji " + constDeclaration.getLine() + " je zauzeto!",
					null);
		}

		SyntaxNode par = constDeclaration.getParent();

		while (!(par instanceof TypeSemiConstDecl))
			par = par.getParent();

		Struct cd = constDeclaration.getConstValue().struct;
		Struct ctype = ((TypeSemiConstDecl) par).getType().struct;

		if (cd.equals(ctype)) {
			if (currentNamespace == null) {
				report_info("INFO: deklarisana konstanta " + name, constDeclaration);
			} else {
				report_info("INFO: deklarisana namespace konstanta " + name, constDeclaration);
			}
		} else {
			report_error("GRESKA: nisu odgovarajuci tipovi na liniji " + constDeclaration.getLine(), null);
		}
		constDeclaration.obj = Tab.insert(Obj.Con, name, ((TypeSemiConstDecl) par).getType().struct);
	}

	// Metode

	public void visit(MethodTypeNameType methodTypeNameType) {

		Obj meth = Tab.currentScope.findSymbol(methodTypeNameType.getMethodName());
		if (meth != null) {
			report_error("GRESKA: ime funkcije na liniji " + methodTypeNameType.getLine() + " je zauzeto!", null);
		}

		currentMethod = Tab.insert(Obj.Meth, methodTypeNameType.getMethodName(), Tab.noType);
		if ("main".equals(methodTypeNameType.getMethodName())) {
			report_error("GRESKA: funkcija main mora biti tipa VOID na liniji " + methodTypeNameType.getLine(), null);
		}

		currentMethod = Tab.insert(Obj.Meth, methodTypeNameType.getMethodName(), methodTypeNameType.getType().struct);
		methodTypeNameType.obj = currentMethod;
		Tab.openScope();
		report_info("INFO: obradjuje se funkcija " + methodTypeNameType.getMethodName(), methodTypeNameType);
	}

	public void visit(MethodTypeNameVoid methodTypeNameVoid) {

		Obj meth = Tab.currentScope.findSymbol(methodTypeNameVoid.getMethodName());
		if (meth != null) {
			report_error("GRESKA: ime funkcije na liniji " + methodTypeNameVoid.getLine() + " je zauzeto!", null);
		}

		currentMethod = Tab.insert(Obj.Meth, methodTypeNameVoid.getMethodName(), Tab.noType);
		if ("main".equals(methodTypeNameVoid.getMethodName())) {
			mainFunction = currentMethod;
		}
		methodTypeNameVoid.obj = currentMethod;
		Tab.openScope();
		report_info("INFO: obradjuje se funkcija " + methodTypeNameVoid.getMethodName(), methodTypeNameVoid);
	}

	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("GRESKA: funckija " + currentMethod.getName() + " na liniji " + methodDecl.getLine()
					+ " nema return iskaz!", null);
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		returnFound = false;
		currentMethod = null;
	}

	// DesignatorStatement

	public void visit(DesignatorIdent designatorIdent) {
		Obj tmp = Tab.find(designatorIdent.getDesigName());
		if (tmp.equals(Tab.noObj)) {
			report_error("GRESKA: nepostojeci simbol na liniji " + designatorIdent.getLine(), null);
			designatorIdent.obj = Tab.noObj;
		} else {
			designatorIdent.obj = tmp;
		}
	}

	public void visit(DesignatorIdentBraces designatorIdentBraces) {
		Obj array = Tab.find(designatorIdentBraces.getDesigName());
		if (array.equals(Tab.noObj)) {
			report_error("GRESKA: nepostojeci simbol niza na liniji " + designatorIdentBraces.getLine(), null);
		}else {
			if (array.getType().getKind() != Struct.Array) {
				report_error("GRESKA: nije niz u liniji " + designatorIdentBraces.getLine(), null);
			}
			if (designatorIdentBraces.getExpr().struct != Tab.intType) {
				report_error("GRESKA: u uglastim zagradama mora biti izraz tipa int na liniji "
						+ designatorIdentBraces.getLine(), null);
			}
			designatorIdentBraces.obj = new Obj(Obj.Elem, designatorIdentBraces.getDesigName(),
					array.getType().getElemType());
		}
	}

	public void visit(DesignatorNamespaceBraces designatorNamespaceBraces) {
		Boolean flag = false;
		designatorNamespaceBraces.obj = Tab.noObj;
		Obj namespace = Tab.find(designatorNamespaceBraces.getNamespaceName());
		if (namespace.getKind() != NewObj.Namespace) {
			report_error("GRESKA: nepostojeci namespace na liniji " + designatorNamespaceBraces.getLine(), null);
		}

		String name = designatorNamespaceBraces.getNamespaceName() + "::" + designatorNamespaceBraces.getDesigName();
		Obj des = Tab.find(name);
		if (des == Tab.noObj) {
			report_error("GRESKA: nepostojeci simbol iz namespace na liniji " + designatorNamespaceBraces.getLine(),
					null);
		}

		if (des.getType().getKind() != Struct.Array) {
			report_error("GRESKA: nije niz iz Namespace-a u liniji " + designatorNamespaceBraces.getLine(), null);
		}
		if (designatorNamespaceBraces.getExpr().struct != Tab.intType) {
			report_error("GRESKA: u uglastim zagradama mora biti izraz tipa int na liniji "
					+ designatorNamespaceBraces.getLine(), null);
		}
		designatorNamespaceBraces.obj = new Obj(Obj.Elem, name, des.getType().getElemType());
	}

	public void visit(DesignatorNamespace designatorNamespace) {
		Boolean flag = false;
		designatorNamespace.obj = Tab.noObj;
		Obj namespace = Tab.find(designatorNamespace.getNamespaceName());
		if (namespace.getKind() != NewObj.Namespace) {
			report_error("GRESKA: nepostojeci namespace na liniji " + designatorNamespace.getLine(), null);
		}

		String name = designatorNamespace.getNamespaceName() + "::" + designatorNamespace.getDesigName();

		Obj des = Tab.find(name);
		if (des == Tab.noObj) {
			report_error("GRESKA: nepostojeci simbol iz namespace na liniji " + designatorNamespace.getLine(), null);
		}
		designatorNamespace.obj = des;
	}

//	public void visit(DesignatorAssign designatorAssign) {
//		Struct leftVariable = designatorAssign.getDesignator().obj.getType();
//		Struct rightVariable = designatorAssign.getExpr().struct;
//
//		if (designatorAssign.getDesignator().obj.getKind() == Obj.Con) {
//			report_error("GRESKA: nije dozvoljena dodela vrednosti kontstanti na liniji " + designatorAssign.getLine(),
//					null);
//		}
//		if (!leftVariable.compatibleWith(rightVariable)) {
//			report_error("GRESKA: nisu odgovarajuci tipovi na liniji " + designatorAssign.getLine(), null);
//		} else if ((leftVariable.getKind() == Struct.Array && rightVariable.getKind() == Struct.Array)
//				&& (leftVariable.getElemType() != rightVariable.getElemType())) {
//			report_error("GRESKA: nisu odgovarajuci tipovi dva niza na liniji " + designatorAssign.getLine(), null);
//		}
//		designatorAssign.obj = designatorAssign.getDesignator().obj;
//	}
	
	public void visit(DesignatorAssign designatorAssign) {

		if (designatorAssign.getDesignator().obj.equals(Tab.noObj)) {
			report_error("GRESKA na liniji " + designatorAssign.getLine(), null);
		}else {
			Struct leftVariable = designatorAssign.getDesignator().obj.getType();
			Struct rightVariable = designatorAssign.getExpr().struct;

			if (designatorAssign.getDesignator().obj.getKind() == Obj.Con) {
				report_error("GRESKA: nije dozvoljena dodela vrednosti kontstanti na liniji " + designatorAssign.getLine(),
						null);
			}
			if (!leftVariable.compatibleWith(rightVariable)) {
				report_error("GRESKA: nisu odgovarajuci tipovi na liniji " + designatorAssign.getLine(), null);
			} else if ((leftVariable.getKind() == Struct.Array && rightVariable.getKind() == Struct.Array)
					&& (leftVariable.getElemType() != rightVariable.getElemType())) {
				report_error("GRESKA: nisu odgovarajuci tipovi dva niza na liniji " + designatorAssign.getLine(), null);
			}
			designatorAssign.obj = designatorAssign.getDesignator().obj;
		}


	}

	public void visit(DesignatorPlusPlus designatorPlusPlus) {

		if (designatorPlusPlus.obj == Tab.noObj) {
			report_error("GRESKA: nepostojeci simbol na liniji " + designatorPlusPlus.getLine(), null);
		}
		if (designatorPlusPlus.getDesignator().obj.getKind() == Obj.Con) {
			report_error("GRESKA: nije dozvoljeno inkrementiranje kontstanti na liniji " + designatorPlusPlus.getLine(),
					null);
		}

		Struct variable = designatorPlusPlus.getDesignator().obj.getType();
		if (variable.getKind() == Struct.Array) {
			if (variable.getElemType() != Tab.intType) {
				report_error("GRESKA: nije dozvoljeno inkrementiranje elementa niza koji nije int na liniji "
						+ designatorPlusPlus.getLine(), null);
			}
		} else if (variable != Tab.intType) {
			report_error("GRESKA: nije dozvoljeno inkrementiranje promenljive koja nije tipa int na liniji "
					+ designatorPlusPlus.getLine(), null);
		}
		designatorPlusPlus.obj = designatorPlusPlus.getDesignator().obj;
	}

	public void visit(DesignatorMinusMinus designatorMinusMinus) {
		Struct variable = designatorMinusMinus.getDesignator().obj.getType();

		if (designatorMinusMinus.getDesignator().obj.getKind() == Obj.Con) {
			report_error(
					"GRESKA: nije dozvoljeno dekrementiranje kontstanti na liniji " + designatorMinusMinus.getLine(),
					null);
		}

		if (variable.getKind() == Struct.Array) {
			if (variable.getElemType() != Tab.intType) {
				report_error("GRESKA: nije dozvoljeno dekrementiranje elementa niza koji nije int na liniji "
						+ designatorMinusMinus.getLine(), null);
			}
		} else if (variable != Tab.intType) {
			report_error("GRESKA: nije dozvoljeno dekrementiranje promenljive koja nije tipa int na liniji "
					+ designatorMinusMinus.getLine(), null);
		}
		designatorMinusMinus.obj = designatorMinusMinus.getDesignator().obj;
	}

	public void visit(FactorDesignator factorDesignator) {
		if(factorDesignator.getDesignator().obj != null)
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
	}

	public void visit(FactorNew factorNew) {
		Struct exprType = factorNew.getExpr().struct;
		if (exprType != Tab.intType) {
			report_error("GRESKA: tip izraza za velicinu niza mora biti int na liniji " + factorNew.getLine(), null);
		}
		factorNew.struct = new Struct(Struct.Array, factorNew.getType().struct);
	}

	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(FactorConstValueNum factorConstValue) {
		factorConstValue.struct = Tab.intType;
	}

	public void visit(FactorConstValueChar factorConstValue) {
		factorConstValue.struct = Tab.charType;
	}

	public void visit(FactorConstValueBool factorConstValue) {
		factorConstValue.struct = NewTab.boolType;
	}

	public void visit(TermMulOp termMulOp) {
		Struct left = termMulOp.getTerm().struct;
		Struct right = termMulOp.getFactor().struct;
		if (left != right) {
			report_error("GRESKA: tipovi nisu odgovarajuci na liniji " + termMulOp.getLine(), null);
		}
		if (left != Tab.intType || right != Tab.intType) {
			report_error("GRESKA: tipovi moraju biti tipa int na liniji " + termMulOp.getLine(), null);
		} else {
			termMulOp.struct = right;
		}
	}

	public void visit(TermFactor termFactor) {
		termFactor.struct = termFactor.getFactor().struct;
	}

	public void visit(ExprMinus exprMinus) {
		Struct exprType = exprMinus.getTerm().struct;
		if (exprType == Tab.intType) {
			exprMinus.struct = exprMinus.getTerm().struct;
		} else {
			report_error("GRESKA: iza negativnog predznaka moze biti samo tip int na liniji " + exprMinus.getLine(),
					null);
		}
	}

	public void visit(ExprAddop exprAddop) {
		Struct left = exprAddop.getExpr().struct;
		Struct right = exprAddop.getTerm().struct;

		if (left != right) {
			report_error("GRESKA: tipovi nisu odgovarajuci na liniji " + exprAddop.getLine(), null);
		}
		if (left != Tab.intType || right != Tab.intType) {
			report_error("GRESKA: tipovi moraju biti tipa int na liniji " + exprAddop.getLine(), null);
		} else {
			exprAddop.struct = right;
		}
	}

	public void visit(ExprTerm exprTerm) {
		exprTerm.struct = exprTerm.getTerm().struct;
	}

	public void visit(StmtPrint stmtPrint) {
		Struct argumentType = stmtPrint.getExpr().struct;
		if (argumentType == null) {
			report_error("GRESKA: mora postojati argument na liniji " + stmtPrint.getLine(), null);
		}
		if (argumentType != Tab.intType && argumentType != Tab.charType && argumentType != NewTab.boolType) {
			report_error("GRESKA: tip argumenta mora biti int, char ili bool na liniji " + stmtPrint.getLine(), null);
		}
	}

	public void visit(StmtPrintNum stmtPrintNum) {
		Struct argumentType = stmtPrintNum.getExpr().struct;
		if (argumentType == null) {
			report_error("GRESKA: mora postojati argument na liniji " + stmtPrintNum.getLine(), null);
		}
		if (argumentType != Tab.intType && argumentType != Tab.charType && argumentType != NewTab.boolType) {
			report_error("GRESKA: tip argumenta mora biti int, char ili bool na liniji " + stmtPrintNum.getLine(),
					null);
		}
	}

	public void visit(StmtRead stmtRead) {
		Obj desig = stmtRead.getDesignator().obj;
		Struct desigType = stmtRead.getDesignator().obj.getType();

		if (desig.getKind() != Obj.Elem && desig.getKind() != Obj.Var) {
			report_error("GRESKA: designator mora biti promenljivu ili element niza na liniji " + stmtRead.getLine(),
					null);
		} else if (desigType != Tab.intType && desigType != Tab.charType && desigType != NewTab.boolType) {
			report_error("GRESKA: pogresan tip prosledjen za read na liniji " + stmtRead.getLine(), null);
		}
	}
}