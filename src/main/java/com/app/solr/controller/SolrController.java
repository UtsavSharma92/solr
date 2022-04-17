package com.app.solr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.solr.service.SolrService;

@RestController
public class SolrController {

	@Autowired
	SolrService solrService;

	@GetMapping(value = "solr/getDocumentResult")
	public String getSolrDocumentResult(@RequestBody String request) throws Exception {

		String result = solrService.getDocumentResult(request);

		return result;

	}

}
