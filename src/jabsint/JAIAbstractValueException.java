package jabsint;

/**
 * This class represents an exception in the interpretation of the world.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Aug 17, 2011
 *
 */
@SuppressWarnings("serial")
public class JAIAbstractValueException extends RuntimeException{
	public String reason = "";
	public JAIAbstractValueException(String reason) {
		this.reason=reason;
	}
}