package EntityConstruct;

import index.Createindex;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryParser.ParseException;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.wltea.analyzer.lucene.IKAnalyzer;

import Entity.ExpertNode;
import Entity.ExpertNodeMap;
import Entity.PaperNode;
import Entity.PaperNodeMap;
import OperOnDb.GraphDb;
import Relationship.RelMap;

public class ParseText extends GraphDb {
	Createindex cindex = new Createindex();
	ExpertNodeMap expertMap;
	PaperNodeMap paperMap;
	RelMap writeRel;
	RelMap cooperatorRel;
	HashSet<String> titleSet;
	// 每篇文章中每个词出现的次数
	HashMap<Integer, HashMap<String, Integer>> absKeys;
	HashMap<Integer, HashMap<String, Double>> absKeysTFIDF;
	HashMap<String, Integer> allAbsKeys;

	public void fenci(Integer textId, String text) throws IOException {
		// 创建分词对象
		int totalnums = 0;
		IKAnalyzer anal = new IKAnalyzer(true);
		StringReader reader = new StringReader(text);
		// 分词
		TokenStream ts = anal.tokenStream(text, reader);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		HashMap<String, Integer> singlePaperTimes = new HashMap<String, Integer>();
		// 遍历分词数据
		while (ts.incrementToken()) {
			// System.out.print(term.toString()+"|");
			totalnums++;
			String absKeyWord = term.toString();
			if (singlePaperTimes.containsKey(absKeyWord)) {
				Integer times = singlePaperTimes.get(absKeyWord);
				times++;
				singlePaperTimes.put(absKeyWord, times);
			} else {
				singlePaperTimes.put(absKeyWord, 1);
			}
		}
		singlePaperTimes.put("totalnums", totalnums);
		reader.close();

		Set<String> absKeysSet;
		absKeysSet = singlePaperTimes.keySet();
		for (String temp : absKeysSet) {
			if (!temp.equalsIgnoreCase("totalnums")) {
				if (allAbsKeys.containsKey(temp)) {
					Integer times = allAbsKeys.get(temp);
					times++;
					allAbsKeys.put(temp, times);
				} else {
					allAbsKeys.put(temp, 1);
				}
			}
		}
		absKeys.put(textId, singlePaperTimes);
	}

	public void setTFIDF() {
		// absKeysTFIDF = new HashMap<Integer, HashMap<String, Double>>();
		try (Transaction tx = graphDb.beginTx()) {
			Integer paperNum = absKeys.size();
			Iterator<Entry<Integer, HashMap<String, Integer>>> iter = absKeys
					.entrySet().iterator();
			while (iter.hasNext()) {
				// HashMap<String, Double> singlePaperTFIDF = new
				// HashMap<String, Double>();
				ArrayList<String> keys = new ArrayList<String>();
				ArrayList<Double> values = new ArrayList<Double>();
				Map.Entry entry = (Map.Entry) iter.next();
				Integer textId = (Integer) entry.getKey();
				HashMap<String, Integer> value = (HashMap<String, Integer>) entry
						.getValue();
				Iterator<Entry<String, Integer>> iter1 = value.entrySet()
						.iterator();
				int wordCount = value.get("totalnums");
				while (iter1.hasNext()) {
					Map.Entry subentry = (Map.Entry) iter1.next();
					String subkey = (String) subentry.getKey();
					if (!subkey.equalsIgnoreCase("totalnums")) {
						Integer subvalue = (Integer) subentry.getValue();
						Integer showsPaperNum = allAbsKeys.get(subkey);
						Double TF = (double) subvalue.intValue() / wordCount;
						Double IDF = java.lang.Math.log10(paperNum
								.doubleValue() / showsPaperNum + 0.1);
						Double TFIDF = TF * IDF;
						// singlePaperTFIDF.put(subkey, TFIDF);
						keys.add(subkey);
						values.add(TFIDF);
						// System.out.println(subkey+":"+subvalue);
					}
				}
				Double sum = 0.0;
				for (Double temp : values) {
					sum += temp * temp;
				}
				sum = Math.sqrt(sum);
				keys.add("keyModel");
				values.add(sum);
				final int keysSize = keys.size();
				final int valuesSize = values.size();
				String[] keyArray = keys.toArray(new String[keysSize]);
				Double[] valueArray = values.toArray(new Double[valuesSize]);
				Label labelIndex = DynamicLabel.label("paperIndex");
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(labelIndex, "paperIndex",
								textId)) {
					node.setProperty("keys", keyArray);
					node.setProperty("values", valueArray);
				}
				// System.out.println(key+":"+value);
			}
			tx.success();
		}
	}

	// public void storeTFIDFInDb() {
	// Iterator<Entry<Integer, HashMap<String, Double>>> iterOut = absKeysTFIDF
	// .entrySet().iterator();
	// try (Transaction tx = graphDb.beginTx()) {
	// while (iterOut.hasNext()) {
	// Map.Entry entryOut = (Map.Entry) iterOut.next();
	// Integer textId=(Integer)entryOut.getKey();
	// HashMap<String,Double>
	// ArrayList<String> keys = new ArrayList<String>();
	// ArrayList<Double> values = new ArrayList<Double>();
	// while (iter.hasNext()) {
	// Map.Entry entry = (Map.Entry) iter.next();
	// String key = (String) entry.getKey();
	// Double value = (Double) entry.getValue();
	// keys.add(key);
	// values.add(value);
	// }
	// final int keysSize = keys.size();
	// final int valuesSize = values.size();
	// String[] keyArray = keys.toArray(new String[keysSize]);
	// Double[] valueArray = values.toArray(new Double[valuesSize]);
	// Label labelIndex = DynamicLabel.label("paperIndex");
	// org.neo4j.graphdb.Node newNode = null;
	// for (org.neo4j.graphdb.Node node : graphDb
	// .findNodesByLabelAndProperty(labelIndex, "paperIndex",
	// textId)) {
	// newNode = node;
	// }
	// }
	// tx.success();
	// }
	//
	// }

	// @Test
	public static void main(String[] argvs) throws IOException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParseText test = new ParseText();
		System.out.println("开始分析文本" + df.format(new Date()));
		GraphDb.deleteFileOrDirectory(new File(GraphDb.DB_PATH));
		GraphDb.startDb();
		GraphDb.prepare();
		test.parseText();
		System.out.println("开始存专家节点" + df.format(new Date()));
		test.createExpertNodeInDb();
		System.out.println("开始存文献节点" + df.format(new Date()));
		test.createPaperNodeInDb();
		System.out.println("开始创建关系" + df.format(new Date()));
		test.createCooperatorRel();
		System.out.println("开始创建写者关系" + df.format(new Date()));
		test.createWriteRel();
		// System.out.println("开始计算tfidf"+df.format(new Date()));
		// test.setTFIDF();
		System.out.println("计算结束" + df.format(new Date()));
		ConstructSimRel cSRel=new  ConstructSimRel();
		cSRel.constructRel();
		CreateAreaRel cRel=new CreateAreaRel();
		cRel.setExpertKeysMap();
	}

	public void createExpertNodeInDb() {
		try (Transaction tx = graphDb.beginTx()) {
			Label labelName = DynamicLabel.label("expert");
			Label labelIndex = DynamicLabel.label("expertIndex");
			org.neo4j.graphdb.Node newNode = null;
			// Create some nodes
			for (ExpertNode iter : expertMap.getAllNodes()) {
				newNode = null;
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(labelIndex, "expertIndex",
								iter.hashCode())) {
					newNode = node;
				}
				if (newNode == null) {
					newNode = graphDb.createNode(labelName);
					newNode.addLabel(labelIndex);
					newNode.setProperty("expertName", iter.getExpertName());
					newNode.setProperty("units", iter.getUnits());
					newNode.setProperty("expert_org", iter.getExpert_org());
					newNode.setProperty("expertIndex", iter.hashCode());
					newNode.setProperty("type","expert");					
				}
			}
			tx.success();
		}
	}

	public void createPaperNodeInDb() {
		try (Transaction tx = graphDb.beginTx()) {
			Label labelName = DynamicLabel.label("paper");
			Label labelIndex = DynamicLabel.label("paperIndex");
			org.neo4j.graphdb.Node newNode = null;
			// Create some nodes
			for (PaperNode iter : paperMap.getAllNodes()) {
				// System.out.println("create paper:"+iter.hashCode());
				newNode = null;
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(labelIndex, "paperIndex",
								iter.hashCode())) {
					newNode = node;
				}
				if (newNode == null) {
					newNode = graphDb.createNode(labelName);
					newNode.addLabel(labelIndex);
					newNode.setProperty("expertName", iter.getExpertName());
					newNode.setProperty("units", iter.getUnits());
					newNode.setProperty("expert_org", iter.getExpert_org());
					newNode.setProperty("paperIndex", iter.hashCode());
					newNode.setProperty("abs", iter.getAbs());
					newNode.setProperty("authors", iter.getAuthor_cn());
					newNode.setProperty("title", iter.getTitle());
					newNode.setProperty("journal_cn", iter.getJournal_cn());
					// System.out.println(iter.hashCode());
					newNode.setProperty("keys", iter.getMainkeys());
					// newNode.setProperty("mainKeys",iter.getMainkeys());
					newNode.setProperty("app_date", iter.getApp_date());
				}

			}
			tx.success();
		}
	}

	public void createCooperatorRel() {
		Label label = DynamicLabel.label("expertIndex");
		try (Transaction tx = graphDb.beginTx()) {
			Iterator<Relationship.RelMap.PairsOfNode> iterReal=cooperatorRel.getAll().iterator();
			while (iterReal.hasNext()) {
				org.neo4j.graphdb.Node tempCooperNode = null;
				org.neo4j.graphdb.Node tempCoopNode = null;
				Relationship.RelMap.PairsOfNode iter=iterReal.next();
				// ProjectNode
				// proNode=this.projectNodeMap.getNode(iter.getNode1Index());
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(label, "expertIndex",
								iter.getNode1Index())) {
					tempCooperNode = node;
				}
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(label, "expertIndex",
								iter.getNode2Index())) {
					tempCoopNode = node;
				}
				if (tempCoopNode != null && tempCooperNode != null) {
					tempCooperNode.createRelationshipTo(tempCoopNode,
							RelTypes.COOPRERATOR);
					tempCoopNode.createRelationshipTo(tempCooperNode,
							RelTypes.COOPRERATOR);
				}
				//iterReal.remove();
			}
			tx.success();
		}

	}

	public void createWriteRel() {
		Label labelExpert = DynamicLabel.label("expertIndex");
		Label labelPaper = DynamicLabel.label("paperIndex");
		try (Transaction tx = graphDb.beginTx()) {
			for (Relationship.RelMap.PairsOfNode iter : writeRel.getAll()) {
				org.neo4j.graphdb.Node tempWriterNode = null;
				org.neo4j.graphdb.Node tempPaperNode = null;
				// ProjectNode
				// proNode=this.projectNodeMap.getNode(iter.getNode1Index());
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(labelExpert,
								"expertIndex", iter.getNode1Index())) {
					tempWriterNode = node;
				}
				for (org.neo4j.graphdb.Node node : graphDb
						.findNodesByLabelAndProperty(labelPaper, "paperIndex",
								iter.getNode2Index())) {
					tempPaperNode = node;
				}
				if (tempWriterNode != null && tempPaperNode != null) {
					tempWriterNode.createRelationshipTo(tempPaperNode,
							RelTypes.WRITE);
				}
				// System.out.println("create write rel:"+iter.getNode1Index()+"-->:"+iter.getNode2Index());
			}
			tx.success();
		}

	}

	public void parseText() throws IOException {
		titleSet = new HashSet<String>();
		expertMap = new ExpertNodeMap();
		paperMap = new PaperNodeMap();
		cooperatorRel = new RelMap();
		writeRel = new RelMap();
		absKeys = new HashMap<Integer, HashMap<String, Integer>>();
		allAbsKeys = new HashMap<String, Integer>();
		ReadFile readFile = new ReadFile();
		StringBuilder stringContainer = readFile.getString();
		String abs = null;
		String author_cn = null;
		String unit = null;
		String journal_cn = null;
		String app_date = null;
		String key = null;
		String title = null;
		String expert_name = null;
		String expert_org = null;
		Integer textId = 1;
		while (stringContainer.length() > 50) {
			Integer start = stringContainer.indexOf("{\r\n");
			Integer end = stringContainer.indexOf("},\r\n");
			if (end == -1) {
				end = stringContainer.indexOf("}");
			}
			String textUnit = stringContainer.substring(start + 1, end);
			// get abs
			Integer local1 = textUnit.indexOf("\"title\"");
			Integer local2 = textUnit.indexOf("\"key\"");
			Integer local3 = textUnit.indexOf("\"unit\"");
			if (local3 > local2) {
				abs = getTextDetail("\"abs\"", "\"app_date\"", textUnit);
				// get app_date
				app_date = getTextDetail("\"app_date\"", "\"author_cn\"",
						textUnit);
				// get author_cn
				author_cn = getTextDetail("\"author_cn\"", "\"journal_cn\"",
						textUnit);
				// get journal
				journal_cn = getTextDetail("\"journal_cn\"", "\"key\"",
						textUnit);
				// get key
				key = getTextDetail("\"key\"", "\"title\"", textUnit);
				// get title
				title = getTextDetail("\"title\"", "\"unit\"", textUnit);
				// get unit
				unit = getTextDetail("\"unit\"", "\"expert_name\"", textUnit);
				// get expertname
				expert_name = getTextDetail("\"expert_name\"",
						"\"expert_org\"", textUnit);
				// get expertorg
				Integer expertStart = textUnit.indexOf("expert_org");
				expert_org = textUnit.substring(expertStart + 15,
						textUnit.length() - 3);
				// System.out.println(expert_org);
				// delete parsed text
			} else if (local2 < local1) {
				abs = getTextDetail("\"abs\"", "\"author_cn\"", textUnit);
				// get author_cn
				author_cn = getTextDetail("\"author_cn\"", "\"unit\"", textUnit);
				// get unit
				unit = getTextDetail("\"unit\"", "\"journal_cn\"", textUnit);
				// get journal
				journal_cn = getTextDetail("\"journal_cn\"", "\"app_date\"",
						textUnit);
				// get app_date
				app_date = getTextDetail("\"app_date\"", "\"key\"", textUnit);
				// get key
				key = getTextDetail("\"key\"", "\"title\"", textUnit);
				// get title
				title = getTextDetail("\"title\"", "\"expert_name\"", textUnit);
				// get expertname
				expert_name = getTextDetail("\"expert_name\"",
						"\"expert_org\"", textUnit);
				// get expertorg
				Integer expertStart = textUnit.indexOf("expert_org");
				expert_org = textUnit.substring(expertStart + 15,
						textUnit.length() - 3);

				// System.out.println(expert_org);
				// delete parsed text
			} else {
				// get title
				title = getTextDetail("\"title\"", "\"abs\"", textUnit);
				abs = getTextDetail("\"abs\"", "\"author_cn\"", textUnit);
				// get author_cn
				author_cn = getTextDetail("\"author_cn\"", "\"unit\"", textUnit);
				// get unit
				unit = getTextDetail("\"unit\"", "\"journal_cn\"", textUnit);
				// get journal
				journal_cn = getTextDetail("\"journal_cn\"", "\"app_date\"",
						textUnit);
				// get app_date
				app_date = getTextDetail("\"app_date\"", "\"key\"", textUnit);
				// get key
				key = getTextDetail("\"key\"", "\"expert_name\"", textUnit);

				// get expertname
				expert_name = getTextDetail("\"expert_name\"",
						"\"expert_org\"", textUnit);
				// get expertorg
				Integer expertStart = textUnit.indexOf("expert_org");
				expert_org = textUnit.substring(expertStart + 15,
						textUnit.length() - 3);
				// System.out.println(expert_org);
				// delete parsed text
			}
			if (titleSet.contains(title)) {
				//System.out.println(title);
				stringContainer.delete(0, end + 3);
				continue;
			} else {
				titleSet.add(title);
				// fenci(textId, key);
				String[] keys = key.split(";");
				for (int i = 0; i < keys.length; i++) {
					keys[i] = keys[i].replaceAll(" ", "");
					// System.out.println(keys[i]);
				}
				String[] units = unit.split(";");
				for (int i = 0; i < units.length; i++) {
					units[i] = units[i].replaceAll(" ", "");
					// System.out.println(units[i]);
				}
				String[] experts = author_cn.split(";");
				for (int i = 0; i < experts.length; i++) {
					experts[i] = experts[i].replaceAll(" ", "");
					int tempindex = experts[i].indexOf("[");
					if (tempindex != -1)
						experts[i] = experts[i].substring(0, tempindex - 1);
					// System.out.println(experts[i]);
				}
				ExpertNode expert = new ExpertNode(expert_name, units,
						expert_org);
				if (!expertMap.checkNodeExist(expert_name)) {
					expertMap.addNode(expert);
				}
				writeRel.addOneRel(expert.hashCode(), textId);
				for (int i = 0; i < experts.length; i++) {
					for (int j = i + 1; j < experts.length; j++) {
						cooperatorRel.addOneRel(experts[i].hashCode(),
								experts[j].hashCode());
					}
				}
				cindex.add(textId, expert_name, textUnit);
				PaperNode paper = new PaperNode(expert_name, units, expert_org,
						abs, experts, keys, journal_cn, app_date, title, textId);
				paperMap.addNode(paper);
				textId++;
				stringContainer.delete(0, end + 3);
				if((textId%2000)==0){
					System.out.println("textId:"+textId);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("开始存专家节点" + df.format(new Date()));
					createExpertNodeInDb();
					System.out.println("开始存文献节点" + df.format(new Date()));
					createPaperNodeInDb();
//					System.out.println("开始创建关系" + df.format(new Date()));
//					createCooperatorRel();
					System.out.println("开始创建写者关系" + df.format(new Date()));
					createWriteRel();
					// System.out.println("开始计算tfidf"+df.format(new Date()));
					// test.setTFIDF();
					System.out.println("计算结束" + df.format(new Date()));
					expertMap = new ExpertNodeMap();
					paperMap = new PaperNodeMap();
					writeRel = new RelMap();					
				}
			}
		}
		System.out.println("textNum:" + textId);
	}

	public String getTextDetail(String startStr, String endStr, String baseStr) {
		String result = "";
		Integer start = baseStr.indexOf(startStr);
		Integer end = baseStr.indexOf(endStr);
		result = baseStr.substring(start + startStr.length() + 4, end - 6);
		// System.out.println(result);
		baseStr = baseStr.substring(end, baseStr.length());
		return result;
	}
}
