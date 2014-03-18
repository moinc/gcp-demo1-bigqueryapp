package nl.gcompany.bigqueryapp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.QueryRequest;
import com.google.api.services.bigquery.model.QueryResponse;

public class Main {

	// Some generic objects needed to connect to the BigQuery API:
	private static final String BIGQUERY_SCOPE = "https://www.googleapis.com/auth/bigquery";
	// Note that we use NetHttpTransport in this standalone app. On App Engine
	// we would use UrlFetchTransport instead.
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	// Copied from our project in the Developer Console:
	private static final String PROJECT_ID = "proven-mystery-518";
	private static final String P12 = "14e3be9060a23e21c0f5f293814d37fa40b9f0b5-privatekey.p12";
	private static final String SERVICE_ACCOUNT_EMAIL = "309982918366-diggo2jqjaicldhalu4n38ak8djqscr6@developer.gserviceaccount.com";

	public static GoogleCredential createGoogleCredential() throws IOException,
			GeneralSecurityException, URISyntaxException {

		// Open the certificate file
		URL theCertificate = Main.class.getResource(P12);
		File privateKey = new File(theCertificate.toURI());

		// Create a new GoogleCredential
		// The GoogleCredential requires:
		// - A transport mechanism, in this case HTTP.
		// - A JsonFactory implementation which takes care of serialization and
		// deserialization.
		// - The Serviceaccount copied from our project in the developer
		// console.
		// - A scope, in this case it is the BigQuery service.
		// - The certificate file. This is also downloaded from our project in
		// the developer console.
		return new GoogleCredential.Builder()
				.setTransport(TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountScopes(
						Arrays.asList(new String[] { BIGQUERY_SCOPE }))
				.setServiceAccountPrivateKeyFromP12File(privateKey).build();

	}

	public static void main(String[] args) throws IOException,
			GeneralSecurityException, URISyntaxException {

		// Create an instance of the BigQuery API Client.
		// The client requires:
		// - A transport mechanism, in this case HTTP.
		// - A JsonFactory implementation which takes care of serialization and
		// deserialization.
		// - An initialized GoogleCredential instance
		// - The project id (which is not required but good practice)
		Bigquery bigQuery = new Bigquery.Builder(TRANSPORT, JSON_FACTORY,
				createGoogleCredential()).setApplicationName(PROJECT_ID)
				.build();

		// Let's construct a query
		String sql = "SELECT count(*) AS NUMBER_OF_RECORDS FROM [dataset_1.medals]";

		// Create the request
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setQuery(sql);

		// And finally, it is time to send the query to BigQuery.
		// Notice that the Project ID is in the request and it is used by
		// BigQuery to determine where the bill goes for this query.
		QueryResponse queryResponse = bigQuery.jobs()
				.query(PROJECT_ID, queryRequest).execute();

		// And we print the results
		Utilities.displayTheResults(queryResponse);

	}

}
