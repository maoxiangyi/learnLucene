package cn.maoxiangyi.lucene.crud;


import cn.maoxiangyi.lucene.crud.domain.Article;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public interface ArticleDao {
	
	/**
	 * 插入新闻内容
	 * @param article
	 * @throws Exception
	 */
	void insert(Article article)  throws Exception;
	/**
	 * 通过编号删除新闻
	 * @param id
	 */
	void del(String id);
	/**
	 * 更新新闻内容
	 * @param article
	 */
	void update(Article article);
	/**
	 * 查询新闻内容
	 * @param id
	 * @return
	 */
	Article query(String id);
	

}
