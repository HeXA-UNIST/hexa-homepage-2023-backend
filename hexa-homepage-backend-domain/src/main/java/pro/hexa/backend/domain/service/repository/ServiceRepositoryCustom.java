package pro.hexa.backend.domain.service.repository;

import java.util.List;
import java.util.Optional;
import pro.hexa.backend.domain.service.domain.Service;

public interface ServiceRepositoryCustom {
    List<Service> getAll();
    Optional<Service> findByIdByQuery(Long id);
}
