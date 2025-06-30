package com.jerocaller.QuartzStudy.feign.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JacksonXmlRootElement(
    localName = "new_item",
    namespace = "https://trends.google.com/trending/rss"
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsItem {

    @JacksonXmlProperty(
        localName = "news_item_title",
        namespace = "https://trends.google.com/trending/rss"
    )
    private String title;

    @JacksonXmlProperty(
        localName = "news_item_url",
        namespace = "https://trends.google.com/trending/rss"
    )
    private String url;

    @JacksonXmlProperty(
        localName = "news_item_source",
        namespace = "https://trends.google.com/trending/rss"
    )
    private String source;
}
