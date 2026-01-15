package com.maple.common.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class LuceneUtils {

    @Value("${file.lucenePath}")
    private String lucenePath;

    public String initLuceneData(List<LuceneDataModel> list) {

        Collection<Document> docs = new ArrayList<>();
        for (LuceneDataModel dataModel : list) {
            
            Document document = new Document();

            if (dataModel.getType() == 0) {
                
                document.add(new StoredField("id", "M" + dataModel.getId()));
            } else {
                document.add(new StoredField("id", dataModel.getId()));
            }
            document.add(new StoredField("imageUrl", StringUtils.isBlank(dataModel.getImageUrl()) ? "" : dataModel.getImageUrl()));
            document.add(new StoredField("originalUrl", StringUtils.isBlank(dataModel.getOriginalUrl()) ? "" : dataModel.getOriginalUrl()));
            document.add(new StringField("type", String.valueOf(dataModel.getType()), Field.Store.YES));
            document.add(new TextField("title", dataModel.getTitle(), Field.Store.YES));
            document.add(new TextField("description", StringUtils.isBlank(dataModel.getDescription()) ? "" : dataModel.getDescription(), Field.Store.YES));
            document.add(new TextField("content", StringUtils.isBlank(dataModel.getContent()) ? "" : dataModel.getContent(), Field.Store.NO));
            docs.add(document);
        }
        
        Analyzer analyzer = new IKAnalyzer(true);
        
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(lucenePath));
             IndexWriter indexWriter = new IndexWriter(directory, conf)) {
            
            indexWriter.addDocuments(docs);
            
            indexWriter.commit();
        } catch (Exception e) {
            log.error("创建索引失败", e);
            return "创建索引失败";
        }
        return "创建索引成功";
    }

    public String addLuceneData(LuceneDataModel dataModel) {
        
        Collection<Document> docs = new ArrayList<>();
        
        Document document = new Document();

        if (dataModel.getType() == 0) {
            
            document.add(new StoredField("id", "M" + dataModel.getId()));
        } else {
            document.add(new StoredField("id", dataModel.getId()));
        }
        document.add(new StoredField("imageUrl", StringUtils.isBlank(dataModel.getImageUrl()) ? "" : dataModel.getImageUrl()));
        document.add(new StoredField("originalUrl", StringUtils.isBlank(dataModel.getOriginalUrl()) ? "" : dataModel.getOriginalUrl()));
        document.add(new StringField("type", String.valueOf(dataModel.getType()), Field.Store.YES));
        document.add(new TextField("title", dataModel.getTitle(), Field.Store.YES));
        document.add(new TextField("description", StringUtils.isBlank(dataModel.getDescription()) ? "" : dataModel.getDescription(), Field.Store.YES));
        document.add(new TextField("content", StringUtils.isBlank(dataModel.getContent()) ? "" : dataModel.getContent(), Field.Store.NO));
        docs.add(document);
        
        Analyzer analyzer = new IKAnalyzer(true);
        
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        
        conf.setOpenMode(IndexWriterConfig.OpenMode.APPEND);

        try (Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(lucenePath));
             IndexWriter indexWriter = new IndexWriter(directory, conf)) {
            
            indexWriter.addDocuments(docs);
            
            indexWriter.commit();
        } catch (Exception e) {
            log.error("创建索引失败", e);
            return "创建索引失败";
        }
        return "创建索引成功";
    }

    public String updateLuceneData(LuceneDataModel dataModel) {
        
        Document document = new Document();

        document.add(new StoredField("id", dataModel.getId()));
        document.add(new StoredField("imageUrl", StringUtils.isBlank(dataModel.getImageUrl()) ? "" : dataModel.getImageUrl()));
        document.add(new StoredField("originalUrl", StringUtils.isBlank(dataModel.getOriginalUrl()) ? "" : dataModel.getOriginalUrl()));
        document.add(new StringField("type", String.valueOf(dataModel.getType()), Field.Store.YES));
        document.add(new TextField("title", dataModel.getTitle(), Field.Store.YES));
        document.add(new TextField("description", StringUtils.isBlank(dataModel.getDescription()) ? "" : dataModel.getDescription(), Field.Store.YES));
        document.add(new TextField("content", StringUtils.isBlank(dataModel.getContent()) ? "" : dataModel.getContent(), Field.Store.NO));
        
        Analyzer analyzer = new IKAnalyzer(true);
        
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        
        conf.setOpenMode(IndexWriterConfig.OpenMode.APPEND);

        try (Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(lucenePath));
             IndexWriter indexWriter = new IndexWriter(directory, conf)) {
            
            Term term;
            if (dataModel.getType() == 0) {
                term = new Term("id", "M" + dataModel.getId());
            } else {
                term = new Term("id", String.valueOf(dataModel.getId()));
            }
            indexWriter.updateDocument(term, document);
            
            indexWriter.commit();
        } catch (Exception e) {
            log.error("修改索引失败", e);
            return "修改索引失败";
        }
        return "修改索引成功";
    }

    public String deleteLuceneData(LuceneDataModel dataModel) {
        Analyzer analyzer = new IKAnalyzer(true);
        
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);

        try (Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(lucenePath));
             IndexWriter indexWriter = new IndexWriter(directory, conf)) {
            
            Term term;
            if (dataModel.getType() == 0) {
                term = new Term("id", "M" + dataModel.getId());
            } else {
                term = new Term("id", String.valueOf(dataModel.getId()));
            }
            indexWriter.deleteDocuments(term);
            
            indexWriter.commit();
        } catch (Exception e) {
            log.error("删除索引失败", e);
            return "删除索引失败";
        }
        return "删除索引成功";
    }
}
