package cn.maoxiangyi.lucene.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class LuceneUtilTest {

	public Document getDocument() {
		Document doc = new Document();
		doc.add(new StringField("id", "3", Store.YES));
		doc.add(new TextField("title", "lucene", Store.YES));
		doc.add(new TextField("content", "lucene的秘诀是创建索引和查询索引", Store.YES));
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
