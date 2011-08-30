package jabsint;

public class JAIDebug {

	/**
	 * The array of classes on which the debug messages should be shown. 
	 * example of classes to debug:	
	 * <code>public static String []enabledDebugClasses={"jeffects.JEAbstractInterpreter"};</code>
	 */
//	public static String []enabledDebugClasses={"jeffects.JEAbstractInterpreter"};
	public static String []enabledDebugClasses={};

	/**
	 * Method used to print debugging messages.
	 * 
	 * @param message the debugging message to use.
	 * @param objectInWhichCalled the caller or the class of the caller in case it is in a static method.
	 */
	@SuppressWarnings("rawtypes")
	public static void print(String message, Object objectInWhichCalled) {
		String className;

		// if it is a class we check directly for this class
		if (objectInWhichCalled instanceof Class)
			className = ((Class)objectInWhichCalled).getName();
		// else we get the class of the object passed as a parameter
		else
			className = objectInWhichCalled.getClass().getName();		
		
		// we check that the class is in the classes to print
		boolean isPrintable = false;
		for(String s : enabledDebugClasses){
			if (className.equals(s)) {
				isPrintable = true; 
				break;
			}
		}
		if (isPrintable) 
			// we print the message with maximum information so it is easier 
			// to know where it comes from 
			System.err.println("JEffects DEBUG:"+className+": "+message);
	}
	
	/**
	 * Method used to print debugging messages. Prints the message if isTemporary is true.
	 * 
	 * @param message the debugging message to use.
	 * @param objectInWhichCalled the caller or the class of the caller in case it is in a static method.
	 * @param isTemporary print the message anyway if true.
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void print(String message, Object objectInWhichCalled, boolean isTemporary) {
		if (isTemporary)
			if (objectInWhichCalled instanceof Class)
				System.err.println("JEffects DEBUG:TMP:"+((Class)objectInWhichCalled).getName()+": "+message);
			else
				System.err.println("JEffects DEBUG:TMP:"+objectInWhichCalled.getClass().getName()+": "+message);
	}


}
