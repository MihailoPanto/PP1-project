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

	Logger log = Logger.getLogger(getClass());

	// Greske i informacije

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

	public void visit(NamespaceName namespaceName) {
		currentNamespace = Tab.insert(Obj.Type, namespaceName.getNamespaceName(), Tab.noType);
		namespaceName.obj = currentNamespace;
		Tab.openScope();
		report_info("Obradjuje se namespace " + currentNamespace.getName(), namespaceName);
	}

	public void visit(Namespace namespace) {
		currentNamespace = null;
		Tab.chainLocalSymbols(namespace.getNamespaceName().obj);
		Tab.closeScope();
	}

	// Deklaracija promenljivih

	public void visit(VDeclaration vDeclaration) {
		SyntaxNode par = vDeclaration.getParent();
		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;

		if (currentNamespace == null) {
			report_info("Deklarisana promenljiva " + vDeclaration.getVarName(), vDeclaration);
		} else {
			report_info("Deklarisana namespace promenljiva " + currentNamespace.getName() + "::"
					+ vDeclaration.getVarName(), vDeclaration);
		}
		Obj varD = Tab.insert(Obj.Var, vDeclaration.getVarName(), tsvd.getType().struct);

	}

	public void visit(VArrayDeclaration vArrayDeclaration) {
		SyntaxNode par = vArrayDeclaration.getParent();
		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;

		if (currentNamespace == null) {
			report_info("Deklarisana promenljiva niza " + vArrayDeclaration.getVarName(), vArrayDeclaration);
		} else {
			report_info("Deklarisana namespace promenljiva niza " + currentNamespace.getName() + "::"
					+ vArrayDeclaration.getVarName(), vArrayDeclaration);
		}
		Obj varD = Tab.insert(Obj.Var, vArrayDeclaration.getVarName(), new Struct(Struct.Array, tsvd.getType().struct));
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
		// izvedi iz Tab
	}

	public void visit(ConstDeclaration constDeclaration) {
		SyntaxNode par = constDeclaration.getParent();
		while (!(par instanceof TypeSemiConstDecl))
			par = par.getParent();

		TypeSemiConstDecl tscd = (TypeSemiConstDecl) par;

		Struct cd = constDeclaration.getConstValue().struct;
		Struct ctype = tscd.getType().struct;

		if (cd.equals(ctype)) {
			if (currentNamespace == null) {
				report_info("Deklarisana konstanta " + constDeclaration.getConstName(), constDeclaration);
				Obj conD = Tab.insert(Obj.Con, constDeclaration.getConstName(), tscd.getType().struct);
			} else {
				report_info("Deklarisana namespace konstanta " + currentNamespace.getName() + "::"
						+ constDeclaration.getConstName(), constDeclaration);
				Obj conD = Tab.insert(Obj.Con, constDeclaration.getConstName(), tscd.getType().struct);
			}
		} else {
			report_error("Greska na liniji " + constDeclaration.getLine()
					+ " : nekompatibilni tipovi pri definisanju konstante", null);
		}
	}

	// Metode

	public void visit(MethodTypeNameType methodTypeNameType) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeNameType.getMethodName(), methodTypeNameType.getType().struct);
		methodTypeNameType.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeNameType.getMethodName(), methodTypeNameType);
	}

	public void visit(MethodTypeNameVoid methodTypeNameVoid) {
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
		designatorIdentBraces.obj = new Obj(Obj.Elem, "", array.getType().getElemType());
	}

	public void visit(DesignatorNamespaceBraces designatorNamespaceBraces) {

	}

	public void visit(DesignatorNamespace designatorNamespace) {
//		Obj namespace = Tab.find(designatorNamespace.getNamespaceName());
//		// dodaj proveru da li je ovo namespace
//		if (namespace.getLocalSymbols() != null) {
//			namespace.get;
//		}
	}

	public void visit(DesignatorIdent designatorIdent) {
		designatorIdent.obj = Tab.find(designatorIdent.getDesigName());
		if (designatorIdent.obj == Tab.noObj) {
			report_error("Nepostojeci simbol na liniji " + designatorIdent.getLine(), null);
		}
	}

	public void visit(DesignatorAssign designatorAssign) {
		Struct leftVariable = designatorAssign.getDesignator().obj.getType();
		Struct rightVariable = designatorAssign.getExpr().struct;

		if (leftVariable.getKind() == Struct.Array && rightVariable.getKind() == Struct.Array) {
			if (leftVariable.getElemType() == rightVariable.getElemType()) {
				return;
			}
			report_error("Nisu odgovarajuci tipovi dva niza " + designatorAssign.getLine(), null);
		} else if (leftVariable != rightVariable) {
			report_error("Nisu odgovarajuci tipovi " + designatorAssign.getLine(), null);
		}

		designatorAssign.obj = designatorAssign.getDesignator().obj;
	}

	public void visit(DesignatorPlusPlus designatorPlusPlus) {
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

	public void visit(FactorConstValue factorConstValue) {
		factorConstValue.struct = factorConstValue.getConstValue().struct;
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
		if (exprMinus.struct != Tab.intType) {
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
		// dodaj i za bool
		if (exprType != Tab.intType && exprType != Tab.charType) {
			report_error("Expr mora biti biti int u liniji " + stmtPrint.getLine(), null);
		}
	}

}