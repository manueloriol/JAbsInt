package jabsint;


/**
 * This class represents a value which can span a set of values.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 8, 2011
 *
 */
@SuppressWarnings("rawtypes")
public abstract class JAIAbstractValue<T> implements Cloneable {

	/**
	 * The name of the type(s) in here.
	 */
	private String typeName;


	/**
	 * A simple constructor indicating the name of the type it represents.
	 * 
	 * @param typeName
	 */
	public JAIAbstractValue(String typeName) {
		super();
		this.typeName = typeName;
	}

	/**
	 * Creates a copy of this abstract value.
	 * 
	 * @param value the value to copy.
	 */
	public JAIAbstractValue(JAIAbstractValue value) {
		super();
		this.typeName = value.typeName;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int typeCategory() {
		if (typeName.equals("long")||typeName.equals("double")) return 2;
		if (typeName.contains(":")) return 0;
		return 1;

	}

	/**
	 * Returns true if the abstract value includes the argument
	 * 
	 * @param value the value to compare
	 * @return true if it contains it in full
	 */
	public abstract boolean subsumes(JAIAbstractValue value) throws JAIAbstractValueException;

	/**
	 * Returns true if the abstract value equals the argument
	 * 
	 * @param value the value to compare
	 * @return true if it contains it in full
	 */
	public abstract boolean equals(Object o) throws JAIAbstractValueException;

	/**
	 * Returns the intersection.
	 * 
	 * @param value the value to intersect with
	 * @return the intersection
	 */
	public abstract JAIAbstractValue intersection(JAIAbstractValue value) throws JAIAbstractValueException;

	/**
	 * Returns the union.
	 * 
	 * @param value the value to unite with
	 * @return the union
	 */
	public abstract JAIAbstractValue union(JAIAbstractValue value) throws JAIAbstractValueException;

	/**
	 * Returns the subtraction (in the sense of the set theory) of one value by another.
	 * 
	 * @param value the value to subtract
	 * @return the subtraction
	 */
	public abstract JAIAbstractValue minus(JAIAbstractValue value) throws JAIAbstractValueException;

	
	/**
	 * Returns true if all concrete values represented by this abstract value are lower than the argument value
	 *
	 * @param value the value to compare with
	 * @return true if this value is lower than the other one.
	 * @throws JAIAbstractValueException 
	 */
	public abstract boolean isValueLowerThan(JAIAbstractValue value) throws JAIAbstractValueException;

	/**
	 * Returns true if all concrete values represented by this abstract value are lower or equal than the argument value
	 *
	 * @param value the value to compare with
	 * @return true if this value is lower or equal to the other one.
	 * @throws JAIAbstractValueException 
	 */
	public abstract boolean isValueLowerOrEqualTo(JAIAbstractValue value) throws JAIAbstractValueException;

	/**
	 * Returns true if all concrete values represented by this abstract value are higher than the argument value
	 *
	 * @param value the value to compare with
	 * @return true if this value is higher than the other one.
	 * @throws JAIAbstractValueException 
	 */
	public boolean isValueGreaterThan(JAIAbstractValue value) throws JAIAbstractValueException{
		return value.isValueLowerOrEqualTo(this);
	}

	/**
	 * Returns true if all concrete values represented by this abstract value are higher or equal to the argument value
	 *
	 * @param value the value to compare with
	 * @return true if this value is higher or equal to the other one.
	 * @throws JAIAbstractValueException 
	 */
	public boolean isValueGreaterOrEqualTo(JAIAbstractValue value) throws JAIAbstractValueException{
		return value.isValueLowerThan(this);		
	}

	/**
	 * Returns true if there is no clear ordering for the concrete values represented by this abstract value and the argument value
	 *
	 * @param value the value to compare with
	 * @return true if this value is not ordered with the other one.
	 * @throws JAIAbstractValueException 
	 */
	public abstract boolean isUnordered(JAIAbstractValue value) throws JAIAbstractValueException;
	
	/**
	 * Returns true if it is top.
	 * 
	 * @return true if it can represent any values in the type
	 */
	public abstract boolean isTop() throws JAIAbstractValueException;

	/**
	 * Returns true if it is bottom.
	 * 
	 * @return true if it cannot represent any value in the type
	 */
	public abstract boolean isBottom() throws JAIAbstractValueException;


	/**
	 * The concretization operator (gamma in Abstract Interpretation) that outputs a variable set.
	 * 
	 * @return
	 */
	public abstract JAIValueSet<T> makeConcrete();

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public JAIAbstractValue clone() {
		return this;
	}

	/** 
	 * A call to dereference an element from an array.
	 * 
	 * @param the index of the array
	 * @return the appropriate JAIAbstractValue
	 * @throws a JAIAbstractValueException when this is not an array.
	 */
	public abstract JAIAbstractValue loadFromArrayref(JAIAbstractValue index) throws JAIAbstractValueException;

	/** 
	 * A call to dereference an element from an array.
	 * 
	 * @param the index of the array
	 * @return the appropriate JAIAbstractValue
	 * @throws a JAIAbstractValueException when this is not an array.
	 */
	public abstract void storeInArrayref(JAIAbstractValue index, JAIAbstractValue value);

	/**
	 * An addition between two integers
	 * 
	 * @param v2 the other integer to add to this one
	 * @return the sum
	 */
	public abstract JAIAbstractValue integerAdd(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * An addition between two longs.
	 * 
	 * @param v2 the other long to add to this one
	 * @return the sum
	 */
	public abstract JAIAbstractValue longAdd(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * An addition between two floats.
	 * 
	 * @param v2 the other float to add to this one
	 * @return the sum
	 */
	public abstract JAIAbstractValue floatAdd(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * An addition between two doubles.
	 * 
	 * @param v2 the other double to add to this one
	 * @return the sum
	 */
	public abstract JAIAbstractValue doubleAdd(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A subtraction between two integers.
	 * 
	 * @param v2 the other integer to subtract to this one
	 * @return the subtraction
	 */
	public abstract JAIAbstractValue integerSub(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A subtraction between two longs.
	 * 
	 * @param v2 the other long to subtract to this one
	 * @return the subtraction
	 */
	public abstract JAIAbstractValue longSub(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A subtraction between two floats.
	 * 
	 * @param v2 the other float to subtract to this one
	 * @return the subtraction
	 */
	public abstract JAIAbstractValue floatSub(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A subtraction between two doubles.
	 * 
	 * @param v2 the other double to subtract to this one
	 * @return the subtraction
	 */
	public abstract JAIAbstractValue doubleSub(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A multiplication between two integers.
	 * 
	 * @param v2 the other integer to multiply to this one
	 * @return the product
	 */
	public abstract JAIAbstractValue integerMul(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A multiplication between two longs.
	 * 
	 * @param v2 the other long to multiply to this one
	 * @return the product
	 */
	public abstract JAIAbstractValue longMul(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A multiplication between two floats.
	 * 
	 * @param v2 the other float to multiply to this one
	 * @return the product
	 */
	public abstract JAIAbstractValue floatMul(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A multiplication between two doubles.
	 * 
	 * @param v2 the other double to multiply to this one
	 * @return the product
	 */
	public abstract JAIAbstractValue doubleMul(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A division between two integers.
	 * 
	 * @param v2 the other integer by which divide this one
	 * @return the division
	 */
	public abstract JAIAbstractValue integerDiv(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A division between two longs.
	 * 
	 * @param v2 the other long by which divide this one
	 * @return the division
	 */
	public abstract JAIAbstractValue longDiv(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A division between two floats.
	 * 
	 * @param v2 the other float by which divide this one
	 * @return the division
	 */
	public abstract JAIAbstractValue floatDiv(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * A division between two doubles.
	 * 
	 * @param v2 the other double by which divide this one
	 * @return the division
	 */
	public abstract JAIAbstractValue doubleDiv(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the remainder of a division.
	 * 
	 * @param v2 the other integer by which divide this one
	 * @return the remainder
	 */
	public abstract JAIAbstractValue integerRemainder(JAIAbstractValue v2) throws JAIAbstractValueException;
	/**
	 * Returns the remainder of a division.
	 * 
	 * @param v2 the other long by which divide this one
	 * @return the remainder
	 */
	public abstract JAIAbstractValue longRemainder(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the remainder of a division.
	 * 
	 * @param v2 the other float by which divide this one
	 * @return the remainder
	 */
	public abstract JAIAbstractValue floatRemainder(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the remainder of a division.
	 * 
	 * @param v2 the other double by which divide this one
	 * @return the remainder
	 */
	public abstract JAIAbstractValue doubleRemainder(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the negation of an integer.
	 * 
	 * @return the negated value
	 */
	public abstract JAIAbstractValue integerNegation() throws JAIAbstractValueException;

	/**
	 * Returns the negation of a long.
	 * 
	 * @return the negated value
	 */
	public abstract JAIAbstractValue longNegation() throws JAIAbstractValueException;

	/**
	 * Returns the negation of a float.
	 * 
	 * @return the negated value
	 */
	public abstract JAIAbstractValue floatNegation() throws JAIAbstractValueException;

	/**
	 * Returns the negation of a double.
	 * 
	 * @return the negated value
	 */
	public abstract JAIAbstractValue doubleNegation() throws JAIAbstractValueException;

	/**
	 * Returns the integer shifted left.
	 * @param v2 the number of bits to shift (on the low 5 bits)
	 * 
	 * @return the value shifted left
	 */
	public abstract JAIAbstractValue integerShiftLeft(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the long shifted left.
	 * @param v2 the number of bits to shift (on the low 5 bits)
	 * 
	 * @return the value shifted left
	 */
	public abstract JAIAbstractValue longShiftLeft(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the integer shifted right.
	 * @param v2 the number of bits to shift (on the low 5 bits)
	 * 
	 * @return the value shifted right
	 */
	public abstract JAIAbstractValue integerShiftRight(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the long shifted right.
	 * @param v2 the number of bits to shift (on the low 5 bits)
	 * 
	 * @return the value shifted right
	 */
	public abstract JAIAbstractValue longShiftRight(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the integer logically shifted right.
	 * @param v2 the number of bits to shift (on the low 5 bits)
	 * 
	 * @return the value shifted right
	 */
	public abstract JAIAbstractValue integerLogicalShiftRight(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the long logically shifted right.
	 * @param v2 the number of bits to shift (on the low 5 bits)
	 * 
	 * @return the value shifted right
	 */
	public abstract JAIAbstractValue longLogicalShiftRight(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the integer which is an "and" between the two integers.
	 *
	 * @param v2 the other integer
	 * @return the "and" value
	 */
	public abstract JAIAbstractValue integerAnd(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the long which is an "and" between the two longs.
	 *
	 * @param v2 the other long
	 * @return the "and" value
	 */
	public abstract JAIAbstractValue longAnd(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the integer which is an "or" between the two integers.
	 *
	 * @param v2 the other integer
	 * @return the "or" value
	 */
	public abstract JAIAbstractValue integerOr(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the long which is an "and" between the two longs.
	 *
	 * @param v2 the other long
	 * @return the "or" value
	 */
	public abstract JAIAbstractValue longOr(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the integer which is an "xor" between the two integers.
	 *
	 * @param v2 the other integer
	 * @return the "xor" value
	 */
	public abstract JAIAbstractValue integerXor(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns the long which is an "xor" between the two longs.
	 *
	 * @param v2 the other long
	 * @return the "xor" value
	 */
	public abstract JAIAbstractValue longXor(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns a long from this integer.
	 *
	 * @return the long equivalent value
	 */
	public abstract JAIAbstractValue integer2Long() throws JAIAbstractValueException;

	/**
	 * Returns a float from this integer.
	 *
	 * @return the float equivalent value
	 */
	public abstract JAIAbstractValue integer2Float() throws JAIAbstractValueException;

	/**
	 * Returns a double from this integer.
	 *
	 * @return the double equivalent value
	 */
	public abstract JAIAbstractValue integer2Double() throws JAIAbstractValueException;

	/**
	 * Returns an integer from this long.
	 *
	 * @return the integer equivalent value
	 */
	public abstract JAIAbstractValue long2Integer() throws JAIAbstractValueException;

	/**
	 * Returns a double from this long.
	 *
	 * @return the double equivalent value
	 */
	public abstract JAIAbstractValue long2Double() throws JAIAbstractValueException;

	/**
	 * Returns an integer from this float.
	 *
	 * @return the integer equivalent value
	 */
	public abstract JAIAbstractValue float2Integer() throws JAIAbstractValueException;

	/**
	 * Returns a float from this long.
	 *
	 * @return the float equivalent value
	 */
	public abstract JAIAbstractValue long2Float() throws JAIAbstractValueException;

	/**
	 * Returns an long from this float.
	 *
	 * @return the long equivalent value
	 */
	public abstract JAIAbstractValue float2Long() throws JAIAbstractValueException;

	/**
	 * Returns an double from this float.
	 *
	 * @return the double equivalent value
	 */
	public abstract JAIAbstractValue float2Double() throws JAIAbstractValueException;

	/**
	 * Returns an integer from this double.
	 *
	 * @return the integer equivalent value
	 */
	public abstract JAIAbstractValue double2Integer() throws JAIAbstractValueException;

	/**
	 * Returns an long from this double.
	 *
	 * @return the long equivalent value
	 */
	public abstract JAIAbstractValue double2Long() throws JAIAbstractValueException;

	/**
	 * Returns a float from this double.
	 *
	 * @return the float equivalent value
	 */
	public abstract JAIAbstractValue double2Float() throws JAIAbstractValueException;

	/**
	 * Returns a byte from this integer.
	 *
	 * @return the byte equivalent value
	 */
	public abstract JAIAbstractValue integer2Byte() throws JAIAbstractValueException;

	/**
	 * Returns a character from this integer.
	 *
	 * @return the character equivalent value
	 */
	public abstract JAIAbstractValue integer2Character() throws JAIAbstractValueException;

	/**
	 * Returns a short from this integer.
	 *
	 * @return the short equivalent value
	 */
	public abstract JAIAbstractValue integer2Short() throws JAIAbstractValueException;

	/**
	 * Returns comparison between this long and the argument long.
	 *
	 * @param v2 the other long
	 * @return the compared value value
	 */
	public abstract JAIAbstractValue longCompare(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns comparison between this float and the argument float.
	 * From the JVM spec (http://java.sun.com/docs/books/jvms/second_edition/html/Instructions2.doc4.html):
	 * 
	 * If value1' is greater than value2', the int value 1 is pushed onto the operand stack.
	 * Otherwise, if value1' is equal to value2', the int value 0 is pushed onto the operand stack.
	 * Otherwise, if value1' is less than value2', the int value -1 is pushed onto the operand stack.
	 * Otherwise, at least one of value1' or value2' is NaN. 
	 * The fcmpl instruction pushes the int value -1 onto the operand stack.
	 *
	 * @param v2 the other float
	 * @return the compared value value
	 */
	public abstract JAIAbstractValue floatCompareL(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns comparison between this float and the argument float.
	 * From the JVM spec (http://java.sun.com/docs/books/jvms/second_edition/html/Instructions2.doc4.html):
	 * 
	 * If value1' is greater than value2', the int value 1 is pushed onto the operand stack.
	 * Otherwise, if value1' is equal to value2', the int value 0 is pushed onto the operand stack.
	 * Otherwise, if value1' is less than value2', the int value -1 is pushed onto the operand stack.
	 * Otherwise, at least one of value1' or value2' is NaN. The fcmpg instruction pushes the int value 1 onto the operand stack
	 *
	 *
	 * @param v2 the other float
	 * @return the compared value value
	 */
	public abstract JAIAbstractValue floatCompareG(JAIAbstractValue v2) throws JAIAbstractValueException;

	
	/**
	 * Returns comparison between this float and the argument float.
	 * From the JVM spec (http://java.sun.com/docs/books/jvms/second_edition/html/Instructions2.doc3.html):
	 * 
	 * If value1' is greater than value2', the int value 1 is pushed onto the operand stack.
	 * Otherwise, if value1' is equal to value2', the int value 0 is pushed onto the operand stack.
	 * Otherwise, if value1' is less than value2', the int value -1 is pushed onto the operand stack.
	 * Otherwise, at least one of value1' or value2' is NaN. The dcmpl instruction pushes the int value -1 onto the operand stack.
	 *
	 * @param v2 the other float
	 * @return the compared value value
	 */
	public abstract JAIAbstractValue doubleCompareL(JAIAbstractValue v2) throws JAIAbstractValueException;

	/**
	 * Returns comparison between this float and the argument float.
	 * From the JVM spec (http://java.sun.com/docs/books/jvms/second_edition/html/Instructions2.doc3.html):
	 * 
	 * If value1' is greater than value2', the int value 1 is pushed onto the operand stack.
	 * Otherwise, if value1' is equal to value2', the int value 0 is pushed onto the operand stack.
	 * Otherwise, if value1' is less than value2', the int value -1 is pushed onto the operand stack.
	 * Otherwise, at least one of value1' or value2' is NaN. The dcmpg instruction pushes the int value 1 onto the operand stack.
	 *
	 *
	 * @param v2 the other float
	 * @return the compared value value
	 */
	public abstract JAIAbstractValue doubleCompareG(JAIAbstractValue v2) throws JAIAbstractValueException;

	
	/**
	 * Returns the highest concrete integer value that this abstract (integer) value represents.
	 *
	 * @return the highest value
	 */
	public abstract int getHighestPossibleIntegerValue() throws JAIAbstractValueException;

	/**
	 * Returns the lowest concrete integer value that this abstract (integer) value represents.
	 *
	 * @return the lowest concrete value
	 */
	public abstract int getLowestPossibleIntegerValue() throws JAIAbstractValueException;
	
	
	

}
