package cn.maoxiangyi.lucene.api;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import cn.maoxiangyi.lucene.util.LuceneUtil;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class DocumentMain {
	
	public Document getDocument() {
		//Document是lucene创建索引和查询索引的基本单位
		Document doc = new Document();
		
		////Adds a field to a document
		//通过add方法在document中添加字段,可以添加多个字段
		//StringField  A field that is indexed but not tokenized
		doc.add(new StringField("id", "1", Store.YES));
		//A field that is indexed and tokenized 
		doc.add(new TextField("title", "学习lucene", Store.YES));
		doc.add(new TextField("content", "学习lucene的秘诀是创建索引和查询索引", Store.YES));
		
		//总结:
		//1、StringField不分词，TextField分词;
		//2、每种基本类型的都有一种对应的Field，在实际的使用过程中使用StringField和TextField居多。
		return doc;

	}

	@Test
	public void cretaeIndex() throws Exception {

		IndexWriter indexWriter = LuceneUtil.getIndexWriter();
		indexWriter.addDocument(getDocument());
		indexWriter.commit();
	}

	@Test
	public void queryIndex() throws Exception {
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();

		TermQuery query = new TermQuery(new Term("title", "lucene"));
		TopDocs topDocs = indexSearcher.search(query, 10);

		LuceneUtil.printResult(indexSearcher, topDocs);

	}

}
