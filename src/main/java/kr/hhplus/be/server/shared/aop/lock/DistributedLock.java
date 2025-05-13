package kr.hhplus.be.server.shared.aop.lock;

import kr.hhplus.be.server.shared.aop.lock.LockKeyValueGenerator.LockKeyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static kr.hhplus.be.server.shared.aop.lock.LockType.SPIN_LOCK;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    LockKeyType keyType();
    String key();
    long waitTime() default 5L;
    long leaseTime() default 3L;
    LockType lockType() default SPIN_LOCK;
}
