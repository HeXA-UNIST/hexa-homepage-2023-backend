package pro.hexa.backend.main.api.domain.seminar.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.hexa.backend.domain.abstract_activity.domain.AbstractActivity;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.seminar.domain.Seminar;
import pro.hexa.backend.domain.seminar.repository.SeminarRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.domain.seminar.dto.SeminarAttatchmentDto;
import pro.hexa.backend.main.api.domain.seminar.dto.SeminarDto;
import pro.hexa.backend.main.api.domain.seminar.dto.SeminarListResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SeminarPageControllerTest {

    @Autowired
    private SeminarPageController seminarPageController;

    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private UserRepository userRepository;

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

        User user2 = User.create(
                "user2",
                "user2",
                GENDER_TYPE.남,
                STATE_TYPE.재학,
                (short) 2022,
                "20202020",
                "User",
                "password",
                AUTHORIZATION_TYPE.Member
        );

        User user3 = User.create(
                "user3",
                "user3",
                GENDER_TYPE.남,
                STATE_TYPE.재학,
                (short) 2022,
                "20202023",
                "User",
                "password",
                AUTHORIZATION_TYPE.Member
        );


        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<Seminar> seminars = new ArrayList<>();
        seminars.add(Seminar.create(
            LocalDateTime.of(2023, 1, 1, 1, 1),
            user1,
            new ArrayList<>(),
                "title2",
                "content2"
        ));

        seminars.add(Seminar.create(
            LocalDateTime.of(2024, 1, 1, 1, 1),
            user2,
            new ArrayList<>(),
                "title1",
                "content1"
        ));

        Seminar seminar1 = Seminar.create(
            LocalDateTime.of(2023, 1, 2, 1, 1),
            user3,
            new ArrayList<>(),
                "title1",
                "content1"
        );
        seminars.add(seminar1);

        seminarRepository.saveAll(seminars);

        // when
        SeminarListResponse response = seminarPageController.getSeminarListResponse("t", 2023, 1, 3).getBody();

        // then
        assertThat(response).isNotNull();

        Map<Long, Seminar> seminarMap = seminars.stream()
            .collect(Collectors.toMap(AbstractActivity::getId, Function.identity()));
        response.getSeminars().parallelStream()
            .forEach(seminarDto -> {
                Seminar seminar = seminarMap.get(seminarDto.getSeminarId());
                assertSeminar(seminarDto, seminar);
            });

        assertEquals(response.getSeminars().size(), 3);

    }

    private void assertSeminar(SeminarDto seminarDto, Seminar seminar) {
        assertEquals(seminarDto.getTitle(), seminar.getTitle());
        assertEquals(seminarDto.getContent(), seminar.getContent());
        assertEquals(seminarDto.getWriterUserId(), seminar.getUser().getId());
        assertEquals(seminarDto.getWriterName(), seminar.getUser().getName());

        // assert attachments
        assertAttachments(seminarDto.getAttachment(), seminar.getAttachments());
    }

    private void assertAttachments(List<SeminarAttatchmentDto> attatchmentDtos, List<Attachment> attachments) {
        List<Triple<String, String, String>> attachmentDtoTripleList = attatchmentDtos.stream()
            .map(attachment -> Triple.of(attachment.getName(), attachment.getSize().toString(), attachment.getUrl()))
            .collect(Collectors.toList());

        Attachment failedAttachment = attachments.stream().filter(attachment ->
            attachmentDtoTripleList.stream().anyMatch(triple ->
                triple.getLeft().equals(attachment.getName()) &&
                    triple.getMiddle().equals(attachment.getSize().toString()) &&
                    triple.getRight().equals(attachment.getLocation()))
        ).findAny().orElse(null);

        assertNull(failedAttachment);
    }
}
