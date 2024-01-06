package pro.hexa.backend.main.api.domain.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.attachment.repository.AttachmentRepository;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project.repository.ProjectRepository;
import pro.hexa.backend.domain.project_member.domain.ProjectMember;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;
import pro.hexa.backend.domain.project_tech_stack.repository.ProjectTechStackRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.domain.project.dto.AdminCreateProjectRequestDto;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectDetailResponse;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectDto;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectListResponse;

class ProjectAdminPageServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectTechStackRepository projectTechStackRepository;
    @Mock
    private AttachmentRepository attachmentRepository;
    @InjectMocks
    private ProjectAdminPageService projectAdminPageService;


    @Test
    void getAdminProjectList() {
        // given
        int pageNum = 1;
        int perPage = 3;
        List<Project> projectList = initProjectListForTest();
        Mockito.when(projectRepository.findAllInAdminPage(pageNum, perPage)).thenReturn(projectList.subList(0, 3));
        Mockito.when(projectRepository.getAdminMaxPage(perPage)).thenReturn(2);

        // when
        AdminProjectListResponse result = projectAdminPageService.getAdminProjectList(pageNum, perPage);
        AdminProjectListResponse expectedResult = AdminProjectListResponse
            .builder()
            .totalPage(2)
            .list(projectList.subList(0, 3).stream()
                .map(p -> {
                    AdminProjectDto dto = new AdminProjectDto();
                    dto.fromProject(p);
                    return dto;
                }).collect(Collectors.toList()))
            .build();

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getTotalPage(), expectedResult.getTotalPage());
        for(int i = 0; i < perPage; i++){
            AdminProjectDto resultDto = result.getList().get(i);
            AdminProjectDto expectedDto = expectedResult.getList().get(i);
            Assertions.assertEquals(resultDto.getProjectId(), expectedDto.getProjectId());
            Assertions.assertEquals(resultDto.getThumbnail(), expectedDto.getThumbnail());
            Assertions.assertEquals(resultDto.getState(), expectedDto.getState());
            Assertions.assertEquals(resultDto.getTitle(), expectedDto.getTitle());
        }

    }

    @Test
    void getAdminProjectDetail() {
        // given
        Long projectId = 2L;
        List<Project> projectList = initProjectListForTest();
        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectList.get(2)));

        // when
        AdminProjectDetailResponse result = projectAdminPageService.getAdminProjectDetail(projectId);
        AdminProjectDetailResponse expectedResult = new AdminProjectDetailResponse();
        expectedResult.fromProject(projectList.get(2));

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getProjectTechStacks(), expectedResult.getProjectTechStacks());
        Assertions.assertEquals(result.getContent(), expectedResult.getContent());
        Assertions.assertEquals(result.getEndDate(), expectedResult.getEndDate());
        Assertions.assertEquals(result.getState(), expectedResult.getState());
        Assertions.assertEquals(result.getStartDate(), expectedResult.getStartDate());
        Assertions.assertEquals(result.getTitle(), expectedResult.getTitle());
        Assertions.assertEquals(result.getThumbnail(), expectedResult.getThumbnail());


    }

    @Test
    void adminCreateProject() {
        // given
        List<String> projectTechStacks = new ArrayList<>(Arrays.asList("java", "python"));
        AdminCreateProjectRequestDto adminCreateProjectRequestDto1 = AdminCreateProjectRequestDto.builder()
            .title("")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .projectTechStacks(projectTechStacks)
            .state("recruitment")
            .content("make the chat gpt")
            .thumbnail(0L)
            .build();
        AdminCreateProjectRequestDto adminCreateProjectRequestDto2 = AdminCreateProjectRequestDto.builder()
            .title("projectName")
            .startDate(null)
            .endDate(LocalDate.now())
            .projectTechStacks(projectTechStacks)
            .state("recruitment")
            .content("make the chat gpt")
            .thumbnail(0L)
            .build();
        AdminCreateProjectRequestDto adminCreateProjectRequestDto3 = AdminCreateProjectRequestDto.builder()
            .title("projectName")
            .startDate(LocalDate.now())
            .endDate(null)
            .projectTechStacks(projectTechStacks)
            .state("recruitment")
            .content("make the chat gpt")
            .thumbnail(0L)
            .build();
        AdminCreateProjectRequestDto adminCreateProjectRequestDto4 = AdminCreateProjectRequestDto.builder()
            .title("projectName")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .projectTechStacks(null)
            .state("recruitment")
            .content("make the chat gpt")
            .thumbnail(0L)
            .build();
        AdminCreateProjectRequestDto adminCreateProjectRequestDto5 = AdminCreateProjectRequestDto.builder()
            .title("projectName")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .projectTechStacks(projectTechStacks)
            .state("")
            .content("make the chat gpt")
            .thumbnail(0L)
            .build();
        AdminCreateProjectRequestDto adminCreateProjectRequestDto6 = AdminCreateProjectRequestDto.builder()
            .title("projectName")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .projectTechStacks(projectTechStacks)
            .state("recruitment")
            .content("")
            .thumbnail(0L)
            .build();
        AdminCreateProjectRequestDto adminCreateProjectRequestDto7 = AdminCreateProjectRequestDto.builder()
            .title("projectName")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .projectTechStacks(projectTechStacks)
            .state("recruitment")
            .content("make the chat gpt")
            .thumbnail(null)
            .build();


        // when & then (invalid input test)
        Assertions.assertThrows(BadRequestException.class, ()-> {projectAdminPageService.adminCreateProject(adminCreateProjectRequestDto1);}, "title이 NULL입니다");
        Assertions.assertThrows(BadRequestException.class, ()-> {projectAdminPageService.adminCreateProject(adminCreateProjectRequestDto2);}, "startDate가 NULL입니다");
        Assertions.assertThrows(BadRequestException.class, ()-> {projectAdminPageService.adminCreateProject(adminCreateProjectRequestDto3);}, "endDate가 NULL입니다");
        Assertions.assertThrows(BadRequestException.class, ()-> {projectAdminPageService.adminCreateProject(adminCreateProjectRequestDto4);}, "projectStacks이 NULL입니다");
        Assertions.assertThrows(BadRequestException.class, ()-> {projectAdminPageService.adminCreateProject(adminCreateProjectRequestDto5);}, "state가 NULL입니다");
        Assertions.assertThrows(BadRequestException.class, ()-> {projectAdminPageService.adminCreateProject(adminCreateProjectRequestDto6);}, "content가 NULL입니다");
        Assertions.assertThrows(BadRequestException.class, ()-> {projectAdminPageService.adminCreateProject(adminCreateProjectRequestDto7);}, "thumbnail이 NULL입니다");

    }



    private List<Project> initProjectListForTest() {
        List<ProjectTechStack> projectTechStackList = new ArrayList<>();
        projectTechStackList.add(ProjectTechStack.create("Java"));
        projectTechStackList.add(ProjectTechStack.create("Python"));

        List<ProjectMember> projectMembers1 = new ArrayList<>();
        User user1 = User.create("hexa1", "hexa1@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2021, "20211234", "KIM JS", "1234", AUTHORIZATION_TYPE.Member);
        User user2 = User.create("hexa2", "hexa2@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2020, "20201234", "MJ", "4321", AUTHORIZATION_TYPE.Admin);
        User user3 = User.create("hexa3", "hexa3@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2023, "20231234", "JEUNGHUN KIM", "5123", AUTHORIZATION_TYPE.Pro);
        User user4 = User.create("hexa4", "hexa4@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2021, "20211234", "YHS", "2468", AUTHORIZATION_TYPE.Member);
        User user5 = User.create("hexa5", "hexa5@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2020, "20201234", "JJM", "9876", AUTHORIZATION_TYPE.Admin);
        User user6 = User.create("hexa6", "hexa6@unist.ac.kr", GENDER_TYPE.남, STATE_TYPE.재학, (short) 2023, "20231234", "KIM LEE PARK", "6123", AUTHORIZATION_TYPE.Pro);

        projectMembers1.add(ProjectMember.create(user1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Member));
        projectMembers1.add(ProjectMember.create(user2, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Admin));
        Attachment attachmentForTest = Attachment.create("home/in/", "thumbnailForTest", 3L);

        Project project1 = Project.createForTest(0L, "hexa homepage renewal1", LocalDateTime.now(), LocalDateTime.now(), projectTechStackList, projectMembers1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Admin,
            pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "please many people join this project1", attachmentForTest);
        Project project2 = Project.createForTest(1L, "hexa homepage renewal2", LocalDateTime.now(), LocalDateTime.now(), projectTechStackList, projectMembers1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Admin,
            pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "please many people join this project2", attachmentForTest);
        Project project3 = Project.createForTest(2L, "hexa homepage renewal3", LocalDateTime.now(), LocalDateTime.now(), projectTechStackList, projectMembers1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Admin,
            pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "please many people join this project3", attachmentForTest);
        Project project4 = Project.createForTest(3L, "hexa homepage renewal4", LocalDateTime.now(), LocalDateTime.now(), projectTechStackList, projectMembers1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Admin,
            pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "please many people join this project4", attachmentForTest);
        Project project5 = Project.createForTest(4L, "hexa homepage renewal5", LocalDateTime.now(), LocalDateTime.now(), projectTechStackList, projectMembers1, pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE.Admin,
            pro.hexa.backend.domain.project.model.STATE_TYPE.모집중, "please many people join this project5", attachmentForTest);
        return new ArrayList<>(Arrays.asList(project1, project2, project3, project4, project5));
    }
}