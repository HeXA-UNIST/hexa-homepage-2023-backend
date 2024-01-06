package pro.hexa.backend.domain.attachment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttachmentRepositoryImpl {
    private final JPAQueryFactory queryFactory;
}
