package pro.hexa.backend.main.api.domain.main.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.hexa.backend.domain.news.domain.News;
import pro.hexa.backend.domain.news.model.NEWS_TYPE;
import pro.hexa.backend.domain.news.repository.NewsRepository;
import pro.hexa.backend.main.api.domain.main.dto.MainPageNewsDto;
import pro.hexa.backend.main.api.domain.main.dto.MainPageResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MainPageServiceTest {
    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private MainPageService mainPageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given : News를 몇 개 채워서 save해줌.
        LocalDate nowDate = LocalDate.now();

        newsRepository.deleteAll();
        List<News> newsList = new ArrayList<>(Arrays.asList(
                News.create(1L, NEWS_TYPE.공지, "HeXA OT", nowDate, "Come to the Club Room"),
                News.create(2L, NEWS_TYPE.공지, "HeXA Graduation", nowDate.minusMonths(3), "congratulation"),
                News.create(3L, NEWS_TYPE.공지, "HeXA MT", nowDate.plusDays(3), "Let's Go to the Busan"),
                News.create(4L, NEWS_TYPE.수상, "HeXA Top Coder contest", nowDate.plusMonths(3), "student A is top coder"),
                News.create(5L, NEWS_TYPE.공지, "HeXA Meeting", nowDate.plusDays(10), "meeting about project A"))
        );
        newsRepository.saveAll(newsList);
        when(newsRepository.findAll()).thenReturn(newsList);


    }

    @Test
    public void TestGetMainPageResponse() {

        /*
        given : 테스트를 위한 데이터를 준비하는 단계

        when : 테스트를 실행하는 단계 (실제 테스트하려는 함수를 쓰는 것)

        then : 테스트가 잘 통과했는지 검증하는 단계

         * task 1 : mainPageResponse가 null인지 check

         * task 2 : mainPageResponse의 News 들 중에서 id값이 공통인 게 없는지 check
         * task 3 : mainPageResponse의 News 들의 date가 format에 맞는지 check
         * task 3 - 1 : + date가 null이 아닌지 check
         * task 4 : mainPageResponse의 News 들의 title이 null이 아닌지 check
         * */
        // task 1
        checkMainPageResponseNullity();
        // task 2
        checkDuplicatedId();
        // task 3
        checkDateFormat();
        // task 3-1
        checkDateNullity();
        // task 4
        checkTitleNullity();
    }

    @Test
    @DisplayName("mainPageResponse가 null인지 check")
    private void checkMainPageResponseNullity() {
        // when
        MainPageResponse mainPageResponse = mainPageService.getMainPageResponse();

        // then
        assertNotNull(mainPageResponse);
    }

    @Test
    @DisplayName("mainPageResponse의 News 들 중에서 id값이 공통인 게 없는지 check")
    private void checkDuplicatedId() {
        // when
        MainPageResponse mainPageResponse = mainPageService.getMainPageResponse();

        // then
        Stream<Long> distinctIds = mainPageResponse.getNewsList()
                .stream()
                .map(MainPageNewsDto::getNewsId)
                .distinct();
        assertEquals(mainPageResponse.getNewsList()
                .stream()
                .map(MainPageNewsDto::getNewsId), distinctIds);
    }


    @Test
    @DisplayName("date의 format check")
    private void checkDateFormat() {
        // when
        MainPageResponse mainPageResponse = mainPageService.getMainPageResponse();

        // then
        final String dateRegex = "(0-9)*.(0-9)*.(0-9)*";
        assertEquals(true, mainPageResponse.getNewsList()
                .stream()
                .map(MainPageNewsDto::getDate)
                .allMatch(str -> str.matches(dateRegex)));

    }

    @Test
    @DisplayName("date의 null check")
    private void checkDateNullity() {
        // when
        MainPageResponse mainPageResponse = mainPageService.getMainPageResponse();

        // then
        assertEquals(true, mainPageResponse.getNewsList()
                .stream()
                .map(MainPageNewsDto::getDate)
                .allMatch(Objects::nonNull));
    }

    @Test
    @DisplayName("mainPageResponse의 News 들의 title이 null이 아닌지 check")
    private void checkTitleNullity() {
        // when
        MainPageResponse mainPageResponse = mainPageService.getMainPageResponse();

        // then
        assertEquals(true, mainPageResponse.getNewsList()
                .stream()
                .map(MainPageNewsDto::getTitle)
                .allMatch(Objects::nonNull));
    }

}