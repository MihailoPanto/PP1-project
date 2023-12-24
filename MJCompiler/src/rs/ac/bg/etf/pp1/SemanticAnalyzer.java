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
	String currentNamespace = "";

	Logger log = Logger.getLogger(getClass());

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

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}

	public void visit(Program program) {

		Obj funcMain = Tab.currentScope.findSymbol("main");

//		if (funcMain == null) {
//			report_error("U tabeli simbola ne postoji MAIN funkcija!", program);
//		} else {
//			if (funcMain.getKind() != Obj.Meth) {
//				report_error("Main mora biti funkcija!", program);
//			} else if (funcMain.getType() != Tab.noType) {
//				report_error("Main mora imati tip VOID!", program);
//			}
//		}

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

	public void visit(NamespaceName namespaceName) {
		currentNamespace = namespaceName.getNamespaceName();
		report_info("Obradjuje se namespace " + currentNamespace, namespaceName);
	}

	public void visit(Namespace namespace) {
		currentNamespace = "";
	}

	public void visit(VDeclaration vDeclaration) {
		SyntaxNode par = vDeclaration.getParent();
		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;

		if (currentNamespace == "") {
			report_info("Deklarisana promenljiva " + vDeclaration.getVarName(), vDeclaration);
			Obj varD = Tab.insert(Obj.Var, vDeclaration.getVarName(), tsvd.getType().struct);
		} else {
			report_info("Deklarisana promenljiva " + currentNamespace + "::" + vDeclaration.getVarName(), vDeclaration);
			Obj varD = Tab.insert(Obj.Var, currentNamespace + "::" + vDeclaration.getVarName(), tsvd.getType().struct);
		}

	}

	public void visit(VArrayDeclaration vArrayDeclaration) {
		SyntaxNode par = vArrayDeclaration.getParent();
		while (!(par instanceof TypeSemiVarDecl))
			par = par.getParent();

		TypeSemiVarDecl tsvd = (TypeSemiVarDecl) par;

		if (currentNamespace == "") {
			report_info("Deklarisana promenljiva niza " + vArrayDeclaration.getVarName(), vArrayDeclaration);
			Obj varD = Tab.insert(Obj.Var, vArrayDeclaration.getVarName(),
					new Struct(Struct.Array, tsvd.getType().struct));
		} else {
			report_info("Deklarisana promenljiva niza " + currentNamespace + "::" + vArrayDeclaration.getVarName(),
					vArrayDeclaration);
			Obj varD = Tab.insert(Obj.Var, currentNamespace + "::" + vArrayDeclaration.getVarName(),
					new Struct(Struct.Array, tsvd.getType().struct));
		}
	}
	
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
	
//	public void visit(ConstDeclaration constDeclaration) {
//		SyntaxNode par = constDeclaration.getParent();
//		while (!(par instanceof TypeSemiVarDecl))
//			par = par.getParent();
//
//		ConstDecl constDecl = (ConstDecl) par;
//		
//		if (currentNamespace == "") {
//			report_info("Deklarisana konstanta " + constDeclaration.getConstName(), constDeclaration);
//			Obj varD = Tab.insert(Obj.Var, constDeclaration.getConstName(),
//					new Struct(Struct.Array, tsvd.getType().struct));
//		} else {
//			report_info("Deklarisana promenljiva niza " + currentNamespace + "::" + vArrayDeclaration.getVarName(),
//					vArrayDeclaration);
//			Obj varD = Tab.insert(Obj.Var, currentNamespace + vArrayDeclaration.getVarName(),
//					new Struct(Struct.Array, tsvd.getType().struct));
//		}
//	}
}
