package fi.turku.mip.reportserver.service.impl;

import java.util.Map;

import fi.turku.mip.reportserver.service.QueryService;

/**
 * Query service implementation
 *
 * @author mip
 *
 */
public class QueryServiceImpl implements QueryService {
	// NOT A @Component because xml config

	// Map to hold the queries
	private Map<String, String> queries;

	/*
	 * (non-Javadoc)
	 * @see com.atrsoft.ascom.ac.service.QueryService#getQuery(java.lang.String)
	 */
	public String getQuery(String queryName) throws IllegalArgumentException {

		if (queries!=null && queries.containsKey(queryName)) {
			return queries.get(queryName);
		}

		throw new IllegalArgumentException("Named query "+queryName+" not found or queries not initialized.");
	}


	/**
	 * Setter for the queries map.
	 *
	 * @param queries the queries to set
	 */
	public void setQueries(Map<String, String> queries) {
		this.queries = queries;
	}

}
