package com.sugon;

import java.io.IOException;





import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Test {

	public static void main(String[] args) {
		// Store the index in memory:
		Directory directory = new RAMDirectory();
		
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		
		try {
			IndexWriter writer = new IndexWriter(directory, config);
			
			String text = "This is the text to be indexed.";
			String text1 = "This ndexed.";
			String text2 = "This text.";
			String text3 = "This is the text ";
			addDoc(writer, text);
			addDoc(writer, text1);
			addDoc(writer, text2);
			addDoc(writer, text3);
			writer.close();
			
			
			// Now search the index:
			DirectoryReader reader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);

			// Parse a simple query that searches for "text":
			QueryParser parser = new QueryParser(Version.LUCENE_40, "fieldName", analyzer);
			Query query = parser.parse("text");
			ScoreDoc[] hits= searcher.search(query, null,1000).scoreDocs;
			
			// assertEquals(1, hits.length);
			
			// Iterate through the results:
			for (int i = 0; i < hits.length; i++) {
				Document document2 = searcher.doc(hits[i].doc);
				// assertEquals("This is the text to be indexed.", hitDoc.get("fieldName"));
				System.out.println(document2.get("fieldName"));
			}
			
			reader.close();
			directory.close();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addDoc(IndexWriter w, String fieldval){
		Document document = new Document();
		document.add(new Field("fieldName", fieldval, TextField.TYPE_STORED));
		try {
			w.addDocument(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
