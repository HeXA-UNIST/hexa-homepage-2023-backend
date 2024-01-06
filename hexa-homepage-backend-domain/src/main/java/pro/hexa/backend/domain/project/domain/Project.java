package pro.hexa.backend.domain.project.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.util.CollectionUtils;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.model.model.AbstractEntity;
import pro.hexa.backend.domain.project.model.STATE_TYPE;
import pro.hexa.backend.domain.project_member.domain.ProjectMember;
import pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;

@Entity(name = "project")
@Getter
public class Project extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Comment(value = "제목")
    @Column(length = 300)
    private String title;

    @Comment(value = "시작일")
    @Column
    private LocalDateTime startDate;

    @Comment(value = "종료일")
    @Column
    private LocalDateTime endDate;

    @Comment(value = "기술스택")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ProjectTechStack> projectTechStacks = new HashSet<>();

    @Comment(value = "멤버")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ProjectMember> members = new HashSet<>();

    @Comment(value = "노출")
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private AUTHORIZATION_TYPE authorization;

    @Comment(value = "상태")
    @Enumerated(EnumType.STRING)
    @Column(length = 4)
    private STATE_TYPE state;

    @Comment(value = "내용")
    @Column(length = 3000)
    private String content;

    @Comment("썸네일")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Attachment thumbnail;

    public static Project create(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<ProjectTechStack> projectTechStacks,
        List<ProjectMember> members,
        AUTHORIZATION_TYPE authorization,
        STATE_TYPE state,
        String content,
        Attachment thumbnail
    ) {
        Project project = new Project();
        project.title = title;
        project.startDate = startDate;
        project.endDate = endDate;
        project.addProjectTechStacksAll(projectTechStacks);
        project.addMembersAll(members);
        project.authorization = authorization;
        project.state = state;
        project.content = content;
        project.thumbnail = thumbnail;
        return project;
    }

    public static Project createForTest(
        Long id,
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<ProjectTechStack> projectTechStacks,
        List<ProjectMember> members,
        AUTHORIZATION_TYPE authorization,
        STATE_TYPE state,
        String content,
        Attachment thumbnail
    ) {
        Project project = new Project();
        project.id = id;
        project.title = title;
        project.startDate = startDate;
        project.endDate = endDate;
        project.addProjectTechStacksAll(projectTechStacks);
        project.addMembersAll(members);
        project.authorization = authorization;
        project.state = state;
        project.content = content;
        project.thumbnail = thumbnail;
        return project;
    }

    public void update(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<ProjectTechStack> projectTechStacks,
        List<ProjectMember> members,
        AUTHORIZATION_TYPE authorization,
        STATE_TYPE state,
        String content,
        Attachment thumbnail
    ) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        addProjectTechStacksAll(projectTechStacks);
        addMembersAll(members);
        this.authorization = authorization;
        this.state = state;
        this.content = content;
        this.thumbnail = thumbnail;

    }

    public void addProjectTechStack(ProjectTechStack projectTechStack) {
        if (projectTechStack == null) {
            return;
        }

        projectTechStacks.add(projectTechStack);

        if (projectTechStack.getProject() != this) {
            projectTechStack.setProject(this);
        }
    }

    public void addProjectTechStacksAll(List<ProjectTechStack> projectTechStacks) {
        if (CollectionUtils.isEmpty(projectTechStacks)) {
            return;
        }

        for (ProjectTechStack projectTechStack : projectTechStacks) {
            addProjectTechStack(projectTechStack);
        }
    }

    public void addMember(ProjectMember member) {
        if (member == null) {
            return;
        }

        members.add(member);

        if (member.getProject() != this) {
            member.setProject(this);
        }
    }

    public void addMembersAll(List<ProjectMember> members) {
        if (CollectionUtils.isEmpty(members)) {
            return;
        }

        for (ProjectMember member : members) {
            addMember(member);
        }
    }
}
