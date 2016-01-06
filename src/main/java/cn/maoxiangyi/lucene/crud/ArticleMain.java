package cn.maoxiangyi.lucene.crud;

import java.util.List;

import cn.maoxiangyi.lucene.crud.domain.Article;
import org.junit.Test;

import cn.maoxiangyi.lucene.crud.impl.ArticleServiceImpl;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class ArticleMain {

	@Test
	public void insert() throws Exception {
		ArticleService articleService = new ArticleServiceImpl();
		Article article = new Article();
		article.setId("101");
		article.setTitle("对新闻系统进行升级,再加一条记录");
		article.setContent("通过lucene创建索引和查询索引的功能");
		articleService.insert(article);
	}

	@Test
	public void del() {
		ArticleService articleService = new ArticleServiceImpl();
		articleService.del("100");
	}

	@Test
	public void update() {
		ArticleService articleService = new ArticleServiceImpl();
		Article article = new Article();
		article.setId("100");
		article.setTitle("对新闻系统进行升级，我正在修改");
		article.setContent("通过lucene创建索引和查询索引的功能");
		articleService.update(article);
	}

	@Test
	public void query() {
		ArticleService articleService = new ArticleServiceImpl();
		Article article = articleService.query("100");
		System.out.println(article);
	}
	
	@Test
	public void queryByKeyWord() throws Exception{
		ArticleService articleService = new ArticleServiceImpl();
		List<Article> articles = articleService.queryByKeyWord("新闻系统");
		System.out.println(articles);
	}

}
