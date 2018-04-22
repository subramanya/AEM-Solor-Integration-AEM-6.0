package com.adobe.aem.core.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.core.SolrServerConfiguration;


/**
 * 
 * @author kavarana 
 * This servlet deletes all the indexes from the configured Solr server
 *
 */
@SlingServlet(paths = "/bin/solr/delete/all/indexes", methods="POST")
public class DeleteIndexesFromSolr extends SlingAllMethodsServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(DeleteIndexesFromSolr.class);
	@Reference
	SolrServerConfiguration solrConfigurationService;
	
	@Override
    protected void doPost(final SlingHttpServletRequest reqest,
            final SlingHttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		final String protocol = solrConfigurationService.getSolrProtocol();
		final String serverName = solrConfigurationService.getSolrServerName();
		final String serverPort = solrConfigurationService.getSolrServerPort();
		final String coreName = solrConfigurationService.getSolrCoreName();
		String URL = protocol + "://" + serverName + ":" + serverPort
				+ "/solr/" + coreName;
		HttpSolrClient server = new HttpSolrClient(URL);
		try {
			server.deleteByQuery("*:*");
			server.commit();
			server.close();
			response.getWriter().write("<h3>Deleted all the indexes from solr server </h3>");
		} catch (SolrServerException e) {
			LOG.error("Exception due to", e);
		}
		
        
    }
}
