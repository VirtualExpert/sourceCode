package SearchInDb;

import java.util.ArrayList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;


public class SearchPaper extends SearchInDb{
	public ArrayList<Node> searchByName(Integer textId) {
		Label label = DynamicLabel.label("paperIndex");
		ArrayList<Node> resultNodes = new ArrayList<Node>();
		try (Transaction tx = graphDb.beginTx()) {
			for (Node node : graphDb.findNodesByLabelAndProperty(label,
					"paperIndex", textId)) {
				resultNodes.add(node);
				tx.success();
			}
		}
		return resultNodes;
	}

	public ArrayList<Node> searchSamePaperByPaper(Long nodeId) {
		ArrayList<Node> resultNodes = new ArrayList<Node>();
		try (Transaction tx = graphDb.beginTx()) {
			Node expertNode = graphDb.getNodeById(nodeId);
			for (Relationship iter : expertNode.getRelationships(
					RelTypes.SIMPAPER, Direction.INCOMING)) {
				resultNodes.add(iter.getStartNode());
			}
			tx.success();
			return resultNodes;
		}
	}

	public ArrayList<Node> searchExpertByPaper(Long nodeId) {
		ArrayList<Node> resultNodes = new ArrayList<Node>();
		try (Transaction tx = graphDb.beginTx()) {
			Node paperNode = graphDb.getNodeById(nodeId);
			for (Relationship iter : paperNode.getRelationships(RelTypes.WRITE,
					Direction.INCOMING)) {
				resultNodes.add(iter.getStartNode());
			}
			tx.success();
			return resultNodes;
		}
	}

}
