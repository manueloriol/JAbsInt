/**
 * 
 */
package jabsint;

import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Mnemonic;
import javassist.bytecode.Opcode;

/**
 * This class represents an abstract interpreter.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 8, 2011
 *
 */
public class JAIAbstractInterpreter {

	/**
	 * A variable that stores all the worlds that need to be recomposed afterwards.
	 */
	private Vector<JAIWorld> resultsOfExecutionToCollect = new Vector<JAIWorld>();

	/**
	 * A variable that stores all the worlds that need to be recomposed afterwards.
	 */
	private Stack<Integer> returnIndexesForSubroutines = new Stack<Integer>();

	/**
	 * The method to interpret.
	 */
	private CtMethod m = null;

	/**
	 * Simple getter for the method to interpret.
	 * 
	 * @return the method to interpret.
	 */
	public CtMethod getM() {
		return m;
	}


	/**
	 * Simple constructor for the given method.
	 * 
	 * @param m the method to interpret.
	 */
	public JAIAbstractInterpreter(CtMethod m) {
		super();
		this.m = m;
	}

	/**
	 * Generates a method per bytecode to call an equivalent method in the abstract world.
	 * 
	 * @return a String containing the methods
	 */
	public static String generateAllvisitorMehtods() {
		String result = "";
		for (int j=0;j<203;j++) {
			try {
				String opcode = Mnemonic.OPCODE[j];
				if(opcode!=null) {

					String begin =	"/**\n* Interprets a ";
					String middle1 =	"\n* \n* @param world the abstract world.\n* @param ci the code iterator.\n* @param index the index in the bytecode.\n* @return the new state of abstract variables.\n*/	\npublic JEWorld interpret";
					String middle2 =	"(JEWorld world, CodeIterator ci, int index) {\nJEDebug.print(\"Interpreting a ";
					String end1 =		" :\"+Mnemonic.OPCODE[ci.byteAt(index)], this);\nreturn world.evaluate";
					String end2	=	"();\n}\n\n";
					String opcodeForMethodName = opcode.substring(0,1).toUpperCase()+opcode.substring(1);
					result+=(begin+opcode+middle1+opcodeForMethodName+middle2+opcode+end1+opcodeForMethodName+end2);
				} else 
					throw new Exception("Not available");
			} catch (Exception e) {
				//				System.out.println(j+": NOT AVAILABLE");
			}
		}
		return result;
	}

	/**
	 * Generates a method per bytecode for the abstract world.
	 * 
	 * @return a String containing the methods
	 */
	public static String generateAllWorldMehtods() {
		String result = "";
		for (int j=0;j<203;j++) {
			try {
				String opcode = Mnemonic.OPCODE[j];
				if(opcode!=null) {

					String begin =	"/**\n* Evaluates a ";
					String middle1 =	"\n* @return the new state of the abstract world.\n*/	\npublic JEWorld evaluate";
					String middle2 =	"() {\nJEDebug.print(\"Evaluate a ";
					String end =		" \", this);\nreturn this;\n}\n\n";
					String opcodeForMethodName = opcode.substring(0,1).toUpperCase()+opcode.substring(1);
					result+=begin+opcode+middle1+opcodeForMethodName+middle2+opcode+end;
				} else 
					throw new Exception("Not available");
			} catch (Exception e) {
				//				System.out.println(j+": NOT AVAILABLE");
			}
		}
		return result;
	}
	/**
	 * Generates a case block per bytecode for the abstract world.
	 * 
	 * @return a String containing the methods
	 */
	public static String generateAllCasesBlocks() {
		String result = "";
		for (int j=0;j<203;j++) {
			try {
				String opcode = Mnemonic.OPCODE[j].toUpperCase();
				if(opcode!=null) {
					String begin =	"case Opcode.";
					String middle =	": \n    modifiedWorld = this.interpret";
					String end =		"(modifiedWorld, ci, index);\n    break;\n";
					String opcodeForMethodName = opcode.substring(0,1).toUpperCase()+opcode.substring(1).toLowerCase();
					result+=begin+opcode+middle+opcodeForMethodName+end;
				} else 
					throw new Exception("Not available");
			} catch (Exception e) {
				//				System.out.println(j+": NOT AVAILABLE");
			}
		}
		return result;
	}

	/**
	 * The main. Simply pass the name of the class to instrument as an argument.
	 * The program is not resilient at all. Only use twice on the program otherwise there will be exceptions...
	 * 
	 * @param args
	 * @throws NotFoundException 
	 * @throws CannotCompileException 
	 * @throws IOException 
	 * @throws BadBytecode 
	 */
	public static void main(String[] args) throws NotFoundException, IOException, CannotCompileException, BadBytecode {
		String className = args[0];
		// we retrieve the class file into a Javassist utility class
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get(className);

		String methodName = args[1];
		JAIAbstractInterpreter i = new JAIAbstractInterpreter(cc.getDeclaredMethod(methodName));
		i.interpret(new JAIWorld());
		//System.out.println(generateAllvisitorMehtods());
		//System.out.println(generateAllWorldMehtods());
		//System.out.println(generateAllCasesBlocks());
		//i.testInterpreterRecognisesAllInstructions();
	}

	/**
	 * A simple test that shows that the interpreter recognises all bytecodes.
	 */ 
	public void testInterpreterRecognisesAllInstructions(){

		CodeAttribute ca = m.getMethodInfo().getCodeAttribute();
		CodeIterator ci = ca.iterator();
		// TODO build a proper test case
		JAIWorld world = new JAIWorld();

		for (int j=0;j<203;j++) {
			try {
				String opcode = Mnemonic.OPCODE[j].toUpperCase();
				if(opcode!=null) {
					JAIWorld tmpWorld = this.interpretOneInstruction(world, ci, j);
					if(tmpWorld==null) {
						throw new Exception("Did not recognize: ");
					}
				}
			} catch (Exception e) {
				//				System.out.println(j+": NOT AVAILABLE");
			}
		}

	}
	/**
	 * Method that interprets the method on a given world.
	 * 
	 * @param world the world on which to interpret.
	 * @return the interpreted world.
	 * @throws BadBytecode
	 */
	public JAIWorld interpret(JAIWorld world) {
		JAIWorld resultWorld = interpretFrom(world,0);
		for(JAIWorld w: this.resultsOfExecutionToCollect) {
			resultWorld = resultWorld.combineWith(w);
		}
		JAIDebug.print("Number of paths: "+this.resultsOfExecutionToCollect.size(), this, true);
		return resultWorld;
	}


	/**
	 * Method that interprets code from a given index on a give world.
	 * 
	 * @param world the world to interpret.
	 * @param index the index at which we should start in the code.
	 * @return the interpreted world.
	 * @throws BadBytecode
	 */
	public JAIWorld interpretFrom(JAIWorld world, int index) {
		JAIDebug.print("Started an interpreter from: "+index, this, true);
		CodeAttribute ca = m.getMethodInfo().getCodeAttribute();
		CodeIterator ci = ca.iterator();
		ci.move(index);


		JAIWorld modifiedWorld = null;

		while (ci.hasNext()) {
			modifiedWorld = null;
			int index0;
			try {
				index0 = ci.next();
			} catch (BadBytecode e) {
				return world;
			} 
			modifiedWorld = this.interpretOneInstruction(world, ci, index0);
			if (modifiedWorld==null) {
				int op = ci.byteAt(index0);
				JAIDebug.print("Not interpreting "+Mnemonic.OPCODE[op], this);
				return null;
			}
		}
		return modifiedWorld;
	}

	/**
	 * Interprets one instruction.
	 * 
	 * @param world the world in which interpret it.
	 * @param ci the code iterator.
	 * @param index the index in the code.
	 * @return the modified world
	 */
	public JAIWorld interpretOneInstruction(JAIWorld world, CodeIterator ci, int index) {
		int op = ci.byteAt(index);
		if (Mnemonic.OPCODE[op].contains("pop")||Mnemonic.OPCODE[op].contains("dup")||Mnemonic.OPCODE[op].contains("swap")) {
			return interpretStack(world, ci, index);
		}
		if (Mnemonic.OPCODE[op].substring(1).contains("aload")||Mnemonic.OPCODE[op].substring(1).contains("astore")||Mnemonic.OPCODE[op].contains("array")) {
			return interpretArrayOperation(world, ci, index);
		}

		if (Mnemonic.OPCODE[op].contains("field")||Mnemonic.OPCODE[op].equals("getstatic")||Mnemonic.OPCODE[op].equals("getstatic")) {
			return interpretFieldsOperation(world, ci, index);
		}
		if (Mnemonic.OPCODE[op].contains("monitor")) {
			return interpretConcurrencyOperations(world, ci, index);
		}
		if (Mnemonic.OPCODE[op].equals("new")||Mnemonic.OPCODE[op].equals("aconst_null")) {
			return interpretObjectStackOperation(world, ci, index);
		}

		if (Mnemonic.OPCODE[op].contains("invoke")) {
			return interpretInvokeOperation(world, ci, index);
		}

		if (Mnemonic.OPCODE[op].contains("return")) {
			return interpretReturnOperation(world, ci, index);
		}

		if (Mnemonic.OPCODE[op].contains("load")||Mnemonic.OPCODE[op].contains("store")||Mnemonic.OPCODE[op].equals("iinc")) {
			return interpretLocalVariableLoadAndStore(world, ci, index);
		}

		if (Mnemonic.OPCODE[op].startsWith("ldc")) {
			return interpretLoadingFromConstantPool(world, ci, index);
		}
		if (Mnemonic.OPCODE[op].startsWith("goto")||Mnemonic.OPCODE[op].startsWith("jsr")||Mnemonic.OPCODE[op].equals("ret")||Mnemonic.OPCODE[op].contains("if")||Mnemonic.OPCODE[op].contains("switch")) {
			return interpretControlFlowStructure(world, ci, index);
		}

		if (Mnemonic.OPCODE[op].equals("nop")||Mnemonic.OPCODE[op].equals("athrow")||Mnemonic.OPCODE[op].equals("instanceof")||Mnemonic.OPCODE[op].equals("checkcast")||Mnemonic.OPCODE[op].equals("wide")) {
			return interpretSpecialOperation(world, ci, index);
		}

		if (Mnemonic.OPCODE[op].contains("ipush")||Mnemonic.OPCODE[op].contains("const")||Mnemonic.OPCODE[op].substring(1,2).equals("2")||Mnemonic.OPCODE[op].contains("add")||Mnemonic.OPCODE[op].contains("div")||Mnemonic.OPCODE[op].contains("rem")||Mnemonic.OPCODE[op].contains("mul")||Mnemonic.OPCODE[op].contains("neg")||Mnemonic.OPCODE[op].contains("cmp")||Mnemonic.OPCODE[op].contains("and")||Mnemonic.OPCODE[op].contains("or")||Mnemonic.OPCODE[op].contains("sh")||Mnemonic.OPCODE[op].contains("xor")) {
			return interpretPrimitiveTypeOperation(world, ci, index);
		}
		return null;
	}






	/**
	 * Interprets a primitive type operation (constant pushing, and, mul...)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretPrimitiveTypeOperation(JAIWorld world,
			CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a primitive type operation (and, mul...) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);		
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.ICONST_M1: 
			modifiedWorld = this.interpretIconst_m1(modifiedWorld, ci, index);
			break;
		case Opcode.ICONST_0: 
			modifiedWorld = this.interpretIconst_0(modifiedWorld, ci, index);
			break;
		case Opcode.ICONST_1: 
			modifiedWorld = this.interpretIconst_1(modifiedWorld, ci, index);
			break;
		case Opcode.ICONST_2: 
			modifiedWorld = this.interpretIconst_2(modifiedWorld, ci, index);
			break;
		case Opcode.ICONST_3: 
			modifiedWorld = this.interpretIconst_3(modifiedWorld, ci, index);
			break;
		case Opcode.ICONST_4: 
			modifiedWorld = this.interpretIconst_4(modifiedWorld, ci, index);
			break;
		case Opcode.ICONST_5: 
			modifiedWorld = this.interpretIconst_5(modifiedWorld, ci, index);
			break;
		case Opcode.LCONST_0: 
			modifiedWorld = this.interpretLconst_0(modifiedWorld, ci, index);
			break;
		case Opcode.LCONST_1: 
			modifiedWorld = this.interpretLconst_1(modifiedWorld, ci, index);
			break;
		case Opcode.FCONST_0: 
			modifiedWorld = this.interpretFconst_0(modifiedWorld, ci, index);
			break;
		case Opcode.FCONST_1: 
			modifiedWorld = this.interpretFconst_1(modifiedWorld, ci, index);
			break;
		case Opcode.FCONST_2: 
			modifiedWorld = this.interpretFconst_2(modifiedWorld, ci, index);
			break;
		case Opcode.DCONST_0: 
			modifiedWorld = this.interpretDconst_0(modifiedWorld, ci, index);
			break;
		case Opcode.DCONST_1: 
			modifiedWorld = this.interpretDconst_1(modifiedWorld, ci, index);
			break;
		case Opcode.BIPUSH: 
			modifiedWorld = this.interpretBipush(modifiedWorld, ci, index);
			break;
		case Opcode.SIPUSH: 
			modifiedWorld = this.interpretSipush(modifiedWorld, ci, index);
			break;
		case Opcode.IADD: 
			modifiedWorld = this.interpretIadd(modifiedWorld, ci, index);
			break;
		case Opcode.LADD: 
			modifiedWorld = this.interpretLadd(modifiedWorld, ci, index);
			break;
		case Opcode.FADD: 
			modifiedWorld = this.interpretFadd(modifiedWorld, ci, index);
			break;
		case Opcode.DADD: 
			modifiedWorld = this.interpretDadd(modifiedWorld, ci, index);
			break;
		case Opcode.ISUB: 
			modifiedWorld = this.interpretIsub(modifiedWorld, ci, index);
			break;
		case Opcode.LSUB: 
			modifiedWorld = this.interpretLsub(modifiedWorld, ci, index);
			break;
		case Opcode.FSUB: 
			modifiedWorld = this.interpretFsub(modifiedWorld, ci, index);
			break;
		case Opcode.DSUB: 
			modifiedWorld = this.interpretDsub(modifiedWorld, ci, index);
			break;
		case Opcode.IMUL: 
			modifiedWorld = this.interpretImul(modifiedWorld, ci, index);
			break;
		case Opcode.LMUL: 
			modifiedWorld = this.interpretLmul(modifiedWorld, ci, index);
			break;
		case Opcode.FMUL: 
			modifiedWorld = this.interpretFmul(modifiedWorld, ci, index);
			break;
		case Opcode.DMUL: 
			modifiedWorld = this.interpretDmul(modifiedWorld, ci, index);
			break;
		case Opcode.IDIV: 
			modifiedWorld = this.interpretIdiv(modifiedWorld, ci, index);
			break;
		case Opcode.LDIV: 
			modifiedWorld = this.interpretLdiv(modifiedWorld, ci, index);
			break;
		case Opcode.FDIV: 
			modifiedWorld = this.interpretFdiv(modifiedWorld, ci, index);
			break;
		case Opcode.DDIV: 
			modifiedWorld = this.interpretDdiv(modifiedWorld, ci, index);
			break;
		case Opcode.IREM: 
			modifiedWorld = this.interpretIrem(modifiedWorld, ci, index);
			break;
		case Opcode.LREM: 
			modifiedWorld = this.interpretLrem(modifiedWorld, ci, index);
			break;
		case Opcode.FREM: 
			modifiedWorld = this.interpretFrem(modifiedWorld, ci, index);
			break;
		case Opcode.DREM: 
			modifiedWorld = this.interpretDrem(modifiedWorld, ci, index);
			break;
		case Opcode.INEG: 
			modifiedWorld = this.interpretIneg(modifiedWorld, ci, index);
			break;
		case Opcode.LNEG: 
			modifiedWorld = this.interpretLneg(modifiedWorld, ci, index);
			break;
		case Opcode.FNEG: 
			modifiedWorld = this.interpretFneg(modifiedWorld, ci, index);
			break;
		case Opcode.DNEG: 
			modifiedWorld = this.interpretDneg(modifiedWorld, ci, index);
			break;
		case Opcode.ISHL: 
			modifiedWorld = this.interpretIshl(modifiedWorld, ci, index);
			break;
		case Opcode.LSHL: 
			modifiedWorld = this.interpretLshl(modifiedWorld, ci, index);
			break;
		case Opcode.ISHR: 
			modifiedWorld = this.interpretIshr(modifiedWorld, ci, index);
			break;
		case Opcode.LSHR: 
			modifiedWorld = this.interpretLshr(modifiedWorld, ci, index);
			break;
		case Opcode.IUSHR: 
			modifiedWorld = this.interpretIushr(modifiedWorld, ci, index);
			break;
		case Opcode.LUSHR: 
			modifiedWorld = this.interpretLushr(modifiedWorld, ci, index);
			break;
		case Opcode.IAND: 
			modifiedWorld = this.interpretIand(modifiedWorld, ci, index);
			break;
		case Opcode.LAND: 
			modifiedWorld = this.interpretLand(modifiedWorld, ci, index);
			break;
		case Opcode.IOR: 
			modifiedWorld = this.interpretIor(modifiedWorld, ci, index);
			break;
		case Opcode.LOR: 
			modifiedWorld = this.interpretLor(modifiedWorld, ci, index);
			break;
		case Opcode.IXOR: 
			modifiedWorld = this.interpretIxor(modifiedWorld, ci, index);
			break;
		case Opcode.LXOR: 
			modifiedWorld = this.interpretLxor(modifiedWorld, ci, index);
			break;
		case Opcode.I2L: 
			modifiedWorld = this.interpretI2l(modifiedWorld, ci, index);
			break;
		case Opcode.I2F: 
			modifiedWorld = this.interpretI2f(modifiedWorld, ci, index);
			break;
		case Opcode.I2D: 
			modifiedWorld = this.interpretI2d(modifiedWorld, ci, index);
			break;
		case Opcode.L2I: 
			modifiedWorld = this.interpretL2i(modifiedWorld, ci, index);
			break;
		case Opcode.L2F: 
			modifiedWorld = this.interpretL2f(modifiedWorld, ci, index);
			break;
		case Opcode.L2D: 
			modifiedWorld = this.interpretL2d(modifiedWorld, ci, index);
			break;
		case Opcode.F2I: 
			modifiedWorld = this.interpretF2i(modifiedWorld, ci, index);
			break;
		case Opcode.F2L: 
			modifiedWorld = this.interpretF2l(modifiedWorld, ci, index);
			break;
		case Opcode.F2D: 
			modifiedWorld = this.interpretF2d(modifiedWorld, ci, index);
			break;
		case Opcode.D2I: 
			modifiedWorld = this.interpretD2i(modifiedWorld, ci, index);
			break;
		case Opcode.D2L: 
			modifiedWorld = this.interpretD2l(modifiedWorld, ci, index);
			break;
		case Opcode.D2F: 
			modifiedWorld = this.interpretD2f(modifiedWorld, ci, index);
			break;
		case Opcode.I2B: 
			modifiedWorld = this.interpretI2b(modifiedWorld, ci, index);
			break;
		case Opcode.I2C: 
			modifiedWorld = this.interpretI2c(modifiedWorld, ci, index);
			break;
		case Opcode.I2S: 
			modifiedWorld = this.interpretI2s(modifiedWorld, ci, index);
			break;		  
		case Opcode.LCMP: 
			modifiedWorld = this.interpretLcmp(modifiedWorld, ci, index);
			break;
		case Opcode.FCMPL: 
			modifiedWorld = this.interpretFcmpl(modifiedWorld, ci, index);
			break;
		case Opcode.FCMPG: 
			modifiedWorld = this.interpretFcmpg(modifiedWorld, ci, index);
			break;
		case Opcode.DCMPL: 
			modifiedWorld = this.interpretDcmpl(modifiedWorld, ci, index);
			break;
		case Opcode.DCMPG: 
			modifiedWorld = this.interpretDcmpg(modifiedWorld, ci, index);
			break;		
		}
		return modifiedWorld;	}


	/**
	 * Interprets a special operation (nop, athrow, instanceof, checkcast, wide)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretSpecialOperation(JAIWorld world, CodeIterator ci,
			int index) {
		JAIDebug.print("Interpreting a special operation (nop, athrow, instanceof, checkcast, wide) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);

		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.NOP:
			modifiedWorld = this.interpretNop(modifiedWorld, ci, index);
			break;
		case Opcode.ATHROW:
			modifiedWorld = this.interpretAthrow(modifiedWorld, ci, index);			
			break;
		case Opcode.INSTANCEOF:
			modifiedWorld = this.interpretInstanceof(modifiedWorld, ci, index);			
			break;
		case Opcode.CHECKCAST: 
			modifiedWorld = this.interpretCheckcast(modifiedWorld, ci, index);
			break;
		case Opcode.WIDE:
			modifiedWorld = this.interpretWide(modifiedWorld, ci, index);			
			break;
		}

		return modifiedWorld;
	}


	/**
	 * Interprets a control-flow statement (if, ret, jsr, goto, switch)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretControlFlowStructure(JAIWorld world,
			CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a control-flow statement (if, ret, jsr, goto, switch) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.IF_ACMPEQ:
			modifiedWorld = this.interpretIf_acmpeq(modifiedWorld, ci, index);
			break;
		case Opcode.IF_ACMPNE:
			modifiedWorld = this.interpretIf_acmpne(modifiedWorld, ci, index);
			break;
		case Opcode.IF_ICMPEQ:
			modifiedWorld = this.interpretIf_icmpeq(modifiedWorld, ci, index);
			break;
		case Opcode.IF_ICMPGE:
			modifiedWorld = this.interpretIf_icmpge(modifiedWorld, ci, index);
			break;
		case Opcode.IF_ICMPGT:
			modifiedWorld = this.interpretIf_icmpgt(modifiedWorld, ci, index);
			break;
		case Opcode.IF_ICMPLE:
			modifiedWorld = this.interpretIf_icmple(modifiedWorld, ci, index);
			break;
		case Opcode.IF_ICMPLT:
			modifiedWorld = this.interpretIf_icmplt(modifiedWorld, ci, index);
			break;
		case Opcode.IF_ICMPNE:
			modifiedWorld = this.interpretIf_icmpne(modifiedWorld, ci, index);
			break;
		case Opcode.IFEQ:
			modifiedWorld = this.interpretIfeq(modifiedWorld, ci, index);
			break;
		case Opcode.IFGE:
			modifiedWorld = this.interpretIfge(modifiedWorld, ci, index);
			break;
		case Opcode.IFGT:
			modifiedWorld = this.interpretIfgt(modifiedWorld, ci, index);
			break;
		case Opcode.IFLE:
			modifiedWorld = this.interpretIfle(modifiedWorld, ci, index);
			break;
		case Opcode.IFLT:
			modifiedWorld = this.interpretIflt(modifiedWorld, ci, index);
			break;
		case Opcode.IFNE:
			modifiedWorld = this.interpretIfne(modifiedWorld, ci, index);
			break;
		case Opcode.IFNONNULL:
			modifiedWorld = this.interpretIfnonnull(modifiedWorld, ci, index);
			break;
		case Opcode.IFNULL:
			modifiedWorld = this.interpretIfnull(modifiedWorld, ci, index);
			break;
		case Opcode.RET:
			modifiedWorld = this.interpretRet(modifiedWorld, ci, index);
			break;
		case Opcode.JSR:
			modifiedWorld = this.interpretJsr(modifiedWorld, ci, index);
			break;
		case Opcode.JSR_W:
			modifiedWorld = this.interpretJsr_w(modifiedWorld, ci, index);
			break;
		case Opcode.GOTO:
			modifiedWorld = this.interpretGoto(modifiedWorld, ci, index);
			break;
		case Opcode.GOTO_W:
			modifiedWorld = this.interpretGoto_w(modifiedWorld, ci, index);
			break;
		case Opcode.TABLESWITCH:
			modifiedWorld = this.interpretTableswitch(modifiedWorld, ci, index);
			break;
		case Opcode.LOOKUPSWITCH: 
			modifiedWorld = this.interpretLookupswitch(modifiedWorld, ci, index);
			break;
		}		
		return modifiedWorld;
	}


	/**
	 * Interprets a load statement from constant pool (ldc...)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLoadingFromConstantPool(JAIWorld world,
			CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a load statement from constant pool (ldc...) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);		
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.LDC: 
			modifiedWorld = this.interpretLdc(modifiedWorld, ci, index);
			break;
		case Opcode.LDC_W: 
			modifiedWorld = this.interpretLdc_w(modifiedWorld, ci, index);
			break;
		case Opcode.LDC2_W: 
			modifiedWorld = this.interpretLdc2_w(modifiedWorld, ci, index);
			break;

		}
		return modifiedWorld;
	}


	/**
	 * Interprets a load or a store statement from/to local variable (iinc, ?load and ?store)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLocalVariableLoadAndStore(JAIWorld world,
			CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a load or a store statement (iinc, ?load and ?store,) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);		
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.ALOAD: 
			modifiedWorld = this.interpretAload(modifiedWorld, ci, index);
			break;
		case Opcode.ALOAD_0: 
			modifiedWorld = this.interpretAload_0(modifiedWorld, ci, index);
			break;
		case Opcode.ALOAD_1: 
			modifiedWorld = this.interpretAload_1(modifiedWorld, ci, index);
			break;
		case Opcode.ALOAD_2: 
			modifiedWorld = this.interpretAload_2(modifiedWorld, ci, index);
			break;
		case Opcode.ALOAD_3: 
			modifiedWorld = this.interpretAload_3(modifiedWorld, ci, index);
			break;
		case Opcode.ASTORE: 
			modifiedWorld = this.interpretAstore(modifiedWorld, ci, index);
			break;
		case Opcode.ASTORE_0: 
			modifiedWorld = this.interpretAstore_0(modifiedWorld, ci, index);
			break;
		case Opcode.ASTORE_1: 
			modifiedWorld = this.interpretAstore_1(modifiedWorld, ci, index);
			break;
		case Opcode.ASTORE_2: 
			modifiedWorld = this.interpretAstore_2(modifiedWorld, ci, index);
			break;
		case Opcode.ASTORE_3: 
			modifiedWorld = this.interpretAstore_3(modifiedWorld, ci, index);
			break;
		case Opcode.IINC:
			modifiedWorld = this.interpretIinc(modifiedWorld, ci, index);
			break;
		case Opcode.ILOAD: 
			modifiedWorld = this.interpretIload(modifiedWorld, ci, index);
			break;
		case Opcode.LLOAD: 
			modifiedWorld = this.interpretLload(modifiedWorld, ci, index);
			break;
		case Opcode.FLOAD: 
			modifiedWorld = this.interpretFload(modifiedWorld, ci, index);
			break;
		case Opcode.DLOAD: 
			modifiedWorld = this.interpretDload(modifiedWorld, ci, index);
			break;
		case Opcode.ILOAD_0: 
			modifiedWorld = this.interpretIload_0(modifiedWorld, ci, index);
			break;
		case Opcode.ILOAD_1: 
			modifiedWorld = this.interpretIload_1(modifiedWorld, ci, index);
			break;
		case Opcode.ILOAD_2: 
			modifiedWorld = this.interpretIload_2(modifiedWorld, ci, index);
			break;
		case Opcode.ILOAD_3: 
			modifiedWorld = this.interpretIload_3(modifiedWorld, ci, index);
			break;
		case Opcode.LLOAD_0: 
			modifiedWorld = this.interpretLload_0(modifiedWorld, ci, index);
			break;
		case Opcode.LLOAD_1: 
			modifiedWorld = this.interpretLload_1(modifiedWorld, ci, index);
			break;
		case Opcode.LLOAD_2: 
			modifiedWorld = this.interpretLload_2(modifiedWorld, ci, index);
			break;
		case Opcode.LLOAD_3: 
			modifiedWorld = this.interpretLload_3(modifiedWorld, ci, index);
			break;
		case Opcode.FLOAD_0: 
			modifiedWorld = this.interpretFload_0(modifiedWorld, ci, index);
			break;
		case Opcode.FLOAD_1: 
			modifiedWorld = this.interpretFload_1(modifiedWorld, ci, index);
			break;
		case Opcode.FLOAD_2: 
			modifiedWorld = this.interpretFload_2(modifiedWorld, ci, index);
			break;
		case Opcode.FLOAD_3: 
			modifiedWorld = this.interpretFload_3(modifiedWorld, ci, index);
			break;
		case Opcode.DLOAD_0: 
			modifiedWorld = this.interpretDload_0(modifiedWorld, ci, index);
			break;
		case Opcode.DLOAD_1: 
			modifiedWorld = this.interpretDload_1(modifiedWorld, ci, index);
			break;
		case Opcode.DLOAD_2: 
			modifiedWorld = this.interpretDload_2(modifiedWorld, ci, index);
			break;
		case Opcode.DLOAD_3: 
			modifiedWorld = this.interpretDload_3(modifiedWorld, ci, index);
			break;
		case Opcode.ISTORE: 
			modifiedWorld = this.interpretIstore(modifiedWorld, ci, index);
			break;
		case Opcode.LSTORE: 
			modifiedWorld = this.interpretLstore(modifiedWorld, ci, index);
			break;
		case Opcode.FSTORE: 
			modifiedWorld = this.interpretFstore(modifiedWorld, ci, index);
			break;
		case Opcode.DSTORE: 
			modifiedWorld = this.interpretDstore(modifiedWorld, ci, index);
			break;
		case Opcode.ISTORE_0: 
			modifiedWorld = this.interpretIstore_0(modifiedWorld, ci, index);
			break;
		case Opcode.ISTORE_1: 
			modifiedWorld = this.interpretIstore_1(modifiedWorld, ci, index);
			break;
		case Opcode.ISTORE_2: 
			modifiedWorld = this.interpretIstore_2(modifiedWorld, ci, index);
			break;
		case Opcode.ISTORE_3: 
			modifiedWorld = this.interpretIstore_3(modifiedWorld, ci, index);
			break;
		case Opcode.LSTORE_0: 
			modifiedWorld = this.interpretLstore_0(modifiedWorld, ci, index);
			break;
		case Opcode.LSTORE_1: 
			modifiedWorld = this.interpretLstore_1(modifiedWorld, ci, index);
			break;
		case Opcode.LSTORE_2: 
			modifiedWorld = this.interpretLstore_2(modifiedWorld, ci, index);
			break;
		case Opcode.LSTORE_3: 
			modifiedWorld = this.interpretLstore_3(modifiedWorld, ci, index);
			break;
		case Opcode.FSTORE_0: 
			modifiedWorld = this.interpretFstore_0(modifiedWorld, ci, index);
			break;
		case Opcode.FSTORE_1: 
			modifiedWorld = this.interpretFstore_1(modifiedWorld, ci, index);
			break;
		case Opcode.FSTORE_2: 
			modifiedWorld = this.interpretFstore_2(modifiedWorld, ci, index);
			break;
		case Opcode.FSTORE_3: 
			modifiedWorld = this.interpretFstore_3(modifiedWorld, ci, index);
			break;
		case Opcode.DSTORE_0: 
			modifiedWorld = this.interpretDstore_0(modifiedWorld, ci, index);
			break;
		case Opcode.DSTORE_1: 
			modifiedWorld = this.interpretDstore_1(modifiedWorld, ci, index);
			break;
		case Opcode.DSTORE_2: 
			modifiedWorld = this.interpretDstore_2(modifiedWorld, ci, index);
			break;
		case Opcode.DSTORE_3: 
			modifiedWorld = this.interpretDstore_3(modifiedWorld, ci, index);
			break;

		}
		return modifiedWorld;
	}


	/**
	 * Interprets a return statement (return, dreturn, freturn, ireturn, lreturn, areturn)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretReturnOperation(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a return statement (return, dreturn, freturn, ireturn, lreturn, areturn) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.IRETURN: 
			modifiedWorld = this.interpretIreturn(modifiedWorld, ci, index);
			break;
		case Opcode.LRETURN: 
			modifiedWorld = this.interpretLreturn(modifiedWorld, ci, index);
			break;
		case Opcode.FRETURN: 
			modifiedWorld = this.interpretFreturn(modifiedWorld, ci, index);
			break;
		case Opcode.DRETURN: 
			modifiedWorld = this.interpretDreturn(modifiedWorld, ci, index);
			break;
		case Opcode.ARETURN: 
			modifiedWorld = this.interpretAreturn(modifiedWorld, ci, index);
			break;
		case Opcode.RETURN: 
			modifiedWorld = this.interpretReturn(modifiedWorld, ci, index);
			break;

		}
		return modifiedWorld;
	}


	/**
	 * Interprets an invocation (invokeinterface, invokespecial, invokestatic, and invokevirtual)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretInvokeOperation(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting an invocation (invokeinterface, invokespecial, invokestatic, or invokevirtual) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.INVOKEVIRTUAL: 
			modifiedWorld = this.interpretInvokevirtual(modifiedWorld, ci, index);
			break;
		case Opcode.INVOKESPECIAL: 
			modifiedWorld = this.interpretInvokespecial(modifiedWorld, ci, index);
			break;
		case Opcode.INVOKESTATIC: 
			modifiedWorld = this.interpretInvokestatic(modifiedWorld, ci, index);
			break;
		case Opcode.INVOKEINTERFACE: 
			modifiedWorld = this.interpretInvokeinterface(modifiedWorld, ci, index);
			break;

		}
		return modifiedWorld;
	}


	/**
	 * Interprets an object-related operation adding an instance to the stack (new or aconst_null)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */
	public JAIWorld interpretObjectStackOperation(JAIWorld world,
			CodeIterator ci, int index) {
		JAIDebug.print("Interpreting an object-related operation adding an instance to the stack (new or aconst_null) : "+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.NEW: 
			modifiedWorld = this.interpretNew(modifiedWorld, ci, index);
			break;
		case Opcode.ACONST_NULL: 
			modifiedWorld = this.interpretAconst_null(modifiedWorld, ci, index);
			break;

		}
		return modifiedWorld;
	}


	/**
	 * Interprets a concurrency-related operation (monitorenter or monitorexit)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */
	public JAIWorld interpretConcurrencyOperations(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a concurrency-related operation (monitorenter or monitorexit): "+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.MONITORENTER: 
			modifiedWorld = this.interpretMonitorenter(modifiedWorld, ci, index);
			break;
		case Opcode.MONITOREXIT: 
			modifiedWorld = this.interpretMonitorexit(modifiedWorld, ci, index);
			break;


		}
		return modifiedWorld;
	}


	/**
	 * Interprets a field operation (getfield, putfield, putstatic or getstatic)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */
	public JAIWorld interpretFieldsOperation(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a field operation (getfield, getstatic,...): "+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.GETFIELD: 
			modifiedWorld = this.interpretGetfield(modifiedWorld, ci, index);
			break;
		case Opcode.PUTFIELD: 
			modifiedWorld = this.interpretPutfield(modifiedWorld, ci, index);
			break;
		case Opcode.GETSTATIC: 
			modifiedWorld = this.interpretGetstatic(modifiedWorld, ci, index);
			break;
		case Opcode.PUTSTATIC: 
			modifiedWorld = this.interpretPutstatic(modifiedWorld, ci, index);
			break;
		}
		return modifiedWorld;
	}


	/**
	 * Interprets an array-related operation (baload, bastore, newarray...)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretArrayOperation(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting an array operation (baload, bastore, newarray...): "+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.IALOAD: 
			modifiedWorld = this.interpretIaload(modifiedWorld, ci, index);
			break;
		case Opcode.LALOAD: 
			modifiedWorld = this.interpretLaload(modifiedWorld, ci, index);
			break;
		case Opcode.FALOAD: 
			modifiedWorld = this.interpretFaload(modifiedWorld, ci, index);
			break;
		case Opcode.DALOAD: 
			modifiedWorld = this.interpretDaload(modifiedWorld, ci, index);
			break;
		case Opcode.AALOAD: 
			modifiedWorld = this.interpretAaload(modifiedWorld, ci, index);
			break;
		case Opcode.BALOAD: 
			modifiedWorld = this.interpretBaload(modifiedWorld, ci, index);
			break;
		case Opcode.CALOAD: 
			modifiedWorld = this.interpretCaload(modifiedWorld, ci, index);
			break;
		case Opcode.SALOAD: 
			modifiedWorld = this.interpretSaload(modifiedWorld, ci, index);
			break;
		case Opcode.IASTORE: 
			modifiedWorld = this.interpretIastore(modifiedWorld, ci, index);
			break;
		case Opcode.LASTORE: 
			modifiedWorld = this.interpretLastore(modifiedWorld, ci, index);
			break;
		case Opcode.FASTORE: 
			modifiedWorld = this.interpretFastore(modifiedWorld, ci, index);
			break;
		case Opcode.DASTORE: 
			modifiedWorld = this.interpretDastore(modifiedWorld, ci, index);
			break;
		case Opcode.AASTORE: 
			modifiedWorld = this.interpretAastore(modifiedWorld, ci, index);
			break;
		case Opcode.BASTORE: 
			modifiedWorld = this.interpretBastore(modifiedWorld, ci, index);
			break;
		case Opcode.CASTORE: 
			modifiedWorld = this.interpretCastore(modifiedWorld, ci, index);
			break;
		case Opcode.SASTORE: 
			modifiedWorld = this.interpretSastore(modifiedWorld, ci, index);
			break;
		case Opcode.NEWARRAY: 
			modifiedWorld = this.interpretNewarray(modifiedWorld, ci, index);
			break;
		case Opcode.ANEWARRAY: 
			modifiedWorld = this.interpretAnewarray(modifiedWorld, ci, index);
			break;
		case Opcode.ARRAYLENGTH: 
			modifiedWorld = this.interpretArraylength(modifiedWorld, ci, index);
			break;
		case Opcode.MULTIANEWARRAY: 
			modifiedWorld = this.interpretMultianewarray(modifiedWorld, ci, index);
			break;
		}
		return modifiedWorld;
	}


	/**
	 * Interprets an stack-related operation (pop, dup, swap and their variants)
	 * 
	 * @param vars the abstract variables.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretStack(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a stack operation (pop, dup, swap and their variants): "+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world;
		switch (ci.byteAt(index)) {
		case Opcode.POP: 
			modifiedWorld = this.interpretPop(modifiedWorld, ci, index);
			break;
		case Opcode.POP2: 
			modifiedWorld = this.interpretPop2(modifiedWorld, ci, index);
			break;
		case Opcode.DUP: 
			modifiedWorld = this.interpretDup(modifiedWorld, ci, index);
			break;
		case Opcode.DUP_X1: 
			modifiedWorld = this.interpretDup_x1(modifiedWorld, ci, index);
			break;
		case Opcode.DUP_X2: 
			modifiedWorld = this.interpretDup_x2(modifiedWorld, ci, index);
			break;
		case Opcode.DUP2: 
			modifiedWorld = this.interpretDup2(modifiedWorld, ci, index);
			break;
		case Opcode.DUP2_X1: 
			modifiedWorld = this.interpretDup2_x1(modifiedWorld, ci, index);
			break;
		case Opcode.DUP2_X2: 
			modifiedWorld = this.interpretDup2_x2(modifiedWorld, ci, index);
			break;
		case Opcode.SWAP: 
			modifiedWorld = this.interpretSwap(modifiedWorld, ci, index);
			break;
		}
		return modifiedWorld;
	}

	/**
	 * Interprets a nop
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretNop(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a nop :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateNop();
	}

	/**
	 * Interprets a aconst_null
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAconst_null(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aconst_null :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAconst_null();
	}

	/**
	 * Interprets a iconst_m1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIconst_m1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iconst_m1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIconst_m1();
	}

	/**
	 * Interprets a iconst_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIconst_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iconst_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIconst_0();
	}

	/**
	 * Interprets a iconst_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIconst_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iconst_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIconst_1();
	}

	/**
	 * Interprets a iconst_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIconst_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iconst_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIconst_2();
	}

	/**
	 * Interprets a iconst_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIconst_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iconst_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIconst_3();
	}

	/**
	 * Interprets a iconst_4
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIconst_4(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iconst_4 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIconst_4();
	}

	/**
	 * Interprets a iconst_5
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIconst_5(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iconst_5 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIconst_5();
	}

	/**
	 * Interprets a lconst_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLconst_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lconst_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLconst_0();
	}

	/**
	 * Interprets a lconst_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLconst_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lconst_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLconst_1();
	}

	/**
	 * Interprets a fconst_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFconst_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fconst_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFconst_0();
	}

	/**
	 * Interprets a fconst_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFconst_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fconst_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFconst_1();
	}

	/**
	 * Interprets a fconst_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFconst_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fconst_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFconst_2();
	}

	/**
	 * Interprets a dconst_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDconst_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dconst_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDconst_0();
	}

	/**
	 * Interprets a dconst_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDconst_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dconst_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDconst_1();
	}

	/**
	 * Interprets a bipush
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretBipush(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a bipush :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		byte b = (byte)ci.byteAt(index+1);
		return world.evaluateBipush(b);
	}

	/**
	 * Interprets a sipush
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretSipush(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a sipush :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		short s = (short)ci.s16bitAt(index+1);
		return world.evaluateSipush(s);
	}

	/**
	 * Interprets a ldc
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLdc(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ldc :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateLdc(getM().getDeclaringClass().getClassFile().getConstPool().getLdcValue(i));
	}

	/**
	 * Interprets a ldc_w
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLdc_w(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ldc_w :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.s16bitAt(index+1);
		return world.evaluateLdc_w(getM().getDeclaringClass().getClassFile().getConstPool().getLdcValue(i));
	}

	/**
	 * Interprets a ldc2_w
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLdc2_w(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ldc2_w :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.s16bitAt(index+1);
		return world.evaluateLdc2_w(getM().getDeclaringClass().getClassFile().getConstPool().getLdcValue(i));
	}

	/**
	 * Interprets a iload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateIload(i);
	}

	/**
	 * Interprets a lload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateLload(i);
	}

	/**
	 * Interprets a fload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateFload(i);
	}

	/**
	 * Interprets a dload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateDload(i);
	}

	/**
	 * Interprets a aload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateAload(i);
	}

	/**
	 * Interprets a iload_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIload_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iload_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIload_0();
	}

	/**
	 * Interprets a iload_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIload_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iload_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIload_1();
	}

	/**
	 * Interprets a iload_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIload_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iload_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIload_2();
	}

	/**
	 * Interprets a iload_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIload_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iload_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIload_3();
	}

	/**
	 * Interprets a lload_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLload_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lload_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLload_0();
	}

	/**
	 * Interprets a lload_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLload_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lload_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLload_1();
	}

	/**
	 * Interprets a lload_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLload_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lload_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLload_2();
	}

	/**
	 * Interprets a lload_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLload_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lload_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLload_3();
	}

	/**
	 * Interprets a fload_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFload_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fload_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFload_0();
	}

	/**
	 * Interprets a fload_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFload_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fload_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFload_1();
	}

	/**
	 * Interprets a fload_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFload_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fload_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFload_2();
	}

	/**
	 * Interprets a fload_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFload_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fload_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFload_3();
	}

	/**
	 * Interprets a dload_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDload_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dload_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDload_0();
	}

	/**
	 * Interprets a dload_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDload_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dload_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDload_1();
	}

	/**
	 * Interprets a dload_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDload_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dload_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDload_2();
	}

	/**
	 * Interprets a dload_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDload_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dload_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDload_3();
	}

	/**
	 * Interprets a aload_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAload_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aload_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAload_0();
	}

	/**
	 * Interprets a aload_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAload_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aload_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAload_1();
	}

	/**
	 * Interprets a aload_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAload_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aload_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAload_2();
	}

	/**
	 * Interprets a aload_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAload_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aload_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAload_3();
	}

	/**
	 * Interprets a iaload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iaload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIaload();
	}

	/**
	 * Interprets a laload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a laload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLaload();
	}

	/**
	 * Interprets a faload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a faload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFaload();
	}

	/**
	 * Interprets a daload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a daload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDaload();
	}

	/**
	 * Interprets a aaload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aaload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAaload();
	}

	/**
	 * Interprets a baload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretBaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a baload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateBaload();
	}

	/**
	 * Interprets a caload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretCaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a caload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateCaload();
	}

	/**
	 * Interprets a saload
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretSaload(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a saload :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateSaload();
	}

	/**
	 * Interprets a istore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIstore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a istore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateIstore(i);
	}

	/**
	 * Interprets a lstore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLstore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lstore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateLstore(i);
	}

	/**
	 * Interprets a fstore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFstore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fstore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateFstore(i);
	}

	/**
	 * Interprets a dstore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDstore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dstore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateDstore(i);
	}

	/**
	 * Interprets a astore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAstore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a astore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int i = (int)ci.byteAt(index+1);
		return world.evaluateAstore(i);
	}

	/**
	 * Interprets a istore_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIstore_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a istore_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIstore_0();
	}

	/**
	 * Interprets a istore_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIstore_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a istore_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIstore_1();
	}

	/**
	 * Interprets a istore_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIstore_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a istore_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIstore_2();
	}

	/**
	 * Interprets a istore_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIstore_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a istore_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIstore_3();
	}

	/**
	 * Interprets a lstore_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLstore_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lstore_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLstore_0();
	}

	/**
	 * Interprets a lstore_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLstore_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lstore_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLstore_1();
	}

	/**
	 * Interprets a lstore_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLstore_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lstore_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLstore_2();
	}

	/**
	 * Interprets a lstore_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLstore_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lstore_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLstore_3();
	}

	/**
	 * Interprets a fstore_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFstore_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fstore_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFstore_0();
	}

	/**
	 * Interprets a fstore_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFstore_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fstore_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFstore_1();
	}

	/**
	 * Interprets a fstore_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFstore_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fstore_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFstore_2();
	}

	/**
	 * Interprets a fstore_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFstore_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fstore_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFstore_3();
	}

	/**
	 * Interprets a dstore_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDstore_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dstore_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDstore_0();
	}

	/**
	 * Interprets a dstore_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDstore_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dstore_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDstore_1();
	}

	/**
	 * Interprets a dstore_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDstore_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dstore_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDstore_2();
	}

	/**
	 * Interprets a dstore_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDstore_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dstore_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDstore_3();
	}

	/**
	 * Interprets a astore_0
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAstore_0(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a astore_0 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAstore_0();
	}

	/**
	 * Interprets a astore_1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAstore_1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a astore_1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAstore_1();
	}

	/**
	 * Interprets a astore_2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAstore_2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a astore_2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAstore_2();
	}

	/**
	 * Interprets a astore_3
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAstore_3(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a astore_3 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAstore_3();
	}

	/**
	 * Interprets a iastore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iastore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIastore();
	}

	/**
	 * Interprets a lastore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lastore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLastore();
	}

	/**
	 * Interprets a fastore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fastore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFastore();
	}

	/**
	 * Interprets a dastore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dastore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDastore();
	}

	/**
	 * Interprets a aastore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a aastore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateAastore();
	}

	/**
	 * Interprets a bastore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretBastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a bastore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateBastore();
	}

	/**
	 * Interprets a castore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretCastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a castore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateCastore();
	}

	/**
	 * Interprets a sastore
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretSastore(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a sastore :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateSastore();
	}

	/**
	 * Interprets a pop
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretPop(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a pop :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluatePop();
	}

	/**
	 * Interprets a pop2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretPop2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a pop2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluatePop2();
	}

	/**
	 * Interprets a dup
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDup(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dup :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDup();
	}

	/**
	 * Interprets a dup_x1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDup_x1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dup_x1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDup_x1();
	}

	/**
	 * Interprets a dup_x2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDup_x2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dup_x2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDup_x2();
	}

	/**
	 * Interprets a dup2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDup2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dup2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDup2();
	}

	/**
	 * Interprets a dup2_x1
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDup2_x1(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dup2_x1 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDup2_x1();
	}

	/**
	 * Interprets a dup2_x2
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDup2_x2(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dup2_x2 :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDup2_x2();
	}

	/**
	 * Interprets a swap
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretSwap(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a swap :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateSwap();
	}

	/**
	 * Interprets a iadd
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIadd(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iadd :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIadd();
	}

	/**
	 * Interprets a ladd
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLadd(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ladd :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLadd();
	}

	/**
	 * Interprets a fadd
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFadd(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fadd :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFadd();
	}

	/**
	 * Interprets a dadd
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDadd(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dadd :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDadd();
	}

	/**
	 * Interprets a isub
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIsub(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a isub :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIsub();
	}

	/**
	 * Interprets a lsub
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLsub(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lsub :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLsub();
	}

	/**
	 * Interprets a fsub
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFsub(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fsub :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFsub();
	}

	/**
	 * Interprets a dsub
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDsub(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dsub :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDsub();
	}

	/**
	 * Interprets a imul
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretImul(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a imul :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateImul();
	}

	/**
	 * Interprets a lmul
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLmul(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lmul :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLmul();
	}

	/**
	 * Interprets a fmul
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFmul(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fmul :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFmul();
	}

	/**
	 * Interprets a dmul
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDmul(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dmul :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDmul();
	}

	/**
	 * Interprets a idiv
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIdiv(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a idiv :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIdiv();
	}

	/**
	 * Interprets a ldiv
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLdiv(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ldiv :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLdiv();
	}

	/**
	 * Interprets a fdiv
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFdiv(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fdiv :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFdiv();
	}

	/**
	 * Interprets a ddiv
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDdiv(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ddiv :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDdiv();
	}

	/**
	 * Interprets a irem
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIrem(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a irem :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIrem();
	}

	/**
	 * Interprets a lrem
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLrem(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lrem :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLrem();
	}

	/**
	 * Interprets a frem
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFrem(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a frem :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFrem();
	}

	/**
	 * Interprets a drem
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDrem(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a drem :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDrem();
	}

	/**
	 * Interprets a ineg
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIneg(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ineg :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIneg();
	}

	/**
	 * Interprets a lneg
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLneg(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lneg :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLneg();
	}

	/**
	 * Interprets a fneg
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFneg(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fneg :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFneg();
	}

	/**
	 * Interprets a dneg
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDneg(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dneg :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDneg();
	}

	/**
	 * Interprets a ishl
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIshl(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ishl :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIshl();
	}

	/**
	 * Interprets a lshl
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLshl(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lshl :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLshl();
	}

	/**
	 * Interprets a ishr
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIshr(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ishr :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIshr();
	}

	/**
	 * Interprets a lshr
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLshr(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lshr :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLshr();
	}

	/**
	 * Interprets a iushr
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIushr(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iushr :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIushr();
	}

	/**
	 * Interprets a lushr
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLushr(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lushr :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLushr();
	}

	/**
	 * Interprets a iand
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIand(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iand :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIand();
	}

	/**
	 * Interprets a land
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLand(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a land :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLand();
	}

	/**
	 * Interprets a ior
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIor(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ior :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIor();
	}

	/**
	 * Interprets a lor
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLor(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lor :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLor();
	}

	/**
	 * Interprets a ixor
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIxor(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ixor :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateIxor();
	}

	/**
	 * Interprets a lxor
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLxor(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lxor :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLxor();
	}

	/**
	 * Interprets a iinc
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIinc(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iinc :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int indexValue = ci.byteAt(index+1);
		int increment = ci.byteAt(index+2);

		return world.evaluateIinc(indexValue,increment);
	}

	/**
	 * Interprets a i2l
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretI2l(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a i2l :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateI2l();
	}

	/**
	 * Interprets a i2f
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretI2f(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a i2f :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateI2f();
	}

	/**
	 * Interprets a i2d
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretI2d(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a i2d :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateI2d();
	}

	/**
	 * Interprets a l2i
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretL2i(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a l2i :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateL2i();
	}

	/**
	 * Interprets a l2f
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretL2f(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a l2f :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateL2f();
	}

	/**
	 * Interprets a l2d
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretL2d(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a l2d :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateL2d();
	}

	/**
	 * Interprets a f2i
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretF2i(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a f2i :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateF2i();
	}

	/**
	 * Interprets a f2l
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretF2l(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a f2l :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateF2l();
	}

	/**
	 * Interprets a f2d
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretF2d(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a f2d :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateF2d();
	}

	/**
	 * Interprets a d2i
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretD2i(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a d2i :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateD2i();
	}

	/**
	 * Interprets a d2l
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretD2l(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a d2l :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateD2l();
	}

	/**
	 * Interprets a d2f
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretD2f(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a d2f :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateD2f();
	}

	/**
	 * Interprets a i2b
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretI2b(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a i2b :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateI2b();
	}

	/**
	 * Interprets a i2c
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretI2c(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a i2c :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateI2c();
	}

	/**
	 * Interprets a i2s
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretI2s(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a i2s :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateI2s();
	}

	/**
	 * Interprets a lcmp
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLcmp(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lcmp :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateLcmp();
	}

	/**
	 * Interprets a fcmpl
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFcmpl(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fcmpl :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFcmpl();
	}

	/**
	 * Interprets a fcmpg
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFcmpg(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a fcmpg :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateFcmpg();
	}

	/**
	 * Interprets a dcmpl
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDcmpl(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dcmpl :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDcmpl();
	}

	/**
	 * Interprets a dcmpg
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDcmpg(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dcmpg :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateDcmpg();
	}

	/**
	 * Interprets a ifeq
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIfeq(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ifeq :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIfeq();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIfeq();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;
	}

	/**
	 * Interprets a ifne
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIfne(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ifne :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIfne();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIfne();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;
	}

	/**
	 * Interprets a iflt
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIflt(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a iflt :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIflt();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIflt();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	}

	/**
	 * Interprets a ifge
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIfge(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ifge :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIfge();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIfge();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	}

	/**
	 * Interprets a ifgt
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIfgt(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ifgt :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIfgt();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIfgt();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	}

	/**
	 * Interprets a ifle
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIfle(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ifle :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIfle();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIfle();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}

	/**
	 * Interprets a if_icmpeq
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_icmpeq(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_icmpeq :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_icmpeq();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_icmpeq();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}

	/**
	 * Interprets a if_icmpne
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_icmpne(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_icmpne :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_icmpne();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_icmpne();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}
	/**
	 * Interprets a if_icmplt
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_icmplt(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_icmplt :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_icmplt();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_icmplt();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}
	/**
	 * Interprets a if_icmpge
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_icmpge(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_icmpge :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_icmpge();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_icmpge();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	 
	}
	/**
	 * Interprets a if_icmpgt
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_icmpgt(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_icmpgt :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_icmpgt();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_icmpgt();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}
	/**
	 * Interprets a if_icmple
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_icmple(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_icmple :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_icmple();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_icmple();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}
	/**
	 * Interprets a if_acmpeq
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_acmpeq(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_acmpeq :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_acmpeq();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_acmpeq();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}

	/**
	 * Interprets a if_acmpne
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIf_acmpne(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a if_acmpne :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIf_acmpne();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIf_acmpne();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;	
	}


	/**
	 * Interprets a goto
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretGoto(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a goto :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int gotoBranch = ci.s16bitAt(index+1);
		ci.move(index+gotoBranch);
		return world.evaluateGoto();
	}

	/**
	 * Interprets a jsr
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretJsr(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a jsr :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int subroutineAddress = ci.s16bitAt(index+1);
		this.returnIndexesForSubroutines.push(index+3);
		ci.move(subroutineAddress);
		return world.evaluateJsr();
	}

	/**
	 * Interprets a ret
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretRet(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ret :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		ci.move(this.returnIndexesForSubroutines.pop());
		return world.evaluateRet();
	}

	/**
	 * Interprets a tableswitch
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretTableswitch(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a tableswitch :"+Mnemonic.OPCODE[ci.byteAt(index)], this);

		int indexOfDefault;
		if (index%4==0) {
			indexOfDefault = index;
		}
		else {
			indexOfDefault=((index/4)+1)*4;
		}
		int defaut = ci.s32bitAt(indexOfDefault);
		int low = ci.s32bitAt(indexOfDefault+4);
		int high = ci.s32bitAt(indexOfDefault+8);
		JAIDebug.print("Interpreting tableswitch low: "+low+", high: "+high+", default: "+defaut, this, true);

		JAIWorld modifiedWorld = world.evaluateTableswitch();
		if (modifiedWorld.allBranchesNeedToBeEvaluated()) {
			for (int j = low; j<=high;j++) {
				JAIDebug.print("Launching branch "+j, this, true);
				interpretFrom(modifiedWorld, index+ci.s32bitAt(indexOfDefault+12+j*4)); 

			}
			interpretFrom(modifiedWorld, ci.s32bitAt(index+defaut)); 				
		}
		else {

			// otherwise, we execute only the indexes whose indexes are returned
			// by the world
			int []possibleBranches = modifiedWorld.getValuesOfBranchesToEvaluate();
			
			for (int b: possibleBranches) {
				JAIDebug.print("Possible branch: "+b, this, true);				
			}
			
			// we define an array to keep track of whether branches have been called
			boolean []hasBranchBeenCalled = new boolean[high-low+1];
			JAIDebug.print("Number of branches:  "+possibleBranches.length, this, true);

			// for all branches we will check that they have not been evaluated yet
			boolean hasDefaultBeenCalled = false;
			//we iterate through all possibilities
			
			for (int i = 0; i<possibleBranches.length;i++){
				int branchIndex = possibleBranches[i];
				// if the default has not been called and it refers to another branch
				if (!hasDefaultBeenCalled&&((branchIndex<low) ||(branchIndex>high))) {
					interpretFrom(modifiedWorld, index+defaut);					
					hasDefaultBeenCalled = true;				
				} else if (((branchIndex>=low) || (branchIndex<=high))&&!hasBranchBeenCalled[i]){
					// if it hasn't been evaluated yet, we launch it
					JAIDebug.print("Launching branch "+branchIndex, this, true);
					interpretFrom(modifiedWorld, index+ci.s32bitAt(indexOfDefault+12+i*4)); 
					hasBranchBeenCalled[i]=true;
				}

			}

		}
		return modifiedWorld;
	}

	/**
	 * Interprets a lookupswitch
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLookupswitch(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lookupswitch :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int indexOfDefault;
		// we first calculate the offset
		if (index%4==0) {
			indexOfDefault = index;
		}
		else {
			indexOfDefault=((index/4)+1)*4;
		}

		// then we read default and npairs
		int defaut = ci.s32bitAt(indexOfDefault);
		int npairs = ci.s32bitAt(indexOfDefault+4);

		JAIWorld modifiedWorld = world.evaluateLookupswitch();
		// if we need to evaluate all branches, we do that
		if (modifiedWorld.allBranchesNeedToBeEvaluated()) {
			for (int j = 0; j<npairs;j++) {
				JAIDebug.print("Launching branch "+j, this);
				interpretFrom(modifiedWorld, index+ci.s32bitAt(indexOfDefault+12+j*8)); 

			}
			// as well as the default
			interpretFrom(modifiedWorld, index+defaut); 				
		}
		else {
			// otherwise, we execute only the indexes whose indexes are returned
			// by the world
			int []possibleBranches = modifiedWorld.getValuesOfBranchesToEvaluate();
			// we define an array to keep track of whether branches have been called
			boolean []hasBranchBeenCalled = new boolean[npairs];

			// for all branches we will check that they have not been evaluated yet
			boolean hasDefaultBeenCalled = false;
			//we iterate through all possibilities
			for (int i = 0; i<possibleBranches.length;i++){
				int branchIndex = possibleBranches[i];
				boolean hasMatched=false;
				// we iterate through all possible branches
				for (int j = 0; j<npairs;j++) {
					// if it hasn't been evaluated yet, we launch it
					if (!hasBranchBeenCalled[j]&&ci.s32bitAt(indexOfDefault+8+j*8)==branchIndex) {
						JAIDebug.print("Launching branch "+j, this);
						interpretFrom(modifiedWorld, index+ci.s32bitAt(indexOfDefault+12+j*8)); 
						hasMatched=true;
						hasBranchBeenCalled[j]=true;
					}
				}
				// if it hadn't been evaluated wedo it
				if (!hasMatched&&!hasDefaultBeenCalled) {
					interpretFrom(modifiedWorld, index+defaut);
					hasDefaultBeenCalled=true;
				}
			}
		}
		return modifiedWorld;	
	}

	/**
	 * Interprets a ireturn
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIreturn(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ireturn :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world.evaluateIreturn();
		this.resultsOfExecutionToCollect.add(modifiedWorld);
		ci.move(ci.getCodeLength()+1);
		return modifiedWorld;
	}

	/**
	 * Interprets a lreturn
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretLreturn(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a lreturn :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world.evaluateLreturn();
		this.resultsOfExecutionToCollect.add(modifiedWorld);
		ci.move(ci.getCodeLength()+1);
		return modifiedWorld;
	}

	/**
	 * Interprets a freturn
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretFreturn(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a freturn :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world.evaluateFreturn();
		this.resultsOfExecutionToCollect.add(modifiedWorld);
		ci.move(ci.getCodeLength()+1);
		return modifiedWorld;
	}

	/**
	 * Interprets a dreturn
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretDreturn(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a dreturn :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world.evaluateDreturn();
		this.resultsOfExecutionToCollect.add(modifiedWorld);
		ci.move(ci.getCodeLength()+1);
		return modifiedWorld;
	}

	/**
	 * Interprets a areturn
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAreturn(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a areturn :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world.evaluateAreturn();
		this.resultsOfExecutionToCollect.add(modifiedWorld);
		ci.move(ci.getCodeLength()+1);
		return modifiedWorld;
	}

	/**
	 * Interprets a return
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretReturn(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a return :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world.evaluateReturn();
		this.resultsOfExecutionToCollect.add(modifiedWorld);
		ci.move(ci.getCodeLength()+1);
		return modifiedWorld;
	}

	/**
	 * Interprets a getstatic
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretGetstatic(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a getstatic :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int staticVariableIndex = ci.s16bitAt(index+1);
		return world.evaluateGetstatic(getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefClassName(staticVariableIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefName(staticVariableIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefType(staticVariableIndex));
	}

	/**
	 * Interprets a putstatic
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretPutstatic(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a putstatic :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int staticVariableIndex = ci.s16bitAt(index+1);
		return world.evaluatePutstatic(getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefClassName(staticVariableIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefName(staticVariableIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefType(staticVariableIndex));
	}

	/**
	 * Interprets a getfield
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretGetfield(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a getfield :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int fieldIndex = ci.s16bitAt(index+1);
		return world.evaluateGetfield(getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefClassName(fieldIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefName(fieldIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefType(fieldIndex));
	}

	/**
	 * Interprets a putfield
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretPutfield(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a putfield :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int fieldIndex = ci.s16bitAt(index+1);
		return world.evaluatePutfield(getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefClassName(fieldIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefName(fieldIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getFieldrefType(fieldIndex));
	}

	/**
	 * Interprets a invokevirtual
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretInvokevirtual(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a invokevirtual :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int methodIndex = ci.s16bitAt(index+1);
		JAIWorld modifiedWorld = world.evaluateInvokevirtual(getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefClassName(methodIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefName(methodIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefType(methodIndex));
		if (modifiedWorld.shouldInterpretMehtodCalls()) {
			//TODO
		}
		return modifiedWorld;	}

	/**
	 * Interprets a invokespecial
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretInvokespecial(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a invokespecial :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int methodIndex = ci.s16bitAt(index+1);
		JAIWorld modifiedWorld = world.evaluateInvokespecial(getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefClassName(methodIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefName(methodIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefType(methodIndex));
		if (modifiedWorld.shouldInterpretMehtodCalls()) {
			//TODO
		}
		return modifiedWorld;
	}

	/**
	 * Interprets a invokestatic
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretInvokestatic(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a invokestatic :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int methodIndex = ci.s16bitAt(index+1);
		JAIWorld modifiedWorld = world.evaluateInvokestatic(getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefClassName(methodIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefName(methodIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefType(methodIndex));
		if (modifiedWorld.shouldInterpretMehtodCalls()) {
			//TODO
		}
		return modifiedWorld;
	}

	/**
	 * Interprets a invokeinterface
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretInvokeinterface(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a invokeinterface :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int methodIndex = ci.s16bitAt(index+1);
		int argCount = (int) ci.byteAt(index+3);
		JAIWorld modifiedWorld = world.evaluateInvokeinterface(getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefClassName(methodIndex)+"/"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefName(methodIndex)+":"
				+getM().getDeclaringClass().getClassFile().getConstPool().getMethodrefType(methodIndex), argCount);
		if (modifiedWorld.shouldInterpretMehtodCalls()) {
			//TODO
		}
		return modifiedWorld;
	}

	/**
	 * Interprets a new
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretNew(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a new :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int classIndex = ci.s16bitAt(index+1);
		JAIWorld modifiedWorld = world.evaluateNew(getM().getDeclaringClass().getClassFile().getConstPool().getClassInfo(classIndex));
		return modifiedWorld;
	}

	/**
	 * Interprets a newarray
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretNewarray(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a newarray :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int typeIndex = (int)ci.byteAt(index+1);
		String typeName = "";
		switch(typeIndex) {
		case 4: 
			typeName = "Z";
			break;
		case 5: 
			typeName = "C";
			break;
		case 6: 
			typeName = "F";
			break;
		case 7: 
			typeName = "D";
			break;
		case 8: 
			typeName = "B";
			break;
		case 9: 
			typeName = "S";
			break;
		case 10: 
			typeName = "I";
			break;
		case 11: 
			typeName = "J";
			break;
		}
		JAIWorld modifiedWorld = world.evaluateNewarray(typeName);
		return modifiedWorld;
	}

	/**
	 * Interprets a anewarray
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAnewarray(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a anewarray :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int classIndex = ci.s16bitAt(index+1);
		JAIWorld modifiedWorld = world.evaluateAnewarray(getM().getDeclaringClass().getClassFile().getConstPool().getClassInfo(classIndex));
		return modifiedWorld;
	}

	/**
	 * Interprets a arraylength
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretArraylength(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a arraylength :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateArraylength();
	}

	/**
	 * Interprets a athrow
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretAthrow(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a athrow :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIWorld modifiedWorld = world.evaluateAthrow();
		this.resultsOfExecutionToCollect.add(modifiedWorld);
		ci.move(ci.getCodeLength()+1);
		return modifiedWorld;
	}

	/**
	 * Interprets a checkcast
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretCheckcast(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a checkcast :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int classIndex = ci.s16bitAt(index+1);
		JAIWorld modifiedWorld = world.evaluateCheckcast(getM().getDeclaringClass().getClassFile().getConstPool().getClassInfo(classIndex));
		return modifiedWorld;
	}

	/**
	 * Interprets a instanceof
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretInstanceof(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a instanceof :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int classIndex = ci.s16bitAt(index+1);
		JAIWorld modifiedWorld = world.evaluateInstanceof(getM().getDeclaringClass().getClassFile().getConstPool().getClassInfo(classIndex));
		return modifiedWorld;
	}

	/**
	 * Interprets a monitorenter
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretMonitorenter(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a monitorenter :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateMonitorenter();
	}

	/**
	 * Interprets a monitorexit
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretMonitorexit(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a monitorexit :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		return world.evaluateMonitorexit();
	}

	/**
	 * Interprets a wide
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretWide(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a wide :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		JAIDebug.print("Interpreting a primitive type operation (and, mul...) :"+Mnemonic.OPCODE[ci.byteAt(index)], this);		
		JAIWorld modifiedWorld = world.evaluateWide();
		int widerIndex = ci.s16bitAt(index+2);
		
		switch (ci.byteAt(index+1)) {
		case Opcode.IINC:
			int constantValue = ci.s16bitAt(index+4);
			modifiedWorld = modifiedWorld.evaluateIinc(widerIndex, constantValue);
			break;
		case Opcode.ILOAD: 
			modifiedWorld = modifiedWorld.evaluateIload(widerIndex);
			break;
		case Opcode.FLOAD: 
			modifiedWorld = modifiedWorld.evaluateFload(widerIndex);
			break;
		case Opcode.ALOAD: 
			modifiedWorld = modifiedWorld.evaluateAload(widerIndex);
			break;
		case Opcode.LLOAD: 
			modifiedWorld = modifiedWorld.evaluateLload(widerIndex);
			break;
		case Opcode.DLOAD: 
			modifiedWorld = modifiedWorld.evaluateDload(widerIndex);
			break;
		case Opcode.ISTORE: 
			modifiedWorld = modifiedWorld.evaluateIstore(widerIndex);
			break;
		case Opcode.FSTORE: 
			modifiedWorld = modifiedWorld.evaluateFstore(widerIndex);
			break;
		case Opcode.ASTORE: 
			modifiedWorld = modifiedWorld.evaluateAstore(widerIndex);
			break;
		case Opcode.LSTORE: 
			modifiedWorld = modifiedWorld.evaluateLstore(widerIndex);
			break;
		case Opcode.DSTORE: 
			modifiedWorld = modifiedWorld.evaluateDstore(widerIndex);
			break;
		case Opcode.RET: 
			modifiedWorld = modifiedWorld.evaluateRet();
			break;
		}
		return modifiedWorld;
	}

	/**
	 * Interprets a multianewarray
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretMultianewarray(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a multianewarray :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int classIndex = ci.s16bitAt(index+1);
		int dimensions = (int)ci.byteAt(index+3);
		JAIWorld modifiedWorld = world.evaluateMultianewarray(getM().getDeclaringClass().getClassFile().getConstPool().getClassInfo(classIndex),dimensions);
		return modifiedWorld;
	}

	/**
	 * Interprets a ifnull
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIfnull(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ifnull :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIfnull();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIfnull();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;		
	}

	/**
	 * Interprets a ifnonnull
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretIfnonnull(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a ifnonnull :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int thenBranch = ci.s16bitAt(index+1);
		if (thenBranch<0) {
			// we are in a loop we have to continue on that path
			JAIWorld modifiedWorld = world.evaluateIfnonnull();
			// If we have the right to loop, we set the next address for this code iterator
			if (modifiedWorld.isLoopingValid()) {
				ci.move(index+thenBranch);
			}
			return modifiedWorld;
		}
		// we are not in a loop we have two branches
		JAIWorld modifiedWorld = world.evaluateIfnonnull();

		if (!modifiedWorld.allBranchesNeedToBeEvaluated()) {
			// if we do not need to evaluate both branches
			if (modifiedWorld.shouldEvaluateThen()) {
				ci.move(index+thenBranch);
			}
		} else {
			// if we need to evaluate both branches, we evaluate the then (the rest will proceed on its own)
			// we will collect the result in resultsOfExecutionToCollect vector.
			interpretFrom(modifiedWorld, index+thenBranch); 
		}
		return modifiedWorld;		}

	/**
	 * Interprets a goto_w
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretGoto_w(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a goto_w :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int gotoBranch = ci.s32bitAt(index+1);
		ci.move(index+gotoBranch);
		return world.evaluateGoto_w();	
	}

	/**
	 * Interprets a jsr_w
	 * 
	 * @param world the abstract world.
	 * @param ci the code iterator.
	 * @param index the index in the bytecode.
	 * @return the new state of abstract variables.
	 */	
	public JAIWorld interpretJsr_w(JAIWorld world, CodeIterator ci, int index) {
		JAIDebug.print("Interpreting a jsr_w :"+Mnemonic.OPCODE[ci.byteAt(index)], this);
		int subroutineAddress = ci.s32bitAt(index+1);
		this.returnIndexesForSubroutines.push(index+3);
		ci.move(subroutineAddress);
		return world.evaluateJsr_w();	
	}



}
