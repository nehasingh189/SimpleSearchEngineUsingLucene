package MyLucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.automaton.RegExp;

public class Indexer 
{

	private IndexWriter writer;
	private static Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_47); //new StandardAnalyzer(Version.LUCENE_47);
	private ArrayList<File> queue = new ArrayList<File>();
	
	public Indexer(String indexDirectoryPath) throws IOException
	{
	      //this directory will contain the indexes
	      Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));

	      IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,analyzer);
	      
	      //create the indexer
	      writer = new IndexWriter(indexDirectory,config);
	      
	}
	public void close() throws CorruptIndexException, IOException
	{
	      writer.close();
	}
	
	private Document getDocument(File file) throws IOException{
	      Document document = new Document();
	      
	      String content= new Scanner(file).useDelimiter("\\Z").next();
	      content = content.replaceAll("<html>", "");
	      content = content.replaceAll("</html>", "");
	      content = content.replaceAll("<pre>", "");
	      content = content.replaceAll("</pre>", "");
	      document.add(new TextField("contents", new StringReader(content)));
	      document.add(new StringField("path", file.getPath(), Field.Store.YES));
	      document.add(new StringField("filename", file.getName(),Field.Store.YES));
	      
	      return document;
	   }   
	
	 private void indexFile(File file) throws IOException
	 {
	      System.out.println("Indexing "+file.getCanonicalPath());
	      Document document = getDocument(file);
	      writer.addDocument(document);
	 }
	 
	public int createIndex(String dataDirPath, FileFilter filter) throws IOException
	{
		      //get all files in the data directory
		      File[] files = new File(dataDirPath).listFiles();

		      for (File file : files) {
		         if(!file.isDirectory()
		            && !file.isHidden()
		            && file.exists()
		            && file.canRead()
		            && filter.accept(file)
		         ){
		            indexFile(file);
		         }
		      }
		      return writer.numDocs();
		   }
	
}
