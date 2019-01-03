package net.dunotech.venus.system.service.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableWriteLog {

    String remark() default "";

    String operationType() default "4";
}
