package com.app.solr.SolrServiceImpl;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.solr.service.SolrService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class SolrServiceImpl implements SolrService {

	@Value("${solr.url}")
	private String solrUrl;

	public Gson gson = new Gson();

	private SolrQuery query = new SolrQuery();

	private static final Logger log = LoggerFactory.getLogger(SolrServiceImpl.class);

	public SolrClient getSolrClient(String index) {

		return new HttpSolrClient.Builder(solrUrl + index).withConnectionTimeout(10000).withSocketTimeout(60000)
				.build();
	}

	public String getDocumentResult(String request) throws Exception {

		JsonObject inputRequest = gson.fromJson(request, JsonObject.class);

		String coreName = inputRequest.get("coreName").getAsString();
		JsonArray searchFields = inputRequest.getAsJsonArray("searchFields");
		String searchType = inputRequest.get("searchType").getAsString();
		String searchItem = inputRequest.get("searchItem").getAsString();
		JsonArray searchRange = inputRequest.getAsJsonArray("searchRange");

		// log.info("Display search item:" + searchItem);

		query.clear();
		query.setStart(searchRange.get(0).getAsInt());
		query.setRows(searchRange.get(1).getAsInt());
		query.setFields("id,name, _version_ ,price_c ");
		query.setQuery("*:*");
		// log.info("main query is " + "allFields : \\" + searchItem + "\\");
		query.setFilterQueries("name:" + searchItem + " OR " + " series_t:" + searchItem);
		// query.setQuery("*:*");
		log.info("name:" + searchItem + " OR " + "series_t:" + searchItem);

		QueryResponse response = getSolrClient(coreName).query(query);

		JsonArray finalResult = gson.fromJson(gson.toJson(response.getResults()), JsonArray.class);

		JsonObject finalResultObject = new JsonObject();

		finalResultObject.add("outputDocument", finalResult);

		return gson.toJson(finalResultObject);

//		log.info("core name:" + coreName);
//
//		log.info("searchFields:" + searchFields);
//
//		log.info("searchType:" + searchType);
//
//		log.info("searchItem:" + searchItem);
//
//		log.info("searchRange:" + searchRange);

	}

}
