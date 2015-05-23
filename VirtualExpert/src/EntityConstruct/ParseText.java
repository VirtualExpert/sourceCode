package EntityConstruct;

import java.io.File;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;

import Entity.ExpertNode;
import Entity.ExpertNodeMap;
import OperOnDb.GraphDb;
import Relationship.RelMap;

public class ParseText extends GraphDb{
	ExpertNodeMap expertMap;
	RelMap cooperatorRel;
	//@Test
	public static void main(String [] argvs){
		ParseText test=new ParseText();
		test.parseText();
//		GraphDb.deleteFileOrDirectory(new File(GraphDb.DB_PATH));
//		GraphDb.startDb();
//		GraphDb.prepare();
//		test.createExpertNodeInDb();
//		test.createCooperatorRel();
	}
	public void createExpertNodeInDb() {
		try (Transaction tx = graphDb.beginTx()) {
			Label labelName = DynamicLabel.label("expert");
			Label labelIndex = DynamicLabel.label("index");
			org.neo4j.graphdb.Node newNode = null;
			// Create some nodes
			for (ExpertNode iter :expertMap.getAllNodes()) {
				newNode = null;
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(labelIndex, "index",
								iter.hashCode())) {
					newNode = node;					
				}
				if (newNode == null) {
					newNode = graphDb.createNode(labelName);
					newNode.addLabel(labelIndex);
					newNode.setProperty("expertName", iter.getExpertName());
					newNode.setProperty("unit", iter.getUnit());
					newNode.setProperty("expert_org", iter.getExpert_org());
					newNode.setProperty("index", iter.hashCode());
				}

			}
			tx.success();
		}
	}
	public void createCooperatorRel() {
		Label label = DynamicLabel.label("index");
		try (Transaction tx = graphDb.beginTx()) {
			org.neo4j.graphdb.Node tempCooperNode = null;
			org.neo4j.graphdb.Node tempCoopNode = null;
			for (Relationship.RelMap.PairsOfNode iter : cooperatorRel.getAll()) {
				// ProjectNode
				// proNode=this.projectNodeMap.getNode(iter.getNode1Index());
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(label, "index",
								iter.getNode1Index())) {
					tempCooperNode = node;
				}
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(label, "index",
								iter.getNode2Index())) {
					tempCoopNode = node;
				}
				tempCooperNode.createRelationshipTo(tempCoopNode,
						RelTypes.COOPRERATOR);
				tempCoopNode.createRelationshipTo(tempCooperNode,
						RelTypes.COOPRERATOR);
			}
			tx.success();
		}

	}
	public void parseText(){
		expertMap=new ExpertNodeMap();
		cooperatorRel=new RelMap();
		ReadFile readFile=new ReadFile();
		StringBuilder stringContainer=readFile.getString();
		String abs;
		String author_cn;
		String unit;
		String journal_cn;
		String app_date;
		String key;
		String title;
		String expert_name;
		String expert_org;
		while(stringContainer.length()>50){
		Integer start=stringContainer.indexOf("{");
		Integer end=stringContainer.indexOf("},");		
		String textUnit=stringContainer.substring(start+1,end);
		// get abs
		abs=getTextDetail("abs","author_cn",textUnit);		
		// get author_cn
		author_cn=getTextDetail("author_cn","unit",textUnit);		
		// get unit	
	    unit=getTextDetail("unit","journal_cn",textUnit);			
        // get journal	
		journal_cn=getTextDetail("journal_cn","app_date",textUnit);	
		// get app_date
		app_date=getTextDetail("app_date","key",textUnit);		
		// get key			
		key=getTextDetail("key","title",textUnit);		
		// get title
		title=getTextDetail("title","expert_name",textUnit);
		// get expertname
		expert_name=getTextDetail("expert_name","expert_org",textUnit);
		// get expertorg	
		Integer expertStart=textUnit.indexOf("expert_org");
		expert_org=textUnit.substring(expertStart+15, textUnit.length()-3);
		//System.out.println(expert_org);		
		// delete parsed text
		String [] keys=key.split(";");
		for(int i=0;i<keys.length;i++){
			keys[i]=keys[i].replaceAll(" ","");
			//System.out.println(keys[i]);
		}
		String [] units=unit.split(";");
		for(int i=0;i<units.length;i++){
			units[i]=units[i].replaceAll(" ","");
			//System.out.println(units[i]);
		}
		String [] experts=author_cn.split(";");
		for(int i=0;i<experts.length;i++){
			experts[i]=experts[i].replaceAll(" ","");
			ExpertNode expert=new ExpertNode(experts[i],units[0],expert_org);
			if(expertMap.checkNodeExist(experts[i])){				
			}else{
				expertMap.addNode(expert);
			}
			//System.out.println(experts[i]);
		}
		for(int i=0;i<experts.length;i++){
			for(int j=i;j<experts.length;j++){
				cooperatorRel.addOneRel(experts[i].hashCode(), experts[j].hashCode());
			}
		}

		stringContainer.delete(start,end+2);
		
		}
	}
	public String getTextDetail(String startStr,String endStr,String baseStr){
		String result = "";
		Integer start=baseStr.indexOf(startStr);
		Integer end=baseStr.indexOf(endStr);
		try {
			result=baseStr.substring(start+startStr.length()+5, end-7);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
}
