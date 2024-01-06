package pro.hexa.backend.main.api.domain.project.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.attachment.repository.AttachmentRepository;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project.model.STATE_TYPE;
import pro.hexa.backend.domain.project.repository.ProjectRepository;
import pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;
import pro.hexa.backend.domain.project_tech_stack.repository.ProjectTechStackRepository;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.domain.project.dto.AdminCreateProjectRequestDto;
import pro.hexa.backend.main.api.domain.project.dto.AdminModifyProjectRequestDto;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectDetailResponse;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectDto;
import pro.hexa.backend.main.api.domain.project.dto.AdminProjectListResponse;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectAdminPageService {

    private final ProjectRepository projectRepository;
    private final ProjectTechStackRepository projectTechStackRepository;
    private final AttachmentRepository attachmentRepository;

    public AdminProjectListResponse getAdminProjectList(Integer pageNum, Integer perPage) {

        List<Project> projectList = projectRepository.findAllInAdminPage(pageNum, perPage);
        if (projectList.isEmpty()) {
            return AdminProjectListResponse.builder()
                .totalPage(0)
                .list(new ArrayList<>(0))
                .build();
        }
        int maxPage = projectRepository.getAdminMaxPage(perPage);
        List<AdminProjectDto> projects = projectList.stream()
            .map(project ->
            {
                AdminProjectDto adminProjectDto = new AdminProjectDto();
                adminProjectDto.fromProject(project);
                return adminProjectDto;
            })
            .collect(Collectors.toList());

        return AdminProjectListResponse
            .builder()
            .totalPage(maxPage)
            .list(projects)
            .build();
    }

    public AdminProjectDetailResponse getAdminProjectDetail(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) {
            throw new BadRequestException(BadRequestType.PROJECT_NOT_FOUND);
        }

        AdminProjectDetailResponse adminProjectDetailResponse = new AdminProjectDetailResponse();
        adminProjectDetailResponse.fromProject(project.get());
        return adminProjectDetailResponse;
    }

    @Transactional
    public void adminCreateProject(AdminCreateProjectRequestDto adminCreateProjectRequestDto) {
        validateAdminCreateProjectRequest(adminCreateProjectRequestDto);
        List<ProjectTechStack> projectTechStackList = getProjectTechStackListFromTheContentList(
            adminCreateProjectRequestDto.getProjectTechStacks()
        );

        Attachment thumbnail = Optional.ofNullable(adminCreateProjectRequestDto.getThumbnail())
            .map(attachmentId ->
                attachmentRepository.findById(attachmentId)
                    .orElseThrow(() -> new BadRequestException(BadRequestType.ATTACHMENT_NOT_EXIST))
            ).orElse(null);

        Project project = Project.create(
            adminCreateProjectRequestDto.getTitle(),
            adminCreateProjectRequestDto.getStartDate().atStartOfDay(),
            adminCreateProjectRequestDto.getEndDate().atStartOfDay(),
            projectTechStackList,
            null,
            AUTHORIZATION_TYPE.All,
            STATE_TYPE.valueOf(adminCreateProjectRequestDto.getState()),
            adminCreateProjectRequestDto.getContent(),
            thumbnail
        );

        projectRepository.save(project);
    }

    private void validateAdminCreateProjectRequest(AdminCreateProjectRequestDto adminCreateProjectRequestDto) {
        if (Objects.equals(adminCreateProjectRequestDto.getTitle(), "")) {
            throw new BadRequestException(BadRequestType.INVALID_CREATE_DTO_TITLE);
        }
        if (Objects.equals(adminCreateProjectRequestDto.getContent(), "")) {
            throw new BadRequestException(BadRequestType.INVALID_CREATE_DTO_CONTENT);
        }
        if (adminCreateProjectRequestDto.getProjectTechStacks() == null) {
            throw new BadRequestException(BadRequestType.INVALID_CREATE_DTO_PROJECTSTACKS);
        }
        if (adminCreateProjectRequestDto.getThumbnail() == null) {
            throw new BadRequestException(BadRequestType.INVALID_CREATE_DTO_THUMBNAIL);
        }
        if (adminCreateProjectRequestDto.getStartDate() == null) {
            throw new BadRequestException(BadRequestType.INVALID_CREATE_DTO_START_DATE);
        }
        if (adminCreateProjectRequestDto.getEndDate() == null) {
            throw new BadRequestException(BadRequestType.INVALID_CREATE_DTO_END_DATE);
        }
        if (Objects.equals(adminCreateProjectRequestDto.getState(), "")) {
            throw new BadRequestException(BadRequestType.INVALID_CREATE_DTO_STATE);
        }

    }


    private List<ProjectTechStack> getProjectTechStackListFromTheContentList(List<String> contentList) {
        /*
         * If there is no entity for the given content, it automatically creates and saves one.
         * */
        if (contentList.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<ProjectTechStack> presentingProjectTechStackList = projectTechStackRepository.getTechStackByContentList(contentList);
        List<String> presentingProjectTechStackContentList = presentingProjectTechStackList.stream()
            .map(ProjectTechStack::getContent)
            .collect(Collectors.toList());

        List<ProjectTechStack> newlyCreatedProjectTechStackList = contentList.stream()
            .filter(content -> !presentingProjectTechStackContentList.contains(content))
            .map(ProjectTechStack::create)
            .collect(Collectors.toList());
        projectTechStackRepository.saveAll(newlyCreatedProjectTechStackList);

        return Stream.of(presentingProjectTechStackList, newlyCreatedProjectTechStackList)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    @Transactional
    public void adminModifyProject(AdminModifyProjectRequestDto adminModifyProjectRequestDto) {
        Project project = projectRepository.findByQuery(adminModifyProjectRequestDto.getProjectId())
            .orElseThrow(() -> new BadRequestException(BadRequestType.PROJECT_NOT_FOUND));

        validateAdminModifyProjectRequest(adminModifyProjectRequestDto);

        adminModifyProjectRequestDto.setTitle(
            Optional.ofNullable(adminModifyProjectRequestDto.getTitle())
                .orElseGet(project::getTitle)
        );

        adminModifyProjectRequestDto.setStartDate(
            Optional.ofNullable(adminModifyProjectRequestDto.getStartDate())
                .orElseGet(() -> project.getStartDate().toLocalDate())
        );

        adminModifyProjectRequestDto.setEndDate(
            Optional.ofNullable(adminModifyProjectRequestDto.getEndDate())
                .orElseGet(() -> project.getEndDate().toLocalDate())
        );

        if (CollectionUtils.isEmpty(adminModifyProjectRequestDto.getProjectTechStacks())) {
            adminModifyProjectRequestDto.setProjectTechStacks(
                project.getProjectTechStacks().stream()
                    .map(ProjectTechStack::getContent)
                    .collect(Collectors.toList())
            );
        }

        adminModifyProjectRequestDto.setState(
            Optional.ofNullable(adminModifyProjectRequestDto.getState())
                .orElse(project.getState().getValue())
        );

        adminModifyProjectRequestDto.setContent(
            Optional.ofNullable(adminModifyProjectRequestDto.getContent())
                .orElseGet(project::getContent)
        );

        adminModifyProjectRequestDto.setThumbnail(
            Optional.ofNullable(adminModifyProjectRequestDto.getThumbnail())
                .orElseGet(() -> project.getThumbnail().getId())
        );

        project.update(
            adminModifyProjectRequestDto.getTitle(),
            adminModifyProjectRequestDto.getStartDate().atStartOfDay(),
            adminModifyProjectRequestDto.getEndDate().atStartOfDay(),
            getProjectTechStackListFromTheContentList(adminModifyProjectRequestDto.getProjectTechStacks()),
            null, //members
            AUTHORIZATION_TYPE.All,
            STATE_TYPE.valueOf(adminModifyProjectRequestDto.getState()),
            adminModifyProjectRequestDto.getContent(),
            getAttachmentById(adminModifyProjectRequestDto.getThumbnail())
        );
    }

    private Attachment getAttachmentById(Long id) {
        return attachmentRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(BadRequestType.ATTACHMENT_NOT_EXIST));
    }

    private void validateAdminModifyProjectRequest(AdminModifyProjectRequestDto adminModifyProjectRequestDto) {
        if ((adminModifyProjectRequestDto.getTitle() == null)
            && (adminModifyProjectRequestDto.getStartDate() == null)
            && (adminModifyProjectRequestDto.getEndDate() == null)
            && (adminModifyProjectRequestDto.getProjectTechStacks().isEmpty())
            && (adminModifyProjectRequestDto.getState() == null)
            && (adminModifyProjectRequestDto.getContent() == null)
            && (adminModifyProjectRequestDto.getThumbnail() == null)) {
            throw new BadRequestException(BadRequestType.NULL_MODIFY_PROJECT_VALUES);
        }
    }

    @Transactional
    public void adminDeleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}
