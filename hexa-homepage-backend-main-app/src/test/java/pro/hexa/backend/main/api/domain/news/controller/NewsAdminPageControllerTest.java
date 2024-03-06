package pro.hexa.backend.main.api.domain.news.controller;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import pro.hexa.backend.domain.news.domain.News;
import pro.hexa.backend.domain.news.model.NEWS_TYPE;
import pro.hexa.backend.domain.news.repository.NewsRepository;
import pro.hexa.backend.main.api.domain.news.dto.AdminCreateNewsRequestDto;
import pro.hexa.backend.main.api.domain.news.dto.AdminModifyNewsRequestDto;
import pro.hexa.backend.main.api.domain.news.dto.AdminNewsDetailResponse;
import pro.hexa.backend.main.api.domain.news.dto.AdminNewsListResponse;

@SpringBootTest
class NewsAdminPageControllerTest {
    @Autowired
    private NewsAdminPageController newsAdminPageController;

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void getAdminNewsList() {
        // given
        int pageNum = 1;
        int perPage = 3;
        List<News> newsList = makeNewsForTest();
        newsRepository.saveAllAndFlush(newsList);

        // when
        ResponseEntity<AdminNewsListResponse> response = newsAdminPageController.getAdminNewsList(pageNum, perPage);

        // then
        Assertions.assertNotNull(response);

    }

    @Test
    void getAdminNewsDetail() {
        // given
        Long newsId = 2L;
        List<News> newsList = makeNewsForTest();
        newsRepository.saveAllAndFlush(newsList);

        // when
        ResponseEntity<AdminNewsDetailResponse> response = newsAdminPageController.getAdminNewsDetail(newsId);

        // then
        Assertions.assertNotNull(response);
    }

    @Test
    void adminCreateNews() {
        // given
        List<News> newsList = makeNewsForTest();
        News createdNews = newsList.get(0);
        AdminCreateNewsRequestDto adminCreateNewsRequestDto = new AdminCreateNewsRequestDto(
            createdNews.getNewsType(),
            createdNews.getTitle(),
            createdNews.getDate(),
            createdNews.getContent()
        );
        // when
        ResponseEntity response = newsAdminPageController.adminCreateNews(adminCreateNewsRequestDto);

        // then
        Assertions.assertNotNull(response);
    }

    @Test
    void adminModifyNews() {
        // given
        List<News> newsList = makeNewsForTest();
        newsRepository.saveAllAndFlush(newsList);
        AdminModifyNewsRequestDto adminModifyNewsRequestDto = new AdminModifyNewsRequestDto(2L, NEWS_TYPE.공지, "HELLO WWORLD123", LocalDate.now(), "modify content");

        // when
        ResponseEntity response = newsAdminPageController.adminModifyNews(adminModifyNewsRequestDto);

        // then
        Assertions.assertNotNull(response);
    }

    @Test
    void adminDeleteNews() {
        // given
        Long newsId = 2L;
        List<News> newsList = makeNewsForTest();
        newsRepository.saveAllAndFlush(newsList);
        // when
        ResponseEntity response = newsAdminPageController.adminDeleteNews(newsId);

        // then
        Assertions.assertNotNull(response);
    }

    private List<News> makeNewsForTest() {
        News news1 = News.create(0L, NEWS_TYPE.수상, "Hello World1", LocalDate.now(), "Heelo new Students1");
        News news2 = News.create(1L, NEWS_TYPE.수상, "Hello World2", LocalDate.now(), "Heelo new Students2");
        News news3 = News.create(2L, NEWS_TYPE.수상, "Hello World3", LocalDate.now(), "Heelo new Students3");
        News news4 = News.create(3L, NEWS_TYPE.수상, "Hello World4", LocalDate.now(), "Heelo new Students4");
        News news5 = News.create(4L, NEWS_TYPE.수상, "Hello World5", LocalDate.now(), "Heelo new Students5");

        return List.of(news1, news2, news3, news4, news5);
    }
}