package jabsint;

import java.util.Stack;

/**
 * This class represents an abstract stack.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Aug 12, 2011
 *
 */
public class JAIStack implements Cloneable{
	
	
	/**
	 * This class represents an exception linked to the stack.
	 * 
	 * @author Manuel Oriol (manuel@cs.york.ac.uk)
	 * @date Aug 12, 2011
	 *
	 */
	@SuppressWarnings("serial")
	public class JAIStackException extends RuntimeException{
		public String reason = "";
		public JAIStackException(String reason) {
			this.reason=reason;
		}
	}
	
	/**
	 * The actual stack.
	 */
	@SuppressWarnings("rawtypes")
	public Stack<JAIAbstractValue> internalStack = new Stack<JAIAbstractValue>();
	
	/**
	 * A simple stack.
	 */
	public JAIStack() {
		
	}

	/**
	 * Creates a copy of this stack.
	 * 
	 * @param stack
	 */
	@SuppressWarnings("rawtypes")
	public JAIStack(JAIStack stack) {
		Stack<JAIAbstractValue> otherStack = stack.getInternalStack();
		for (JAIAbstractValue v: otherStack) {
			this.push((JAIAbstractValue)v.clone());
		}
	}

	
	/**
	 * Simple getter for the internal stack of this stack.
	 * 
	 * @return the internal stack.
	 */
	@SuppressWarnings("rawtypes")
	public Stack<JAIAbstractValue> getInternalStack() {
		return this.internalStack;
	}

	/**
	 * Simple setter for the internal stack of this stack.
	 * 
	 * @param stack the stack to set.
	 */
	@SuppressWarnings("rawtypes")
	public void setInternalStack(Stack<JAIAbstractValue> stack) {
		this.internalStack=stack;
	}
	
	/**
	 * A simple push used by other instructions to push something on the stack.
	 * 
	 * @param v the object to push on the stack.
	 */
	@SuppressWarnings("rawtypes")
	public void push( JAIAbstractValue v) throws JAIStackException  {

		try {
			internalStack.push(v);
		} catch (Throwable t) {
			throw new JAIStackException("Cannot push value on the stack");
		}
	}
	
	/**
	 * Duplicates the top value on this stack.
	 * 
	 * @throws JAIStackException
	 */
	public void dup() throws JAIStackException {
		if (internalStack.size()==0) {
			throw new JAIStackException("Tried a dup with no value");
		}
		@SuppressWarnings("rawtypes")
		JAIAbstractValue topValueSet = this.internalStack.pop();
		if (topValueSet.typeCategory()!=1) {
			throw new JAIStackException("Wrong category of type for a dup");			
		}
		this.internalStack.push(topValueSet);
		this.internalStack.push(topValueSet);
	}
	/**
	 * Removes the top value on this stack and returns it.
	 * 
	 * @throws JAIStackException
	 */
	@SuppressWarnings("rawtypes")
	public JAIAbstractValue pop() throws JAIStackException {
		if (internalStack.size()==0) {
			throw new JAIStackException("Tried a pop with no value");
		}
		JAIAbstractValue topValueSet = this.internalStack.pop();
		if (topValueSet.typeCategory()!=1) {
			throw new JAIStackException("Wrong category of type for a dup");			
		}
		return topValueSet;
	}
	/**
	 * Removes the top one or two value on this stack.
	 * 
	 * @throws JAIStackException
	 */
	@SuppressWarnings("rawtypes")
	public void pop2() throws JAIStackException {
		JAIAbstractValue v = this.internalStack.pop();
		if (v.typeCategory()==2) return;
		if (v.typeCategory()==1) {
			pop();
			return;
		}
		throw new JAIStackException("trying a pop2 mixing category 1 and 2 fields.");
		
	}
	/**
	 * Duplicates the top one or two value on this stack.
	 * 
	 * @throws JAIStackException
	 */
	@SuppressWarnings("rawtypes")
	public void dup2() throws JAIStackException {
		JAIAbstractValue v = this.internalStack.pop();
		if (v.typeCategory()==2) {
			this.internalStack.push(v);
			this.internalStack.push(v);			
			return;
		}
		if (v.typeCategory()==1) {
			JAIAbstractValue v2 = this.internalStack.pop();
			this.internalStack.push(v2);
			this.internalStack.push(v);
			this.internalStack.push(v2);
			this.internalStack.push(v);
			return;
		}
		throw new JAIStackException("trying a pop2 mixing category 1 and 2 fields.");		
	}
	/**
	 * Swaps the top value on this stack of category 1.
	 * 
	 * @throws JAIStackException
	 */
	public void swap() throws JAIStackException {
		if (internalStack.size()<2) {
			throw new JAIStackException("Tried a swap without enough values");
		}
		@SuppressWarnings("rawtypes")
		JAIAbstractValue topValueSet = this.internalStack.pop();
		if (topValueSet.typeCategory()!=1) {
			throw new JAIStackException("Wrong category of type for a swap");			
		}
		@SuppressWarnings("rawtypes")
		JAIAbstractValue v = this.internalStack.pop();
		this.internalStack.push(topValueSet);
		this.internalStack.push(v);
	}
	/**
	 * dup_x1 the top value on this stack of category 1.
	 * 
	 * @throws JAIStackException
	 */
	public void dup_x1() throws JAIStackException {
		if (internalStack.size()<2) {
			throw new JAIStackException("Tried a dup_x1 without enough values");
		}
		@SuppressWarnings("rawtypes")
		JAIAbstractValue topValueSet = this.internalStack.pop();
		if (topValueSet.typeCategory()!=1) {
			throw new JAIStackException("Wrong category of type for a dup_x1");			
		}
		@SuppressWarnings("rawtypes")
		JAIAbstractValue v = this.internalStack.pop();
		this.internalStack.push(topValueSet);
		this.internalStack.push(v);
		this.internalStack.push(topValueSet);
	}
	/**
	 * dup_x2 on this stack.
	 * 
	 * @throws JAIStackException
	 */
	@SuppressWarnings("rawtypes")
	public void dup_x2() throws JAIStackException {
		JAIAbstractValue v = this.internalStack.pop();
		if (v.typeCategory()==2) {
			JAIAbstractValue v2 = this.internalStack.pop();
			this.internalStack.push(v);
			this.internalStack.push(v2);			
			this.internalStack.push(v);
			return;
		}
		if (v.typeCategory()==1) {
			JAIAbstractValue v2 = this.internalStack.pop();
			JAIAbstractValue v3 = this.internalStack.pop();			
			this.internalStack.push(v);
			this.internalStack.push(v3);
			this.internalStack.push(v2);
			this.internalStack.push(v);
			return;
		}
		throw new JAIStackException("trying a dup_x2 mixing category 1 and 2 fields.");		
	}
	/**
	 * dup2_x1 on this stack.
	 * 
	 * @throws JAIStackException
	 */
	@SuppressWarnings("rawtypes")
	public void dup2_x1() throws JAIStackException {
		JAIAbstractValue v = this.internalStack.pop();
		if (v.typeCategory()==2) {
			JAIAbstractValue v2 = this.internalStack.pop();
			this.internalStack.push(v);
			this.internalStack.push(v2);			
			this.internalStack.push(v);
			return;
		}
		if (v.typeCategory()==1) {
			JAIAbstractValue v2 = this.internalStack.pop();
			JAIAbstractValue v3 = this.internalStack.pop();			
			this.internalStack.push(v2);
			this.internalStack.push(v);
			this.internalStack.push(v3);
			this.internalStack.push(v2);
			this.internalStack.push(v);
			return;
		}
		throw new JAIStackException("trying a dup2_x1 mixing category 1 and 2 fields.");		
	}
	/**
	 * dup2_x2 on this stack.
	 * 
	 * @throws JAIStackException
	 */
	@SuppressWarnings("rawtypes")
	public void dup2_x2() throws JAIStackException {
		JAIAbstractValue v = this.internalStack.pop();
		JAIAbstractValue v2 = this.internalStack.pop();
		if ((v.typeCategory()==2)&&(v2.typeCategory()==2)) {
			this.internalStack.push(v);
			this.internalStack.push(v2);			
			this.internalStack.push(v);
			return;
		}
		JAIAbstractValue v3 = this.internalStack.pop();
		if ((v.typeCategory()==1)&&(v2.typeCategory()==1)&&(v3.typeCategory()==2)) {
			this.internalStack.push(v2);			
			this.internalStack.push(v);
			this.internalStack.push(v3);			
			this.internalStack.push(v2);
			this.internalStack.push(v);
			return;
		}
		if ((v.typeCategory()==2)&&(v2.typeCategory()==1)&&(v3.typeCategory()==1)) {
			this.internalStack.push(v);
			this.internalStack.push(v3);			
			this.internalStack.push(v2);
			this.internalStack.push(v);
			return;
		}
		JAIAbstractValue v4 = this.internalStack.pop();		
		if ((v.typeCategory()==1)&&(v2.typeCategory()==1)&&(v3.typeCategory()==1)&&(v4.typeCategory()==1)) {
			this.internalStack.push(v2);			
			this.internalStack.push(v);
			this.internalStack.push(v4);
			this.internalStack.push(v3);
			this.internalStack.push(v2);			
			this.internalStack.push(v);
			return;
		}

		throw new JAIStackException("trying a dup2_x1 mixing incorrectly category 1 and 2 fields.");		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public JAIStack clone() {
		return new JAIStack(this);
	}
}
