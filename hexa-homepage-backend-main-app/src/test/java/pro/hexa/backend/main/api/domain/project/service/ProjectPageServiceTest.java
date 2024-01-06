package pro.hexa.backend.main.api.domain.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project.model.STATE_TYPE;
import pro.hexa.backend.domain.project.repository.ProjectRepository;
import pro.hexa.backend.domain.project_member.domain.ProjectMember;
import pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;
import pro.hexa.backend.domain.project_tech_stack.repository.ProjectTechStackRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.main.api.domain.project.dto.ProjectListResponse;
import pro.hexa.backend.main.api.domain.project.dto.ProjectResponse;
import pro.hexa.backend.main.api.domain.project.dto.ProjectTechStackResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


class ProjectPageServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectTechStackRepository projectTechStackRepository;

    @InjectMocks
    private ProjectPageService projectPageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProjectListResponse() {
        // given
        String searchText = "test";
        List<STATE_TYPE> status = List.of(STATE_TYPE.승인중);
        String sort = "asc";
        List<String> includeTechStack = List.of("Java", "Spring");
        List<String> excludeTechStack = List.of("Python");
        Integer year = 2023;
        Integer pageNum = 110;
        int perPage = 10;

        List<Project> mockedProjects = createMockedProjects(pageNum);

        // Mock repository
        when(projectRepository.findAllByQuery(
            searchText, status, sort, includeTechStack, excludeTechStack, year, pageNum, perPage
        )).thenReturn(mockedProjects);

        when(projectRepository.getTotalCount(searchText, status, sort, includeTechStack, excludeTechStack, year))
            .thenReturn(1300);

        // When
        ProjectListResponse response = projectPageService.getProjectListResponse(
            searchText, status, sort, includeTechStack, excludeTechStack, year, pageNum, perPage
        );

        // Then
        assertNotNull(response);
        assertEquals(perPage, response.getPage());
        assertEquals(130, response.getMaxPage());
    }


    private List<Project> createMockedProjects(int count) {
        List<Project> projects = new ArrayList<>();
        ProjectTechStack projectTechStack1 = ProjectTechStack.create("Java");
        ProjectTechStack projectTechStack3 = ProjectTechStack.create("Python");

        Attachment attachment = Attachment.create("/path/to/file.jpg", "file.jpg", 2048L);

        User user1 = User.createForTest("testId",
            "hexapro",
            attachment
        );
        ProjectMember projectMember1 = ProjectMember.create(user1, AUTHORIZATION_TYPE.Member);

        Attachment attachment2 = Attachment.create("/path/to/thumbnail.jpg", "thumbnail.jpg", 100L);

        for (int i = 0; i < count; i++) {
            Project project = Project.createForTest(
                (long) i,
                "title",
                LocalDateTime.of(2023, 3, 3, 3, 3),
                LocalDateTime.of(2023, 4, 3, 3, 3),
                List.of(projectTechStack1, projectTechStack3), List.of(projectMember1),
                AUTHORIZATION_TYPE.Member,
                STATE_TYPE.승인중,
                "This is a project about web development",
                attachment2
            );

            projects.add(project);
        }

        return projects;
    }

    @Test
    void testGetProjectResponse1() {
        // Given
        Long projectId = 1L;
        Optional<Project> mockedProject = createMockedProject(projectId);

        // Mock repository
        when(projectRepository.findByQuery(projectId)).thenReturn(mockedProject);

        // When
        ProjectResponse response = projectPageService.getProjectResponse(projectId);

        // Then
        assertNotNull(response);
        assertEquals(projectId, response.getProjectId());
    }

    @Test
    void testGetProjectResponse2() {
        // Given
        Long projectId = 1L;

        // Mock repository
        when(projectRepository.findByQuery(projectId)).thenReturn(Optional.empty());

        // When
        ProjectResponse response = projectPageService.getProjectResponse(projectId);

        // Then
        assertNotNull(response);
        assertNull(response.getProjectId());
    }

    private Optional<Project> createMockedProject(Long projectId) {

        ProjectTechStack projectTechStack1 = ProjectTechStack.create("Java");
        Attachment attachment1 = Attachment.create("/path/to/file.jpg", "file.jpg", 2048L);

        User user1 = User.createForTest(
            "testId",
            "hexapro",
            attachment1
        );

        ProjectMember projectMember1 = ProjectMember.create(user1, AUTHORIZATION_TYPE.Member);

        Attachment attachment2 = Attachment.create("/path/to/thumbnail.jpg", "thumbnail.jpg", 100L);

        return Optional.of(Project.createForTest(
            projectId,
            "title",
            LocalDateTime.of(2023, 3, 3, 3, 3),
            LocalDateTime.of(2023, 4, 3, 3, 3),
            List.of(projectTechStack1), List.of(projectMember1),
            AUTHORIZATION_TYPE.Member,
            STATE_TYPE.승인중,
            "This is a project about web development",
            attachment2
        ));
    }

    @Test
    void getProjectTechStackResponse() {
        // Given
        List<ProjectTechStack> mockedProjectTechStackList = createMockedProjectTechStacks(25);


        when(projectTechStackRepository.findTechStackByQuery()).thenReturn(mockedProjectTechStackList);
        ProjectTechStackResponse response = projectPageService.getProjectTechStackResponse();

        // Then
        assertNotNull(response);
        assertEquals(25, response.getTechStackList().size());

    }

    private List<ProjectTechStack> createMockedProjectTechStacks(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> ProjectTechStack.create("내용" + i))
            .collect(Collectors.toList());
    }

}
