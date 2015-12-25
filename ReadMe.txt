//NOTE: for the best readability open with Notepad++

1. Eclipse Release Version 4.5.0 (Mars) was used to develop and test this crawler prototype.
2. java version : "1.8.0_60" 
3. External Library used: lucene-core-4.7.2.jar, lucene-core-4.7.2.jar, lucene-analyzers-common-4.7.2.jar
4. Name of the workspace: MyLucene
5. Import the workspace in eclipse, inport the mentioned libraries too.
6. To execute the code run  the file "LueceScore.java"
7. Before running the code, it is important to define the following 3 variables in LueceScore.java:
	indexDir : this is the path of the folder where index has to be built.
	dataDir  : this is the path of the corpus folder 
	WriteResultTo : this is path of the file where you want to write values of (term ,term frequency) pair
	
	EXAMPLE: 
	static String indexDir = "C:\\Users\\Neha\\Documents\\information_retreival\\HW4\\myIndex";
	String dataDir = "C:\\Users\\Neha\\Documents\\information_retreival\\HW4\\cacm";
	static String WriteResultTo = "C:\\Users\\Neha\\Documents\\information_retreival\\HW4\\termFrequency.txt";
	
	
8. Index should be built only once. 
	File : LueceScore.java
	Line number : 59
	uncomment this line "ls.createIndex();" when index has to be built and for all consecutive runs, comment it out.
	try 
		{
			LueceScore ls = new LueceScore();
			PrintWriter writer1 = new PrintWriter(WriteResultTo, "UTF-8");
	   //   ls.createIndex(); // uncomment this method if u want to create a index file
	
	Since lucene read even html contents and indexes them, we have used a filter before passing the document to indexer to remove html and pre tags.
						

PART 1: 
Index the raw (unpreprocessed) CACM corpus http://www.searchenginesbook.com/collections/ using Lucene. Make sure to use “SimpleAnalyzer” as your
analyzer.

- SimpleAnalyzer is used parse the corpus here. 
		private static Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_47);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,analyzer);

		
PART 2:		
Build a list of (unique term, term_frequency) pairs over the entire collection. Sort by frequency.
		The list of (unique term, term_frequency) pairs is written in the file "termFrequency.txt". 
		Only the content field is used for generating these pairs :
				Fields fields = MultiFields.getFields(reader);
				Terms terms = fields.terms("contents");
				TermsEnum iterator = terms.iterator(null);
		Find the file in termFrequency.txt in the folder.
		
		The sorted list can be referred from "Zipfian_curve_for_the_corpus.xls".

		
PART 3:
Plot the Zipfian curve based on the list generated in (1) (you do not need Lucene to perform this. You may use any platform/software of your choosing to generate the plot)
		the zipfian graph is plotted Rank Vs Frequency on the log scale. Refer the excel file "Zipfian_curve_for_the_corpus.xls"
		
		
PART 4: 
Perform search for the queries below. You need to return the top 100 results for each query. Use the default document scoring/ranking function provided by Lucene.
			a. portable operating systems
			b. code optimization for space efficiency
			c. parallel algorithms
			d. parallel processor in information retrieval		

		These queries are stored in the text file "queries.txt" in workspace. These queries are read line by line and query parser is used.
					query = queryParser.parse(searchQuery);
					return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
		
		The result of the search is stored in the text file "results_of_query_<queryID>.txt"
					NOTE : docId starts from 0 in lucene. 

PART 5:
Compare the total number of matches in 4 to that obtained in hw3
		This comparison is done in excel file "COMPARISON_TABLE"