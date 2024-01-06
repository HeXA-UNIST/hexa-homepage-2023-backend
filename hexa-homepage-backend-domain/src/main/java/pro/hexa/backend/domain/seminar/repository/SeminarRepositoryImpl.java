package pro.hexa.backend.domain.seminar.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pro.hexa.backend.domain.attachment.domain.QAttachment;
import pro.hexa.backend.domain.seminar.domain.QSeminar;
import pro.hexa.backend.domain.seminar.domain.Seminar;


@RequiredArgsConstructor
public class SeminarRepositoryImpl implements SeminarRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Seminar> findAllByQuery(String searchText, Integer year, Integer pageNum, Integer perPage) {
        QSeminar seminar = QSeminar.seminar;
        QAttachment attachment = QAttachment.attachment;

        BooleanExpression whereQuery = getWhereQuery(searchText, year, seminar);

        return queryFactory.selectFrom(seminar)
            .leftJoin(seminar.attachments, attachment).fetchJoin()
            .where(whereQuery)
            .offset((long) pageNum * perPage)
            .limit(perPage)
            .fetch();
    }

    @Override
    public int getMaxPage(String searchText, Integer year, Integer perPage) {
        QSeminar seminar = QSeminar.seminar;

        BooleanExpression whereQuery = getWhereQuery(searchText, year, seminar);

        return Math.toIntExact(queryFactory.select(seminar.id.count())
            .from(seminar)
            .where(whereQuery)
            .fetchFirst()) / perPage;
    }

    private BooleanExpression getWhereQuery(String searchText, Integer year, QSeminar seminar) {
        BooleanExpression whereQuery = seminar.createdAt.isNotNull();

        if (StringUtils.isNotBlank(searchText)) {
            whereQuery = whereQuery.and(seminar.title.contains(searchText));
        }

        if (year != null) {
            LocalDateTime standardDateForYear = LocalDateTime.of(year, 1, 1, 0, 0);
            whereQuery = whereQuery.and(seminar.updatedAt.between(standardDateForYear, standardDateForYear.plusYears(1)));
        }
        return whereQuery;
    }

    @Override
    public Optional<Seminar> findByQuery(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        QSeminar seminar = QSeminar.seminar;
        QAttachment attachment = QAttachment.attachment;

        Seminar result = queryFactory.selectFrom(seminar)
                .leftJoin(seminar.attachments, attachment).fetchJoin()
                .where(seminar.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Seminar> findAllInAdminPage(Integer pageNum, Integer perPage) {
        QSeminar seminar = QSeminar.seminar;
        return queryFactory.selectFrom(seminar)
                .orderBy(seminar.title.desc())
                .offset((long) pageNum * perPage)
                .limit(perPage)
                .fetch();
    }

    @Override
    public int getAdminMaxPage(Integer perPage) {
        QSeminar seminar = QSeminar.seminar;

        return Math.toIntExact(queryFactory.select(seminar.id.count().divide(perPage))
                .from(seminar)
                .fetchFirst());
    }
}
