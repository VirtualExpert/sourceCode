package EntityConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import SearchInDb.SearchExperts;
import SearchInDb.SearchInDb;

public class CreateAreaRel extends SearchInDb {
	public static void main(String argv[]){
		CreateAreaRel cRel=new CreateAreaRel();
		cRel.setExpertKeysMap();
	}
    public void setExpertKeysMap(){
	  try (Transaction tx = graphDb.beginTx()) {
		  for(int i=0;i<74787;i++){
			  long nodeId=i;
			  Node node=graphDb.getNodeById(nodeId);			 
			  if(node.hasProperty("expertIndex")){
				  node.setProperty("type","expert");
				  SearchExperts sExpert=new SearchExperts();
				  ArrayList<Node> result=sExpert.searchPaperByExpert(node.getId());
				  HashMap<String,Integer> keyMap=new HashMap<String,Integer>();
				  for(Node temp:result){
					  String keys[]=(String[]) temp.getProperty("keys");
					  for(int j=0;j<keys.length;j++){
						  if(keyMap.containsKey(keys[j])){
							  Integer count=keyMap.get(keys[j]);
							  count++;
							  keyMap.put(keys[j], count);
						  }else{
							  keyMap.put(keys[j], 1);
						  }
					  }
				  }
				    List<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(keyMap.entrySet());
				    System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
				    Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>()
				      { 
				    	    public int compare(Map.Entry<String, Integer> o1,  
				    	            Map.Entry<String, Integer> o2) {  
				    	        return ( o2.getValue()-o1.getValue());  
				    	    }  
				      });
				    ArrayList<String> keys=new ArrayList<String>();
				    for(int k=0;k<5&&k<list_Data.size();k++){
				    	Map.Entry<String, Integer> entry=list_Data.get(k);
				    	String key=(String)entry.getKey();
				    	keys.add(key);
				    }
				    final int size=keys.size();
				    String keysArray[]=(String[])keys.toArray(new String[size]);
				    node.setProperty("keys", keysArray);
			  }
		  }
		  tx.success();
	  }
	  
  }
}
