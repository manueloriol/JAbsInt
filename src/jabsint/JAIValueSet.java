package jabsint;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class represents a set of values.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 8, 2011
 *
 */
public class JAIValueSet<T> extends AbstractSet<T>{

	public Vector<T> values= new Vector<T>();


	int category = 1;
	
	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}


	
}
