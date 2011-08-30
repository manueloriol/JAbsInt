/**
 * 
 */
package jabsint;

/**
 * This class represents a variable and its current value.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 8, 2011
 *
 */
public class JAIVariable<T> {
	
	/**
	 * The variable name.
	 */
	private String name;
	
	/**
	 * Simpel getter for the variable name.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Simpler setter for the variable name.
	 * 
	 * @param name The name of the variable
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The value of the variable.
	 */
	private T value;

	/**
	 * Simple getter for the value.
	 * 
	 * @return the value.
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * A simple setter for the value of this variable.
	 * 
	 * @param value The value to set.
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * Returns the type of the value.
	 * 
	 * @return The type of the value.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getType(){
		return (Class<T>) value.getClass();
	}

}
