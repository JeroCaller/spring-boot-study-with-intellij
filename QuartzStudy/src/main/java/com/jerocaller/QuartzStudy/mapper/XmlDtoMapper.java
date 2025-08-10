package com.jerocaller.QuartzStudy.mapper;

import com.jerocaller.QuartzStudy.data.entity.GoogleTrend;
import com.jerocaller.QuartzStudy.data.entity.News;
import com.jerocaller.QuartzStudy.feign.response.Item;
import com.jerocaller.QuartzStudy.feign.response.NewsItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlDtoMapper {

    public static LocalDateTime convertPubDate(Item item) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        OffsetDateTime offsetDateTime = null;

        try {
            offsetDateTime = OffsetDateTime.parse(item.getPubDateStr(), dateTimeFormatter);
        } catch (DateTimeParseException e) {
            log.error("날짜 파싱 에러");
            e.printStackTrace();
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }

    public static Integer parseTraffic(Item item) {
        String pureNumber = item.getApproxTraffic().replace("+", "");
        return Integer.parseInt(pureNumber);
    }

    public static GoogleTrend toGoogleTrendEntity(Item item) {
        return GoogleTrend.builder()
            .title(item.getTitle())
            .pubDate(convertPubDate(item))
            .imageUrl(item.getImageUrl())
            .imageSource(item.getImageSource())
            .leastTraffic(parseTraffic(item))
            .build();
    }

    public static News toNewsEntity(NewsItem newsItem, GoogleTrend savedGoogleTrend) {
        return News.builder()
            .title(newsItem.getTitle())
            .url(newsItem.getUrl())
            .newsSource(newsItem.getSource())
            .googleTrend(savedGoogleTrend)
            .build();
    }

    /**
     * <p>
     *     Dirty Checking 방식으로 업데이트
     * </p>
     * @param googleTrend
     * @param item
     */
    public static void updateGoogleTrendEntity(GoogleTrend googleTrend, Item item) {
        googleTrend.setPubDate(convertPubDate(item));
        googleTrend.setImageSource(item.getImageSource());
        googleTrend.setLeastTraffic(parseTraffic(item));
        googleTrend.updateCounter();
    }
}
