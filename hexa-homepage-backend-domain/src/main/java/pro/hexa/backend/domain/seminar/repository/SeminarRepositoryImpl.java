package pro.hexa.backend.domain.seminar.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import pro.hexa.backend.domain.attachment.domain.QAttachment;
import pro.hexa.backend.domain.seminar.domain.QSeminar;
import pro.hexa.backend.domain.seminar.domain.Seminar;
import java.util.List;

@RequiredArgsConstructor
public class SeminarRepositoryImpl implements SeminarRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Seminar> findForSeminarListByQuery(String searchText, Integer year, Integer pageNum, Integer page) {
        QSeminar seminar = QSeminar.seminar;
        QAttachment attatchment = QAttachment.attachment;

        List<Seminar> content = queryFactory.selectFrom(seminar)
            .leftJoin(seminar.attachments, attatchment).fetchJoin()
            .where(seminar.title.contains(searchText)
                // year는 무엇을 기준으로 할지 제대로 정의 되어있지 않음
            )
            .offset(pageNum)
            .limit(page)
            .fetch();

        return content; // totalsize도 같이 넘겨야됨

    }

    @Override
    public int getMaxPage(String searchText, Integer year, Integer pageNum, Integer page) {
        QSeminar seminar = QSeminar.seminar;
        QAttachment attatchment = QAttachment.attachment;

        int maxPage = queryFactory.selectFrom(seminar)
            .leftJoin(seminar.attachments, attatchment).fetchJoin()
            .where(seminar.title.contains(searchText)
                // year는 무엇을 기준으로 할지 제대로 정의 되어있지 않음
            )
            .fetch().size();

        return maxPage;
    }
}
