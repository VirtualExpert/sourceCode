package Entity;
import java.util.Collection;
import java.util.HashMap;

public class PaperNodeMap {
		public HashMap<Integer,PaperNode> paperMap;
		public PaperNodeMap(){
			paperMap=new HashMap<Integer,PaperNode>();
		}
		public boolean checkNodeExist(String expertName){
			return paperMap.containsKey(expertName.hashCode());
		}
		public void addNode(PaperNode paper){
			paperMap.put(paper.hashCode(), paper);
		}
		public Collection<PaperNode> getAllNodes() {
			return this.paperMap.values();
		}

}
