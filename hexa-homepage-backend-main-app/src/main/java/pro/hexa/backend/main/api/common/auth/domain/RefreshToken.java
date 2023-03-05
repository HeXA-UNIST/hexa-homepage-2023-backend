package pro.hexa.backend.main.api.common.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24)
public class RefreshToken {

    @Id
    private Long id;

    @Indexed
    private String accessToken;

    @Indexed
    private String refreshToken;

    public static RefreshToken create(String accessToken, String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.accessToken = accessToken;
        refreshTokenEntity.refreshToken = refreshToken;
        return refreshTokenEntity;
    }
}
