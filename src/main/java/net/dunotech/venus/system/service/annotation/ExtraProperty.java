package net.dunotech.venus.system.service.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtraProperty {
    String value() default "";
}
