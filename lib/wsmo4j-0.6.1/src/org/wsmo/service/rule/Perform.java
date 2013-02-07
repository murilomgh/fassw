package org.wsmo.service.rule;

import org.wsmo.common.IRI;


public interface Perform extends Rule {

    	
    	/**
    	 */
    	public abstract IRI getPerformIRI();

        	
        	/**
        	 */
        	public abstract void setPerformIRI(IRI iri);
        	
    	

}
