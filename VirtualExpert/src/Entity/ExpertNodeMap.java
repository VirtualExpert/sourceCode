package Entity;

import java.util.Collection;
import java.util.HashMap;


public class ExpertNodeMap {
	public HashMap<Integer,ExpertNode> expertMap;
	public ExpertNodeMap(){
		expertMap=new HashMap<Integer,ExpertNode>();
	}
	public boolean checkNodeExist(String expertName){
		return expertMap.containsKey(expertName.hashCode());
	}
	public void addNode(ExpertNode expert){
		expertMap.put(expert.expertName.hashCode(), expert);
	}
	public Collection<ExpertNode> getAllNodes() {
		return this.expertMap.values();
	}
}
