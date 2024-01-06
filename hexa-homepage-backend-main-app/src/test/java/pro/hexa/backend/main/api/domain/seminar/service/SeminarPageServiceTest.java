package pro.hexa.backend.main.api.domain.seminar.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.hexa.backend.domain.seminar.domain.Seminar;
import pro.hexa.backend.domain.seminar.repository.SeminarRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.main.api.domain.seminar.dto.SeminarListResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


public class SeminarPageServiceTest {

    @Mock
    private SeminarRepository seminarRepository;

    @InjectMocks
    private SeminarPageService seminarPageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetSeminarListResponse() {
        String searchText = "example";
        Integer year = 2023;
        Integer pageNum = 200;
        Integer page = 10;

        // Mock findAllByQuery
        List<Seminar> mockedSeminars = createMockedSeminars(pageNum);
        when(seminarRepository.findAllByQuery(searchText, year, pageNum, page)).thenReturn(mockedSeminars);

        // Mock getMaxPage
        when(seminarRepository.getMaxPage(searchText, year, page)).thenReturn(mockedSeminars.size());

        SeminarListResponse seminarListResponse = seminarPageService.getSeminarListResponse(searchText, year, pageNum, page);

        assertNotNull(seminarListResponse);
        assertEquals(mockedSeminars.size(), seminarListResponse.getMaxPage());
        assertEquals(page, seminarListResponse.getPage());
    }

    private List<Seminar> createMockedSeminars(int count) {
        List<Seminar> seminars = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = createUser(i);
            Seminar seminar = Seminar.create(LocalDateTime.of(2023, 1, 1, 0, 0), user,new ArrayList<>());
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
}
