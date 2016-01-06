package cn.maoxiangyi.lucene;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 * 
 * lucene是一套用来开发搜索引擎的api，主要是创建索引和查询索引
 *  这个类主要演示：创建索引、查询索引
 * 
 * @author maoxiangyi
 *
 */
public class LuceneQuickStart {
	/**
	 * 创建索引
	 * 
	 * @throws Exception
	 */
	@Test
	public void createIndex() throws Exception {
		//创建索引和查询索引的基本单位，对于外部数据来讲，需要将外部数据转化成Document
		Document doc = new Document();
		doc.add(new StringField("id", "1", Store.YES));
		doc.add(new TextField("title", "学习lucene", Store.YES));
		doc.add(new TextField("content", "学习lucene的秘诀是创建索引和查询索引", Store.YES));
		
		//Directory是一个文件目录，里面有很多文件，文件在索引库创建的时被创建，可以对这些文件进行随机的读写。
		//FSDirectory 是一个基于文件系统的索引库，通过使用静态的open方法打开
		Directory d = FSDirectory.open(new File("index"));
		
		//lucene提供的标准的分词器，在lucene源码中提供了针对多种语言的分词器，我们这里采用标准的analyzer
		Analyzer analyzer = new StandardAnalyzer();
		//定义索引查询器初始化时的配置信息
		IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST,analyzer);
		IndexWriter indexWriter = new IndexWriter(d, conf);

		//将Lucene能够识别的Document添加到IndexWriter中，开始创建索引
		indexWriter.addDocument(doc);
		indexWriter.commit();
		indexWriter.close();

	}

	/**
	 * 查询索引
	 * 
	 * @throws Exception
	 */
	@Test
	public void queryIndex() throws Exception {
		//Directory是一个文件目录，里面有很多文件，文件在索引库创建的时被创建，可以对这些文件进行随机的读写。
		//FSDirectory 是一个基于文件系统的索引库，通过使用静态的open方法打开
		Directory directory = FSDirectory.open(new File("index"));
		
		//打开索引库，用来读取索引
		IndexReader indexReader = DirectoryReader.open(directory);
		
		//索引查询器，用来对索引进行搜索
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		//用户的意图分析，lucene是一个词条，title是查询的目标。
		//翻译：查询索引库中，所有title包含lucene这个词条的文档信息
		TermQuery query = new TermQuery(new Term("title", "lucene"));
		
		//通过索引查询器的search方法查询前10条记录
		TopDocs topDocs = indexSearcher.search(query, 10);
		//totalHits,所有满足查询条件的文档数量
		System.out.println(topDocs.totalHits);
		
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			//当前文档的得分。
			//得分有专门的算法，主要有两个因素:一个词在文档中出现的数量，越多得分越高；一个词在所有文档中出现的次数，越少得分越高
			System.out.println(scoreDoc.score);
			
			//通过文档在索引库中的document id，获取文档的原始内容
			//建索引和查询索引的基本单位，查询的结果转化成Document
			Document document = indexSearcher.doc(scoreDoc.doc);
			
			//从Document中获取数据
			System.out.println("id:" + document.get("id"));
			System.out.println("title:" + document.get("title"));
			System.out.println("content:" + document.get("content"));
		}

	}
}
