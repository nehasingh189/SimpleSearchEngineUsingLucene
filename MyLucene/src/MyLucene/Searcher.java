package MyLucene;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class Searcher {

		IndexReader reader;
		IndexSearcher indexSearcher;
		QueryParser queryParser;
		Query query;
	   
		public Searcher(String indexDirectoryPath) throws IOException
		{
			
			reader = DirectoryReader.open(FSDirectory.open(new File(indexDirectoryPath)));
		    indexSearcher = new IndexSearcher(reader);
		    queryParser = new QueryParser(Version.LUCENE_47,LuceneConstants.CONTENTS,new SimpleAnalyzer(Version.LUCENE_47));
		}
		public Searcher(String indexDirectoryPath, Boolean isList) throws IOException
		{
			reader = DirectoryReader.open(FSDirectory.open(new File(indexDirectoryPath)));
		    
		}
		public TopDocs search( String searchQuery) throws IOException, ParseException
		{
			      query = queryParser.parse(searchQuery);
			      return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
		}
		public Document getDocument(ScoreDoc scoreDoc)throws CorruptIndexException, IOException
		{
			     return indexSearcher.doc(scoreDoc.doc);	
		}
		public void close() throws IOException
		{
		      //indexSearcher.close();
			reader.close();
		}
		
		public TreeMap<String,Integer> BuildList() throws IOException
		{
			TreeMap<String,Integer> hTree = new TreeMap<String,Integer>();
			Fields fields = MultiFields.getFields(reader);
		    Terms terms = fields.terms("contents");
	        TermsEnum iterator = terms.iterator(null);
	        BytesRef byteRef = null;
	        while((byteRef = iterator.next()) != null) 
	        {
	        	String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
	        	hTree.put(term,(int)iterator.totalTermFreq());
	            
	        }
	        return hTree;
		}
}