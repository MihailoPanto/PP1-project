package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class NewObj extends Obj {
	
	public static final int Namespace = 7;

	public NewObj(int kind, String name, Struct type) {
		super(kind, name, type);
		// TODO Auto-generated constructor stub
	}

	public NewObj(int kind, String name, Struct type, int adr, int level) {
		super(kind, name, type, adr, level);
		// TODO Auto-generated constructor stub
	}

}
