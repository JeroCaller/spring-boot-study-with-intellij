package com.jerocaller.QuartzStudy.feign.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JacksonXmlRootElement(localName = "item")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class Item {

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(
        localName = "approx_traffic",
        namespace = "https://trends.google.com/trending/rss"
    )
    private String approxTraffic;

    @JacksonXmlProperty(
        localName = "picture",
        namespace = "https://trends.google.com/trending/rss"
    )
    private String imageUrl;

    @JacksonXmlProperty(
        localName = "picture_source",
        namespace = "https://trends.google.com/trending/rss"
    )
    private String imageSource;

    @JacksonXmlProperty(localName = "pubDate")
    private String pubDateStr;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(
        localName = "news_item",
        namespace = "https://trends.google.com/trending/rss"
    )
    private List<NewsItem> newsItems;
}
