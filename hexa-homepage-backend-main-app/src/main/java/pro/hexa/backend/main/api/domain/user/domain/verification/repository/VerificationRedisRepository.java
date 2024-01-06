package pro.hexa.backend.main.api.domain.user.domain.verification.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.hexa.backend.main.api.domain.user.domain.verification.domain.Verification;

@Repository
public interface VerificationRedisRepository extends CrudRepository<Verification, Long> {
    Optional<Verification> findByUserId(String userId);
}
