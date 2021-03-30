package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.services.CarouselService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    private List<String> images = new ArrayList<>();
    private Logger LOGGER = LoggerFactory.getLogger(CarouselServiceImpl.class);

    public CarouselServiceImpl(@Value("${carousel.images}") List<String> images) {
        this.images.addAll(images);
    }

    @PostConstruct
    public void afterInitialize() {
        if (images.size() < 3) {
            throw new IllegalArgumentException("Configured less than three images");
        }
    }

    @Override
    public String firstImage() {
        return images.get(0);
    }

    @Override
    public String secondImage() {
        return images.get(1);
    }

    @Override
    public String thirdImage() {
        return images.get(3);
    }

    @Scheduled(cron =  "*/10 * * * * *")
    public void refresh() {
        LOGGER.info("Shuffling images...");
        Collections.shuffle(images);
    }
}
