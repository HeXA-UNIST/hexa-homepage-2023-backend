package pro.hexa.backend.domain.project.repository;

import java.util.List;
import java.util.Optional;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project.model.STATE_TYPE;

public interface ProjectRepositoryCustom {

    List<Project> findAllByQuery(String searchText, List<STATE_TYPE> status, String sort, List<String> includeTechStack,
        List<String> excludeTechStack, Integer year, Integer pageNum, Integer perPage);

    Optional<Project> findByQuery(Long projectId);

    int getTotalCount(String searchText, List<STATE_TYPE> status, String sort, List<String> includeTechStack,
        List<String> excludeTechStack, Integer year);

    List<Project> findAllInAdminPage(Integer pageNum, Integer perPage);

    int getAdminMaxPage(Integer perPage);
}
