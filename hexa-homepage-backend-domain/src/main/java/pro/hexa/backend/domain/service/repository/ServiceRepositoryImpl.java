package pro.hexa.backend.domain.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import pro.hexa.backend.domain.attachment.domain.QAttachment;
import pro.hexa.backend.domain.service.domain.QService;
import pro.hexa.backend.domain.service.domain.Service;

@RequiredArgsConstructor
public class ServiceRepositoryImpl implements ServiceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Service> getAll() {
        QService service = QService.service;
        QAttachment attachment = QAttachment.attachment;
        return queryFactory.selectFrom(service)
            .leftJoin(service.thumbnail, attachment)
            .fetchJoin()
            .fetch();
    }

    @Override
    public Optional<Service> findByIdByQuery(Long id) {
        QService service = QService.service;
        QAttachment attachment = QAttachment.attachment;
        return Optional.ofNullable(queryFactory.selectFrom(service)
            .leftJoin(service.thumbnail, attachment)
            .fetchJoin()
            .fetchOne());
    }

}
