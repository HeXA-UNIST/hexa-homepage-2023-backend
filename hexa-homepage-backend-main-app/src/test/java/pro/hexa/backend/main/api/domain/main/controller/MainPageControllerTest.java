package pro.hexa.backend.main.api.domain.main.controller;


import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import pro.hexa.backend.domain.news.domain.News;
import pro.hexa.backend.domain.news.repository.NewsRepository;
import pro.hexa.backend.main.api.domain.main.dto.MainPageResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pro.hexa.backend.domain.news.model.NEWS_TYPE.공지;

@SpringBootTest
class MainPageControllerTest {

    @Autowired
    private MainPageController mainPageController;

    @Autowired
    private NewsRepository newsRepository;


    @Test
    void getMainPageResponse() {
        News news1 = News.create(
            127L,
            공지,
            "title",
            LocalDate.of(2023, 1, 1),
            "some"
        );

        newsRepository.save(news1);

        ResponseEntity<MainPageResponse> response = mainPageController.getMainPageResponse();

        MainPageResponse mainPageResponse = response.getBody();
        assertNotNull(mainPageResponse);

    }
}
