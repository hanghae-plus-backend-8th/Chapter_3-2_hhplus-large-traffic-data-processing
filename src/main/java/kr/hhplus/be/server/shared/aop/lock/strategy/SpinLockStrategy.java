package kr.hhplus.be.server.shared.aop.lock.strategy;

import kr.hhplus.be.server.shared.aop.lock.LockKeyValueGenerator.KeyValue;
import kr.hhplus.be.server.shared.aop.lock.LockType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static kr.hhplus.be.server.shared.aop.lock.LockType.SPIN_LOCK;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpinLockStrategy implements LockStrategy {

    private final StringRedisTemplate redisTemplate;
    private final String UNLOCK_SCRIPT = """
        if redis.call('get', KEYS[1]) == ARGV[1] then
            return redis.call('del', KEYS[1])
        else
            return 0
        end
    """;

    @Override
    public boolean support(LockType lockType) {
        return SPIN_LOCK.equals(lockType);
    }

    @Override
    public void lock(KeyValue keyValue, long waitTime, long leaseTime) throws InterruptedException {
        LocalDateTime expireTime = LocalDateTime.now()
                .plusSeconds(waitTime);

        while (tryLock(keyValue, leaseTime)) {
            if (timeout(expireTime)) {
                throw new InterruptedException();
            }
            Thread.onSpinWait();
        }
    }

    @Override
    public void unlock(KeyValue keyValue) {
        if (keyValue == null) {
            throw new NullPointerException("NullPointerException ::: keyValue is null");
        }
        redisTemplate.execute(
                new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class),
                Collections.singletonList(keyValue.key()),
                keyValue.value()
        );
    }

    private boolean tryLock(KeyValue keyValue, long leaseTime) {
        Boolean isLocked = redisTemplate.opsForValue()
                .setIfAbsent(keyValue.key(), keyValue.value(), Duration.ofSeconds(leaseTime));

        return isLocked == null || !isLocked;
    }

    private boolean timeout(LocalDateTime expireTime) {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
