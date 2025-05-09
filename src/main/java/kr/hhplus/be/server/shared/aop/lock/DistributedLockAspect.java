package kr.hhplus.be.server.shared.aop.lock;

import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final String LOCK_PREFIX = "lock:";
    private final String UNLOCK_SCRIPT = """
        if redis.call('get', KEYS[1]) == ARGV[1] then
            return redis.call('del', KEYS[1])
        else
            return 0
        end
    """;
    private final StringRedisTemplate redisTemplate;

    @Around(value = "@annotation(kr.hhplus.be.server.shared.aop.lock.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = LOCK_PREFIX + parseKey(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        String value = UUID.randomUUID().toString();
        LocalTime waitTime = LocalTime.now()
                .plusSeconds(distributedLock.waitTime());

        try {
            tryLock(key, value, waitTime, distributedLock.leaseTime());

            return joinPoint.proceed();
        } finally {
            unlock(key, value);
        }
    }

    private Object parseKey(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, Object.class);
    }

    private void tryLock(String key, String value, LocalTime waitTime, long leaseTime) throws InterruptedException {
        while (true) {
            Boolean isLocked = redisTemplate.opsForValue()
                    .setIfAbsent(key, value, Duration.ofSeconds(leaseTime));

            // Lock 획득 시, 탈출
            if (isLocked != null && isLocked) {
                break;
            }

            // Lock Timeout 초과 시, 예외
            if (LocalTime.now().isAfter(waitTime)) {
                throw new IllegalMonitorStateException("Lock 획득 Timeout 초과");
            }
            Thread.sleep(50);
        }
    }

    private void unlock(String key, String value) {
        redisTemplate.execute((RedisCallback<Object>) connection ->
                connection.eval(
                        UNLOCK_SCRIPT.getBytes(StandardCharsets.UTF_8),
                        ReturnType.INTEGER,
                        1,
                        key.getBytes(StandardCharsets.UTF_8),
                        value.getBytes(StandardCharsets.UTF_8)
                )
        );
    }
}
