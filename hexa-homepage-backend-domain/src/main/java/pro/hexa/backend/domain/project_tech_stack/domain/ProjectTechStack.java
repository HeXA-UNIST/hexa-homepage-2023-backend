package pro.hexa.backend.domain.project_tech_stack.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import pro.hexa.backend.domain.model.model.AbstractEntity;
import pro.hexa.backend.domain.project.domain.Project;

@Entity(name = "project_tech_stack")
@Getter
public class ProjectTechStack extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_tech_stack_id")
    private Long id;

    @Comment("내용")
    @Column(length = 127)
    private String content;

    @Comment("프로젝트")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public static ProjectTechStack create(
        String content
    ) {
        ProjectTechStack projectTechStack = new ProjectTechStack();
        projectTechStack.content = content;
        return projectTechStack;
    }

    public void setProject(Project project) {
        this.project = project;

        if (project != null && !project.getProjectTechStacks().contains(this)) {
            project.addProjectTechStack(this);
        }
    }
}

