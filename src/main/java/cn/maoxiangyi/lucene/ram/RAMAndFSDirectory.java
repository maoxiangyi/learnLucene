package cn.maoxiangyi.lucene.ram;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class RAMAndFSDirectory {

	/**
	 * 将文件索引库中的数据导入到内存索引库
	 * @throws Exception 
	 */
	@SuppressWarnings("resource")
	@Test
	public void readInRam() throws Exception {
		
		//1，将文件索引库中的数据导入到内存索引库
		RAMDirectory ramDirectory = new RAMDirectory();
		IndexWriter ramIndexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LATEST, new IKAnalyzer()));
		ramIndexWriter.addIndexes(FSDirectory.open(new File("index")));
		ramIndexWriter.commit();
		
		//2，查询内存索引库总的数据
		IndexSearcher ramiIndexSearcher = new IndexSearcher(DirectoryReader.open(ramDirectory));
		TopDocs topDocs = ramiIndexSearcher.search(new TermQuery(new Term("title", "lucene")), 10);
		System.out.println(topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			System.out.println(scoreDoc.score);
			Document document = ramiIndexSearcher.doc(scoreDoc.doc);
			System.out.println("id:" + document.get("id"));
			System.out.println("title:" + document.get("title"));
			System.out.println("content:" + document.get("content"));
		}
		
	}
	/**
	 * 先导入文件索引库中的数据，然后基于内存索引库创建索引。
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	@Test
	public void addDocumentInRam() throws Exception {
		
		//1，将文件索引库中的数据导入到内存索引库
		RAMDirectory ramDirectory = new RAMDirectory();
		IndexWriter ramIndexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LATEST, new IKAnalyzer()));
		ramIndexWriter.addIndexes(FSDirectory.open(new File("index")));
		ramIndexWriter.commit();
		
		//2，给内存索引库中添加数据
		Document doc = new Document();
		doc.add(new StringField("id", "1", Store.YES));
		doc.add(new TextField("title", "学习lucene ram", Store.YES));
		doc.add(new TextField("content", "学习lucene的秘诀是创建索引和查询索引  ram", Store.YES));
		ramIndexWriter.addDocument(doc);
		ramIndexWriter.commit();
		
		//3，查询内存索引库总的数据
		IndexSearcher ramiIndexSearcher = new IndexSearcher(DirectoryReader.open(ramDirectory));
		TopDocs topDocs = ramiIndexSearcher.search(new TermQuery(new Term("title", "lucene")), 10);
		System.out.println(topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			System.out.println(scoreDoc.score);
			Document document = ramiIndexSearcher.doc(scoreDoc.doc);
			System.out.println("id:" + document.get("id"));
			System.out.println("title:" + document.get("title"));
			System.out.println("content:" + document.get("content"));
		}
		
	}
	
	/**
	 * 将内存索引库中的数据保存到文件索引库中
	 * @throws Exception
	 */
	@Test
	public void saveRam2fs() throws Exception {
		
		//1，将文件索引库中的数据导入到内存索引库
		RAMDirectory ramDirectory = new RAMDirectory();
		IndexWriter ramIndexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LATEST, new IKAnalyzer()));
		
		//2，给内存索引库中添加数据
		Document doc = new Document();
		doc.add(new StringField("id", "1", Store.YES));
		doc.add(new TextField("title", "学习lucene ram", Store.YES));
		doc.add(new TextField("content", "学习lucene的秘诀是创建索引和查询索引  ram", Store.YES));
		ramIndexWriter.addDocument(doc);
		ramIndexWriter.commit();
		//这里一定要close，不然保存数据的时候不行
		ramIndexWriter.close(); 
		
		//3，查询内存索引库总的数据
		IndexSearcher ramiIndexSearcher = new IndexSearcher(DirectoryReader.open(ramDirectory));
		TopDocs topDocs = ramiIndexSearcher.search(new TermQuery(new Term("title", "lucene")), 10);
		System.out.println(topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			System.out.println(scoreDoc.score);
			Document document = ramiIndexSearcher.doc(scoreDoc.doc);
			System.out.println("id:" + document.get("id"));
			System.out.println("title:" + document.get("title"));
			System.out.println("content:" + document.get("content"));
		}
		
		//4,将内存索引库中的数据导入到文件索引库中
		Directory directory = FSDirectory.open(new File("index1"));
		IndexWriter fsIndexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LATEST, new IKAnalyzer()));
		fsIndexWriter.addIndexes(ramDirectory);
		fsIndexWriter.commit();
		fsIndexWriter.close();
		
	}
	
}
