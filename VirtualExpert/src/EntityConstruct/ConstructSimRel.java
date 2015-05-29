package EntityConstruct;

import index.Createindex;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import OperOnDb.GraphDb;
import SearchInDb.SearchExperts;
import SearchInDb.SearchInDb;
import SearchInDb.SearchPaper;

public class ConstructSimRel extends SearchInDb {

	public  String stringFilter(String test){
		char[] chars = test.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			if(!((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122)|| (chars[i] >= 48 && chars[i] <= 57) || (chars[i] >= 65 && chars[i] <= 90))){
				//System.out.print(chars[i]);
				chars[i]=' ';
			}
		}
		test=new String(chars);
		return test;
	}

	public void constructRel() throws IOException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("开始分析文本:textId:" +" 1 "+ df.format(new Date()));
		try (Transaction tx = graphDb.beginTx()) {
			Createindex cindex = new Createindex();
			Label labelIndex = DynamicLabel.label("paperIndex");
			Label labelExpert = DynamicLabel.label("expert");
			for(int i=1;i<=68765;i++){
				if(i%2000==0){
					System.out.println("开始分析文本:textId:" +i+ df.format(new Date()));
				}
			for (org.neo4j.graphdb.Node node : graphDb
					.findNodesByLabelAndProperty(labelIndex, "paperIndex", i)) {
				String keys[];
				String keyReal="";
				keys = (String[]) node.getProperty("keys");
				String abs=(String)node.getProperty("abs");	
				for(int j=0;j<keys.length;j++){
					//System.out.println(keys[j]);
					keyReal+=keys[j];
				}
				List<List<String>> result= new ArrayList<List<String>>();
				String a=keyReal+abs;
				if(a.equalsIgnoreCase("")) continue;
				a=stringFilter(a);
				System.out.println(a);
				result=cindex.findByAbsandKey(a);
				if(result==null){
					result=cindex.findByKey(keyReal);
					System.out.println(i+":error:error");
				}
				if(result==null){
					System.out.println(i+":error:error:error:error");
					continue;
				}
				SearchPaper sPaper=new SearchPaper();
				ArrayList<Node> expertNodes=sPaper.searchExpertByPaper(node.getId());
				//System.out.println(i);
				Node expertNode=expertNodes.get(0);
				List<String> simExperts=result.get(1);
				List<String> simPapers=result.get(0);
				for(String temp:simExperts){
					for(org.neo4j.graphdb.Node simExpert : graphDb
							.findNodesByLabelAndProperty(labelExpert, "expertName", temp)){
						Long nodeId=simExpert.getId();
						Long currentNodeId=expertNode.getId();
						if(currentNodeId==nodeId){							
						}else{
						expertNode.createRelationshipTo(simExpert, RelTypes.SIMINTEREST);
						}
					}
				}
				for(String temp:simPapers){
					Integer textId=Integer.parseInt(temp);
					//System.out.println("simtext of "+ i+":"+textId);
					for(org.neo4j.graphdb.Node simPaper : graphDb
							.findNodesByLabelAndProperty(labelIndex, "paperIndex", textId)){
						Long nodeId=simPaper.getId();
						Long currentNodeId=node.getId();
						if(currentNodeId==nodeId){							
						}else{
							node.createRelationshipTo(simPaper, RelTypes.SIMPAPER);
						}
						
					}
				}
			}
			}
			tx.success();
		}
	}
	public static void main(String argv[]) throws IOException, ParseException{
		ConstructSimRel cSRel=new  ConstructSimRel();
		cSRel.constructRel();	
	}

}
