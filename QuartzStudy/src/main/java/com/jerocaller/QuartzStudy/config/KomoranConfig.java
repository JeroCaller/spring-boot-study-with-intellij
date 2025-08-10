package com.jerocaller.QuartzStudy.config;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KomoranConfig {

    @Bean
    public Komoran getKomoran() {
        return new Komoran(DEFAULT_MODEL.FULL);
    }
}
