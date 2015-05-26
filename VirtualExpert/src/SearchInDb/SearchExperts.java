package SearchInDb;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import OperOnDb.GraphDb;

public class SearchExperts extends GraphDb {
	public ArrayList<Node> searchByName(String methodName) {
		Label label = DynamicLabel.label("expert");
		ArrayList<Node> resultNodes = new ArrayList<Node>();
		try (Transaction tx = graphDb.beginTx()) {
			for (Node node : graphDb.findNodesByLabelAndProperty(label,
					"expertName", methodName)) {
				resultNodes.add(node);
				tx.success();
			}

		}
		return resultNodes;
	}

	@Test
	public void testList(){
		ArrayList<String> test=new ArrayList<String>();
		for(int i=100;i>0;i--){
			test.add(String.valueOf(i));
		}
		String strs[]=(String[])test.toArray(new String[100]);
		for(int i=0;i<100;i++){
			System.out.println(strs[i]);
		}
	}
	public static void main(String [] argv) {
		GraphDb.deleteFileOrDirectory(new File(GraphDb.DB_PATH));
		GraphDb.startDb();
		GraphDb.prepare();
		try (Transaction tx = graphDb.beginTx()) {
			Label labelExpert = DynamicLabel.label("expert");
			Label labelIndex = DynamicLabel.label("index");
			Label labelPaper = DynamicLabel.label("paper");
			Node newNode1 = graphDb.createNode(labelExpert);
			newNode1.addLabel(labelIndex);
			newNode1.setProperty("index", 1);
			ArrayList<String> test=new ArrayList<String>();
			for(int i=100;i>0;i--){
				test.add(String.valueOf(i));
			}
			tx.success();
		}
			
	}
}
