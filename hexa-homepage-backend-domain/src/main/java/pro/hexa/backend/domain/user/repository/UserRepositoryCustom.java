package pro.hexa.backend.domain.user.repository;

import java.util.List;
import pro.hexa.backend.domain.user.domain.User;

public interface UserRepositoryCustom {
    List<User> findAdminAll();

    List<User> findByName(String name);
}
