package pro.hexa.backend.domain.project_tech_stack.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;
import pro.hexa.backend.domain.project_tech_stack.domain.QProjectTechStack;

import java.util.List;

@RequiredArgsConstructor
public class ProjectTechStackRepositoryImpl implements ProjectTechStackRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProjectTechStack> findTechStackByQuery() {
        QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
        List<ProjectTechStack> techStackList = queryFactory.selectFrom(projectTechStack).fetch();
        return techStackList;
    }

    @Override
    public List<ProjectTechStack> getTechStackByContentList(List<String> content) {
        QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
        return queryFactory.selectFrom(projectTechStack)
            .where(projectTechStack.content.in(content))
            .fetch();
    }
}
