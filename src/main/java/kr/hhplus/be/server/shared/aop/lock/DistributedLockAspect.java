package kr.hhplus.be.server.shared.aop.lock;

import kr.hhplus.be.server.shared.aop.lock.LockKeyValueGenerator.KeyValue;
import kr.hhplus.be.server.shared.aop.lock.strategy.LockStrategy;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
public class DistributedLockAspect {

    private final Map<LockType, LockStrategy> lockStrategyMap;

    public DistributedLockAspect(List<LockStrategy> lockStrategyList) {
        this.lockStrategyMap = new HashMap<>();
        for (LockType lockType : LockType.values()) {
            LockStrategy lockStrategy = lockStrategyList.stream()
                    .filter(strategy -> strategy.support(lockType))
                    .findFirst()
                    .orElseThrow();

            lockStrategyMap.put(lockType, lockStrategy);
        }
    }

    @Around(value = "@annotation(kr.hhplus.be.server.shared.aop.lock.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        KeyValue keyValue = LockKeyValueGenerator.generateKeyValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                distributedLock.keyType(),
                distributedLock.key()
        );
        LockStrategy lockStrategy = lockStrategyMap.get(distributedLock.lockType());
        boolean isLocked = false;

        try {
            lockStrategy.lock(keyValue, distributedLock.waitTime(), distributedLock.leaseTime());
            isLocked = true;

            return joinPoint.proceed();
        } catch (InterruptedException ex) {
            log.error("InterruptedException ::: Lock Failed");
            throw ex;
        } finally {
            if (isLocked) {
                lockStrategy.unlock(keyValue);
            }
        }
    }
}
