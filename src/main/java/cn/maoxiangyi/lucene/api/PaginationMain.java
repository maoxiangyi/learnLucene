package cn.maoxiangyi.lucene.api;

import java.util.ArrayList;
import java.util.List;

import cn.maoxiangyi.lucene.crud.domain.Article;
import cn.maoxiangyi.lucene.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

/**
 * 分页的原理分析
 * 
 * 需求分析：
 * 1，每页显示10条记录，第一页显示1-10条记录，第二页显示11-20条记录。
 * 2，明确总的分页数量
 * 
 * 思路：
 * page = 1; //定义页码
 * num = 10; //定义每页显示什么
 * totalhits = 100; //定义总条数
 * 
 * -------------------------------------
 * page	start  end	  start 计算			end计算
 * 1	1		10	 (page-1)*num+1 	page*num
 * 2	11		20   (page-1)*num+1		page*num
 * 3	21		30   (page-1)*num+1		page*num
 * 
 * @author maoxiangyi
 *
 */
public class PaginationMain {
	@Test
	public void pagination() throws Exception, ParseException {
		List<Article> articles =new ArrayList<Article>();
		int start = 21;//正常分页的开始数值
		int end = 30;
		
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		QueryParser queryParser = new QueryParser("title",LuceneUtil.getAnalyzer());
		TopDocs topDocs = indexSearcher.search(queryParser.parse("lucene"),Integer.MAX_VALUE);
		ScoreDoc[] scoreDocs =  topDocs.scoreDocs;
		if(topDocs.totalHits>end){
			//由于数组从角标0开始计算，所以开始数值要减去1
			for (int i = (start-1); i < end; i++) {
				Article article = new Article();
				Document document = indexSearcher.doc(scoreDocs[i].doc);
				article.setId(document.get("id"));
				article.setTitle(document.get("title"));
				article.setContent(document.get("content"));
				articles.add(article);
			}
		}
		System.out.println(articles);
	}
	
	@Test
	public void batchCreateIndex() throws Exception, ParseException {
		IndexWriter indexWriter  = LuceneUtil.getIndexWriter();
		List<Document> listDocuments  = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
			Document doc = new Document();
			doc.add(new StringField("id", i+"", Store.YES));
			doc.add(new TextField("title", "学习lucene"+i, Store.YES));
			doc.add(new TextField("content", "学习lucene的秘诀是创建索引和查询索引"+i, Store.YES));
			listDocuments.add(doc);
		}
		indexWriter.addDocuments(listDocuments);
		indexWriter.commit();
	}
	
	
	
}
