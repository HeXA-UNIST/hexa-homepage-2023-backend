package pro.hexa.backend.domain.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.major.domain.Major;
import pro.hexa.backend.domain.model.model.AbstractEntity;
import pro.hexa.backend.domain.sns.domain.SNS;
import pro.hexa.backend.domain.tech_stack.domain.TechStack;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.POSITION_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractEntity {

    @Id
    @Column(name = "user_id")
    private String id;

    @Comment("비밀번호")
    @Column(length = 127)
    private String password;

    @Comment("이름")
    @Column(length = 8)
    private String name;

    @Comment("프사")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Attachment profileImage;

    @Comment("이메일")
    @Column(length = 320, unique = true)
    private String email;

    @Comment("성별")
    @Enumerated(EnumType.STRING)
    @Column(length = 4)
    private GENDER_TYPE gender;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    @Column(length = 2)
    private STATE_TYPE state;

    @Comment("학번")
    @Column(length = 8)
    private String regNum;

    @Comment("입학년도")
    @Column
    private Short regYear;

    @Comment("기수")
    @Column
    private Short generation;

    @Comment("전화번호")
    @Column(length = 13)
    private String contactNumber;

    @Comment("소속 학과")
    @ManyToOne(fetch = FetchType.LAZY)
    private Major mainMajor;

    @Comment("직책")
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private POSITION_TYPE position;

    @Comment("권한")
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private AUTHORIZATION_TYPE authorization;

    @Comment("기술 스택 리스트")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TechStack> techStacks = new ArrayList<>();

    @Comment("깃헙주소")
    @Column(length = 127)
    private String githubLink;

    @Comment("sns 주소 리스트")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SNS> snsList = new ArrayList<>();

//    @Comment("참여 프로젝트 - 프로젝트 멤버 리스트")
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<ProjectMember> projectMembers = new ArrayList<>();
}
