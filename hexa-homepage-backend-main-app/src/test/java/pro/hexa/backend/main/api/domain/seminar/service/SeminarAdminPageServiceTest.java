package pro.hexa.backend.main.api.domain.seminar.service;

import java.util.Arrays;
import java.util.Date;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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

@ExtendWith(MockitoExtension.class)
class SeminarAdminPageServiceTest {

    @Mock
    private SeminarRepository seminarRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SeminarAdminPageService seminarAdminPageService;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
    }

    private List<Seminar> createMockedSeminars(int count) {
        List<Seminar> seminars = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = createUser(i);
            Seminar seminar = Seminar.create(
                    LocalDateTime.of(2023, 1, 1, 0, 0),
                    user,
                    new ArrayList<>(),
                    "title1",
                    "content1");
            seminars.add(seminar);
        }
        return seminars;
    }

    private User createUser(int index) {
        return User.create(
                "user" + index, // ID
                "user" + index + "@example.com", // Email
                GENDER_TYPE.남, // Gender
                STATE_TYPE.재학, // State
                (short) 2020, // Registration Year
                "20202020" + index, // Registration Number
                "User " + index, // Name
                "password" + index, // Password
                AUTHORIZATION_TYPE.Member
        );
    }

    @Test
    void getAdminSeminarList_WhenListIsNotEmpty() {
        int pageNum = 1;
        int perPage = 10;
        List<Seminar> mockedSeminars = createMockedSeminars(10);
        when(seminarRepository.findAllInAdminPage(pageNum, perPage)).thenReturn(mockedSeminars);
        when(seminarRepository.getAdminMaxPage(perPage)).thenReturn(mockedSeminars.size());

        var response = seminarAdminPageService.getAdminSeminarList(pageNum, perPage);

        assertNotNull(response);
        assertEquals(10, response.getTotalPage());
        assertEquals(mockedSeminars.size(), response.getList().size());
    }

    @Test
    void getAdminSeminarDetail() {
        Long seminarId = 100L;
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
        Seminar seminar1 = Seminar.create(
                LocalDateTime.of(2024, 1, 1, 1, 1),
                user1,
                new ArrayList<>(),
                "title1",
                "content1"
        );
        when(seminarRepository.findById(seminarId)).thenReturn(Optional.of(seminar1));

        var response = seminarAdminPageService.getAdminSeminarDetail(seminarId);

        assertNotNull(response);

    }
}
