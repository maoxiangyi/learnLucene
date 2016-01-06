package cn.maoxiangyi.lucene.api;

import cn.maoxiangyi.lucene.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.junit.Test;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class HighlighterMain {
	/**
	 * 高亮的核心 1，先查出匹配的结果 
	 * 2，在结果中的关键字上添加html标签<fond color='red'>keyword</font>
	 * 
	 * @throws Exception
	 * @throws ParseException
	 */

	@Test
	public void highlighter() throws Exception, ParseException {
		// QueryParser用来分析用户的查询意图，需要一个分词器和指定查询字段。
		QueryParser queryParser = new QueryParser("title",
				LuceneUtil.getAnalyzer());
		// 指定用户的输入内容：学习lucene
		Query query = queryParser.parse("学习lucene");
		
		//创建一个简单的formatter
		Formatter formatter = new SimpleHTMLFormatter("<fond color='red'>","</font>");
		//
		Scorer fragmentScorer  = new QueryTermScorer(query);
		Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(20));

		// 通过工具类获取IndexSearcher
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		// 查询操作
		TopDocs topDocs = indexSearcher.search(query, 10);
		
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			System.out.println(scoreDoc.score);
			Document document = indexSearcher.doc(scoreDoc.doc);
			String title = document.get("title");
			String titleHighlighter =highlighter.getBestFragment(LuceneUtil.getAnalyzer(),"title",title);
			System.out.println(titleHighlighter);
			String content = document.get("content");
			String contentHighlighter =highlighter.getBestFragment(LuceneUtil.getAnalyzer(),"content",content);
			System.out.println(contentHighlighter);
		}
		
		
		

	}

}
