package com.example.stock.test.redis;

import com.example.stock.common.AbstractTestContainer;
import com.example.stock.common.annotation.Description;
import com.example.stock.web.redis.RedisTransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("레디스 트랜잭션 통합 테스트")
class RedisTransactionServiceTest extends AbstractTestContainer {

    @Autowired
    private RedisTransactionService redisTransactionService;

    @Test
    @DisplayName("DAU를 증가시키는 로직을 수행 중 트랜잭션이 실패하면 DAU 값이 증가하지 않는다.")
    @Description(content = "레디스 명령어들이 Queue에 들어가서 트랜잭션이 성공해야만 값이 반환된다.")
    void 트랜잭션_지원시_트랜잭션_실패_통합_테스트() {
        // when
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> redisTransactionService.increaseDAU(key, -1));

        // then
        Assertions.assertEquals(0, redisTransactionRedisTemplate.opsForValue().get(key));
    }

    @Test
    @DisplayName("레디스의 트랜잭션에서는 Propagation.REQUIRES_NEW를 사용해도 별도의 트랜잭션이 생성되지 않는다.")
    @Description(content = "하나의 트랜잭션이 끝나기 전까지 새로운 트랜잭션이 커밋되더라도 반영되지 않는다.")
    void 트랜잭션_지원시_트랜잭션_전파_통합_테스트() {
        // when
        redisTransactionService.increaseDAUwithPropagation(key, 1);

        // then
        Assertions.assertEquals(0, redisTransactionRedisTemplate.opsForValue().get(key));
    }
}
