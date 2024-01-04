package rs.ac.bg.etf.pp1;

import java.util.Collection;

import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	private Obj outerScope = null;
	private Obj currentMethod = null;

	public CodeGenerator(Obj outerScope) {
		super();
		this.outerScope = outerScope;
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

		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);

		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(currentMethod.getLocalSymbols().size());
	}

	public void visit(MethodDecl methodDecl) {
		currentMethod = null;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(StmtRead StmtRead) {
		Obj tmp = StmtRead.getDesignator().obj;
		if (tmp.getType().getKind() == Struct.Char) {
			Code.put(Code.bread);
			Code.store(tmp);
		} else {
			Code.put(Code.read);
			Code.store(tmp);
		}
	}

	public void visit(StmtPrint printStmt) {
		Struct exprType = printStmt.getExpr().struct;
		if (exprType == Tab.charType) {
			Code.loadConst(1);
			Code.put(Code.bprint);
		} else {
			Code.loadConst(5);
			Code.put(Code.print);
		}
	}

	public void visit(StmtPrintNum printStmt) {
		Struct exprType = printStmt.getExpr().struct;
		if (exprType == Tab.charType) {
			Code.loadConst(printStmt.getN2());
			Code.put(Code.bprint);
		} else {
			Code.loadConst(printStmt.getN2());
			Code.put(Code.print);

		}
	}

	public void visit(FactorConstValueNum factorConstValue) {
		Code.loadConst(factorConstValue.getN1());
	}

	public void visit(FactorConstValueChar factorConstValue) {
		Code.loadConst(factorConstValue.getC1().charAt(1));
	}

	public void visit(FactorConstValueBool factorConstValue) {
		if (factorConstValue.getB1().equals("true")) {
			Code.loadConst(1);
		} else if (factorConstValue.getB1().equals("false")) {
			Code.loadConst(0);
		}
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

	public void visit(DesignatorPlusPlus designator) {
		if (designator.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Obj tmp = designator.getDesignator().obj;
		Code.load(tmp);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(tmp);
	}

	public void visit(DesignatorMinusMinus designator) {
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

	public void visit(Array array) {
		String name = "";
		if (array.getParent() instanceof DesignatorIdentBraces) {
			DesignatorIdentBraces par = (DesignatorIdentBraces) array.getParent();
			name = par.getDesigName();
		} else if (array.getParent() instanceof DesignatorNamespaceBraces) {
			DesignatorNamespaceBraces par = (DesignatorNamespaceBraces) array.getParent();
			name = par.getNamespaceName() + "::" + par.getDesigName();
		}
		Obj arr = Tab.find(name);
		if (arr.equals(Tab.noObj)) {
			if (currentMethod != null) {
				arr = getFromScope(currentMethod, name);
			}
			if (arr == null) {
				arr = getFromScope(outerScope, name);
			}
		}
		if (arr != null) {
			Code.load(arr);
		}
	}

	public void visit(DesignatorIdent designatorIdent) {
		if (!(designatorIdent.getParent() instanceof DesignatorAssign)
				&& !(designatorIdent.getParent() instanceof DesignatorPlusPlus)
				&& !(designatorIdent.getParent() instanceof DesignatorMinusMinus)) {
			Code.load(designatorIdent.obj);
		}
	}

	public void visit(DesignatorIdentBraces designatorIdentBraces) {
		if (!(designatorIdentBraces.getParent() instanceof DesignatorAssign)
				&& !(designatorIdentBraces.getParent() instanceof DesignatorPlusPlus)
				&& !(designatorIdentBraces.getParent() instanceof DesignatorMinusMinus)) {
			if (designatorIdentBraces.obj.getType() == Tab.intType
					|| designatorIdentBraces.obj.getType() == NewTab.boolType) {
				Code.put(Code.aload);
			} else {
				Code.put(Code.baload);
			}
		}
	}

	public void visit(DesignatorNamespace designatorNamespace) {
		System.out.println(designatorNamespace.obj.getAdr() + "  " + designatorNamespace.getDesigName() + "   "
				+ designatorNamespace.getLine());
		if (!(designatorNamespace.getParent() instanceof DesignatorAssign)
				&& !(designatorNamespace.getParent() instanceof DesignatorPlusPlus)
				&& !(designatorNamespace.getParent() instanceof DesignatorMinusMinus)) {
			Code.load(designatorNamespace.obj);
		}
	}

	public void visit(DesignatorNamespaceBraces designatorNamespaceBraces) {
		if (!(designatorNamespaceBraces.getParent() instanceof DesignatorAssign)
				&& !(designatorNamespaceBraces.getParent() instanceof DesignatorPlusPlus)
				&& !(designatorNamespaceBraces.getParent() instanceof DesignatorMinusMinus)) {
			if (designatorNamespaceBraces.obj.getType() == Tab.intType
					|| designatorNamespaceBraces.obj.getType() == NewTab.boolType) {
				Code.put(Code.aload);
			} else {
				Code.put(Code.baload);
			}
		}
	}

	public void visit(DesignatorAssign designatorAssign) {
		if ((designatorAssign.getDesignator() instanceof DesignatorIdentBraces)
				|| (designatorAssign.getDesignator() instanceof DesignatorNamespaceBraces)) {
			if (designatorAssign.getDesignator().obj.getType() == Tab.intType
					|| designatorAssign.getDesignator().obj.getType() == NewTab.boolType) {
				Code.put(Code.astore);
			} else {
				Code.put(Code.bastore);
			}
		} else {
			Code.store(designatorAssign.getDesignator().obj);
		}
	}

	public void visit(ExprMinus exprMinus) {
		Code.put(Code.neg);
	}

	public void visit(ExprAddop exprAddop) {
		SyntaxNode addOrSub = exprAddop.getAddOp();
		Code.put(getAddOp(addOrSub));
	}

	public void visit(TermMulOp termMulOp) {
		SyntaxNode mulOperation = termMulOp.getMulOp();
		Code.put(getMulOp(mulOperation));
	}

	public int getAddOp(SyntaxNode node) {
		if (node instanceof AddOpPlus)
			return Code.add;
		else
			return Code.sub;
	}

	public int getMulOp(SyntaxNode node) {
		if (node instanceof Mul)
			return Code.mul;
		else if (node instanceof Div)
			return Code.div;
		else
			return Code.rem;
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