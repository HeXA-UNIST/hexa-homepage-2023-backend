package pro.hexa.backend;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Profile({"default"})
public class HexaHomepageBackendApplicationStartUp implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createSupervisor();
    }

    private void createSupervisor() {
        List<User> users = userRepository.findAdminAll();
        if (!users.isEmpty()) {
            return;
        }

        User user = User.create(
            "HexaSuperVis0r",
            "hexa@unist.ac.kr",
            GENDER_TYPE.기타,
            STATE_TYPE.재학,
            (short) 2023,
            "20230000",
            "관리자",
            "0000",
            AUTHORIZATION_TYPE.Admin
        );

        userRepository.save(user);
    }
}
