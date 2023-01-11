package com.example.stock.test.redis;

import com.example.stock.common.AbstractTestContainer;
import com.example.stock.common.annotation.Description;
import com.example.stock.web.redis.RedisSessionCallbackService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("레디스 SessionCallback 통합 테스트")
class RedisSessionCallbackServiceTest extends AbstractTestContainer {

    @Autowired
    private RedisSessionCallbackService redisSessionCallbackService;

    @Test
    @DisplayName("SessionCallback을 사용했을때 트랜잭션이 성공하면 값이 증가된다.")
    @Description(content = "SessionCallback을 사용할 경우 연산 중에도 값을 변경할 수 있다.")
    void SessionCallback_트랜잭션_성공_통합_테스트() {
        redisSessionCallbackService.increaseDAU(key, 1);

        Assertions.assertEquals(1, redisSessionCallback.opsForValue().get(key));
    }

    @Test
    @DisplayName("SessionCallback을 사용했을때 트랜잭션이 성공하면 값이 증가된다.")
    @Description(content = "레디스 명령어들이 Queue에 들어가서 트랜잭션이 성공해야만 값이 반환된다.")
    void SessionCallback_트랜잭션_실패_통합_테스트V1() {
        redisSessionCallbackService.increaseDAU(key, 1);

        Assertions.assertEquals(1, redisSessionCallback.opsForValue().get(key));
    }

    @Test
    @DisplayName("SessionCallback을 사용했을때 트랜잭션이 실패하면 값이 증가되지 않는다.")
    @Description(content = "SessionCallback을 사용했을 때 트랜잭션이 실패하면 Discard 된다.")
    void SessionCallback_트랜잭션_실패_통합_테스트V2() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> redisSessionCallbackService.increaseDAU(key, -1));

        Assertions.assertEquals(0, redisSessionCallback.opsForValue().get(key));
    }

    @Test
    @DisplayName("SessionCallback을 사용했을때 트랜잭션이 실패하면 값이 증가되지 않는다.")
    @Description(content = "SessionCallback을 사용했을 때 트랜잭션이 실패하면 Discard 된다.")
    void SessionCallback_트랜잭션_실패_통합_테스트V3() {
        redisSessionCallbackService.increaseDAUWithWatch(key, -1);
        Assertions.assertEquals(0, redisSessionCallback.opsForValue().get(key));
    }

    @Test
    @DisplayName("SessionCallback을 사용했을때 반환값이 null이 아니면 결과가 실패한다.")
    @Description(content = "레디스 명령어들이 Queue에 들어가서 트랜잭션이 성공해야만 값이 반환된다.")
    void SessionCallback_트랜잭션_실패_통합_테스트4() {
        redisSessionCallbackService.increaseDoubleDAUwithResult(key, 1);

        Assertions.assertEquals(0, redisSessionCallback.opsForValue().get(key));
    }
}
