package com.jerocaller.QuartzStudy.service;

import com.jerocaller.QuartzStudy.data.entity.GoogleTrend;
import com.jerocaller.QuartzStudy.data.repository.GoogleTrendRepository;
import com.jerocaller.QuartzStudy.data.repository.NewsRepository;
import com.jerocaller.QuartzStudy.feign.GoogleTrendFeign;
import com.jerocaller.QuartzStudy.feign.response.Item;
import com.jerocaller.QuartzStudy.feign.response.NewsItem;
import com.jerocaller.QuartzStudy.feign.response.RssWrapper;
import com.jerocaller.QuartzStudy.mapper.XmlDtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleTrendService {

    private final GoogleTrendRepository googleTrendRepository;
    private final NewsRepository newsRepository;
    private final GoogleTrendFeign googleTrendFeign;

    public RssWrapper getResult() {
        RssWrapper result = googleTrendFeign.getAllTrending();
        log.info("구글 트렌드 검색 가져오기 결과");
        log.info(result.toString());

        log.info("First item info");
        LocalDateTime pubDate = XmlDtoMapper.convertPubDate(
            result.getChannel()
                .getItems()
                .getFirst()
        );
        log.info("publishd date at first item: {}", pubDate);
        log.info("least traffic at first item: {}", XmlDtoMapper.parseTraffic(
            result.getChannel()
                .getItems()
                .getFirst()
        ));

        log.info("item size: {}", result.getChannel().getItems().size());

        return result;
    }

    @Transactional
    public void saveData() {
        RssWrapper rssResult = googleTrendFeign.getAllTrending();

        for (Item item : rssResult.getChannel().getItems()) {
            Optional<GoogleTrend> googleTrendOpt = googleTrendRepository
                .findByTitle(item.getTitle());
            GoogleTrend googleTrend = null;

            if (googleTrendOpt.isEmpty()) {
                googleTrend = googleTrendRepository.save(XmlDtoMapper.toGoogleTrendEntity(item));
            } else {
                googleTrend = googleTrendOpt.get();
                XmlDtoMapper.updateGoogleTrendEntity(googleTrend, item);
            }

            if (item.getNewsItems() != null) {
                for (NewsItem newsItem : item.getNewsItems()) {
                    if (newsRepository.existsByTitle(newsItem.getTitle())) {
                        continue;
                    }
                    newsRepository.save(XmlDtoMapper.toNewsEntity(newsItem, googleTrend));
                }
            }
        }

    }
}
