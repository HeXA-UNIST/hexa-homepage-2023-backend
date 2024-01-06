package pro.hexa.backend.domain.project_member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import pro.hexa.backend.domain.model.model.AbstractEntity;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.domain.User;

@Entity(name = "project_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @Comment("프로젝트")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Comment("참여 맴버")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Comment("권한")
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private AUTHORIZATION_TYPE authorization;

    public static ProjectMember create(
        User user,
        AUTHORIZATION_TYPE authorization

    ) {
        ProjectMember projectmember = new ProjectMember();
        projectmember.user = user;
        projectmember.authorization = authorization;
        return projectmember;
    }

    public void setProject(Project project) {
        this.project = project;

        if (project != null && !project.getMembers().contains(this)) {
            project.addMember(this);
        }
    }
}
