package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;




public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	private Obj mainScope = null;
	private Obj currentMethod = null;

	public CodeGenerator(Obj s) {
		super();
		this.mainScope = s;
	}

	public int getMainPc() {
		return mainPc;
	}

	public void visit(MethodTypeNameVoid methodTypeName) {
		if ("main".equalsIgnoreCase(methodTypeName.getMethodName())) {
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
		currentMethod = methodTypeName.obj;

		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);

		// Generate the entry
		Code.put(Code.enter);
		Code.put(0);
		Code.put(currentMethod.getLocalSymbols().size());
	}

	public void visit(MethodDecl methodDecl) {
		currentMethod = null;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(ReadStmt readStmt) {
		Obj tmp = readStmt.getDesignator().obj;
		if (tmp.getType().getKind() == Struct.Char) {
			Code.put(Code.bread);
			Code.store(tmp);
		} else {
			Code.put(Code.read);
			Code.store(tmp);
		}
	}

	public void visit(PrintNumStmt printStmt) {
		Struct exprType = printStmt.getExpr().struct;
		if (exprType == Tab.charType) {
			Code.loadConst(printStmt.getN2());
			Code.put(Code.bprint);
		} else {
			Code.loadConst(printStmt.getN2());
			Code.put(Code.print);

		}
	}

	public void visit(PrintStmt printStmt) {
		Struct exprType = printStmt.getExpr().struct;
		if (exprType == Tab.charType) {
			Code.loadConst(1);
			Code.put(Code.bprint);
		} else {
			Code.loadConst(5);
			Code.put(Code.print);
		}
	}

	public void visit(FactorConstValueBool factorConstValue) {
		if (factorConstValue.getB1().equals("true")) {
			Code.loadConst(1);
		} else if (factorConstValue.getB1().equals("false")) {
			Code.loadConst(0);
		}
	}

	public void visit(FactorConstValueChar factorConstValue) {
		Code.loadConst(factorConstValue.getC1().charAt(1));
	}

	public void visit(FactorConstValueNum factorConstValue) {
		Code.loadConst(factorConstValue.getN1());
	}

	public void visit(ConstDeclaration constDeclaration) {
		Struct type = constDeclaration.getConstValue().struct;
		if (type == Tab.intType) {
			constDeclaration.obj.setAdr(((NumberConst) constDeclaration.getConstValue()).getN1());
		} else if (type == Tab.charType) {
			constDeclaration.obj.setAdr(((CharConst) constDeclaration.getConstValue()).getC1().charAt(1));
		} else if (type == NewTab.boolType) {
			if (((BoolConst) constDeclaration.getConstValue()).getB1().equals("true")) {
				constDeclaration.obj.setAdr(1);
			} else {
				constDeclaration.obj.setAdr(2);
			}
		}
		Code.load(constDeclaration.obj);
	}

	public void visit(DesignatorPP designator) {
		if (designator.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Obj tmp = designator.getDesignator().obj;
		Code.load(tmp);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(tmp);
	}

	public void visit(DesignatorMM designator) {
		if (designator.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Obj tmp = designator.getDesignator().obj;
		Code.load(tmp);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(tmp);
	}

	public Obj getFromScope(Obj scope, String name) {
		if (scope == null) {
			return null;
		}
		for (Obj tmp : scope.getLocalSymbols()) {
			if (tmp.getName().equals(name) && (tmp.getKind() == Obj.Var || tmp.getKind() == Obj.Con)) {
				return tmp;
			}
		}
		return null;
	}

	public void visit(DesignatorIdent designatorIdent) {
		if (!(designatorIdent.getParent() instanceof DesignatorAssign) && !(designatorIdent.getParent() instanceof DesignatorPP)
			&& !(designatorIdent.getParent() instanceof DesignatorMM) && !(designatorIdent.getParent() instanceof ReadStmt)) {
			Code.load(designatorIdent.obj);
		}
	}

	public void visit(DesignatorIdentBraces designatorIdentBraces) {
		String name = designatorIdentBraces.getDesigName();
		Obj arr = null;
		if (currentMethod != null) {
			arr = getFromScope(currentMethod, name);
		}
		if (arr == null) {
			arr = getFromScope(mainScope, name);
		}
		Code.load(arr);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		if (!(designatorIdentBraces.getParent() instanceof DesignatorAssign) && !(designatorIdentBraces.getParent() instanceof DesignatorPP)
			&& !(designatorIdentBraces.getParent() instanceof DesignatorMM) && !(designatorIdentBraces.getParent() instanceof ReadStmt)) {
			if (designatorIdentBraces.obj.getType() == Tab.intType
					|| designatorIdentBraces.obj.getType() == NewTab.boolType) {
				Code.put(Code.aload);
			} else {
				Code.put(Code.baload);
			}
		}

	}

	public void visit(DesignatorNamespace designatorNamespace) {
		if (!(designatorNamespace.getParent() instanceof DesignatorAssign) && !(designatorNamespace.getParent() instanceof DesignatorPP)
			&& !(designatorNamespace.getParent() instanceof DesignatorMM) && !(designatorNamespace.getParent() instanceof ReadStmt)) {
			Code.load(designatorNamespace.obj);
		}
	}

	public void visit(DesignatorNamespaceBraces designatorNamespaceBraces) {
		String name = designatorNamespaceBraces.getNamespaceName() + "::" + designatorNamespaceBraces.getDesigName();
		Obj arr = null;
		if (currentMethod != null) {
			arr = getFromScope(currentMethod, name);
		}
		if (arr == null) {
			arr = getFromScope(mainScope, name);
		}
		Code.load(arr);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		if (!(designatorNamespaceBraces.getParent() instanceof DesignatorAssign) && !(designatorNamespaceBraces.getParent() instanceof DesignatorPP)
			&& !(designatorNamespaceBraces.getParent() instanceof DesignatorMM) && !(designatorNamespaceBraces.getParent() instanceof ReadStmt)) {
			if (designatorNamespaceBraces.obj.getType() == Tab.intType
					|| designatorNamespaceBraces.obj.getType() == NewTab.boolType) {
				Code.put(Code.aload);
			} else {
				Code.put(Code.baload);
			}
		}
	}

	public void visit(DesignatorAssign designatorAssign) {
		if (!(designatorAssign.getDesignator() instanceof DesignatorIdentBraces)
			&& !(designatorAssign.getDesignator() instanceof DesignatorNamespaceBraces)) {
			Code.store(designatorAssign.getDesignator().obj);
		} else {
			if (designatorAssign.getDesignator().obj.getType() == Tab.intType
					|| designatorAssign.getDesignator().obj.getType() == NewTab.boolType) {
				Code.put(Code.astore);
			} else {
				Code.put(Code.bastore);
			}
		}
	}
	
	public int getAddOp(SyntaxNode node) {
		if (node instanceof Plus) {
			return Code.add;
		} else {
			return Code.sub;
		}

	}

	public void visit(ExprAddop exprAddop) {
		SyntaxNode addOrSub = exprAddop.getAddOp();
		Code.put(getAddOp(addOrSub));
	}
	
	public void visit(ExprMinus exprMinus) {
		Code.put(Code.neg);
	}

	public void visit(TermMulOp termMulOp) {
		SyntaxNode mulOperation = termMulOp.getMulOp();
		Code.put(getMulOp(mulOperation));
	}



	public int getMulOp(SyntaxNode node) {
		if (node instanceof Mul) {
			return Code.mul;
		} else if (node instanceof Div) {
			return Code.div;
		} else {
			return Code.rem;
		}
	}

	public void visit(FactorNew factorNew) {
		Struct newType = factorNew.getType().struct;
		Code.put(Code.newarray);
		if (newType == Tab.intType || newType == NewTab.boolType) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}
}