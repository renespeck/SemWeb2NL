/**
 * 
 */
package org.aksw.avatar;

import java.util.Set;
import java.util.TreeSet;

import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.dllearner.kb.sparql.SparqlEndpoint;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * @author Lorenz Buehmann
 *
 */
public class DBpediaPropertyAnalyzer {
	
	public static void main(String[] args) throws Exception {
		SparqlEndpoint endpoint = SparqlEndpoint.getEndpointDBpedia();
		QueryExecutionFactory qef = new QueryExecutionFactoryHttp(endpoint.getURL().toString(), endpoint.getDefaultGraphURIs());
		//get all object properties
		Set<String> properties = new TreeSet<>();
		String query = "SELECT ?p WHERE {?p a <http://www.w3.org/2002/07/owl#ObjectProperty>}";
		QueryExecution qe = qef.createQueryExecution(query);
		ResultSet rs = qe.execSelect();
		QuerySolution qs;
		while(rs.hasNext()){
			qs = rs.next();
			properties.add(qs.getResource("p").getURI());
		}
		qe.close();
		//get a value for each property
		for (String property : properties) {
			query = "SELECT ?o WHERE {?s <" + property + "> ?o.} LIMIT 1";
			qe = qef.createQueryExecution(query);
			rs = qe.execSelect();
			if(rs.hasNext()){
				qs = rs.next();
				if(qs.get("o").isURIResource()){
					String objectURI = qs.getResource("o").getURI();
					if(objectURI.contains("__")){
						System.out.println(property);
					}
				}
			}
			qe.close();
		}
		
	}

}
