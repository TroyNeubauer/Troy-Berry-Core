package com.troyberry.util;

import sun.misc.Cleaner;

@SuppressWarnings("restriction")
public abstract class Resource {
	
	private Cleaner cleaner = null;
	
	public final void setDeallocator(Deallocator deallocator) {
		if(cleaner != null) throw new UnsupportedOperationException("A cleaner has already been created for this object! " + this.getClass() +", "+ this.toString());
		cleaner = Cleaner.create(this, deallocator);
	}
	
	

	public final void delete() {
		cleaner.clean();
	}
}
