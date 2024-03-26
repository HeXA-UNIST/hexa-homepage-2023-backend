package pro.hexa.backend.main.api.domain.seminar.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.attachment.repository.AttachmentRepository;
import pro.hexa.backend.domain.seminar.domain.Seminar;
import pro.hexa.backend.domain.seminar.repository.SeminarRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.config.security.dto.CustomUserDetails;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminCreateSeminarRequestDto;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminModifySeminarRequestDto;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarDetailResponse;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarListResponse;

@SpringBootTest
class SeminarAdminPageControllerTest {

    @Autowired
    private SeminarAdminPageController seminarAdminPageController;
    @Autowired
    private SeminarRepository seminarRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        User user1 = User.create(
                "user",
                "user",
                GENDER_TYPE.남,
                STATE_TYPE.재학,
                (short) 2020,
                "20202020",
                "User",
                "password",
                AUTHORIZATION_TYPE.Member
        );
        CustomUserDetails user = new CustomUserDetails(user1);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));

    }
    @Test
    @DisplayName("세미나 리스트 조회")
    void getSeminarListResponse() {
        // given
        User user1 = User.create(
                "user",
                "user",
                GENDER_TYPE.남,
                STATE_TYPE.재학,
                (short) 2020,
                "20202020",
                "User",
                "password",
                AUTHORIZATION_TYPE.Member
        );

        userRepository.save(user1);

        List<Seminar> seminars = new ArrayList<>();
        seminars.add(Seminar.create(
                LocalDateTime.of(2023, 1, 1, 1, 1),
                user1,
                new ArrayList<>(),
                "title1",
                "content1"
        ));

        seminars.add(Seminar.create(
                LocalDateTime.of(2024, 1, 1, 1, 1),
                user1,
                new ArrayList<>(),
                "title1",
                "content1"
        ));

        Seminar seminar1 =Seminar.create(
                LocalDateTime.of(2023, 1, 2, 1, 1),
                user1,
                new ArrayList<>(),
                "title3",
                "content3");
        seminars.add(seminar1);

        seminarRepository.saveAll(seminars);

        // when
        AdminSeminarListResponse response = seminarAdminPageController.getAdminSeminarList(1,3).getBody();

        // then
        assertThat(response).isNotNull();

        assertEquals(response.getTotalPage(), 0);
        assertEquals(response.getList().size(), 0);
    }

    @Test
    @DisplayName("세미나 수정 창에서 보여줄 정보 조회")
    void getAdminSeminarDetail() {
        User user1 = User.create(
                "user",
                "user",
                GENDER_TYPE.남,
                STATE_TYPE.재학,
                (short) 2020,
                "20202020",
                "User",
                "password",
                AUTHORIZATION_TYPE.Member
        );

        userRepository.save(user1);

        List<Seminar> seminars = new ArrayList<>();
        seminars.add(Seminar.create(
                LocalDateTime.of(2023, 1, 1, 1, 1),
                user1,
                new ArrayList<>(),
                "title1",
                "content1"
        ));

        seminars.add(Seminar.create(
                LocalDateTime.of(2024, 1, 1, 1, 1),
                user1,
                new ArrayList<>(),
                "title1",
                "content1"
        ));

        Seminar seminar1 =Seminar.create(
                LocalDateTime.of(2023, 1, 2, 1, 1),
                user1,
                new ArrayList<>(),
                "title3",
                "content3");

        seminars.add(seminar1);
        seminarRepository.saveAll(seminars);

        AdminSeminarDetailResponse response = seminarAdminPageController.getAdminSeminarDetail(seminar1.getId()).getBody();

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("세미나 생성")
    void adminCreateSeminar() {
        Attachment attachment1 = Attachment.create("1","att1", 100L);
        Attachment attachment2 = Attachment.create("2","att2", 200L);
        Attachment attachment3 = Attachment.create("3","att3", 300L);
        attachmentRepository.save(attachment1);
        attachmentRepository.save(attachment2);
        attachmentRepository.save(attachment3);
        List<Long> longList = new ArrayList<>();
        longList.add(attachment1.getId());
        longList.add(attachment2.getId());
        longList.add(attachment3.getId());
        Date currentdate = new Date();
        AdminCreateSeminarRequestDto adminCreateSeminarRequestDto = new AdminCreateSeminarRequestDto("title1", "this is content1", currentdate, longList);

        seminarAdminPageController.adminCreateSeminar(adminCreateSeminarRequestDto);

        List<Seminar> seminars = seminarRepository.findAll();
        assertEquals(seminars.size(), 1);
    }

    @Test
    @DisplayName("세미나 수정")
    void adminModifySeminar() {
        User user2 = User.create(
                "user",
                "user",
                GENDER_TYPE.남,
                STATE_TYPE.재학,
                (short) 2020,
                "20202020",
                "User",
                "password",
                AUTHORIZATION_TYPE.Member
        );
        userRepository.save(user2);
        Attachment attachment1 = Attachment.create("1","att1", 100L);
        Attachment attachment2 = Attachment.create("2","att2", 200L);
        Attachment attachment3 = Attachment.create("3","att3", 300L);
        attachmentRepository.save(attachment1);
        attachmentRepository.save(attachment2);
        attachmentRepository.save(attachment3);
        List<Long> longList = new ArrayList<>();
        longList.add(attachment1.getId());
        longList.add(attachment2.getId());
        longList.add(attachment3.getId());
        List<Seminar> seminars = new ArrayList<>();
        seminars.add(Seminar.create(
                LocalDateTime.of(2023, 1, 1, 1, 1),
                user2,
                new ArrayList<>(),
                "title1",
                "content1"
        ));

        seminars.add(Seminar.create(
                LocalDateTime.of(2024, 1, 1, 1, 1),
                user2,
                new ArrayList<>(),
                "title2",
                "content2"
        ));

        Seminar seminar1 = Seminar.create(
                LocalDateTime.of(2023, 1, 2, 1, 1),
                user2,
                new ArrayList<>(),
                "title3",
                "content3"
        );

        seminars.add(seminar1);

        seminarRepository.saveAll(seminars);

        AdminModifySeminarRequestDto adminCreateSeminarRequestDto = new AdminModifySeminarRequestDto(seminar1.getId(), "title14", "This is content4", LocalDateTime.of(2023, 1, 11,1,1),longList);

        seminarAdminPageController.adminModifySeminar(adminCreateSeminarRequestDto);

        Optional<Seminar> seminarv = seminarRepository.findById(seminar1.getId());
        assertEquals(seminarv.get().getTitle(), "title14");
    }

    @Test
    @DisplayName("세미나 삭제")
    void adminDeleteSeminar() {

        User user1 = User.create(
                "user",
                "user",
                GENDER_TYPE.남,
                STATE_TYPE.재학,
                (short) 2020,
                "20202020",
                "User",
                "password",
                AUTHORIZATION_TYPE.Member
        );

        userRepository.save(user1);

        List<Seminar> seminars = new ArrayList<>();
        seminars.add(Seminar.create(
                LocalDateTime.of(2023, 1, 1, 1, 1),
                user1,
                new ArrayList<>(),
                "title1",
                "content1"
        ));

        seminars.add(Seminar.create(
                LocalDateTime.of(2024, 1, 1, 1, 1),
                user1,
                new ArrayList<>(),
                "title2",
                "content2"
        ));


        Seminar seminar1 =Seminar.create(
                LocalDateTime.of(2023, 1, 2, 1, 1),
                user1,
                new ArrayList<>(),
                "title3",
                "content3");

        seminars.add(seminar1);

        seminarRepository.saveAll(seminars);

        Optional<Seminar> result1 = seminarRepository.findById(seminar1.getId());

        assertNotNull(result1);

        seminarAdminPageController.adminDeleteSeminar(seminar1.getId());

        Optional<Seminar> result2 = seminarRepository.findById(seminar1.getId());

        assertEquals(Optional.empty(), result2);

    }
}