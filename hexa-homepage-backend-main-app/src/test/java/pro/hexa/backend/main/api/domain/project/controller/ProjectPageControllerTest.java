package pro.hexa.backend.main.api.domain.project.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.attachment.repository.AttachmentRepository;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project.model.STATE_TYPE;
import pro.hexa.backend.domain.project.repository.ProjectRepository;
import pro.hexa.backend.domain.project_member.domain.ProjectMember;
import pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.project_member.repository.ProjectMemberRepository;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;
import pro.hexa.backend.domain.project_tech_stack.repository.ProjectTechStackRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.domain.project.dto.ProjectDto;
import pro.hexa.backend.main.api.domain.project.dto.ProjectListResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProjectPageControllerTest {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectTechStackRepository projectTechStackRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ProjectPageController projectPageController;

    @Test
    void getProjectListResponse() {
        // given
        ProjectTechStack projectTechStack1 = ProjectTechStack.create("Java");
        ProjectTechStack projectTechStack2 = ProjectTechStack.create("Spring");
        ProjectTechStack projectTechStack3 = ProjectTechStack.create("Python");
        ProjectTechStack projectTechStack4 = ProjectTechStack.create("C++");

        projectTechStackRepository.saveAllAndFlush(
            List.of(
                projectTechStack1, projectTechStack2, projectTechStack3, projectTechStack4
            )
        );

        Attachment attachment1 = Attachment.create("path/to/thumbnail", "thumbnail.jpg", 100L);
        Attachment attachment2 = Attachment.create("path/to/thumbnail", "thumbnail.jpg", 100L);
        Attachment attachment3 = Attachment.create("path/to/thumbnail", "thumbnail.jpg", 100L);
        Attachment attachment4 = Attachment.create("path/to/thumbnail", "thumbnail.jpg", 100L);
        Attachment attachment5 = Attachment.create("path/to/thumbnail", "thumbnail.jpg", 100L);

        attachmentRepository.saveAllAndFlush(
            List.of(
                attachment1, attachment2, attachment3, attachment4, attachment5
            )
        );

        User user1 = User.createForTest("id1", "name1", attachment1);
        User user2 = User.createForTest("id2", "name2", attachment2);
        User user3 = User.createForTest("id3", "name3", attachment3);
        User user4 = User.createForTest("id4", "name4", attachment4);
        User user5 = User.createForTest("id5", "name5", attachment5);

        userRepository.saveAllAndFlush(
            List.of(
                user1, user2, user3, user4, user5
            )
        );

        ProjectMember projectMember1 = ProjectMember.create(user1, AUTHORIZATION_TYPE.Member);
        ProjectMember projectMember2 = ProjectMember.create(user2, AUTHORIZATION_TYPE.Member);
        ProjectMember projectMember3 = ProjectMember.create(user3, AUTHORIZATION_TYPE.Member);
        ProjectMember projectMember4 = ProjectMember.create(user4, AUTHORIZATION_TYPE.Member);
        ProjectMember projectMember5 = ProjectMember.create(user5, AUTHORIZATION_TYPE.Member);

        projectMemberRepository.saveAllAndFlush(
            List.of(
                projectMember1, projectMember2, projectMember3, projectMember4, projectMember5
            )
        );

        List<ProjectTechStack> projectTechStacks1 = List.of(projectTechStack1, projectTechStack3);
        List<ProjectTechStack> projectTechStacks2 = List.of(projectTechStack2);
        List<ProjectTechStack> projectTechStacks3 = List.of(projectTechStack4);

        List<ProjectMember> projectMembers1 = List.of(projectMember1, projectMember2);
        List<ProjectMember> projectMembers2 = List.of(projectMember3);
        List<ProjectMember> projectMembers3 = List.of(projectMember4, projectMember5);

        Project project1 = Project.createForTest(
            null,
            "Web Development",
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 12, 31, 0, 0),
            projectTechStacks1,
            projectMembers1,
            AUTHORIZATION_TYPE.Member,
            STATE_TYPE.승인중,
            "This is a project about web development",
            attachment1
        );

        Project project2 = Project.createForTest(
            null,
            "Mobile App Development",
            LocalDateTime.of(2023, 2, 1, 0, 0),
            LocalDateTime.of(2023, 11, 30, 0, 0),
            projectTechStacks2,
            projectMembers2,
            AUTHORIZATION_TYPE.Pro,
            STATE_TYPE.승인중,
            "This is a project about mobile app development",
            attachment3
        );

        Project project3 = Project.createForTest(
            null,
            "Machine Learning Research",
            LocalDateTime.of(2023, 3, 1, 0, 0),
            LocalDateTime.of(2023, 10, 31, 0, 0),
            projectTechStacks3,
            projectMembers3,
            AUTHORIZATION_TYPE.Admin,
            STATE_TYPE.진행완료,
            "This is a project about ML research",
            attachment4
        );

        projectRepository.saveAllAndFlush(
            List.of(
                project1, project2, project3
            )
        );

        // when
        ResponseEntity<ProjectListResponse> response = projectPageController.getProjectListResponse(
            "M",
            List.of(STATE_TYPE.승인중),
            "asc",
            List.of("Spring"),
            List.of(""),
            2023,
            1,
            10
        );

        ProjectListResponse responseBody = response.getBody();

        // then
        List<ProjectDto> projects = Optional.ofNullable(responseBody)
            .map(ProjectListResponse::getProjects)
            .orElse(null);

        assertNotNull(projects);
        assertEquals(0, projects.size());
        assertEquals(10, responseBody.getPage());
        assertEquals(1, responseBody.getMaxPage());

    }

}
