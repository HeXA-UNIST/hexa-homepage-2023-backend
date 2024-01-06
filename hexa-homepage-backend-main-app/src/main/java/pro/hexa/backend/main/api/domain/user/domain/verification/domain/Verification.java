package pro.hexa.backend.main.api.domain.user.domain.verification.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "verification", timeToLive = 60 * 60 * 24)
public class Verification {

    @Id
    private Long id;

    @Indexed
    private String verificationCode;
    
    @Indexed
    private String userId;

    public static Verification create(String verificationCode, String userId) {
        Verification verification = new Verification();
        verification.verificationCode = verificationCode;
        verification.userId = userId;
        return verification;
    }
}
