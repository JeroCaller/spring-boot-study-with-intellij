package com.jerocaller.QuartzStudy.feign;

import com.jerocaller.QuartzStudy.feign.response.RssWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "googleTrend",
    url="https://trends.google.co.kr/trending/rss?geo=KR"
)
public interface GoogleTrendFeign {

    @GetMapping(consumes = MediaType.APPLICATION_RSS_XML_VALUE)
    RssWrapper getAllTrending();
}
