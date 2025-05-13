package kr.hhplus.be.server.shared.aop.lock.strategy;

import kr.hhplus.be.server.shared.aop.lock.LockKeyValueGenerator;
import kr.hhplus.be.server.shared.aop.lock.LockKeyValueGenerator.KeyValue;
import kr.hhplus.be.server.shared.aop.lock.LockType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static kr.hhplus.be.server.shared.aop.lock.LockType.PUB_SUB_LOCK;

@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubLockStrategy implements LockStrategy {

    private final RedissonClient redissonClient;
    private final ThreadLocal<RLock> lockHolder = new ThreadLocal<>();

    @Override
    public boolean support(LockType lockType) {
        return PUB_SUB_LOCK.equals(lockType);
    }

    @Override
    public void lock(KeyValue keyValue, long waitTime, long leaseTime) throws InterruptedException {
        RLock lock = redissonClient.getLock(keyValue.key());
        lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

        lockHolder.set(lock);
    }

    @Override
    public void unlock(KeyValue keyValue) {
        RLock rLock = lockHolder.get();

        if (rLock != null) {
            rLock.unlock();
            lockHolder.remove();
        }
    }
}
