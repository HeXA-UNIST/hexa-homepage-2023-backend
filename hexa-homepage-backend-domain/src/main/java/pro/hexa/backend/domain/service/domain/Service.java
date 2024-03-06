package pro.hexa.backend.domain.service.domain;

import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import pro.hexa.backend.domain.abstract_activity.domain.AbstractActivity;
import pro.hexa.backend.domain.attachment.domain.Attachment;

@Entity(name = "service")
@Getter
public class Service extends AbstractActivity {
    @Comment("썸네일")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Attachment thumbnail;

    @Comment("사이트 주소")
    @Column(length = 127)
    private String siteLink;

    @Comment("깃헙주소")
    @Column(length = 127)
    private String githubLink;

    public static Service create(Attachment attachment, String siteLink, String githubLink,
        String title, String content) {
        Service service = new Service();
        service.thumbnail = attachment;
        service.siteLink = siteLink;
        service.githubLink = githubLink;
        service.title = title;
        service.content = content;

        return service;
    }
    public void update(Attachment attachment, String siteLink, String githubLink,
        String title, String content){
        Optional.ofNullable(attachment).ifPresent((att) -> this.thumbnail = att);
        Optional.ofNullable(siteLink).ifPresent((sl) -> this.siteLink = sl);
        Optional.ofNullable(githubLink).ifPresent((gl) -> this.githubLink = gl);
        Optional.ofNullable(title).ifPresent((t) -> this.title = t);
        Optional.ofNullable(content).ifPresent((cont) -> this.content = cont);
    }
}
