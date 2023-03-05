package pro.hexa.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.hexa.backend.domain.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

}
