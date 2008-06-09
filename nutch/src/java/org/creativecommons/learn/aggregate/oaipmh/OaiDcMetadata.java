package org.creativecommons.learn.aggregate.oaipmh;


import org.creativecommons.learn.TripleStore;
import org.creativecommons.learn.feed.IResourceExtractor;
import org.creativecommons.learn.oercloud.Feed;
import org.creativecommons.learn.oercloud.OaiResource;
import org.creativecommons.learn.oercloud.Resource;
import org.dom4j.Element;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.MetadataFormat;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import thewebsemantic.NotFoundException;

public class OaiDcMetadata extends OaiMetadataFormat implements IResourceExtractor {

/*
	    <element ref="dc:title"/>
	    <element ref="dc:creator"/>
	    <element ref="dc:subject"/>
	    <element ref="dc:description"/>
	    <element ref="dc:publisher"/>
	    <element ref="dc:contributor"/>
	    <element ref="dc:date"/>
	    <element ref="dc:type"/>
	    <element ref="dc:format"/>
	    <element ref="dc:identifier"/>
	    <element ref="dc:source"/>
	    <element ref="dc:language"/>
	    <element ref="dc:relation"/>
	    <element ref="dc:coverage"/>
	    <element ref="dc:rights"/>
*/

	public OaiDcMetadata(MetadataFormat f) {
		super(f);
	}

	@Override
	public void process(Feed feed, OaiPmhServer server, String identifier) throws OAIException {
		
		// Retrieve the resource metadata from the server
		Record oai_record = server.getRecord(identifier, this.format.getPrefix());
		Element metadata = oai_record.getMetadata();
		
		// get the namespace prefix
		metadata.addNamespace("dc", "http://purl.org/dc/elements/1.1/");
		
		// load or create the Resource object
		Resource item = getResource(getNodeText(metadata, "//dc:identifier"));
		item.getSources().add(feed);

		// title
		item.setTitle(getNodeText(metadata, "//dc:title"));
		
		// creator
		item.getCreators().addAll(getNodesText(metadata, "//dc:creator"));
		
		// subject(s)
		item.getSubjects().addAll(getNodesText(metadata, "//dc:subject"));
		
		// description
		item.setDescription(getNodeText(metadata, "//dc:description"));
		
		// contributor(s)
		item.getContributors().addAll(getNodesText(metadata, "//dc:contributor"));
		
		// format(s)
		item.getFormats().addAll(getNodesText(metadata, "//dc:format"));
		
		// language(s)
		item.getLanguages().addAll(getNodesText(metadata, "//dc:language"));
		
		// type(s)
		item.getTypes().addAll(getNodesText(metadata, "//dc:type"));
		
		// source
		item.getSources().add(feed);
		
		// see also
		try {
			item.getSeeAlso().add(TripleStore.get().load(OaiResource.class, identifier));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// persist the Resource
		TripleStore.get().save(item);
	}

}
