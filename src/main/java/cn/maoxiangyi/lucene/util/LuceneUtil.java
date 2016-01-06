package cn.maoxiangyi.lucene.util;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * Describe:
 * Author:   maoxiangyi
 * Domain:   www.maoxiangyi.cn
 * Data:     2016/1/6.
 */
public class LuceneUtil {

	private static IndexWriter indexWriter;
	private static IndexSearcher indexSearcher;
	private static Directory directory;
	private static Analyzer analyzer;

	static {

		try {
			directory = FSDirectory.open(new File("index"));
			analyzer = new IKAnalyzer();
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(
					Version.LATEST, analyzer));
			indexWriter.commit();
			indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						indexWriter.commit();
						indexWriter.close();
						System.out.println("lucene 正常退出！");
					} catch (Exception e) {
						System.out.println("lucene退出异常" + e);
					}
				}
			});

		} catch (IOException e) {
			System.out.println("初始化lucene util 失败！");
		}
	}

	public static IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public static IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static void printResult(IndexSearcher indexSearcher, TopDocs topDocs)
			throws IOException {
		System.out.println(topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			System.out.println(scoreDoc.score);
			Document document = indexSearcher.doc(scoreDoc.doc);
			System.out.println("id:" + document.get("id"));
			System.out.println("title:" + document.get("title"));
			System.out.println("content:" + document.get("content"));
		}
	}

	public static Directory getDirectory() {
		return directory;
	}
	
	

}
