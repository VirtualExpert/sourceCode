package index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.document.*;

public class Createindex {
	public static void deleteAll(File file) {

		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteAll(files[i]);
				files[i].delete();
			}

			if (file.exists()) // 如果文件本身就是目录 ，就要删除目录
				file.delete();
		}
	}

	public static void add(int textid, String expert, String keys)
			throws IOException {
		File f = new File("indexfile");
		if (textid == 1)
			deleteAll(f);
		Directory indexPath = FSDirectory.open(f);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		// 创建IndexWriter
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
				analyzer);
		IndexWriter writer = new IndexWriter(indexPath, config);

		Document doc = new Document();
		Field field = new Field("textid", "" + textid, Field.Store.YES,
				Field.Index.NO);
		doc.add(field);
		field = new Field("expert", "" + expert, Field.Store.YES,
				Field.Index.NO);
		doc.add(field);
		field = new Field("keys", "" + keys, Field.Store.YES,
				Field.Index.ANALYZED);
		doc.add(field);
		writer.addDocument(doc);
		writer.close();
		writer.unlock(indexPath);
		indexPath.close();
	}

	public List<List<String>> findByKey(String text) throws IOException,
			ParseException {
		List<List<String>> res = new ArrayList<>();
		File f = new File("indexfile");
		Directory indexPath = FSDirectory.open(f);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		QueryParser parser = new QueryParser(Version.LUCENE_36, "keys",
				analyzer);
		Query query;
		try {
			query = parser.parse(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// System.out.print("sorry");
			return null;
		}

		IndexSearcher searcher = new IndexSearcher(indexPath);
		TopDocs docs = searcher.search(query, 20);
		ScoreDoc[] hits = docs.scoreDocs;
		// System.out.println(hits.length);
		// for (ScoreDoc hit : hits) {
		// System.out.println("doc: " + hit.doc + " score: " + hit.score);
		//
		// }
		List<String> listId = new ArrayList<String>();
		List<String> listExpert = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			ScoreDoc hit = hits[i];
			if (hit.score > 0.6) {
				// System.out.println("doc: " + hit.doc + " score: " +
				// hit.score);
				Document hitDoc = searcher.doc(hits[i].doc);
				// System.out.println(hitDoc.get("keys"));
				// System.out.println(hitDoc.get("textid"));
				listId.add(hitDoc.get("textid"));
				listExpert.add(hitDoc.get("expert"));
			}
		}
		res.add(listId);
		res.add(listExpert);
		indexPath.close();
		analyzer.close();
		return res;
	}

	public List<List<String>> findByAbsandKey(String text) throws IOException,
			ParseException {
		List<List<String>> res = new ArrayList<>();
		File f = new File("indexfile");
		Directory indexPath = FSDirectory.open(f);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		QueryParser parser = new QueryParser(Version.LUCENE_36, "keys",
				analyzer);
		Query query;
		try {
			query = parser.parse(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// System.out.print("sorry");
			return null;
		}

		IndexSearcher searcher = new IndexSearcher(indexPath);
		TopDocs docs = searcher.search(query, 15);
		ScoreDoc[] hits = docs.scoreDocs;
		// System.out.println(hits.length);
		// for (ScoreDoc hit : hits) {
		// System.out.println("doc: " + hit.doc + " score: " + hit.score);
		//
		// }
		List<String> listId = new ArrayList<String>();
		List<String> listExpert = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			ScoreDoc hit = hits[i];
			if (hit.score > 0.5) {
				// System.out.println("doc: " + hit.doc + " score: " +
				// hit.score);
				Document hitDoc = searcher.doc(hits[i].doc);
				// System.out.println(hitDoc.get("keys"));
				// System.out.println(hitDoc.get("textid"));
				listId.add(hitDoc.get("textid"));
				listExpert.add(hitDoc.get("expert"));
			}
		}
		res.add(listId);
		res.add(listExpert);
		indexPath.close();
		analyzer.close();
		return res;
	}

	public List<String> findId(String text) throws IOException, ParseException {
		// text="过冷度对Fe-Ni-P-B软磁合金凝固组织的影响";
		File f = new File("indexfile");
		Directory indexPath = FSDirectory.open(f);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		QueryParser parser = new QueryParser(Version.LUCENE_36, "keys",
				analyzer);
		Query query;
		try {
			query = parser.parse(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// System.out.print("sorry");
			return null;
		}
		IndexSearcher searcher = new IndexSearcher(indexPath);
		TopDocs docs = searcher.search(query, 20);
		ScoreDoc[] hits = docs.scoreDocs;
		// System.out.println(hits.length);
		// for (ScoreDoc hit : hits) {
		// System.out.println("doc: " + hit.doc + " score: " + hit.score);
		//
		// }
		List<String> listId = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			ScoreDoc hit = hits[i];
			if (hit.score > 0) {
				// System.out.println("doc: " + hit.doc + " score: " +
				// hit.score);
				Document hitDoc = searcher.doc(hits[i].doc);
				// System.out.println(hitDoc.get("textid"));//注释部分测试用
				listId.add(hitDoc.get("textid"));
			}
		}
		indexPath.close();
		return listId;
	}

	public List<String> findExpert(String text) throws IOException,
			ParseException {
		// text="过冷度对Fe-Ni-P-B软磁合金凝固组织的影响";
		File f = new File("indexfile");
		Directory indexPath = FSDirectory.open(f);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		QueryParser parser = new QueryParser(Version.LUCENE_36, "keys",
				analyzer);
		Query query = null;
		try {
			query = parser.parse(text);
		} catch (Exception e) {
			// System.out.println("sorry");
			return null;
		}
		IndexSearcher searcher = new IndexSearcher(indexPath);
		TopDocs docs = searcher.search(query, 20);
		ScoreDoc[] hits = docs.scoreDocs;
		// System.out.println(hits.length);
		// for (ScoreDoc hit : hits) {
		// System.out.println("doc: " + hit.doc + " score: " + hit.score);
		//
		// }
		List<String> listExpert = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			ScoreDoc hit = hits[i];
			if (hit.score > 0) {
				System.out.println("doc: " + hit.doc + " score: " + hit.score);
				Document hitDoc = searcher.doc(hits[i].doc);
				System.out.println(hitDoc.get("textid"));// 注释部分测试用
				listExpert.add(hitDoc.get("expert"));
			}
		}
		indexPath.close();
		return listExpert;
	}

}
