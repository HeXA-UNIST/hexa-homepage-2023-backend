package pro.hexa.backend.main.api.domain.project.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.attachment.repository.AttachmentRepository;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project.repository.ProjectRepository;
import pro.hexa.backend.domain.project_member.domain.ProjectMember;
import pro.hexa.backend.domain.project_member.repository.ProjectMemberRepository;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;
import pro.hexa.backend.domain.project_tech_stack.repository.ProjectTechStackRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.domain.project.dto.AdminCreateProjectRequestDto;
import pro.hexa.backend.main.api.domain.project.dto.AdminModifyProjectRequestDto;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectDetailResponse;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectListResponse;

@SpringBootTest
class ProjectAdminPageControllerTest {
    @Autowired
    private ProjectAdminPageController projectAdminPageController;

    @Autowired
    private ProjectTechStackRepository projectTechStackRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAdminProjectList() {
        // given
        int pageNum = 1;
        int perPage = 3;

        List<Project> projects = makeProjectListForTest();
        projectRepository.saveAllAndFlush(projects);

        // when
        ResponseEntity<AdminProjectListResponse> adminProjectListResponseResponseEntity = projectAdminPageController.getAdminProjectList(pageNum, perPage);

        // then
        Assertions.assertNotNull(adminProjectListResponseResponseEntity);

    }


    @Test
    void getAdminProjectDetail() {
        // given
        List<Project> projects = makeProjectListForTest();
        projects.stream().forEach(p -> projectRepository.save(p));
        Long projectId = 1L;

        // when
        ResponseEntity<AdminProjectDetailResponse> response = projectAdminPageController.getAdminProjectDetail(projectId);

        // then
        Assertions.assertNotNull(response);

    }

    @Test
    void adminCreateProject() {
        // given
        String title = "make chat gpt";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        List<String> projectTechStacks = new ArrayList<>(Arrays.asList("Java", "python"));
        String state = "모집중";
        String content = "chat gpt를 만들어 봐요";
        Long thumbnail = 3L;
        AdminCreateProjectRequestDto adminCreateProjectRequestDto = new AdminCreateProjectRequestDto(
            title, startDate, endDate, projectTechStacks, state, content, thumbnail);

        // when
        ResponseEntity<Void> response = projectAdminPageController.adminCreateProject(adminCreateProjectRequestDto);
        // then
        Assertions.assertNotNull(response);
    }

    @Test
    void adminModifyProject() {
        // given
        Long projectId = 0L;
        List<Project> projects = makeProjectListForTest();
        projects.stream().forEach(p -> projectRepository.save(p));

        Project modifiedProject = projects.get(0);
        List<String> projectTechStack = modifiedProject.getProjectTechStacks().stream()
            .map(p -> p.getContent())
            .collect(Collectors.toList());
        AdminModifyProjectRequestDto adminModifyProjectRequestDto = new AdminModifyProjectRequestDto(0L,
            modifiedProject.getTitle(),
            modifiedProject.getStartDate().toLocalDate(),
            modifiedProject.getEndDate().toLocalDate(),
            projectTechStack,
            modifiedProject.getState().getValue(),
            modifiedProject.getContent(),
            modifiedProject.getThumbnail().getId());

        // when
        ResponseEntity<Void> response = projectAdminPageController.adminModifyProject(adminModifyProjectRequestDto);
        // then
        Assertions.assertNotNull(response);
    }

    @Test
    void adminDeleteProject() {
        // given
        Long projectId = 0L;
        List<Project> projects = makeProjectListForTest();
        projects.stream().forEach(p -> projectRepository.save(p));


        // when
        ResponseEntity<Void> response = projectAdminPageController.adminDeleteProject(projectId);
        // then
        Assertions.assertNotNull(response);

    }

    private List<Project> makeProjectListForTest() {
        User user1 = User.create("abc", "hexa1@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2023, "1234", "MJ", "12345678", AUTHORIZATION_TYPE.Member);
        User user2 = User.create("abcd", "hexa2@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2022, "4321", "JMM", "12344321", AUTHORIZATION_TYPE.Member);
        User user3 = User.create("abcde", "hexa3@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2021, "1324", "JUNG", "56781234", AUTHORIZATION_TYPE.Member);

        ProjectMember member1 = ProjectMember.create(user1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member);
        ProjectMember member2 = ProjectMember.create(user2, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member);
        ProjectMember member3 = ProjectMember.create(user3, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member);

        List<ProjectMember> members1 = new ArrayList<>(Arrays.asList(member1, member2));
        List<ProjectMember> members2 = new ArrayList<>(Arrays.asList(member2, member3));
        List<ProjectMember> members3 = new ArrayList<>(Arrays.asList(member1, member3));

        ProjectTechStack projectTechStack1 = ProjectTechStack.create("Java");
        ProjectTechStack projectTechStack2 = ProjectTechStack.create("Python");
        ProjectTechStack projectTechStack3 = ProjectTechStack.create("C++");

        List<ProjectTechStack> projectTechStackList1 = new ArrayList<>(Arrays.asList(projectTechStack1, projectTechStack2));
        List<ProjectTechStack> projectTechStackList2 = new ArrayList<>(Arrays.asList(projectTechStack2, projectTechStack3));
        List<ProjectTechStack> projectTechStackList3 = new ArrayList<>(Arrays.asList(projectTechStack1, projectTechStack3));


        Attachment attachment = Attachment.create("1234", "thumbnail1", 3L);

        userRepository.saveAllAndFlush(
            List.of(
                user1,
                user2,
                user3
            )
        );

        projectMemberRepository.saveAllAndFlush(
            List.of(
                member1,
                member2,
                member3
            )
        );

        projectTechStackRepository.saveAllAndFlush(
            List.of(
                projectTechStack1,
                projectTechStack2,
                projectTechStack3
            )
        );

        attachmentRepository.saveAllAndFlush(
            List.of(
                attachment
            )
        );

        Project project1 = Project.create("make chatgpt1", LocalDateTime.now(), LocalDateTime.now().plusDays(3), projectTechStackList1, members1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member
            , pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "Let's start make chatgpt with finetuning", attachment);

        Project project2 = Project.create("make chatgpt2", LocalDateTime.now(), LocalDateTime.now().plusDays(3), projectTechStackList2, members2, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member
            , pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "Let's start make chatgpt with finetuning", attachment);

        Project project3 = Project.create("make chatgpt3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), projectTechStackList3, members3, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member
            , pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "Let's start make chatgpt with finetuning", attachment);

        Project project4 = Project.create("make chatgpt4", LocalDateTime.now(), LocalDateTime.now().plusDays(3), projectTechStackList3, members1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member
            , pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "Let's start make chatgpt with finetuning", attachment);

        return new ArrayList<>(Arrays.asList(project1, project2, project3, project4));
    }
}