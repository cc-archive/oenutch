package org.creativecommons.learn;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class CCLEARN {

    protected static final String uri ="http://learn.creativecommons.org/ns#";

    /** returns the URI for this schema
        @return the URI for this schema
    */
    public static String getURI()
        { return uri; }

    protected static final Resource resource( String local )
        { return ResourceFactory.createResource( uri + local ); }

    protected static final Property property( String local )
        { return ResourceFactory.createProperty( uri, local ); }

    public static final Resource resource = resource("Resource");
	public static final Resource feed = resource("Feed");

	public static final Property feedType = property("feedType");
	public static final Property source = property("source");

}