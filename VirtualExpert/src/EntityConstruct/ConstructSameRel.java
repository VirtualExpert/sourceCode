package EntityConstruct;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;

import OperOnDb.GraphDb;

public class ConstructSameRel extends GraphDb {
	@Test
	public void findMaxValue() {
		ArrayList<Double> sameVector = new ArrayList<Double>();
		try (Transaction tx = graphDb.beginTx()) {
			Label labelIndex = DynamicLabel.label("paperIndex");
			String keys1[] = new String[1];
			Double values1[] = new Double[1];
			for (org.neo4j.graphdb.Node node : graphDb
					.findNodesByLabelAndProperty(labelIndex, "paperIndex", 1)) {
				keys1 = (String[]) node.getProperty("keys");
				values1 = (Double[]) node.getProperty("values");
			}
			for (int i = 2; i < 7710; i++) {
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(labelIndex, "paperIndex",
								i)) {
					String keys2[] = (String[]) node.getProperty("keys");
					Double values2[] = (Double[]) node.getProperty("values");
					Double sum = 0.0;
					Double model1 = 0.0;
					Double model2 = 0.0;
					Double same=0.0;
					for (int j = 0; j < keys1.length; j++) {
						String key1 = keys1[j];
						if (!key1.equals("keyModel")) {
							for (int k = 0; k < keys2.length; k++) {
								if (key1.equals(keys2[k])) {
									sum += values1[j] * values2[k];
								}
								if (keys2[k].equals("keyModel")) {
									model2 = values2[k];
								}
							}
						} else {
							model1 = values1[j];
						}
					}
					same=sum/model1*model2;
					sameVector.add(same);
				}
			}
			tx.success();
		}
		Collections.sort(sameVector);
		for(Double temp:sameVector){
			System.out.println(temp);
		}
	}

}
