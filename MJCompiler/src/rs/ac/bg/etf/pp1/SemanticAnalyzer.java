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

		Obj funcMain = Tab.currentScope.findSymbol("main");

		if (funcMain == null) {
			report_error("U tabeli simbola ne postoji MAIN funkcija!", program);
		} else {
			if (funcMain.getKind() != Obj.Meth) {
				report_error("Main mora biti funkcija!", program);
			} else if (funcMain.getType() != Tab.noType) {
				report_error("Main mora imati tip VOID!", program);
			}
		}
		mainScope = program.getProgName().obj;
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
			} else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
		}
	}

	// Namespace

//	public void visit(NamespaceName namespaceName) {
//		currentNamespace = Tab.insert(NewObj.Namespace, namespaceName.getNamespaceName(), Tab.noType);
//		namespaceName.obj = currentNamespace;
//		Tab.openScope();
//		report_info("Obradjuje se namespace " + currentNamespace.getName(), namespaceName);
//	}
//
//	public void visit(Namespace namespace) {
//		Tab.chainLocalSymbols(namespace.getNamespaceName().obj);
//		Tab.closeScope();
//		currentNamespace = null;
//	}

	public void visit(NamespaceName namespaceName) {
		currentNamespace = Tab.insert(NewObj.Namespace, namespaceName.getNamespaceName(), Tab.noType);
		namespaceName.obj = currentNamespace;
		report_info("Obradjuje se namespace " + currentNamespace.getName(), namespaceName);
	}

	public void visit(Namespace namespace) {
		currentNamespace = null;
	}

	// Deklaracija promenljivih

//	public void visit(VDeclaration vDeclaration) {
//
//		Obj var = Tab.currentScope.findSymbol(vDeclaration.getVarName());
//		if (var != null) {
//			report_error("Ime promenljive na liniji " + vDeclaration.getLine() + " je zauzeto!", null);
//		}
//
//		SyntaxNode par = vDeclaration.getParent();
//		while (!(par instanceof TypeSemiVarDecl))
//			par = par.getParent();
//
//		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;
//
//		if (currentNamespace == null) {
//			report_info("Deklarisana promenljiva " + vDeclaration.getVarName(), vDeclaration);
//		} else {
//			report_info("Deklarisana namespace promenljiva " + currentNamespace.getName() + "::"
//					+ vDeclaration.getVarName(), vDeclaration);
//		}
//		vDeclaration.obj = Tab.insert(Obj.Var, vDeclaration.getVarName(), tsvd.getType().struct);
//
//	}
	public void visit(VDeclaration vDeclaration) {
		String name = "";
		if (currentNamespace != null) {
			name = currentNamespace.getName() + "::" + vDeclaration.getVarName();
		} else {
			name = vDeclaration.getVarName();
		}
		Obj var = Tab.currentScope.findSymbol(name);
		if (var != null) {
			report_error("Ime promenljive na liniji " + vDeclaration.getLine() + " je zauzeto!", null);
		}

		SyntaxNode par = vDeclaration.getParent();
		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;

		if (currentNamespace == null) {
			report_info("Deklarisana promenljiva " + name, vDeclaration);
		} else {
			report_info("Deklarisana namespace promenljiva " + name, vDeclaration);
		}
		vDeclaration.obj = Tab.insert(Obj.Var, name, tsvd.getType().struct);

	}

//	public void visit(VArrayDeclaration vArrayDeclaration) {
//
//		Obj var = Tab.currentScope.findSymbol(vArrayDeclaration.getVarName());
//		if (var != null) {
//			report_error("Ime promenljive niza na liniji " + vArrayDeclaration.getLine() + " je zauzeto!", null);
//		}
//
//		SyntaxNode par = vArrayDeclaration.getParent();
//		while (!(par instanceof TypeSemiVarDecl))
//			par = par.getParent();
//
//		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;
//
//		if (currentNamespace == null) {
//			report_info("Deklarisana promenljiva niza " + vArrayDeclaration.getVarName(), vArrayDeclaration);
//		} else {
//			report_info("Deklarisana namespace promenljiva niza " + currentNamespace.getName() + "::"
//					+ vArrayDeclaration.getVarName(), vArrayDeclaration);
//		}
//		vArrayDeclaration.obj = Tab.insert(Obj.Var, vArrayDeclaration.getVarName(),
//				new Struct(Struct.Array, tsvd.getType().struct));
//	}

	public void visit(VArrayDeclaration vArrayDeclaration) {
		String name = "";
		if (currentNamespace != null) {
			name = currentNamespace.getName() + "::" + vArrayDeclaration.getVarName();
		} else {
			name = vArrayDeclaration.getVarName();
		}
		Obj var = Tab.currentScope.findSymbol(name);
		if (var != null) {
			report_error("Ime promenljive niza na liniji " + vArrayDeclaration.getLine() + " je zauzeto!", null);
		}

		SyntaxNode par = vArrayDeclaration.getParent();
		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;

		if (currentNamespace == null) {
			report_info("Deklarisana promenljiva niza " + name, vArrayDeclaration);
		} else {
			report_info("Deklarisana namespace promenljiva niza " + name, vArrayDeclaration);
		}
		vArrayDeclaration.obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, tsvd.getType().struct));
	}

	// Deklaracija konstanti

	public void visit(NumberConst numberConst) {
		numberConst.struct = Tab.intType;
	}

	public void visit(CharConst charConst) {
		charConst.struct = Tab.charType;
	}

	// TODO:
	public void visit(BoolConst boolConst) {
		boolConst.struct = NewTab.boolType;
	}

//	public void visit(ConstDeclaration constDeclaration) {
//
//		Obj con = Tab.currentScope.findSymbol(constDeclaration.getConstName());
//		if (con != null) {
//			report_error("Ime konstante na liniji " + constDeclaration.getLine() + " je zauzeto!", null);
//		}
//
//		SyntaxNode par = constDeclaration.getParent();
//		while (!(par instanceof TypeSemiConstDecl))
//			par = par.getParent();
//
//		TypeSemiConstDecl tscd = (TypeSemiConstDecl) par;
//
//		Struct cd = constDeclaration.getConstValue().struct;
//		Struct ctype = tscd.getType().struct;
//
//		if (cd.equals(ctype)) {
//			if (currentNamespace == null) {
//				report_info("Deklarisana konstanta " + constDeclaration.getConstName(), constDeclaration);
//				constDeclaration.obj = Tab.insert(Obj.Con, constDeclaration.getConstName(), tscd.getType().struct);
//			} else {
//				report_info("Deklarisana namespace konstanta " + currentNamespace.getName() + "::"
//						+ constDeclaration.getConstName(), constDeclaration);
//				constDeclaration.obj = Tab.insert(Obj.Con, constDeclaration.getConstName(), tscd.getType().struct);
//			}
//		} else {
//			report_error("Greska na liniji " + constDeclaration.getLine()
//					+ " : nekompatibilni tipovi pri definisanju konstante", null);
//		}
//	}

	public void visit(ConstDeclaration constDeclaration) {
		String name = "";
		if (currentNamespace != null) {
			name = currentNamespace.getName() + "::" + constDeclaration.getConstName();
		} else {
			name = constDeclaration.getConstName();
		}
		Obj con = Tab.currentScope.findSymbol(name);
		if (con != null) {
			report_error("Ime konstante na liniji " + constDeclaration.getLine() + " je zauzeto!", null);
		}

		SyntaxNode par = constDeclaration.getParent();
		while (!(par instanceof TypeSemiConstDecl))
			par = par.getParent();

		TypeSemiConstDecl tscd = (TypeSemiConstDecl) par;

		Struct cd = constDeclaration.getConstValue().struct;
		Struct ctype = tscd.getType().struct;

		if (cd.equals(ctype)) {
			if (currentNamespace == null) {
				report_info("Deklarisana konstanta " + name, constDeclaration);
				constDeclaration.obj = Tab.insert(Obj.Con, constDeclaration.getConstName(), tscd.getType().struct);
			} else {
				report_info("Deklarisana namespace konstanta " + name, constDeclaration);
				constDeclaration.obj = Tab.insert(Obj.Con, name, tscd.getType().struct);
			}
		} else {
			report_error("Greska na liniji " + constDeclaration.getLine()
					+ " : nekompatibilni tipovi pri definisanju konstante", null);
		}
	}

	// Metode

	public void visit(MethodTypeNameType methodTypeNameType) {

		Obj meth = Tab.currentScope.findSymbol(methodTypeNameType.getMethodName());
		if (meth != null) {
			report_error("Ime funkcije na liniji " + methodTypeNameType.getLine() + " je zauzeto!", null);
		}

		currentMethod = Tab.insert(Obj.Meth, methodTypeNameType.getMethodName(), methodTypeNameType.getType().struct);
		methodTypeNameType.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeNameType.getMethodName(), methodTypeNameType);
	}

	public void visit(MethodTypeNameVoid methodTypeNameVoid) {

		Obj meth = Tab.currentScope.findSymbol(methodTypeNameVoid.getMethodName());
		if (meth != null) {
			report_error("Ime funkcije na liniji " + methodTypeNameVoid.getLine() + " je zauzeto!", null);
		}

		currentMethod = Tab.insert(Obj.Meth, methodTypeNameVoid.getMethodName(), Tab.noType);
		methodTypeNameVoid.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeNameVoid.getMethodName(), methodTypeNameVoid);
	}

	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName()
					+ " nema return iskaz!", null);
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

		returnFound = false;
		currentMethod = null;
	}

	// DesignatorStatement

	public void visit(DesignatorIdent designatorIdent) {
		designatorIdent.obj = Tab.find(designatorIdent.getDesigName());
		if (designatorIdent.obj == Tab.noObj) {
			report_error("Nepostojeci simbol na liniji " + designatorIdent.getLine(), null);
		}
	}

	public void visit(DesignatorIdentBraces designatorIdentBraces) {
		Obj array = Tab.find(designatorIdentBraces.getDesigName());
		if (array == Tab.noObj) {
			report_error("Nepostojeci simbol niza na liniji " + designatorIdentBraces.getLine(), null);
		}
		if (array.getType().getKind() != Struct.Array) {
			report_error("Nije niz u liniji " + designatorIdentBraces.getLine(), null);
		}

		if (designatorIdentBraces.getExpr().struct != Tab.intType) {
			report_error("U uglastim zagradama mora biti tip int " + designatorIdentBraces.getLine(), null);
		}
		designatorIdentBraces.obj = new Obj(Obj.Elem, designatorIdentBraces.getDesigName(),
				array.getType().getElemType());
	}

//	public void visit(DesignatorNamespaceBraces designatorNamespaceBraces) {
//		Boolean flag = false;
//		designatorNamespaceBraces.obj = Tab.noObj;
//		Obj namespace = Tab.find(designatorNamespaceBraces.getNamespaceName());
//		if (namespace.getKind() != NewObj.Namespace) {
//			report_error("Nije napisan postojeci namespace na liniji " + designatorNamespaceBraces.getLine(), null);
//		}
//
//		for (Obj o : namespace.getLocalSymbols()) {
//			if (o.getName().equals(designatorNamespaceBraces.getDesigName())) {
//				Obj array = o;
//				if (array.getType().getKind() != Struct.Array) {
//					report_error("Nije niz Namespace-a u liniji " + designatorNamespaceBraces.getLine(), null);
//				}
//				if (designatorNamespaceBraces.getExpr().struct != Tab.intType) {
//					report_error("U uglastim zagradama mora biti tip int " + designatorNamespaceBraces.getLine(), null);
//				}
//				designatorNamespaceBraces.obj = new Obj(Obj.Elem, designatorNamespaceBraces.getDesigName(),
//						array.getType().getElemType());
//				flag = true;
//				break;
//			}
//		}
//		if (!flag) {
//			report_error("Ne postoji zadat simbol niza u namespace-u na liniji " + designatorNamespaceBraces.getLine(),
//					null);
//		}
//	}

	public void visit(DesignatorNamespaceBraces designatorNamespaceBraces) {
		Boolean flag = false;
		designatorNamespaceBraces.obj = Tab.noObj;
		Obj namespace = Tab.find(designatorNamespaceBraces.getNamespaceName());
		if (namespace.getKind() != NewObj.Namespace) {
			report_error("Nije napisan postojeci namespace na liniji " + designatorNamespaceBraces.getLine(), null);
		}

		Obj des = Tab
				.find(designatorNamespaceBraces.getNamespaceName() + "::" + designatorNamespaceBraces.getDesigName());
		if (des == Tab.noObj) {
			report_error("Nepostojeci simbol iz namespace na liniji " + designatorNamespaceBraces.getLine(), null);
		}

		if (des.getType().getKind() != Struct.Array) {
			report_error("Nije niz Namespace-a u liniji " + designatorNamespaceBraces.getLine(), null);
		}
		if (designatorNamespaceBraces.getExpr().struct != Tab.intType) {
			report_error("U uglastim zagradama mora biti tip int " + designatorNamespaceBraces.getLine(), null);
		}
		designatorNamespaceBraces.obj = new Obj(Obj.Elem, designatorNamespaceBraces.getDesigName(),
				des.getType().getElemType());


//		for (Obj o : namespace.getLocalSymbols()) {
//			if (o.getName().equals(designatorNamespaceBraces.getDesigName())) {
//				Obj array = o;
//				if (array.getType().getKind() != Struct.Array) {
//					report_error("Nije niz Namespace-a u liniji " + designatorNamespaceBraces.getLine(), null);
//				}
//				if (designatorNamespaceBraces.getExpr().struct != Tab.intType) {
//					report_error("U uglastim zagradama mora biti tip int " + designatorNamespaceBraces.getLine(), null);
//				}
//				designatorNamespaceBraces.obj = new Obj(Obj.Elem, designatorNamespaceBraces.getDesigName(),
//						array.getType().getElemType());
//				flag = true;
//				break;
//			}
//		}
//		if (!flag) {
//			report_error("Ne postoji zadat simbol niza u namespace-u na liniji " + designatorNamespaceBraces.getLine(),
//					null);
//		}
	}

//	public void visit(DesignatorNamespace designatorNamespace) {
//		Boolean flag = false;
//		designatorNamespace.obj = Tab.noObj;
//		Obj namespace = Tab.find(designatorNamespace.getNamespaceName());
//		if (namespace.getKind() != NewObj.Namespace) {
//			report_error("Nije napisan postojeci namespace na liniji " + designatorNamespace.getLine(), null);
//		}
//
//		for (Obj o : namespace.getLocalSymbols()) {
//			if (o.getName().equals(designatorNamespace.getDesigName())) {
//				designatorNamespace.obj = o;
//				flag = true;
//				break;
//			}
//		}
//		if (!flag) {
//			report_error("Ne postoji zadat simbol u namespace-u na liniji " + designatorNamespace.getLine(), null);
//		}
//	}

	public void visit(DesignatorNamespace designatorNamespace) {
		Boolean flag = false;
		designatorNamespace.obj = Tab.noObj;
		Obj namespace = Tab.find(designatorNamespace.getNamespaceName());
		if (namespace.getKind() != NewObj.Namespace) {
			report_error("Nije napisan postojeci namespace na liniji " + designatorNamespace.getLine(), null);
		}

		Obj des = Tab.find(designatorNamespace.getNamespaceName() + "::" + designatorNamespace.getDesigName());
		if (des == Tab.noObj) {
			report_error("Nepostojeci simbol iz namespace na liniji " + designatorNamespace.getLine(), null);
		}
		designatorNamespace.obj = des;
	}

	public void visit(DesignatorAssign designatorAssign) {
		Struct leftVariable = designatorAssign.getDesignator().obj.getType();
		Struct rightVariable = designatorAssign.getExpr().struct;

		if (designatorAssign.getDesignator().obj.getKind() == Obj.Con) {
			report_error("Nije dozvoljena dodela vrednosti kontstanti u liniji " + designatorAssign.getLine(), null);
		}

		if (!leftVariable.compatibleWith(rightVariable)) {
			report_error("Nisu odgovarajuci tipovi " + designatorAssign.getLine(), null);
		} else if (leftVariable.getKind() == Struct.Array && rightVariable.getKind() == Struct.Array) {
			if (leftVariable.getElemType() != rightVariable.getElemType()) {
				report_error("Nisu odgovarajuci tipovi dva niza " + designatorAssign.getLine(), null);
			}
		}

		designatorAssign.obj = designatorAssign.getDesignator().obj;
	}

	public void visit(DesignatorPlusPlus designatorPlusPlus) {

		if (designatorPlusPlus.obj == Tab.noObj) {
			report_error("Nepostojeci simbol na liniji " + designatorPlusPlus.getLine(), null);
		}

		if (designatorPlusPlus.getDesignator().obj.getKind() == Obj.Con) {
			report_error("Nije dozvoljeno inkrementiranje kontstanti u liniji " + designatorPlusPlus.getLine(), null);
		}

		Struct variable = designatorPlusPlus.getDesignator().obj.getType();
		if (variable.getKind() == Struct.Array) {
			if (variable.getElemType() != Tab.intType) {
				report_error(
						"Nije dozvoljen inkrement elementa niza koji nije int u liniji " + designatorPlusPlus.getLine(),
						null);
			}
		} else if (variable != Tab.intType) {
			report_error("Nije dozvoljen inkrement koji nije tipa int u liniji " + designatorPlusPlus.getLine(), null);
		}
		designatorPlusPlus.obj = designatorPlusPlus.getDesignator().obj;
	}

	public void visit(DesignatorMinusMinus designatorMinusMinus) {
		Struct variable = designatorMinusMinus.getDesignator().obj.getType();

		if (designatorMinusMinus.getDesignator().obj.getKind() == Obj.Con) {
			report_error("Nije dozvoljeno dekrementiranje kontstanti u liniji " + designatorMinusMinus.getLine(), null);
		}

		if (variable.getKind() == Struct.Array) {
			if (variable.getElemType() != Tab.intType) {
				report_error("Nije dozvoljen dekrement elementa niza koji nije int u liniji "
						+ designatorMinusMinus.getLine(), null);
			}
		} else if (variable != Tab.intType) {
			report_error("Nije dozvoljen dekrement koji nije tipa int u liniji " + designatorMinusMinus.getLine(),
					null);
		}
		designatorMinusMinus.obj = designatorMinusMinus.getDesignator().obj;
	}

	public void visit(FactorDesignator factorDesignator) {
		factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
	}

	public void visit(FactorNew factorNew) {
		Struct exprType = factorNew.getExpr().struct;
		if (exprType != Tab.intType) {
			report_error("Expr mora biti tipa int u liniji " + factorNew.getLine(), null);
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

		if (left != Tab.intType || right != Tab.intType) {
			report_error("Tipovi moraju biti int u liniji " + termMulOp.getLine(), null);
		}
		termMulOp.struct = right;
	}

	public void visit(TermFactor termFactor) {
		termFactor.struct = termFactor.getFactor().struct;
	}

	public void visit(ExprMinus exprMinus) {
		if (exprMinus.getTerm().struct != Tab.intType) {
			report_error("Iza negativnog predznaka moze biti samo tip int u liniji " + exprMinus.getLine(), null);
		}
		exprMinus.struct = exprMinus.getTerm().struct;
	}

	public void visit(ExprAddop exprAddop) {
		Struct left = exprAddop.getExpr().struct;
		Struct right = exprAddop.getTerm().struct;

		if (left != Tab.intType || right != Tab.intType) {
			report_error("Tipovi moraju biti int u liniji " + exprAddop.getLine(), null);
		}
		exprAddop.struct = right;
	}

	public void visit(ExprTerm exprTerm) {
		exprTerm.struct = exprTerm.getTerm().struct;
	}

	public void visit(StmtPrint stmtPrint) {
		Struct exprType = stmtPrint.getExpr().struct;
		if (exprType != Tab.intType && exprType != Tab.charType && exprType != NewTab.boolType) {
			report_error("Expr mora biti biti int u liniji " + stmtPrint.getLine(), null);
		}
	}

	public void visit(StmtRead stmtRead) {
		Obj desig = stmtRead.getDesignator().obj;
		Struct desigType = stmtRead.getDesignator().obj.getType();

		if (desig.getKind() != Obj.Elem && desig.getKind() != Obj.Var) {
			report_error(
					"Greska:  Designator mora označavati promenljivu ili element niza - u liniji " + stmtRead.getLine(),
					null);
		}

		if (desigType != Tab.intType && desigType != Tab.charType && desigType != NewTab.boolType) {
			report_error("Pogresan tip prosledjen za read u liniji " + stmtRead.getLine(), null);
		}
	}

}