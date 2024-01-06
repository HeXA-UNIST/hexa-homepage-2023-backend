package pro.hexa.backend.main.api.domain.news.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.hexa.backend.domain.news.domain.News;
import pro.hexa.backend.domain.news.repository.NewsRepository;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.domain.news.dto.AdminCreateNewsRequestDto;
import pro.hexa.backend.main.api.domain.news.dto.AdminModifyNewsRequestDto;
import pro.hexa.backend.main.api.domain.news.dto.AdminNewsDetailResponse;
import pro.hexa.backend.main.api.domain.news.dto.AdminNewsDto;
import pro.hexa.backend.main.api.domain.news.dto.AdminNewsListResponse;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsAdminPageService {
    private final NewsRepository newsRepository;

    public AdminNewsListResponse getAdminNewsList(
            Integer pageNum, Integer perPage
    ) {
        List<News> newsList = newsRepository.findAllWithPaging(pageNum, perPage);
        List<AdminNewsDto> adminNewsDtos = newsList.stream()
                .map(news -> {
                    AdminNewsDto adminNewsDto = new AdminNewsDto();
                    adminNewsDto.fromNews(news);
                    return adminNewsDto;
                })
                .collect(Collectors.toList());

        int maxPage = newsRepository.getMaxPage(perPage);

        return AdminNewsListResponse.builder()
                .totalPage(maxPage)
                .list(adminNewsDtos)
                .build();
    }

    public AdminNewsDetailResponse getAdminNewsDetail(Long newsId) {
        AdminNewsDetailResponse adminNewsDetailResponse = new AdminNewsDetailResponse();

        News foundNews = newsRepository.findById(newsId)
                .orElseThrow((() -> new BadRequestException(BadRequestType.NEWS_NOT_FOUND)));
        adminNewsDetailResponse.fromNews(foundNews);

        return adminNewsDetailResponse;
    }

    @Transactional
    public void adminCreateNews(AdminCreateNewsRequestDto adminCreateNewsRequestDto) {
        News createdNews = News.create(null,
                adminCreateNewsRequestDto.getNewsType(),
                adminCreateNewsRequestDto.getTitle(),
                adminCreateNewsRequestDto.getDate(),
                adminCreateNewsRequestDto.getContent());

        newsRepository.save(createdNews);
    }

    @Transactional
    public void adminModifyNews(AdminModifyNewsRequestDto adminModifyNewsRequestDto) {
        Long newsId = adminModifyNewsRequestDto.getNewsId();
        News foundNews = newsRepository.findById(newsId)
                            .orElseThrow((() -> new BadRequestException(BadRequestType.NEWS_NOT_FOUND)));

        Optional.ofNullable(adminModifyNewsRequestDto.getNewsType())
                .ifPresent(foundNews::updateNewsType);
        Optional.ofNullable(adminModifyNewsRequestDto.getTitle())
                .ifPresent(foundNews::updateTitle);
        Optional.ofNullable(adminModifyNewsRequestDto.getDate())
                .ifPresent(foundNews::updateDate);
        Optional.ofNullable(adminModifyNewsRequestDto.getContent())
                .ifPresent(foundNews::updateContent);
    }

    @Transactional
    public void adminDeleteNews(Long newsId) {
        newsRepository.deleteById(newsId);
    }
}

