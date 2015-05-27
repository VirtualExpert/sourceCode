package index;

import java.io.File;
import java.io.IOException;

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
		if(textid==1)
			deleteAll(f);
		Directory indexPath = FSDirectory.open(f);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		// 创建IndexWriter
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
				analyzer);
		IndexWriter writer = new IndexWriter(indexPath, config);

		Document doc = new Document();
		Field field = new Field("textid",""+textid,Field.Store.YES, Field.Index.NO);
		doc.add(field);
		field = new Field("expert",""+expert,Field.Store.YES, Field.Index.NO);
		doc.add(field);
		field = new Field("keys",""+keys,Field.Store.YES, Field.Index.ANALYZED);
		doc.add(field);
		writer.addDocument(doc);
		writer.close();
		writer.unlock(indexPath);
		indexPath.close();
	}
}
