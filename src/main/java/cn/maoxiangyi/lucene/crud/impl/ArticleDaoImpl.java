package cn.maoxiangyi.lucene.crud.impl;


import java.sql.ResultSet;
import java.sql.SQLException;

import cn.maoxiangyi.lucene.crud.util.DBUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import cn.maoxiangyi.lucene.crud.ArticleDao;
import cn.maoxiangyi.lucene.crud.domain.Article;
/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class ArticleDaoImpl implements ArticleDao {
	/**
	 * 
	 * dbcTemplate主要提供以下五类方法： execute方法：可以用于执行任何SQL语句，一般用于执行DDL语句；
	 * update方法及batchUpdate方法：update方法用于执行新增、修改、删除等语句；batchUpdate方法用于执行批处理相关语句；
	 * query方法及queryForXXX方法：用于执行查询相关语句； call方法：用于执行存储过程、函数相关语句。
	 * 
	 */
	private JdbcTemplate jdbcTemplate = new JdbcTemplate(DBUtil.getDataSource());

	public void insert(Article article) throws Exception {
		String sql = "INSERT INTO itcast_lucene_news.article (id, title, content) VALUES (?, ?,?)";
		jdbcTemplate.update(sql, article.getId(), article.getTitle(),
				article.getContent());
	}

	public void del(String id) {
		String sql = "DELETE FROM itcast_lucene_news.article WHERE id = ? ";
		jdbcTemplate.update(sql, id);
	}

	public void update(Article article) {
		String sql = "UPDATE  itcast_lucene_news.article SET title = ?,content = ? WHERE id = ?";
		jdbcTemplate.update(sql, article.getTitle(), article.getContent(),
				article.getId());
	}

	public Article query(String id) {
		String sql = "SELECT  id,title,content from itcast_lucene_news.article where id = ?";
		return jdbcTemplate.query(sql, new String[] { id },
				new ResultSetExtractor<Article>() {

					public Article extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						Article article = new Article();
						if (rs.next()) {
							article.setId(rs.getString(1));
							article.setTitle(rs.getString(2));
							article.setContent(rs.getString(3));
						}
						return article;
					}

				});
	}

}
