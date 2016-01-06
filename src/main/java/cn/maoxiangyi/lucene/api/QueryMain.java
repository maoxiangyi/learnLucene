package cn.maoxiangyi.lucene.api;

import cn.maoxiangyi.lucene.util.LuceneUtil;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.junit.Test;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class QueryMain {

	/**
	 * QueryParser用来分析用户的查询意图，需要一个分词器和指定查询字段。
	 * 
	 * @throws Exception
	 */
	@Test
	public void queryParser() throws Exception {
		
		// QueryParser用来分析用户的查询意图，需要一个分词器和指定查询字段。
		QueryParser queryParser = new QueryParser("title",
				LuceneUtil.getAnalyzer());
		// 指定用户的输入内容：学习lucene
		Query query = queryParser.parse("学习lucene");

		// 通过工具类获取IndexSearcher
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		// 查询操作
		TopDocs topDocs = indexSearcher.search(query, 10);
		// 打印返回结果
		LuceneUtil.printResult(indexSearcher, topDocs);

	}

	/**
	 * MultiFieldQueryParser 对多个字段进行查询，需要传入参数：多个字段的名字、分词器。
	 * @throws Exception
	 */
	@Test
	public void multiFieldQueryParser() throws Exception {
		//MultiFieldQueryParser 对多个字段进行查询，需要传入参数：多个字段的名字、分词器。
		MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(
				new String[] { "title", "content" }, LuceneUtil.getAnalyzer());
		
		//setDefaultOperator的默认值是OR,表示文档中任意字段匹配上任意的term都是满足查询要求的。
		//如果设置成true，表示需要至少一个字段上必须匹配上分词后的多个term。
		//在实际的使用场景中，一般使用默认值
		//multiFieldQueryParser经过解析之后使用了BooleanQuery
		multiFieldQueryParser.setDefaultOperator(Operator.OR);
		//用户输入进行解析
		Query query = multiFieldQueryParser.parse("学习lucene");

		// 通过工具类获取IndexSearcher
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		// 查询操作
		TopDocs topDocs = indexSearcher.search(query, 10);
		// 打印返回结果
		LuceneUtil.printResult(indexSearcher, topDocs);

	}

	/**
	 * 通过前缀进行查询， 查询有两方式：一种是使用PrefixQuery直接new一个对象，需要一个Term参数。
	 * 另一种方式是使用QueryParser来创建，输入内容带*即可，如：queryParser.parse("学*");
	 * 
	 * @throws Exception
	 */
	@Test
	public void prefixQuery() throws Exception {
		// 第一种方式：使用PrefixQuery直接new一个对象，需要一个Term参数
		PrefixQuery query = new PrefixQuery(new Term("title", "学"));

		// 第二种方式：使用QueryParser来创建，输入内容带*即可，如：queryParser.parse("学*");
		// QueryParser queryParser = new QueryParser("title",
		// LuceneUtil.getAnalyzer());
		// Query query = queryParser.parse("学*");

		// 通过工具类获取IndexSearcher
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		// 查询操作
		TopDocs topDocs = indexSearcher.search(query, 10);
		// 打印返回结果
		LuceneUtil.printResult(indexSearcher, topDocs);

	}
	/**
	 * 创建一个基于通配符的查询，*匹配任意多个字符，？匹配单个字符
	 * @throws Exception
	 */
	@Test
	public void wildcardQuery() throws Exception {
		//创建一个基于通配符的查询，*匹配任意多个字符，？匹配单个字符
//		WildcardQuery wildcardQuery = new WildcardQuery(new Term("title","l*e"));
		WildcardQuery wildcardQuery = new WildcardQuery(new Term("title","luce?e"));
		
		// 通过工具类获取IndexSearcher
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		// 查询操作
		TopDocs topDocs = indexSearcher.search(wildcardQuery, 10);
		// 打印返回结果
		LuceneUtil.printResult(indexSearcher, topDocs);

	}
	
	/**
	 * 修复查询，通过两次修改能够得到一个词条，并进行查询。
	 * @throws Exception
	 */
	@Test
	public void fuzzyQuery() throws Exception {
		//已为您显示“java”的搜索结果。仍然搜索：jaav
		FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("title","luecne"));
	
		// 通过工具类获取IndexSearcher
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		// 查询操作
		TopDocs topDocs = indexSearcher.search(fuzzyQuery, 10);
		// 打印返回结果
		LuceneUtil.printResult(indexSearcher, topDocs);

	}
	
	/**
	 * 对多个查询结果进行合并，主要是求差集、求并集
	 * @throws Exception
	 */
	@Test
	public void booleanQuery() throws Exception {
		//对多个查询结果进行合并，主要是求差集、求并集
		BooleanQuery booleanQuery = new BooleanQuery();
		//已为您显示“java”的搜索结果。仍然搜索：jaav
		FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("title","luecne"));
		//通过前缀进行查询
		PrefixQuery prefixQuery = new PrefixQuery(new Term("title", "学"));
		//必须包含fuzzyQuery查询的结果集
		booleanQuery.add(fuzzyQuery, Occur.MUST);
		//必须包含prefixQuery查询的结果集
		booleanQuery.add(prefixQuery, Occur.MUST);
	
		// 通过工具类获取IndexSearcher
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		// 查询操作
		TopDocs topDocs = indexSearcher.search(fuzzyQuery, 10);
		// 打印返回结果
		LuceneUtil.printResult(indexSearcher, topDocs);

	}

}