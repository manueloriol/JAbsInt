/**
 * 
 */
package jabsint;

/**
 * This class represents a factory for abstract variables, this is to use as 
 * an entry point for the JAIMinimalWorld (and its descendants) to have different 
 * types of abstract variables. 
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Aug 16, 2011
 *
 */
@SuppressWarnings("rawtypes")
public abstract class JAIAbstractValuesFactory implements Cloneable{

	/* 
	 * Simple redefinition of clone(), returns this because factories must be synchronized by default.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public JAIAbstractValuesFactory clone() {
		return this;
	}

	/**
	 * Creates an abstract value representing a null reference.
	 */
	public abstract JAIAbstractValue generateNullAbstractValue();

	/**
	 * Creates an abstract value representing the integer i.
	 * 
	 * @param i the integer to represent
	 * @return the corresponding abstract value.	
	 */
	public abstract JAIAbstractValue generateIntegerAbstractValue(int i);

	/**
	 * Creates an abstract value representing the long l.
	 * 
	 * @param l the long to represent
	 * @return the corresponding abstract value
	 */
	public abstract JAIAbstractValue generateLongAbstractValue(long l);

	/**
	 * Creates the abstract value representing the float value f.
	 * 
	 * @param f the float to represent
	 * @return the corresponding abstract value
	 */
	public abstract JAIAbstractValue generateFloatAbstractValue(float f);

	/**
	 * Creates the abstract value representing the double value d.
	 * 
	 * @param d the double to represent
	 * @return the corresponding abstract value
	 */
	public abstract JAIAbstractValue generateDoubleAbstractValue(double d);

	/**
	 * Creates the abstract value representing the short value s.
	 * 
	 * @param s the short to represent
	 * @return the corresponding abstract value
	 */
	public abstract JAIAbstractValue generateShortAbstractValue(short s);

	
	/**
	 * Creates the abstract value representing the String value s.
	 * 
	 * @param s the string to represent
	 * @return the corresponding abstract value
	 */
	public abstract JAIAbstractValue generateStringAbstractValue(String s);
	
	/**
	 * Returns the local abstract value representing the String value s.
	 * 
	 * @param number the number of the abstract value
	 * @return the corresponding abstract value
	 */
	public abstract JAIAbstractValue getLocalAbstractValue(int number);
	
	/**
	 * Stores the local abstract value representing the String value s.
	 * 
	 * @param number the number of the abstract value
	 * @param value the value of the abstract value
	 */
	public abstract void setLocalAbstractValue(int number, JAIAbstractValue value);
	 
	

}
