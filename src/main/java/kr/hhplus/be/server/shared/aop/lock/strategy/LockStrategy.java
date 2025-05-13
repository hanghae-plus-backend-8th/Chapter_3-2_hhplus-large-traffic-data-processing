package kr.hhplus.be.server.shared.aop.lock.strategy;

import jakarta.annotation.Nullable;
import kr.hhplus.be.server.shared.aop.lock.LockKeyValueGenerator.KeyValue;
import kr.hhplus.be.server.shared.aop.lock.LockType;

public interface LockStrategy {

    boolean support(LockType lockType);
    void lock(KeyValue keyValue, long waitTime, long leaseTime) throws InterruptedException;
    void unlock(@Nullable KeyValue keyValue);
}
