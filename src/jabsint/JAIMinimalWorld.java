package jabsint;

import java.util.HashMap;

/**
 * This class represents a minimal world that provides an implementation 
 * for a stack and variables.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Aug 16, 2011
 *
 */
@SuppressWarnings("rawtypes")
public class JAIMinimalWorld extends JAIWorld {

	/**
	 * The stack associated to this execution.
	 */
	public JAIStack stack = new JAIStack();

	/**
	 * The list of object variables (fields).
	 */
	public HashMap<String,JAIAbstractValue> fields = new HashMap<String,JAIAbstractValue>();

	/**
	 * The list of class variables (static fields).
	 */
	public HashMap<String,JAIAbstractValue> staticFields = new HashMap<String,JAIAbstractValue>();

	/**
	 * The list of local variables (of the current frame).
	 */
	public HashMap<Integer,JAIAbstractValue> localVariables = new HashMap<Integer,JAIAbstractValue>();

	/**
	 * The factory that will create abstract values.
	 */
	public JAIAbstractValuesFactory factory = null;

	/**
	 * A simple, empty initializer.
	 * 
	 */
	public JAIMinimalWorld(JAIAbstractValuesFactory factory) {
		this.factory=factory;
	}

	/**
	 * simple boolean to know if all branches need to be evaluated. 
	 */
	private boolean evaluateAll = true;

	/**
	 * Checks whether both branches of the next "if" need to be evaluated. By default the answer is yes.
	 * 
	 * @return true if they need, false otherwise.
	 */
	public boolean allBranchesNeedToBeEvaluated() {
		return evaluateThen&&evaluateElse;
	}


	/**
	 * simple boolean to know if the then branch needs to be evaluated. 
	 */
	private boolean evaluateThen = true;

	/**
	 * simple boolean to know if the else branch needs to be evaluated. 
	 */
	private boolean evaluateElse = true;

	/**
	 * In case both branches of the next "if" do not need to be evaluated, checks whether 
	 * it is with the "then" (or the "else"). Default value is false.
	 * 
	 * @return true if "then" needs to be evaluated, flase otherwise.
	 */
	public boolean shouldEvaluateThen() {
		return evaluateThen;
	}


	/**
	 * Sets the value for evaluateThen
	 * 
	 * @param evaluateThen true to evaluate the then branch, false to evaluate else
	 */
	public void setEvaluateThen(boolean evaluateThen) {
		this.evaluateThen = evaluateThen;
	}
	
	/**
	 * Sets the value for evaluateThen
	 * 
	 * @param evaluateThen true to evaluate the then branch, false to evaluate else
	 */
	public void setEvaluateElse(boolean evaluateThen) {
		this.evaluateElse = evaluateThen;
	}

	/**
	 * Gets a local variable
	 * 
	 * @param number the number of the local variable
	 * @return the local variable
	 */
	public JAIAbstractValue getLocalVariable(int number) {
		JAIAbstractValue v = localVariables.get(number);
		if (v==null) {
			v=factory.getLocalVariableAbstractValue(number);
			localVariables.put(number, v);
		}
		return v;
	}

	/**
	 * 
	 * Sets the value of a local variable
	 * 
	 * @param number the number of the local variable
	 * @param value its value
	 */
	public void setLocalVariable(int number, JAIAbstractValue value) {
		localVariables.put(number,value);
	}

	/**
	 * A simple constructor that clones the world passed as a parameter.
	 * 
	 * @param w the world to clone.
	 */
	public JAIMinimalWorld(JAIMinimalWorld w) {
		super();
		this.stack = w.getCurrentStack().clone();
		this.factory = w.getFactory().clone();

		// we do a deep clone of all values in the lists
		HashMap<String, JAIAbstractValue> otherFields = w.getFields();
		HashMap<String, JAIAbstractValue> otherStaticFields = w.getStaticFields();
		HashMap<Integer, JAIAbstractValue> otherLocalVariables = w.getLocalVariables();
		for (String v: otherFields.keySet()) {
			this.fields.put(v, otherFields.get(v).clone());
		}
		for (String v: otherStaticFields.keySet()) {
			this.staticFields.put(v, otherStaticFields.get(v).clone());
		}
		for (Integer i: otherLocalVariables.keySet()) {
			this.localVariables.put(i, otherLocalVariables.get(i).clone());
		}
	}




	/**
	 * Simple getter for the stack
	 * 
	 * @return the currentStack
	 */
	public JAIStack getCurrentStack() {
		return stack;
	}

	/**
	 * Simple setter for the stack.
	 * 
	 * @param currentStack the currentStack to set
	 */
	public void setCurrentStack(JAIStack currentStack) {
		this.stack = currentStack;
	}

	/**
	 * Simple getter for the fields.
	 * 
	 * @return the fields
	 */
	public HashMap<String, JAIAbstractValue> getFields() {
		return fields;
	}

	/**
	 * Simple setter for the fields.
	 * 
	 * @param fields the fields to set
	 */
	public void setFields(HashMap<String, JAIAbstractValue> fields) {
		this.fields = fields;
	}

	/**
	 * Simple getter for the static fields.
	 * 
	 * @return the staticFields
	 */
	public HashMap<String, JAIAbstractValue> getStaticFields() {
		return staticFields;
	}

	/**
	 * Simple setter for the static fields.

	 * @param staticFields the staticFields to set
	 */
	public void setStaticFields(HashMap<String, JAIAbstractValue> staticFields) {
		this.staticFields = staticFields;
	}

	/**
	 * Simple getter for the local variables.
	 *
	 * @return the localVariables
	 */
	public HashMap<Integer, JAIAbstractValue> getLocalVariables() {
		return localVariables;
	}

	/**
	 * Simple setter for the local variables.
	 * 
	 * @param localVariables the localVariables to set
	 */
	public void setLocalVariables(HashMap<Integer, JAIAbstractValue> localVariables) {
		this.localVariables = localVariables;
	}
	/**
	 * Simple getter for the factory.
	 * 
	 * @return the factory
	 */
	public JAIAbstractValuesFactory getFactory() {
		return factory;
	}


	/**
	 * Simple setter for the factory.
	 * 
	 * @param factory the factory to set
	 */
	public void setFactory(JAIAbstractValuesFactory factory) {
		this.factory = factory;
	}



	/**
	 * Evaluates a aconst_null
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAconst_null() {
		stack.push(factory.generateNullAbstractValue());
		JAIDebug.print("Pushed null on the stack", this);
		return this;
	}

	/**
	 * Evaluates a iconst_m1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_m1() {
		stack.push(factory.generateIntegerAbstractValue(-1));		
		JAIDebug.print("Pushed -1 on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_0() {
		stack.push(factory.generateIntegerAbstractValue(0));		
		JAIDebug.print("Pushed 0 on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_1() {
		stack.push(factory.generateIntegerAbstractValue(1));		
		JAIDebug.print("Pushed 1 on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_2() {
		stack.push(factory.generateIntegerAbstractValue(2));		
		JAIDebug.print("Pushed 2 on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_3() {
		stack.push(factory.generateIntegerAbstractValue(3));		
		JAIDebug.print("Pushed -3 on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_4
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_4() {
		stack.push(factory.generateIntegerAbstractValue(4));		
		JAIDebug.print("Pushed 4 on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_5
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_5() {
		stack.push(factory.generateIntegerAbstractValue(5));		
		JAIDebug.print("Pushed 5 on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a lconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLconst_0() {
		stack.push(factory.generateLongAbstractValue(0L));		
		JAIDebug.print("Pushed 0L on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a lconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLconst_1() {
		stack.push(factory.generateLongAbstractValue(1L));		
		JAIDebug.print("Pushed 1L on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a fconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFconst_0() {
		stack.push(factory.generateFloatAbstractValue(0.0f));		
		JAIDebug.print("Pushed 0.0f on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a fconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFconst_1() {
		stack.push(factory.generateFloatAbstractValue(1.0f));		
		JAIDebug.print("Pushed 1.0f on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a fconst_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFconst_2() {
		stack.push(factory.generateFloatAbstractValue(2.0f));		
		JAIDebug.print("Pushed 2.0f on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a dconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDconst_0() {
		stack.push(factory.generateDoubleAbstractValue(0.0d));		
		JAIDebug.print("Pushed 0.0d on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a dconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDconst_1() {
		stack.push(factory.generateDoubleAbstractValue(1.0d));		
		JAIDebug.print("Pushed 1.0d on the stack ", this);
		return this;
	}

	/**
	 * Evaluates a bipush
	 * @param b the byte pushed on the stack
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateBipush(byte b) {
		stack.push(factory.generateIntegerAbstractValue((int)b));		
		JAIDebug.print("Pushed byte "+b+" on the stack as an integer", this);
		return this;
	}

	/**
	 * Evaluates a sipush
	 * @param s the short pushed on the stack.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSipush(short s) {
		stack.push(factory.generateShortAbstractValue(s));		
		JAIDebug.print("Pushed short "+s+" on the stack as a short", this);
		return this;
	}

	/**
	 * Evaluates a ldc
	 * @param o the ldc value there.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdc(Object o) {
		if (o instanceof Integer) {
			stack.push(factory.generateIntegerAbstractValue(((Integer)o).intValue()));					
			JAIDebug.print("Pushed constant integer "+o+" on the stack as an integer", this);
		} else {
			if (o instanceof Float) {
				stack.push(factory.generateFloatAbstractValue(((Float)o).floatValue()));					
				JAIDebug.print("Pushed constant float "+o+" on the stack as a float", this);
			} else {
				if (o instanceof String) {
					stack.push(factory.generateStringAbstractValue((String)o));					
					JAIDebug.print("Pushed constant String \""+o+"\" on the stack as a String", this);
				} else {
					throw new JAIWorldException("Wrong ldc constant type: "+o.getClass()+"expected int, float or String");
				}

			}
		}
		return this;
	}

	/**
	 * Evaluates a ldc_w
	 * @param o the ldc value there.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdc_w(Object o) {
		this.evaluateLdc(o);
		return this;
	}

	/**
	 * Evaluates a ldc2_w
	 * @param o the ldc value there.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdc2_w(Object o) {
		if (o instanceof Long) {
			stack.push(factory.generateLongAbstractValue(((Long)o).longValue()));					
			JAIDebug.print("Pushed constant long "+o+" on the stack as a long", this);
		} else {
			if (o instanceof Double) {
				stack.push(factory.generateDoubleAbstractValue(((Double)o).doubleValue()));					
				JAIDebug.print("Pushed constant double "+o+" on the stack as a double", this);
			} else {
				throw new JAIWorldException("Wrong ldc constant type: "+o.getClass()+"expected int, float or String");
			}
		}
		return this;
	}


	/**
	 * Evaluates a iload
	 * @param i the index of the local variable to load
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload(int i) {
		stack.push(getLocalVariable(i));
		JAIDebug.print("Loaded from local variable "+i+" on the stack (should contain an int)", this);		
		return this;
	}

	/**
	 * Evaluates a lload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload(int i) {
		stack.push(getLocalVariable(i));
		JAIDebug.print("Loaded from local variable "+i+" on the stack (should contain a long)", this);		
		return this;
	}

	/**
	 * Evaluates a fload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload(int i) {
		stack.push(getLocalVariable(i));
		JAIDebug.print("Loaded from local variable "+i+" on the stack (should contain a float)", this);		
		return this;
	}

	/**
	 * Evaluates a dload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload(int i) {
		stack.push(getLocalVariable(i));
		JAIDebug.print("Loaded from local variable "+i+" on the stack (should contain a double)", this);		
		return this;
	}

	/**
	 * Evaluates a aload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload(int i) {
		stack.push(getLocalVariable(i));
		JAIDebug.print("Pushed reference local variable "+i+"on the stack", this);
		return this;
	}

	/**
	 * Evaluates a iload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_0() {
		this.evaluateIload(0);
		return this;
	}

	/**
	 * Evaluates a iload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_1() {
		this.evaluateIload(1);
		return this;
	}

	/**
	 * Evaluates a iload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_2() {
		this.evaluateIload(2);
		return this;
	}

	/**
	 * Evaluates a iload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_3() {
		this.evaluateIload(3);
		return this;
	}

	/**
	 * Evaluates a lload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_0() {
		this.evaluateLload(0);
		return this;
	}

	/**
	 * Evaluates a lload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_1() {
		this.evaluateLload(1);
		return this;
	}

	/**
	 * Evaluates a lload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_2() {
		this.evaluateLload(2);
		return this;
	}

	/**
	 * Evaluates a lload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_3() {
		this.evaluateLload(3);
		return this;
	}

	/**
	 * Evaluates a fload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_0() {
		this.evaluateFload(0);
		return this;
	}

	/**
	 * Evaluates a fload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_1() {
		this.evaluateFload(1);
		return this;
	}

	/**
	 * Evaluates a fload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_2() {
		this.evaluateFload(2);
		return this;
	}

	/**
	 * Evaluates a fload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_3() {
		this.evaluateFload(3);
		return this;
	}

	/**
	 * Evaluates a dload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_0() {
		this.evaluateDload(0);
		return this;
	}

	/**
	 * Evaluates a dload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_1() {
		this.evaluateDload(1);
		return this;
	}

	/**
	 * Evaluates a dload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_2() {
		this.evaluateDload(2);
		return this;
	}

	/**
	 * Evaluates a dload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_3() {
		this.evaluateDload(3);
		return this;
	}

	/**
	 * Evaluates a aload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_0() {
		this.evaluateAload(0);
		return this;
	}

	/**
	 * Evaluates a aload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_1() {
		this.evaluateAload(1);
		return this;
	}

	/**
	 * Evaluates a aload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_2() {
		this.evaluateAload(2);
		return this;
	}

	/**
	 * Evaluates a aload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_3() {
		this.evaluateAload(3);
		return this;
	}

	/**
	 * Evaluates a iaload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed integer value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a laload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed long value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a faload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed float value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a daload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed double value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a aaload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed reference value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a baload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateBaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed byte value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a caload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateCaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed char value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a saload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSaload() {
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		JAIAbstractValue result = array.loadFromArrayref(index);
		stack.push(result);
		JAIDebug.print("Pushed short value at "+index+" from array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a istore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore(int i) {
		setLocalVariable(i,stack.pop());
		JAIDebug.print("Stored int in local variable "+i, this);
		return this;
	}

	/**
	 * Evaluates a lstore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore(int i) {
		setLocalVariable(i,stack.pop());
		JAIDebug.print("Stored long in local variable "+i, this);
		return this;
	}

	/**
	 * Evaluates a fstore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore(int i) {
		setLocalVariable(i,stack.pop());
		JAIDebug.print("Stored float in local variable "+i, this);
		return this;
	}

	/**
	 * Evaluates a dstore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore(int i) {
		setLocalVariable(i,stack.pop());
		JAIDebug.print("Stored double in local variable "+i, this);
		return this;
	}

	/**
	 * Evaluates a astore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore(int i) {
		setLocalVariable(i,stack.pop());
		JAIDebug.print("Stored reference in local variable "+i, this);
		return this;
	}

	/**
	 * Evaluates a istore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_0() {
		this.evaluateIstore(0);
		return this;
	}

	/**
	 * Evaluates a istore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_1() {
		this.evaluateIstore(1);
		return this;
	}

	/**
	 * Evaluates a istore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_2() {
		this.evaluateIstore(2);
		return this;
	}

	/**
	 * Evaluates a istore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_3() {
		this.evaluateIstore(3);
		return this;
	}

	/**
	 * Evaluates a lstore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_0() {
		this.evaluateLstore(0);
		return this;
	}

	/**
	 * Evaluates a lstore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_1() {
		this.evaluateLstore(1);
		return this;
	}

	/**
	 * Evaluates a lstore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_2() {
		this.evaluateLstore(2);
		return this;
	}

	/**
	 * Evaluates a lstore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_3() {
		this.evaluateLstore(3);
		return this;
	}

	/**
	 * Evaluates a fstore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_0() {
		this.evaluateFstore(0);
		return this;
	}

	/**
	 * Evaluates a fstore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_1() {
		this.evaluateFstore(1);
		return this;
	}

	/**
	 * Evaluates a fstore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_2() {
		this.evaluateFstore(2);
		return this;
	}

	/**
	 * Evaluates a fstore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_3() {
		this.evaluateFstore(3);
		return this;
	}

	/**
	 * Evaluates a dstore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_0() {
		this.evaluateDstore(0);
		return this;
	}

	/**
	 * Evaluates a dstore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_1() {
		this.evaluateDstore(1);
		return this;
	}

	/**
	 * Evaluates a dstore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_2() {
		this.evaluateDstore(2);
		return this;
	}

	/**
	 * Evaluates a dstore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_3() {
		this.evaluateDstore(3);
		return this;
	}

	/**
	 * Evaluates a astore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_0() {
		this.evaluateAstore(0);
		return this;
	}

	/**
	 * Evaluates a astore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_1() {
		this.evaluateAstore(1);
		return this;
	}

	/**
	 * Evaluates a astore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_2() {
		this.evaluateAstore(2);
		return this;
	}

	/**
	 * Evaluates a astore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_3() {
		this.evaluateAstore(3);
		return this;
	}

	/**
	 * Evaluates a iastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored int value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a lastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored long value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a fastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored float value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a dastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored double value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a aastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored reference value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a bastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateBastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored byte value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a castore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateCastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored char value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a sastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSastore() {
		JAIAbstractValue value = stack.pop();
		JAIAbstractValue index = stack.pop();
		JAIAbstractValue array = stack.pop();
		array.storeInArrayref(index,value);
		JAIDebug.print("Stored short value at "+index+" in array on the stack", this);
		return this;
	}

	/**
	 * Evaluates a pop
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePop() {
		stack.pop();
		JAIDebug.print("Evaluated a pop ", this);
		return this;
	}

	/**
	 * Evaluates a pop2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePop2() {
		stack.pop2();
		JAIDebug.print("Evaluated a pop2 ", this);
		return this;
	}

	/**
	 * Evaluates a dup
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup() {
		stack.dup();
		JAIDebug.print("Evaluated a dup ", this);
		return this;
	}

	/**
	 * Evaluates a dup_x1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup_x1() {
		stack.dup_x1();
		JAIDebug.print("Evaluated a dup_x1 ", this);
		return this;
	}

	/**
	 * Evaluates a dup_x2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup_x2() {
		stack.dup_x1();
		JAIDebug.print("Evaluated a dup_x2 ", this);
		return this;
	}

	/**
	 * Evaluates a dup2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup2() {
		stack.dup2();
		JAIDebug.print("Evaluated a dup2 ", this);
		return this;
	}

	/**
	 * Evaluates a dup2_x1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup2_x1() {
		stack.dup2_x1();
		JAIDebug.print("Evaluated a dup2_x1 ", this);
		return this;
	}

	/**
	 * Evaluates a dup2_x2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup2_x2() {
		stack.dup2_x2();
		JAIDebug.print("Evaluated a dup2_x2 ", this);
		return this;
	}

	/**
	 * Evaluates a swap
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSwap() {
		stack.swap();
		JAIDebug.print("Evaluated a swap ", this);
		return this;
	}

	/**
	 * Evaluates a iadd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIadd() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerAdd(v2));
		JAIDebug.print("Evaluated a iadd ", this);
		return this;
	}

	/**
	 * Evaluates a ladd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLadd() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longAdd(v2));
		JAIDebug.print("Evaluated an ladd ", this);
		return this;
	}

	/**
	 * Evaluates a fadd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFadd() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.floatAdd(v2));
		JAIDebug.print("Evaluated a fadd ", this);
		return this;
	}

	/**
	 * Evaluates a dadd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDadd() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.doubleAdd(v2));
		JAIDebug.print("Evaluated a dadd ", this);
		return this;
	}

	/**
	 * Evaluates a isub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIsub() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerSub(v2));
		JAIDebug.print("Evaluated a isub ", this);
		return this;
	}

	/**
	 * Evaluates a lsub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLsub() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longSub(v2));
		JAIDebug.print("Evaluated a lsub ", this);
		return this;
	}

	/**
	 * Evaluates a fsub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFsub() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.floatSub(v2));
		JAIDebug.print("Evaluated a fsub ", this);
		return this;
	}

	/**
	 * Evaluates a dsub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDsub() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.doubleSub(v2));
		JAIDebug.print("Evaluated a dsub ", this);
		return this;
	}

	/**
	 * Evaluates a imul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateImul() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerMul(v2));
		JAIDebug.print("Evaluated a imul ", this);
		return this;
	}

	/**
	 * Evaluates a lmul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLmul() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longMul(v2));
		JAIDebug.print("Evaluated a lmul ", this);
		return this;
	}

	/**
	 * Evaluates a fmul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFmul() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.floatMul(v2));
		JAIDebug.print("Evaluated a fmul ", this);
		return this;
	}

	/**
	 * Evaluates a dmul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDmul() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.doubleMul(v2));
		JAIDebug.print("Evaluated a dmul ", this);
		return this;
	}

	/**
	 * Evaluates a idiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIdiv() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerDiv(v2));
		JAIDebug.print("Evaluated a idiv ", this);
		return this;
	}

	/**
	 * Evaluates a ldiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdiv() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longDiv(v2));
		JAIDebug.print("Evaluated a ldiv ", this);
		return this;
	}

	/**
	 * Evaluates a fdiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFdiv() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.floatDiv(v2));
		JAIDebug.print("Evaluated a fdiv ", this);
		return this;
	}

	/**
	 * Evaluates a ddiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDdiv() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.doubleDiv(v2));
		JAIDebug.print("Evaluated a ddiv ", this);
		return this;
	}

	/**
	 * Evaluates a irem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIrem() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerRemainder(v2));
		JAIDebug.print("Evaluated a irem ", this);
		return this;
	}

	/**
	 * Evaluates a lrem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLrem() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longRemainder(v2));
		JAIDebug.print("Evaluated a lrem ", this);
		return this;
	}

	/**
	 * Evaluates a frem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFrem() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.floatRemainder(v2));
		JAIDebug.print("Evaluated a frem ", this);
		return this;
	}

	/**
	 * Evaluates a drem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDrem() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.doubleRemainder(v2));
		JAIDebug.print("Evaluated a drem ", this);
		return this;
	}

	/**
	 * Evaluates a ineg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIneg() {
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.integerNegation());
		JAIDebug.print("Evaluated a ineg ", this);
		return this;
	}

	/**
	 * Evaluates a lneg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLneg() {
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.longNegation());
		JAIDebug.print("Evaluated a lneg ", this);
		return this;
	}

	/**
	 * Evaluates a fneg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFneg() {
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.floatNegation());
		JAIDebug.print("Evaluated a fneg ", this);
		return this;
	}

	/**
	 * Evaluates a dneg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDneg() {
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.doubleNegation());
		JAIDebug.print("Evaluated a dneg ", this);
		return this;
	}

	/**
	 * Evaluates a ishl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIshl() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.integerShiftLeft(v2));
		JAIDebug.print("Evaluated a ishl ", this);
		return this;
	}

	/**
	 * Evaluates a lshl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLshl() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.longShiftLeft(v2));
		JAIDebug.print("Evaluated a lshl ", this);
		return this;
	}

	/**
	 * Evaluates a ishr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIshr() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.integerShiftRight(v2));
		JAIDebug.print("Evaluated a ishr ", this);
		return this;
	}

	/**
	 * Evaluates a lshr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLshr() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.longShiftRight(v2));
		JAIDebug.print("Evaluated a lshr ", this);
		return this;
	}

	/**
	 * Evaluates a iushr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIushr() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.integerLogicalShiftRight(v2));
		JAIDebug.print("Evaluated a iushr ", this);
		return this;
	}

	/**
	 * Evaluates a lushr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLushr() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.longLogicalShiftRight(v2));
		JAIDebug.print("Evaluated a lushr ", this);
		return this;
	}

	/**
	 * Evaluates a iand
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIand() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerAnd(v2));
		JAIDebug.print("Evaluated a iand ", this);
		return this;
	}

	/**
	 * Evaluates a land
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLand() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longAnd(v2));
		JAIDebug.print("Evaluated a land ", this);
		return this;
	}

	/**
	 * Evaluates a ior
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIor() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerOr(v2));
		JAIDebug.print("Evaluated a ior ", this);
		return this;
	}

	/**
	 * Evaluates a lor
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLor() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longOr(v2));
		JAIDebug.print("Evaluated a lor ", this);
		return this;
	}

	/**
	 * Evaluates a ixor
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIxor() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.integerXor(v2));
		JAIDebug.print("Evaluated a ixor ", this);
		return this;
	}

	/**
	 * Evaluates a lxor
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLxor() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		stack.push(v1.longXor(v2));
		JAIDebug.print("Evaluated a lxor ", this);
		return this;
	}

	/**
	 * Evaluates a iinc
	 * @param i the index of the local variable.
	 * @param increment the amount to increment the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIinc(int i, int increment) {
		JAIAbstractValue v1 = getLocalVariable(i);
		JAIAbstractValue v2 = factory.generateIntegerAbstractValue(increment);		
		JAIAbstractValue v3 = v1.integerAdd(v2);
		setLocalVariable(i,v3);
		JAIDebug.print("Evaluated a iinc ", this);
		return this;
	}

	/**
	 * Evaluates a i2l
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2l() {
		stack.push(stack.pop().integer2Long());
		JAIDebug.print("Evaluated a i2l ", this);
		return this;
	}

	/**
	 * Evaluates a i2f
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2f() {
		stack.push(stack.pop().integer2Float());
		JAIDebug.print("Evaluated a i2f ", this);
		return this;
	}

	/**
	 * Evaluates a i2d
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2d() {
		stack.push(stack.pop().integer2Double());
		JAIDebug.print("Evaluated a i2d ", this);
		return this;
	}

	/**
	 * Evaluates a l2i
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateL2i() {
		stack.push(stack.pop().long2Integer());
		JAIDebug.print("Evaluated a l2i ", this);
		return this;
	}

	/**
	 * Evaluates a l2f
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateL2f() {
		stack.push(stack.pop().long2Float());
		JAIDebug.print("Evaluated a l2f ", this);
		return this;
	}

	/**
	 * Evaluates a l2d
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateL2d() {
		stack.push(stack.pop().long2Double());
		JAIDebug.print("Evaluated a l2d ", this);
		return this;
	}

	/**
	 * Evaluates a f2i
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateF2i() {
		stack.push(stack.pop().float2Integer());
		JAIDebug.print("Evaluated a f2i ", this);
		return this;
	}

	/**
	 * Evaluates a f2l
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateF2l() {
		stack.push(stack.pop().float2Long());
		JAIDebug.print("Evaluate a f2l ", this);
		return this;
	}

	/**
	 * Evaluates a f2d
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateF2d() {
		stack.push(stack.pop().float2Double());
		JAIDebug.print("Evaluated a f2d ", this);
		return this;
	}

	/**
	 * Evaluates a d2i
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateD2i() {
		stack.push(stack.pop().double2Integer());
		JAIDebug.print("Evaluated a d2i ", this);
		return this;
	}

	/**
	 * Evaluates a d2l
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateD2l() {
		stack.push(stack.pop().double2Long());
		JAIDebug.print("Evaluated a d2l ", this);
		return this;
	}

	/**
	 * Evaluates a d2f
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateD2f() {
		stack.push(stack.pop().double2Float());
		JAIDebug.print("Evaluated a d2f ", this);
		return this;
	}

	/**
	 * Evaluates a i2b
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2b() {
		stack.push(stack.pop().integer2Byte());
		JAIDebug.print("Evaluated a i2b ", this);
		return this;
	}

	/**
	 * Evaluates a i2c
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2c() {
		stack.push(stack.pop().integer2Character());
		JAIDebug.print("Evaluated a i2c ", this);
		return this;
	}

	/**
	 * Evaluates a i2s
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2s() {
		stack.push(stack.pop().integer2Short());
		JAIDebug.print("Evaluated a i2s ", this);
		return this;
	}

	/**
	 * Evaluates a lcmp
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLcmp() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.longCompare(v2));
		JAIDebug.print("Evaluated a lcmp ", this);
		return this;
	}

	/**
	 * Evaluates a fcmpl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFcmpl() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.floatCompareL(v2));
		JAIDebug.print("Evaluated a fcmpl ", this);
		return this;
	}

	/**
	 * Evaluates a fcmpg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFcmpg() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.floatCompareG(v2));
		JAIDebug.print("Evaluated a fcmpg ", this);
		return this;
	}

	/**
	 * Evaluates a dcmpl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDcmpl() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.doubleCompareL(v2));
		JAIDebug.print("Evaluated a dcmpl ", this);
		return this;
	}

	/**
	 * Evaluates a dcmpg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDcmpg() {
		JAIAbstractValue v2 = stack.pop();
		JAIAbstractValue v1 = stack.pop();
		stack.push(v1.doubleCompareG(v2));
		JAIDebug.print("Evaluated a dcmpg ", this);
		return this;
	}

	/**
	 * Evaluates a ifeq
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfeq() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = factory.generateIntegerAbstractValue(0);
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateThen(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a ifeq ", this);
		return this;
	}

	/**
	 * Evaluates a ifne
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfne() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = factory.generateIntegerAbstractValue(0);
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateElse(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateThen(true);			
		}
		JAIDebug.print("Evaluated a ifne ", this);
		return this;
	}

	/**
	 * Evaluates a iflt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIflt() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = factory.generateIntegerAbstractValueLowerThan(0);
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateThen(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a iflt ", this);
		return this;
	}

	/**
	 * Evaluates a ifge
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfge() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = factory.generateIntegerAbstractValueLowerThan(0);
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateElse(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateThen(true);			
		}
		JAIDebug.print("Evaluated a ifge ", this);
		return this;
	}

	/**
	 * Evaluates a ifgt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfgt() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = factory.generateIntegerAbstractValueLowerThan(1);
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateElse(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateThen(true);			
		}
		JAIDebug.print("Evaluated a ifgt ", this);
		return this;
	}

	/**
	 * Evaluates a ifle
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfle() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = factory.generateIntegerAbstractValueLowerThan(1);
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateThen(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a ifle ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpeq
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpeq() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateThen(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a if_icmpeq ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpne
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpne() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateElse(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateThen(true);			
		}
		JAIDebug.print("Evaluated a if_icmpne ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmplt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmplt() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (v1.getLowestPossibleIntegerValue()<v2.getHighestPossibleIntegerValue()) {
			setEvaluateThen(true);
		} 
		if (v1.getHighestPossibleIntegerValue()>=v2.getLowestPossibleIntegerValue()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a if_icmplt ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpge
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpge() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (v1.getHighestPossibleIntegerValue()<=v2.getLowestPossibleIntegerValue()) {
			setEvaluateThen(true);
		} 
		if (v1.getLowestPossibleIntegerValue()>v2.getHighestPossibleIntegerValue()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a if_icmpge ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpgt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpgt() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (v1.getHighestPossibleIntegerValue()<v2.getLowestPossibleIntegerValue()) {
			setEvaluateThen(true);
		} 
		if (v1.getLowestPossibleIntegerValue()>=v2.getHighestPossibleIntegerValue()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a if_icmpgt ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmple
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmple() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (v1.getLowestPossibleIntegerValue()<=v2.getHighestPossibleIntegerValue()) {
			setEvaluateThen(true);
		} 
		if (v1.getHighestPossibleIntegerValue()>v2.getLowestPossibleIntegerValue()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a if_icmple ", this);
		return this;
	}

	/**
	 * Evaluates a if_acmpeq
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_acmpeq() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateThen(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateElse(true);			
		}
		JAIDebug.print("Evaluated a if_acmpeq ", this);
		return this;
	}

	/**
	 * Evaluates a if_acmpne
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_acmpne() {
		JAIAbstractValue v1 = stack.pop();
		JAIAbstractValue v2 = stack.pop();
		setEvaluateElse(false);
		setEvaluateThen(false);
		if (!v1.intersection(v2).isBottom()) {
			setEvaluateElse(true);
		} 
		if (!v1.minus(v2).isBottom()) {
			setEvaluateThen(true);			
		}
		JAIDebug.print("Evaluate a if_acmpne ", this);
		return this;
	}

	/**
	 * Evaluates a goto
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGoto() {
		JAIDebug.print("Evaluated a goto ", this);
		return this;
	}

	/**
	 * Evaluates a jsr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateJsr() {
		JAIDebug.print("Evaluated a jsr ", this);
		return this;
	}

	/**
	 * Evaluates a ret
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateRet() {
		JAIDebug.print("Evaluated a ret ", this);
		return this;
	}

	/**
	 * Evaluates a tableswitch
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateTableswitch() {
		JAIDebug.print("Evaluated a tableswitch ", this);
		return this;
	}

	/**
	 * Evaluates a lookupswitch
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLookupswitch() {
		JAIDebug.print("Evaluated a lookupswitch ", this);
		return this;
	}

	/**
	 * Evaluates a ireturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIreturn() {
		JAIDebug.print("Evaluated a ireturn ", this);
		return this;
	}

	/**
	 * Evaluates a lreturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLreturn() {
		JAIDebug.print("Evaluated a lreturn ", this);
		return this;
	}

	/**
	 * Evaluates a freturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFreturn() {
		JAIDebug.print("Evaluated a freturn ", this);
		return this;
	}

	/**
	 * Evaluates a dreturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDreturn() {
		JAIDebug.print("Evaluated a dreturn ", this);
		return this;
	}

	/**
	 * Evaluates a areturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAreturn() {
		JAIDebug.print("Evaluated a areturn ", this);
		return this;
	}

	/**
	 * Evaluates a return
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateReturn() {
		JAIDebug.print("Evaluated a return ", this);
		return this;
	}

	/**
	 * Evaluates a getstatic
	 * @param staticFieldName the name of the concerned static member.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGetstatic(String staticFieldName) {
		stack.push(factory.getStaticVariableAbstractValue(staticFieldName));
		JAIDebug.print("Evaluated a getstatic on "+staticFieldName, this);
		return this;
	}

	/**
	 * Evaluates a putstatic
	 * @param staticFieldName the name of the concerned static member.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePutstatic(String staticFieldName) {
		factory.setStaticVariableAbstractValue(staticFieldName, stack.pop());
		JAIDebug.print("Evaluated a putstatic on "+staticFieldName, this);
		return this;
	}

	/**
	 * Evaluates a getfield
	 * @param fieldName the name of the concerned field.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGetfield(String fieldName) {
		// TODO
		JAIDebug.print("Evaluate a getfield "+fieldName, this);
		return this;
	}

	/**
	 * Evaluates a putfield
	 * @param fieldName  the index of the concerned field.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePutfield(String fieldName) {
		// TODO
		JAIDebug.print("Evaluate a putfield "+fieldName, this);
		return this;
	}

	/**
	 * Evaluates a invokevirtual
	 * @param methodDescriptor  a String representing the method called.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInvokevirtual(String methodDescriptor) {
		// TODO
		JAIDebug.print("Evaluate a invokevirtual ", this);
		return this;
	}

	/**
	 * Evaluates a invokespecial
	 * @param methodDescriptor a String representing the method called.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInvokespecial(String methodDescriptor) {
		// TODO
		JAIDebug.print("Evaluate a invokespecial ", this);
		return this;
	}

	/**
	 * Evaluates a invokestatic
	 * @param methodDescriptor a String representing the method called.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInvokestatic(String methodDescriptor) {
		// TODO
		JAIDebug.print("Evaluate a invokestatic ", this);
		return this;
	}

	/**
	 * Evaluates a invokeinterface
	 * @param methodDescriptor a String representing the method called.
	 * @param argCount the number of arguments to the method.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInvokeinterface(String methodDescriptor, int argCount) {
		// TODO
		JAIDebug.print("Evaluate a invokeinterface ", this);
		return this;
	}

	/**
	 * Evaluates a new
	 * @param classInfo The name of the class whose instance is created.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateNew(String classInfo) {
		// TODO
		JAIDebug.print("Evaluate a new "+classInfo, this);
		return this;
	}

	/**
	 * Evaluates a newarray
	 * @param typeInfo the type of the new array
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateNewarray(String typeInfo) {
		// TODO
		JAIDebug.print("Evaluate a newarray "+typeInfo, this);
		return this;
	}

	/**
	 * Evaluates a anewarray
	 * @param classInfo The name of the class whose array is created.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAnewarray(String classInfo) {
		// TODO
		JAIDebug.print("Evaluate a anewarray "+classInfo, this,true);
		return this;
	}

	/**
	 * Evaluates a arraylength
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateArraylength() {
		// TODO
		JAIDebug.print("Evaluate a arraylength ", this);
		return this;
	}

	/**
	 * Evaluates a athrow
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAthrow() {
		// TODO
		JAIDebug.print("Evaluate a athrow ", this);
		return this;
	}

	/**
	 * Evaluates a checkcast
	 * @param typeCast the type in which we want to cast the instance.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateCheckcast(String typeCast) {
		// TODO
		JAIDebug.print("Evaluate a checkcast "+typeCast, this);
		return this;
	}

	/**
	 * Evaluates a instanceof
	 * @param type the type that we want to check
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInstanceof(String type) {
		// TODO
		JAIDebug.print("Evaluate a instanceof "+type, this);
		return this;
	}

	/**
	 * Evaluates a monitorenter
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateMonitorenter() {
		// TODO
		JAIDebug.print("Evaluate a monitorenter ", this);
		return this;
	}

	/**
	 * Evaluates a monitorexit
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateMonitorexit() {
		// TODO
		JAIDebug.print("Evaluate a monitorexit ", this);
		return this;
	}

	/**
	 * Evaluates a wide
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateWide() {
		JAIDebug.print("Evaluated a wide ", this);
		return this;
	}

	/**
	 * Evaluates a multianewarray
	 * @param type type of the cells of the new array
	 * @param dimensions the number of dimensions of the new array.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateMultianewarray(String type, int dimensions) {
		// TODO
		JAIDebug.print("Evaluate a multianewarray of "+type+", of "+dimensions+" dimensions", this, true);
		return this;
	}

	/**
	 * Evaluates a ifnull
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfnull() {
		// TODO
		JAIDebug.print("Evaluate a ifnull ", this);
		return this;
	}

	/**
	 * Evaluates a ifnonnull
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfnonnull() {
		// TODO
		JAIDebug.print("Evaluate a ifnonnull ", this);
		return this;
	}

	/**
	 * Evaluates a goto_w
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGoto_w() {
		JAIDebug.print("Evaluated a goto_w ", this);
		return this;
	}

	/**
	 * Evaluates a jsr_w
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateJsr_w() {
		JAIDebug.print("Evaluated a jsr_w ", this);
		return this;
	}

}
