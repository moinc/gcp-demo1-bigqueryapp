package nl.gcompany.bigqueryapp;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;

public class Utilities {
	public static void displayTheResults(QueryResponse queryResponse) {
		System.out.println("Results:");
		System.out.print("\n");
		TableSchema schema = queryResponse.getSchema();
		int j = 0;
		for (TableFieldSchema field : schema.getFields()) {
			if (j > 0) {
				System.out.print("\t");
			}
			System.out.print(field.getName());
			j++;
		}
		System.out.print("\n");
		for (TableRow row : queryResponse.getRows()) {
			int i = 0;
			for (TableCell cell : row.getF()) {
				Object value = cell.getV();
				if (value == null) {
					value = "NULL";
				}
				if (i > 0) {
					System.out.print("\t");
				}
				System.out.print(value);
				i++;
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
}
