package com.adobe.aem.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.core.SolrSearchService;
import com.adobe.aem.core.SolrServerConfiguration;

/**
 * 
 * This servlet acts as a bulk update to index content pages and assets to the
 * configured Solr server
 *
 */
@Component(immediate = true, metatype = true)
@Service(Servlet.class)
@Properties({
		@Property(name = "sling.servlet.methods", value = "GET"),
		@Property(name = "sling.servlet.paths", value = "/bin/solr/push/pages") })
public class IndexContentToSolr extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(IndexContentToSolr.class);

	@Reference
	SolrServerConfiguration solrConfigurationService;

	@Reference
	SolrSearchService solrSearchService;

	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		doPost(request, response);

	}

	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		response.setContentType("text/html");
		String indexType = request.getParameter("indexType");
		final String protocol = solrConfigurationService.getSolrProtocol();
		final String serverName = solrConfigurationService.getSolrServerName();
		final String serverPort = solrConfigurationService.getSolrServerPort();
		final String coreName = solrConfigurationService.getSolrCoreName();
		final String pagesResourcePath = solrConfigurationService
				.getContentPagePath();
		String URL = protocol + "://" + serverName + ":" + serverPort
				+ "/solr/" + coreName;
		HttpSolrClient server = new HttpSolrClient(URL);
		if (indexType.equalsIgnoreCase("indexpages")) {
			try {
				JSONArray indexPageData = solrSearchService.crawlContent(
						pagesResourcePath, "cq:PageContent");
				boolean resultindexingPages = solrSearchService
						.indexPagesToSolr(indexPageData, server);
				if (resultindexingPages == true) {
					response.getWriter()
							.write("<h3>Successfully indexed content pages to Solr server </h3>");
				} else {
					response.getWriter().write("<h3>Something went wrong</h3>");
				}
			} catch (JSONException | SolrServerException e) {
				LOG.error("Exception due to", e);
				response.getWriter()
						.write("<h3>Something went wrong. Please make sure Solr server is configured properly in Felix</h3>");
			}

		} else {
			response.getWriter().write("<h3>Something went wrong</h3>");
		}

	}

}
