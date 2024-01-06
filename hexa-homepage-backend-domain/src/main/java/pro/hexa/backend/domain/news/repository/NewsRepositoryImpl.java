package pro.hexa.backend.domain.news.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import pro.hexa.backend.domain.news.domain.News;
import pro.hexa.backend.domain.news.domain.QNews;

import java.util.List;



@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<News> findForMainPageByQuery() {
        QNews news = QNews.news;
        return queryFactory.selectFrom(news)
            .orderBy(news.date.desc())
            .limit(3)
            .fetch();
    }

    /*  News 리스트 조회를 위한 메서드 (페이징 처리)
    *  1. input : pageNum - 보여줄 페이지 쪽수, perPage - 한 페이지당 보여줄 항목 수
    *  2. output : news List 리턴 (pageNum 쪽의 perPage 갯수 만큼) */
    @Override
    public List<News> findAllWithPaging(Integer pageNum, Integer perPage) {
        QNews news = QNews.news;

        return queryFactory.selectFrom(news)
                .offset((long) pageNum * perPage)
                .limit(perPage)
                .fetch();
    }

    /*  전체 페이지 수를 리턴하기 위한 메서드
    *  1. input : perPage - 한 페이지당 갯수
    *  2. output : 전체 페이지 수 */
    @Override
    public int getMaxPage(Integer perPage) {
        QNews news = QNews.news;

        BooleanExpression whereQuery = news.createdAt.isNotNull();


        return Math.toIntExact(queryFactory.select(news.id.count())
                .from(news)
                .where(whereQuery)
                .fetchFirst()) / perPage;  // 전체 페이지 수 = 전체 데이터 갯수 / 한 페이지 당 항목 수 perPage
    }
}
