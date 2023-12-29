package rs.ac.bg.etf.pp1;

import java.util.Collection;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

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

	public void visit(MethodTypeNameType methodTypeName) {
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
		Code.put(fpCnt.getCount() + varCnt.getCount());
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
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}

	public void visit(MethodDecl methodDecl) {
		currentMethod = null;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(StmtPrint printStmt) {
		if (printStmt.getExpr().struct == Tab.intType || printStmt.getExpr().struct == NewTab.boolType) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
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
		} else {
			Code.loadConst(0);
		}
	}

	public void visit(ConstDeclaration constDeclaration) {
		Struct type = constDeclaration.getConstValue().struct;
		if (type == Tab.intType) {
			System.out.println("int");
			constDeclaration.obj.setAdr(((NumberConst) constDeclaration.getConstValue()).getN1());
		} else if (type == Tab.charType) {
			System.out.println("char");
			constDeclaration.obj.setAdr(((CharConst) constDeclaration.getConstValue()).getC1().charAt(1));
		} else if (type == NewTab.boolType) {
			System.out.println("bool");
			if (((BoolConst) constDeclaration.getConstValue()).getB1().equals("true")) {
				constDeclaration.obj.setAdr(1);
			} else {
				constDeclaration.obj.setAdr(2);
			}
		} else {
			System.out.println("nista");
		}
		Code.load(constDeclaration.obj);
	}

	public Obj findNamespaceField(String ns, String field) {

		Collection<Obj> localSymbols = outerScope.getLocalSymbols();
		for (Obj o : localSymbols) {
			if (o.getKind() != Obj.Meth && o.getName().equals(ns)) {
				for (Obj f : o.getLocalSymbols()) {
					if (f.getName().equals(field)) {
						return f;
					}
				}
			}
		}
		return null;
	}

	public void visit(DesignatorIdent designatorIdent) {
		Code.load(designatorIdent.obj);
	}

	public void visit(DesignatorIdentBraces designatorIdentBraces) {
		Obj array = getVarConst(designatorIdentBraces.getDesigName());
		if (array != null) {
			Code.load(array);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			if (!(designatorIdentBraces.getParent() instanceof DesignatorAssign)) {

				if (designatorIdentBraces.obj.getType() == Tab.charType) {
					Code.put(Code.baload);
				} else {
					Code.put(Code.aload);
				}
			}
		}
	}

	public void visit(DesignatorNamespace designatorNamespace) {
		Code.load(designatorNamespace.obj);
	}

	public void visit(DesignatorNamespaceBraces designatorNamespaceBraces) {
		Obj array = findNamespaceField(designatorNamespaceBraces.getNamespaceName(),
				designatorNamespaceBraces.getDesigName());
		if (array != null) {
			Code.load(array);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			if (!(designatorNamespaceBraces.getParent() instanceof DesignatorAssign)) {

				if (designatorNamespaceBraces.obj.getType() == Tab.charType) {
					Code.put(Code.baload);
				} else {
					Code.put(Code.aload);
				}
			}
		}
	}

	public void visit(DesignatorAssign designatorAssign) {
		Code.store(designatorAssign.getDesignator().obj);
//		Code.put(Code.aload);
	}

	public void visit(DesignatorPlusPlus designatorPlusPlus) {
		Obj obj = designatorPlusPlus.getDesignator().obj;
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(obj);
	}

	public void visit(DesignatorMinusMinus designatorMinusMinus) {
		Obj obj = designatorMinusMinus.getDesignator().obj;
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(obj);
	}

	public void visit(FactorNew factorNew) {
		Code.put(Code.newarray);
		if (factorNew.getType().struct == Tab.charType) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}

	public Obj getVarConst(String objName) {
		Obj obj = Tab.find(objName);
		if (obj == Tab.noObj) {
			Collection<Obj> localSymbols = outerScope.getLocalSymbols();
			for (Obj o : localSymbols) {
				if (o.getKind() != Obj.Meth && o.getName().equals(objName)) {
					return o;
				}
			}
			if (currentMethod != null) {
				localSymbols = currentMethod.getLocalSymbols();
				for (Obj o : localSymbols) {
					if (o.getName().equals(objName)) {
						return o;
					}
				}
			}
		} else {
			return obj;
		}
		return null;
	}

	public void visit(ExprMinus exprMinus) {
		Code.put(Code.neg);
	}

	public void visit(ExprAddop exprAddop) {
		SyntaxNode addOrSub = exprAddop.getAddOp();
		if (addOrSub instanceof AddOpPlus) {
			Code.put(Code.add);
		} else if (addOrSub instanceof AddOpMinus) {
			Code.put(Code.sub);
		}
	}

	public void visit(TermMulOp termMulOp) {
		SyntaxNode mulOperation = termMulOp.getMulOp();
		if (mulOperation instanceof Mul) {
			Code.put(Code.mul);
		} else if (mulOperation instanceof Div) {
			Code.put(Code.div);
		} else if (mulOperation instanceof Mod) {
			Code.put(Code.rem);
		}
	}

}
