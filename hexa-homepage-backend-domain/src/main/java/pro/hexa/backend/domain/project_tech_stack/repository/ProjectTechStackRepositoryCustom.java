package pro.hexa.backend.domain.project_tech_stack.repository;

import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;

import java.util.List;

public interface ProjectTechStackRepositoryCustom {

    List<ProjectTechStack> findTechStackByQuery();

    List<ProjectTechStack> getTechStackByContentList(List<String> content);
}
