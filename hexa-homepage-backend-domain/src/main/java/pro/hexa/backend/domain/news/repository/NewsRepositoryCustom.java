package pro.hexa.backend.domain.news.repository;

import pro.hexa.backend.domain.news.domain.News;

import java.util.List;

public interface NewsRepositoryCustom {

    List<News> findForMainPageByQuery();

    List<News> findAllWithPaging(Integer pageNum, Integer perPage);

    int getMaxPage(Integer perPage);
}
