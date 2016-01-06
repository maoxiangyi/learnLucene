package cn.maoxiangyi.lucene.crud.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.maoxiangyi.lucene.crud.ArticleDao;
import cn.maoxiangyi.lucene.crud.ArticleService;
import cn.maoxiangyi.lucene.crud.domain.Article;
import cn.maoxiangyi.lucene.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class ArticleServiceImpl implements ArticleService {

	private ArticleDao articleDao = new ArticleDaoImpl();

	/**
	 * 在插入数据到mysql数据库时，先插入然后创建索引。
	 */
	public void insert(Article article) throws Exception {
		// 1, 将数据插入数据库
		articleDao.insert(article);
		try {
			// 2, 为数据创建索引
			createIndex(article);
		} catch (Exception e) {
			System.out.println("create Index failed:" + article);
		}
	}

	/**
	 * 先删除数据，然后删除索引
	 */
	public void del(String id) {
		// 1,删除记录
		articleDao.del(id);
		try {
			// 2, 删除索引
			delIndex(id);
		} catch (Exception e) {
			System.out.println("del Index failed:" + id);
		}
	}

	/**
	 * 先修改数据库中的数据，然后修改索引。 修改索引的要点在于先删除索引，再创建索引
	 */
	public void update(Article article) {
		// 1，更新数据库中的数据
		articleDao.update(article);
		try {
			// 2, 先删除索引，在创建索引
			delIndex(article.getId());
			createIndex(article);
		} catch (Exception e) {
			System.out.println("update Index failed:" + article);
		}

	}

	public Article query(String id) {
		return articleDao.query(id);
	}

	/**
	 * 通过关键字进行查询
	 */
	public List<Article> queryByKeyWord(String keyword) throws Exception {
		List<Article> articles = new ArrayList<Article>();
		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
		QueryParser queryParser = new QueryParser("title",LuceneUtil.getAnalyzer());
		TopDocs topDocs = indexSearcher.search(queryParser.parse(keyword),Integer.MAX_VALUE);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Article article = new Article();
			Document document  = indexSearcher.doc(scoreDoc.doc);
			article.setId(document.get("id"));
			article.setTitle(document.get("title"));
			article.setContent(document.get("content"));
			articles.add(article);
		}
		return articles;

	}

	private void delIndex(String id) throws IOException {
		IndexWriter indexWriter = LuceneUtil.getIndexWriter();
		indexWriter.deleteDocuments(new Term("id", id));
		indexWriter.commit();
	}

	private void createIndex(Article article) throws IOException {
		IndexWriter indexWriter = LuceneUtil.getIndexWriter();
		Document document = new Document();
		document.add(new StringField("id", article.getId(), Store.YES));
		document.add(new TextField("title", article.getTitle(), Store.YES));
		document.add(new TextField("content", article.getContent(), Store.YES));
		indexWriter.addDocument(document);
		indexWriter.commit();
	}

}
