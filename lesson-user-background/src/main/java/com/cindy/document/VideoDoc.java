package com.cindy.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 开发ES的Doc实体类
 * Doc实体类用于映射ES数据，相当于映射数据库数据的Entity实体类
 */
@Data
@Document(indexName = "i_video")  //一个实体类对应一条文档，索引自动创建
public class VideoDoc {

    @Id  //按主键查询时会使用该字段
    private Integer id;

    /**
     * analyzer：表示存储的时候使用哪种分词效果
     * searchAnalyzer：表示查询的时候使用哪种分词效果
     */
    //可以对文本进行分词

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Text)
    private String info;

    //不可以对文本进行分词

    @Field(type = FieldType.Keyword)
    @JsonProperty("summary-image")
    private String summaryImage;

    @Field(type = FieldType.Keyword)
    @JsonProperty("cover-image")
    private String coverImage;

    //Double型属性

    @Field(type = FieldType.Double)
    private Double price;

    //Integer型属性

    @Field(type = FieldType.Integer)
    private Integer star;

    //默认时间戳

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create-time")
    private Date createTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last-modify")
    private Date lastModify;
}
