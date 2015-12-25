package MyLucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class LueceScore 
{

	static String indexDir = "C:\\Users\\Neha\\Documents\\information_retreival\\HW4\\myIndex";
	String dataDir = "C:\\Users\\Neha\\Documents\\information_retreival\\HW4\\cacm";
	static String WriteResultTo = "C:\\Users\\Neha\\Documents\\information_retreival\\HW4\\termFrequency.txt";
	
	Indexer indexer;
	Searcher searcher;
	
	public static void main(String[] args) throws FileNotFoundException  
	{
		// TODO Auto-generated method stub
		String line=null;
		int queryID=0;
		TreeMap<String,Integer> hTree = new TreeMap<String,Integer>(Collections.reverseOrder());
		
		FileReader fr = new FileReader("queries.txt");
		BufferedReader br= new BufferedReader(fr);
		
		try 
		{
			LueceScore ls = new LueceScore();
			PrintWriter writer1 = new PrintWriter(WriteResultTo, "UTF-8");
			
	      ls.createIndex(); // uncomment this method if u want to create a index file
	       while((line=br.readLine())!=null){
	    	 queryID++;
			ls.search(line, queryID);
	       }
	       queryID=0;
			hTree = ls.buildList();
		 
		 	Set set = hTree.entrySet();
	        Iterator i = set.iterator();
	        int rank = 0;
	        while(i.hasNext() ) 
	        {
	        	
	        	Map.Entry me = (Map.Entry) i.next();
	        	//System.out.println(me.getKey() + " " + me.getValue());
	        		        	
	        	writer1.println(me.getKey() + " " + me.getValue());
	        		        	
	        }
			writer1.close();
	    } 
		catch (IOException e) 
		{
	         e.printStackTrace();
	    } 
		catch (ParseException e) 
		{
	         e.printStackTrace();
	    }
	}

	private TreeMap<String,Integer> buildList() throws IOException
	{
		searcher = new Searcher(indexDir,true);
		return searcher.BuildList(); 
	}
	
	
		      

	private void createIndex() throws IOException
	{
		
	      indexer = new Indexer(indexDir);
	      int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
	      long endTime = System.currentTimeMillis();
	      indexer.close();
	      System.out.println(numIndexed+" File indexed, time taken: "
	         +(endTime-startTime)+" ms");		
	}
	
	private void search(String searchQuery, int queryID) throws IOException, ParseException
	{
		File file= new File("results_for_queryID_"+queryID+".txt");
		file.createNewFile();
		BufferedWriter bw= new BufferedWriter( new FileWriter(file));
		
	      searcher = new Searcher(indexDir);
	      long startTime = System.currentTimeMillis();
	      TopDocs hits = searcher.search(searchQuery);
	      
	      long endTime = System.currentTimeMillis();
	     
	      System.out.println(hits.totalHits +
	        " documents found. Time :" + (endTime - startTime) +" ms");
	      for(ScoreDoc scoreDoc : hits.scoreDocs) 
	      {
	    	 
	         Document doc = searcher.getDocument(scoreDoc);
	        // System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
	         int DocID=scoreDoc.doc + 1;
	         String result= "QueryID : "+ queryID +" Doc ID : " + DocID + " Score : " + scoreDoc.score;
	         bw.write(result);
	         bw.newLine();
	      }
	      searcher.close();
	      bw.close();
	}	
}
