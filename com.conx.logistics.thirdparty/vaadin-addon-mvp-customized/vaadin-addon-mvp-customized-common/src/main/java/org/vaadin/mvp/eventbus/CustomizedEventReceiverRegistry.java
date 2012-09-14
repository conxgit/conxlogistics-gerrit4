package org.vaadin.mvp.eventbus;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event handler registry holding <i>active</i> subscribers (i.e. the actual
 * event handlers) as weak references to allow collection in case no other
 * references are held by the application.
 * 
 * @author Sandile
 */
public class CustomizedEventReceiverRegistry extends EventReceiverRegistry {
	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(EventReceiverRegistry.class);

	/**
	 * Event receiver registry using WeakReference to the receiver instances to
	 * allow garbage collection of them. Note that a normal HashMap is used
	 * since a WeakHashMap does only collect non-referenced keys - which are
	 * class objects that will not be collected in our case.
	 */
	private Map<Class<?>, Set<WeakReference<?>>> receivers = new HashMap<Class<?>, Set<WeakReference<?>>>();

	@Override
	public void addReceiver(Object receiver) {
		// clear collected receivers from our map first
		// create a "copy" of the maps keyset to allow modification while
		// looping
		Set<Class<?>> keySet = new HashSet<Class<?>>(receivers.keySet());
		for (Class<?> receiverType : keySet) {
			Set<WeakReference<?>> referenceSet = receivers.get(receiverType);
			for (WeakReference<?> reference : referenceSet) {
				if (reference.get() == null) {
					logger.debug("Removing mapping: {}", receiverType);
					receivers.remove(receiverType);
				}
			}
		}
		Set<WeakReference<?>> referenceSet = receivers.get(receiver.getClass());
		if (referenceSet == null) {
			referenceSet = new HashSet<WeakReference<?>>();
			referenceSet.add(new WeakReference<Object>(receiver));
			receivers.put(receiver.getClass(), referenceSet);
		} else {
			referenceSet.add(new WeakReference<Object>(receiver));
		}
	}

	/**
	 * Lookup all receivers of a given type.
	 * @param <T>
	 * 
	 * @param <T>
	 *            Receiver type
	 * @param receiverType
	 *            Receiver type class
	 * @return the receiver instances if present in the registry or
	 *         <code>null</code>
	 */
	public <T> Set<T> lookupReceivers(Class<T> receiverType) {
		if (receivers.containsKey(receiverType)) {
			Set<WeakReference<?>> referenceSet = receivers.get(receiverType);
			if (referenceSet == null) {
				return null;
			} else {
				Set<T> resultSet = new HashSet<T>();
				T receiver = null;
				for (WeakReference<?> reference : referenceSet) {
					receiver = (T) reference.get();
					if (receiver != null) {
						resultSet.add(receiver);
					}
				}
				return resultSet;
			}
		}
		return null;
	}
}
