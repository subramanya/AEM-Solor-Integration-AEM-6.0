package com.adobe.aem.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;

public final class SolrUtils {
	// private constructor to avoid unnecessary instantiation of the class
	private SolrUtils() {
	}


	/**
	 * This method is used to extract the tags from the content page
	 * 
	 * @param pageContent
	 * @return Array of tags which are attached to the page. Empty array if no
	 *         tags are attached
	 */
	public static String[] getPageTags(Resource pageContent) {
		Page page = pageContent.getParent().adaptTo(Page.class);
		Tag tags[] = page.getTags();
		String tagsArray[] = new String[tags.length];
		for (int i = 0; i < tags.length; i++) {
			Tag tag = tags[i];
			tagsArray[i] = tag.getTitle();
		}
		return tagsArray;
	}


	/**
	 * This method converts jcr formatted date to Solr specification format
	 * 
	 * @param Takes input as Calendar
	 * @return Solr formatted date of type string
	 */
	public static String solrDate(Calendar cal) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"YYYY-MM-DD'T'hh:mm:ss");
		return dateFormat.format(cal.getTime()) + "Z";
	}

	/**
	 * This method returs "" if string is null.
	 * 
	 * @param Takes
	 *            input as string
	 * @return String value. if string value is "null" then ""
	 */
	public static String checkNull(String property) {
		if (StringUtils.isEmpty(property)) {
			return "";
		}
		return property;

	}

}
