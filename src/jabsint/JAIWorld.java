/**
 * 
 */
package jabsint;

/**
 * This class represents the world as the abstract interpreter sees it.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 27, 2011
 *
 */
public class JAIWorld {

	/**
	 * This class represents an exception in the interpretation of the world.
	 * 
	 * @author Manuel Oriol (manuel@cs.york.ac.uk)
	 * @date Aug 17, 2011
	 *
	 */
	@SuppressWarnings("serial")
	public class JAIWorldException extends RuntimeException{
		public String reason = "";
		public JAIWorldException(String reason) {
			this.reason=reason;
		}
	}
	
	/**
	 * Minimal constructor. 
	 */
	public JAIWorld () {


	}

	
	/**
	 * Checks whether it is ok to loop. By default we do not loop.
	 * 
	 * @return true if the next loop should happen, false otherwise.
	 */
	public boolean isLoopingValid() {
		return false;
	}

	/**
	 * Checks whether both branches of the next "if" need to be evaluated. By default the answer is yes.
	 * 
	 * @return true if they need, false otherwise.
	 */
	public boolean allBranchesNeedToBeEvaluated() {

		return true;
	}

	/**
	 * In case both branches of the next "if" do not need to be evaluated, checks whether 
	 * it is with the "then" (or the "else"). Default value is false.
	 * 
	 * @return true if "then" needs to be evaluated, flase otherwise.
	 */
	public boolean shouldEvaluateThen() {
		return false;
	}

	/**
	 * This method allows to combine this world with another one and return the combined world.
	 * 
	 * @param aWorld
	 * @return the combination of both worlds
	 */
	public JAIWorld combineWith(JAIWorld aWorld) {
		return this;
	}

	/**
	 * Checks whether the interpreter should interpret method calls. By default the answer is no.
	 * 
	 * @return true if we should interpret method calls, false otherwise.
	 */
	public boolean shouldInterpretMehtodCalls() {
		return false;
	}

	/**
	 * Returns the values possible branches to evaluate.
	 * 
	 * @return
	 */
	public int[] getValuesOfBranchesToEvaluate() {
		int []indexes=new int[0];
		return indexes;
	}



	/**
	 * Evaluates a nop
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateNop() {
		JAIDebug.print("Evaluate a nop ", this);
		return this;
	}

	/**
	 * Evaluates a aconst_null
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAconst_null() {
		JAIDebug.print("Evaluate a aconst_null ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_m1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_m1() {
		JAIDebug.print("Evaluate a iconst_m1 ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_0() {
		JAIDebug.print("Evaluate a iconst_0 ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_1() {
		JAIDebug.print("Evaluate a iconst_1 ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_2() {
		JAIDebug.print("Evaluate a iconst_2 ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_3() {
		JAIDebug.print("Evaluate a iconst_3 ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_4
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_4() {
		JAIDebug.print("Evaluate a iconst_4 ", this);
		return this;
	}

	/**
	 * Evaluates a iconst_5
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIconst_5() {
		JAIDebug.print("Evaluate a iconst_5 ", this);
		return this;
	}

	/**
	 * Evaluates a lconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLconst_0() {
		JAIDebug.print("Evaluate a lconst_0 ", this);
		return this;
	}

	/**
	 * Evaluates a lconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLconst_1() {
		JAIDebug.print("Evaluate a lconst_1 ", this);
		return this;
	}

	/**
	 * Evaluates a fconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFconst_0() {
		JAIDebug.print("Evaluate a fconst_0 ", this);
		return this;
	}

	/**
	 * Evaluates a fconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFconst_1() {
		JAIDebug.print("Evaluate a fconst_1 ", this);
		return this;
	}

	/**
	 * Evaluates a fconst_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFconst_2() {
		JAIDebug.print("Evaluate a fconst_2 ", this);
		return this;
	}

	/**
	 * Evaluates a dconst_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDconst_0() {
		JAIDebug.print("Evaluate a dconst_0 ", this);
		return this;
	}

	/**
	 * Evaluates a dconst_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDconst_1() {
		JAIDebug.print("Evaluate a dconst_1 ", this);
		return this;
	}

	/**
	 * Evaluates a bipush
	 * @param b the byte pushed on the stack
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateBipush(byte b) {
		JAIDebug.print("Evaluate a bipush ", this);
		return this;
	}

	/**
	 * Evaluates a sipush
	 * @param s the short pushed on the stack.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSipush(short s) {
		JAIDebug.print("Evaluate a sipush ", this);
		return this;
	}

	/**
	 * Evaluates a ldc
	 * @param o the ldc value there.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdc(Object o) {
		JAIDebug.print("Evaluate a ldc ", this);
		return this;
	}

	/**
	 * Evaluates a ldc_w
	 * @param o the ldc value there.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdc_w(Object o) {
		JAIDebug.print("Evaluate a ldc_w ", this);
		return this;
	}

	/**
	 * Evaluates a ldc2_w
	 * @param o the ldc value there.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdc2_w(Object o) {
		JAIDebug.print("Evaluate a ldc2_w ", this);
		return this;
	}

	/**
	 * Evaluates a iload
	 * @param i the index of the local variable to load
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload(int i) {
		JAIDebug.print("Evaluate a iload ", this);
		return this;
	}

	/**
	 * Evaluates a lload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload(int i) {
		JAIDebug.print("Evaluate a lload ", this);
		return this;
	}

	/**
	 * Evaluates a fload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload(int i) {
		JAIDebug.print("Evaluate a fload ", this);
		return this;
	}

	/**
	 * Evaluates a dload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload(int i) {
		JAIDebug.print("Evaluate a dload ", this);
		return this;
	}

	/**
	 * Evaluates a aload
	 * @param i the index of the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload(int i) {
		JAIDebug.print("Evaluate a aload ", this);
		return this;
	}

	/**
	 * Evaluates a iload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_0() {
		JAIDebug.print("Evaluate a iload_0 ", this);
		return this;
	}

	/**
	 * Evaluates a iload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_1() {
		JAIDebug.print("Evaluate a iload_1 ", this);
		return this;
	}

	/**
	 * Evaluates a iload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_2() {
		JAIDebug.print("Evaluate a iload_2 ", this);
		return this;
	}

	/**
	 * Evaluates a iload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIload_3() {
		JAIDebug.print("Evaluate a iload_3 ", this);
		return this;
	}

	/**
	 * Evaluates a lload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_0() {
		JAIDebug.print("Evaluate a lload_0 ", this);
		return this;
	}

	/**
	 * Evaluates a lload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_1() {
		JAIDebug.print("Evaluate a lload_1 ", this);
		return this;
	}

	/**
	 * Evaluates a lload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_2() {
		JAIDebug.print("Evaluate a lload_2 ", this);
		return this;
	}

	/**
	 * Evaluates a lload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLload_3() {
		JAIDebug.print("Evaluate a lload_3 ", this);
		return this;
	}

	/**
	 * Evaluates a fload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_0() {
		JAIDebug.print("Evaluate a fload_0 ", this);
		return this;
	}

	/**
	 * Evaluates a fload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_1() {
		JAIDebug.print("Evaluate a fload_1 ", this);
		return this;
	}

	/**
	 * Evaluates a fload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_2() {
		JAIDebug.print("Evaluate a fload_2 ", this);
		return this;
	}

	/**
	 * Evaluates a fload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFload_3() {
		JAIDebug.print("Evaluate a fload_3 ", this);
		return this;
	}

	/**
	 * Evaluates a dload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_0() {
		JAIDebug.print("Evaluate a dload_0 ", this);
		return this;
	}

	/**
	 * Evaluates a dload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_1() {
		JAIDebug.print("Evaluate a dload_1 ", this);
		return this;
	}

	/**
	 * Evaluates a dload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_2() {
		JAIDebug.print("Evaluate a dload_2 ", this);
		return this;
	}

	/**
	 * Evaluates a dload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDload_3() {
		JAIDebug.print("Evaluate a dload_3 ", this);
		return this;
	}

	/**
	 * Evaluates a aload_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_0() {
		JAIDebug.print("Evaluate a aload_0 ", this);
		return this;
	}

	/**
	 * Evaluates a aload_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_1() {
		JAIDebug.print("Evaluate a aload_1 ", this);
		return this;
	}

	/**
	 * Evaluates a aload_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_2() {
		JAIDebug.print("Evaluate a aload_2 ", this);
		return this;
	}

	/**
	 * Evaluates a aload_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAload_3() {
		JAIDebug.print("Evaluate a aload_3 ", this);
		return this;
	}

	/**
	 * Evaluates a iaload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIaload() {
		JAIDebug.print("Evaluate a iaload ", this);
		return this;
	}

	/**
	 * Evaluates a laload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLaload() {
		JAIDebug.print("Evaluate a laload ", this);
		return this;
	}

	/**
	 * Evaluates a faload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFaload() {
		JAIDebug.print("Evaluate a faload ", this);
		return this;
	}

	/**
	 * Evaluates a daload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDaload() {
		JAIDebug.print("Evaluate a daload ", this);
		return this;
	}

	/**
	 * Evaluates a aaload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAaload() {
		JAIDebug.print("Evaluate a aaload ", this);
		return this;
	}

	/**
	 * Evaluates a baload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateBaload() {
		JAIDebug.print("Evaluate a baload ", this);
		return this;
	}

	/**
	 * Evaluates a caload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateCaload() {
		JAIDebug.print("Evaluate a caload ", this);
		return this;
	}

	/**
	 * Evaluates a saload
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSaload() {
		JAIDebug.print("Evaluate a saload ", this);
		return this;
	}

	/**
	 * Evaluates a istore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore(int i) {
		JAIDebug.print("Evaluate a istore ", this);
		return this;
	}

	/**
	 * Evaluates a lstore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore(int i) {
		JAIDebug.print("Evaluate a lstore ", this);
		return this;
	}

	/**
	 * Evaluates a fstore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore(int i) {
		JAIDebug.print("Evaluate a fstore ", this);
		return this;
	}

	/**
	 * Evaluates a dstore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore(int i) {
		JAIDebug.print("Evaluate a dstore ", this);
		return this;
	}

	/**
	 * Evaluates a astore
	 * @param i the index in which to store the value.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore(int i) {
		JAIDebug.print("Evaluate a astore ", this);
		return this;
	}

	/**
	 * Evaluates a istore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_0() {
		JAIDebug.print("Evaluate a istore_0 ", this);
		return this;
	}

	/**
	 * Evaluates a istore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_1() {
		JAIDebug.print("Evaluate a istore_1 ", this);
		return this;
	}

	/**
	 * Evaluates a istore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_2() {
		JAIDebug.print("Evaluate a istore_2 ", this);
		return this;
	}

	/**
	 * Evaluates a istore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIstore_3() {
		JAIDebug.print("Evaluate a istore_3 ", this);
		return this;
	}

	/**
	 * Evaluates a lstore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_0() {
		JAIDebug.print("Evaluate a lstore_0 ", this);
		return this;
	}

	/**
	 * Evaluates a lstore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_1() {
		JAIDebug.print("Evaluate a lstore_1 ", this);
		return this;
	}

	/**
	 * Evaluates a lstore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_2() {
		JAIDebug.print("Evaluate a lstore_2 ", this);
		return this;
	}

	/**
	 * Evaluates a lstore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLstore_3() {
		JAIDebug.print("Evaluate a lstore_3 ", this);
		return this;
	}

	/**
	 * Evaluates a fstore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_0() {
		JAIDebug.print("Evaluate a fstore_0 ", this);
		return this;
	}

	/**
	 * Evaluates a fstore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_1() {
		JAIDebug.print("Evaluate a fstore_1 ", this);
		return this;
	}

	/**
	 * Evaluates a fstore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_2() {
		JAIDebug.print("Evaluate a fstore_2 ", this);
		return this;
	}

	/**
	 * Evaluates a fstore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFstore_3() {
		JAIDebug.print("Evaluate a fstore_3 ", this);
		return this;
	}

	/**
	 * Evaluates a dstore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_0() {
		JAIDebug.print("Evaluate a dstore_0 ", this);
		return this;
	}

	/**
	 * Evaluates a dstore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_1() {
		JAIDebug.print("Evaluate a dstore_1 ", this);
		return this;
	}

	/**
	 * Evaluates a dstore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_2() {
		JAIDebug.print("Evaluate a dstore_2 ", this);
		return this;
	}

	/**
	 * Evaluates a dstore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDstore_3() {
		JAIDebug.print("Evaluate a dstore_3 ", this);
		return this;
	}

	/**
	 * Evaluates a astore_0
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_0() {
		JAIDebug.print("Evaluate a astore_0 ", this);
		return this;
	}

	/**
	 * Evaluates a astore_1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_1() {
		JAIDebug.print("Evaluate a astore_1 ", this);
		return this;
	}

	/**
	 * Evaluates a astore_2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_2() {
		JAIDebug.print("Evaluate a astore_2 ", this);
		return this;
	}

	/**
	 * Evaluates a astore_3
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAstore_3() {
		JAIDebug.print("Evaluate a astore_3 ", this);
		return this;
	}

	/**
	 * Evaluates a iastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIastore() {
		JAIDebug.print("Evaluate a iastore ", this);
		return this;
	}

	/**
	 * Evaluates a lastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLastore() {
		JAIDebug.print("Evaluate a lastore ", this);
		return this;
	}

	/**
	 * Evaluates a fastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFastore() {
		JAIDebug.print("Evaluate a fastore ", this);
		return this;
	}

	/**
	 * Evaluates a dastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDastore() {
		JAIDebug.print("Evaluate a dastore ", this);
		return this;
	}

	/**
	 * Evaluates a aastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAastore() {
		JAIDebug.print("Evaluate a aastore ", this);
		return this;
	}

	/**
	 * Evaluates a bastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateBastore() {
		JAIDebug.print("Evaluate a bastore ", this);
		return this;
	}

	/**
	 * Evaluates a castore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateCastore() {
		JAIDebug.print("Evaluate a castore ", this);
		return this;
	}

	/**
	 * Evaluates a sastore
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSastore() {
		JAIDebug.print("Evaluate a sastore ", this);
		return this;
	}

	/**
	 * Evaluates a pop
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePop() {
		JAIDebug.print("Evaluate a pop ", this);
		return this;
	}

	/**
	 * Evaluates a pop2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePop2() {
		JAIDebug.print("Evaluate a pop2 ", this);
		return this;
	}

	/**
	 * Evaluates a dup
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup() {
		JAIDebug.print("Evaluate a dup ", this);
		return this;
	}

	/**
	 * Evaluates a dup_x1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup_x1() {
		JAIDebug.print("Evaluate a dup_x1 ", this);
		return this;
	}

	/**
	 * Evaluates a dup_x2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup_x2() {
		JAIDebug.print("Evaluate a dup_x2 ", this);
		return this;
	}

	/**
	 * Evaluates a dup2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup2() {
		JAIDebug.print("Evaluate a dup2 ", this);
		return this;
	}

	/**
	 * Evaluates a dup2_x1
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup2_x1() {
		JAIDebug.print("Evaluate a dup2_x1 ", this);
		return this;
	}

	/**
	 * Evaluates a dup2_x2
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDup2_x2() {
		JAIDebug.print("Evaluate a dup2_x2 ", this);
		return this;
	}

	/**
	 * Evaluates a swap
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateSwap() {
		JAIDebug.print("Evaluate a swap ", this);
		return this;
	}

	/**
	 * Evaluates a iadd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIadd() {
		JAIDebug.print("Evaluate a iadd ", this);
		return this;
	}

	/**
	 * Evaluates a ladd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLadd() {
		JAIDebug.print("Evaluate a ladd ", this);
		return this;
	}

	/**
	 * Evaluates a fadd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFadd() {
		JAIDebug.print("Evaluate a fadd ", this);
		return this;
	}

	/**
	 * Evaluates a dadd
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDadd() {
		JAIDebug.print("Evaluate a dadd ", this);
		return this;
	}

	/**
	 * Evaluates a isub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIsub() {
		JAIDebug.print("Evaluate a isub ", this);
		return this;
	}

	/**
	 * Evaluates a lsub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLsub() {
		JAIDebug.print("Evaluate a lsub ", this);
		return this;
	}

	/**
	 * Evaluates a fsub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFsub() {
		JAIDebug.print("Evaluate a fsub ", this);
		return this;
	}

	/**
	 * Evaluates a dsub
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDsub() {
		JAIDebug.print("Evaluate a dsub ", this);
		return this;
	}

	/**
	 * Evaluates a imul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateImul() {
		JAIDebug.print("Evaluate a imul ", this);
		return this;
	}

	/**
	 * Evaluates a lmul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLmul() {
		JAIDebug.print("Evaluate a lmul ", this);
		return this;
	}

	/**
	 * Evaluates a fmul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFmul() {
		JAIDebug.print("Evaluate a fmul ", this);
		return this;
	}

	/**
	 * Evaluates a dmul
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDmul() {
		JAIDebug.print("Evaluate a dmul ", this);
		return this;
	}

	/**
	 * Evaluates a idiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIdiv() {
		JAIDebug.print("Evaluate a idiv ", this);
		return this;
	}

	/**
	 * Evaluates a ldiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLdiv() {
		JAIDebug.print("Evaluate a ldiv ", this);
		return this;
	}

	/**
	 * Evaluates a fdiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFdiv() {
		JAIDebug.print("Evaluate a fdiv ", this);
		return this;
	}

	/**
	 * Evaluates a ddiv
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDdiv() {
		JAIDebug.print("Evaluate a ddiv ", this);
		return this;
	}

	/**
	 * Evaluates a irem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIrem() {
		JAIDebug.print("Evaluate a irem ", this);
		return this;
	}

	/**
	 * Evaluates a lrem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLrem() {
		JAIDebug.print("Evaluate a lrem ", this);
		return this;
	}

	/**
	 * Evaluates a frem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFrem() {
		JAIDebug.print("Evaluate a frem ", this);
		return this;
	}

	/**
	 * Evaluates a drem
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDrem() {
		JAIDebug.print("Evaluate a drem ", this);
		return this;
	}

	/**
	 * Evaluates a ineg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIneg() {
		JAIDebug.print("Evaluate a ineg ", this);
		return this;
	}

	/**
	 * Evaluates a lneg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLneg() {
		JAIDebug.print("Evaluate a lneg ", this);
		return this;
	}

	/**
	 * Evaluates a fneg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFneg() {
		JAIDebug.print("Evaluate a fneg ", this);
		return this;
	}

	/**
	 * Evaluates a dneg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDneg() {
		JAIDebug.print("Evaluate a dneg ", this);
		return this;
	}

	/**
	 * Evaluates a ishl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIshl() {
		JAIDebug.print("Evaluate a ishl ", this);
		return this;
	}

	/**
	 * Evaluates a lshl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLshl() {
		JAIDebug.print("Evaluate a lshl ", this);
		return this;
	}

	/**
	 * Evaluates a ishr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIshr() {
		JAIDebug.print("Evaluate a ishr ", this);
		return this;
	}

	/**
	 * Evaluates a lshr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLshr() {
		JAIDebug.print("Evaluate a lshr ", this);
		return this;
	}

	/**
	 * Evaluates a iushr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIushr() {
		JAIDebug.print("Evaluate a iushr ", this);
		return this;
	}

	/**
	 * Evaluates a lushr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLushr() {
		JAIDebug.print("Evaluate a lushr ", this);
		return this;
	}

	/**
	 * Evaluates a iand
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIand() {
		JAIDebug.print("Evaluate a iand ", this);
		return this;
	}

	/**
	 * Evaluates a land
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLand() {
		JAIDebug.print("Evaluate a land ", this);
		return this;
	}

	/**
	 * Evaluates a ior
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIor() {
		JAIDebug.print("Evaluate a ior ", this);
		return this;
	}

	/**
	 * Evaluates a lor
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLor() {
		JAIDebug.print("Evaluate a lor ", this);
		return this;
	}

	/**
	 * Evaluates a ixor
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIxor() {
		JAIDebug.print("Evaluate a ixor ", this);
		return this;
	}

	/**
	 * Evaluates a lxor
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLxor() {
		JAIDebug.print("Evaluate a lxor ", this);
		return this;
	}

	/**
	 * Evaluates a iinc
	 * @param i the index of the local variable.
	 * @param increment the amount to increment the local variable.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIinc(int i, int increment) {
		JAIDebug.print("Evaluate a iinc ", this);
		return this;
	}

	/**
	 * Evaluates a i2l
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2l() {
		JAIDebug.print("Evaluate a i2l ", this);
		return this;
	}

	/**
	 * Evaluates a i2f
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2f() {
		JAIDebug.print("Evaluate a i2f ", this);
		return this;
	}

	/**
	 * Evaluates a i2d
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2d() {
		JAIDebug.print("Evaluate a i2d ", this);
		return this;
	}

	/**
	 * Evaluates a l2i
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateL2i() {
		JAIDebug.print("Evaluate a l2i ", this);
		return this;
	}

	/**
	 * Evaluates a l2f
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateL2f() {
		JAIDebug.print("Evaluate a l2f ", this);
		return this;
	}

	/**
	 * Evaluates a l2d
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateL2d() {
		JAIDebug.print("Evaluate a l2d ", this);
		return this;
	}

	/**
	 * Evaluates a f2i
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateF2i() {
		JAIDebug.print("Evaluate a f2i ", this);
		return this;
	}

	/**
	 * Evaluates a f2l
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateF2l() {
		JAIDebug.print("Evaluate a f2l ", this);
		return this;
	}

	/**
	 * Evaluates a f2d
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateF2d() {
		JAIDebug.print("Evaluate a f2d ", this);
		return this;
	}

	/**
	 * Evaluates a d2i
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateD2i() {
		JAIDebug.print("Evaluate a d2i ", this);
		return this;
	}

	/**
	 * Evaluates a d2l
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateD2l() {
		JAIDebug.print("Evaluate a d2l ", this);
		return this;
	}

	/**
	 * Evaluates a d2f
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateD2f() {
		JAIDebug.print("Evaluate a d2f ", this);
		return this;
	}

	/**
	 * Evaluates a i2b
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2b() {
		JAIDebug.print("Evaluate a i2b ", this);
		return this;
	}

	/**
	 * Evaluates a i2c
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2c() {
		JAIDebug.print("Evaluate a i2c ", this);
		return this;
	}

	/**
	 * Evaluates a i2s
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateI2s() {
		JAIDebug.print("Evaluate a i2s ", this);
		return this;
	}

	/**
	 * Evaluates a lcmp
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLcmp() {
		JAIDebug.print("Evaluate a lcmp ", this);
		return this;
	}

	/**
	 * Evaluates a fcmpl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFcmpl() {
		JAIDebug.print("Evaluate a fcmpl ", this);
		return this;
	}

	/**
	 * Evaluates a fcmpg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFcmpg() {
		JAIDebug.print("Evaluate a fcmpg ", this);
		return this;
	}

	/**
	 * Evaluates a dcmpl
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDcmpl() {
		JAIDebug.print("Evaluate a dcmpl ", this);
		return this;
	}

	/**
	 * Evaluates a dcmpg
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDcmpg() {
		JAIDebug.print("Evaluate a dcmpg ", this);
		return this;
	}

	/**
	 * Evaluates a ifeq
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfeq() {
		JAIDebug.print("Evaluate a ifeq ", this);
		return this;
	}

	/**
	 * Evaluates a ifne
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfne() {
		JAIDebug.print("Evaluate a ifne ", this);
		return this;
	}

	/**
	 * Evaluates a iflt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIflt() {
		JAIDebug.print("Evaluate a iflt ", this);
		return this;
	}

	/**
	 * Evaluates a ifge
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfge() {
		JAIDebug.print("Evaluate a ifge ", this);
		return this;
	}

	/**
	 * Evaluates a ifgt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfgt() {
		JAIDebug.print("Evaluate a ifgt ", this);
		return this;
	}

	/**
	 * Evaluates a ifle
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfle() {
		JAIDebug.print("Evaluate a ifle ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpeq
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpeq() {
		JAIDebug.print("Evaluate a if_icmpeq ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpne
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpne() {
		JAIDebug.print("Evaluate a if_icmpne ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmplt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmplt() {
		JAIDebug.print("Evaluate a if_icmplt ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpge
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpge() {
		JAIDebug.print("Evaluate a if_icmpge ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmpgt
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmpgt() {
		JAIDebug.print("Evaluate a if_icmpgt ", this);
		return this;
	}

	/**
	 * Evaluates a if_icmple
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_icmple() {
		JAIDebug.print("Evaluate a if_icmple ", this);
		return this;
	}

	/**
	 * Evaluates a if_acmpeq
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_acmpeq() {
		JAIDebug.print("Evaluate a if_acmpeq ", this);
		return this;
	}

	/**
	 * Evaluates a if_acmpne
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIf_acmpne() {
		JAIDebug.print("Evaluate a if_acmpne ", this);
		return this;
	}

	/**
	 * Evaluates a goto
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGoto() {
		JAIDebug.print("Evaluate a goto ", this);
		return this;
	}

	/**
	 * Evaluates a jsr
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateJsr() {
		JAIDebug.print("Evaluate a jsr ", this);
		return this;
	}

	/**
	 * Evaluates a ret
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateRet() {
		JAIDebug.print("Evaluate a ret ", this);
		return this;
	}

	/**
	 * Evaluates a tableswitch
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateTableswitch() {
		JAIDebug.print("Evaluate a tableswitch ", this);
		return this;
	}

	/**
	 * Evaluates a lookupswitch
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLookupswitch() {
		JAIDebug.print("Evaluate a lookupswitch ", this);
		return this;
	}

	/**
	 * Evaluates a ireturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIreturn() {
		JAIDebug.print("Evaluate a ireturn ", this);
		return this;
	}

	/**
	 * Evaluates a lreturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateLreturn() {
		JAIDebug.print("Evaluate a lreturn ", this);
		return this;
	}

	/**
	 * Evaluates a freturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateFreturn() {
		JAIDebug.print("Evaluate a freturn ", this);
		return this;
	}

	/**
	 * Evaluates a dreturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateDreturn() {
		JAIDebug.print("Evaluate a dreturn ", this);
		return this;
	}

	/**
	 * Evaluates a areturn
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAreturn() {
		JAIDebug.print("Evaluate a areturn ", this);
		return this;
	}

	/**
	 * Evaluates a return
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateReturn() {
		JAIDebug.print("Evaluate a return ", this);
		return this;
	}

	/**
	 * Evaluates a getstatic
	 * @param staticVariableIndex the index of the concerned static member.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGetstatic(String staticFieldName) {
		JAIDebug.print("Evaluate a getstatic on "+staticFieldName, this);
		return this;
	}

	/**
	 * Evaluates a putstatic
	 * @param string the name of the concerned static member.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePutstatic(String staticFieldName) {
		JAIDebug.print("Evaluate a putstatic on "+staticFieldName, this);
		return this;
	}

	/**
	 * Evaluates a getfield
	 * @param fieldName the name of the concerned field.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGetfield(String fieldName) {
		JAIDebug.print("Evaluate a getfield "+fieldName, this);
		return this;
	}

	/**
	 * Evaluates a putfield
	 * @param fieldName  the index of the concerned field.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluatePutfield(String fieldName) {
		JAIDebug.print("Evaluate a putfield "+fieldName, this);
		return this;
	}

	/**
	 * Evaluates a invokevirtual
	 * @param methodDescriptor  a String representing the method called.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInvokevirtual(String methodDescriptor) {
		JAIDebug.print("Evaluate a invokevirtual ", this);
		return this;
	}

	/**
	 * Evaluates a invokespecial
	 * @param methodDescriptor a String representing the method called.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInvokespecial(String methodDescriptor) {
		JAIDebug.print("Evaluate a invokespecial ", this);
		return this;
	}

	/**
	 * Evaluates a invokestatic
	 * @param methodDescriptor a String representing the method called.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInvokestatic(String methodDescriptor) {
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
		JAIDebug.print("Evaluate a invokeinterface ", this);
		return this;
	}

	/**
	 * Evaluates a new
	 * @param classInfo The name of the class whose instance is created.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateNew(String classInfo) {
		JAIDebug.print("Evaluate a new "+classInfo, this);
		return this;
	}

	/**
	 * Evaluates a newarray
	 * @param typeInfo the type of the new array
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateNewarray(String typeInfo) {
		JAIDebug.print("Evaluate a newarray "+typeInfo, this);
		return this;
	}

	/**
	 * Evaluates a anewarray
	 * @param classInfo The name of the class whose array is created.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAnewarray(String classInfo) {
		JAIDebug.print("Evaluate a anewarray "+classInfo, this,true);
		return this;
	}

	/**
	 * Evaluates a arraylength
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateArraylength() {
		JAIDebug.print("Evaluate a arraylength ", this);
		return this;
	}

	/**
	 * Evaluates a athrow
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateAthrow() {
		JAIDebug.print("Evaluate a athrow ", this);
		return this;
	}

	/**
	 * Evaluates a checkcast
	 * @param typeCast the type in which we want to cast the instance.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateCheckcast(String typeCast) {
		JAIDebug.print("Evaluate a checkcast "+typeCast, this);
		return this;
	}

	/**
	 * Evaluates a instanceof
	 * @param type the type that we want to check
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateInstanceof(String type) {
		JAIDebug.print("Evaluate a instanceof "+type, this);
		return this;
	}

	/**
	 * Evaluates a monitorenter
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateMonitorenter() {
		JAIDebug.print("Evaluate a monitorenter ", this);
		return this;
	}

	/**
	 * Evaluates a monitorexit
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateMonitorexit() {
		JAIDebug.print("Evaluate a monitorexit ", this);
		return this;
	}

	/**
	 * Evaluates a wide
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateWide() {
		JAIDebug.print("Evaluate a wide ", this);
		return this;
	}

	/**
	 * Evaluates a multianewarray
	 * @param type type of the cells of the new array
	 * @param dimensions the number of dimensions of the new array.
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateMultianewarray(String type, int dimensions) {
		JAIDebug.print("Evaluate a multianewarray of "+type+", of "+dimensions+" dimensions", this, true);
		return this;
	}

	/**
	 * Evaluates a ifnull
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfnull() {
		JAIDebug.print("Evaluate a ifnull ", this);
		return this;
	}

	/**
	 * Evaluates a ifnonnull
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateIfnonnull() {
		JAIDebug.print("Evaluate a ifnonnull ", this);
		return this;
	}

	/**
	 * Evaluates a goto_w
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateGoto_w() {
		JAIDebug.print("Evaluate a goto_w ", this);
		return this;
	}

	/**
	 * Evaluates a jsr_w
	 * @return the new state of the abstract world.
	 */	
	public JAIWorld evaluateJsr_w() {
		JAIDebug.print("Evaluate a jsr_w ", this);
		return this;
	}





}
