package kr.hhplus.be.server.shared.aop.lock;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.UUID;

public abstract class LockKeyValueGenerator {

    private static final String LOCK_PREFIX = "lock:";
    private static final String LOCK_SEPERATOR = ":";

    public static KeyValue generateKeyValue(String[] parameterNames, Object[] args, LockKeyType lockKeyType, String key) {
        return new KeyValue(
                LOCK_PREFIX + lockKeyType.getType() + LOCK_SEPERATOR + parseKey(parameterNames, args, key),
                UUID.randomUUID().toString()
        );
    }

    private static String parseKey(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    @Getter(AccessLevel.PRIVATE)
    public enum LockKeyType {
        COUPON("coupon")
        ;

        private final String type;

        LockKeyType(String type) {
            this.type = type;
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class KeyValue {
        private String key;
        private String value;

        public String key() {
            return key;
        }

        public String value() {
            return value;
        }
    }
}
