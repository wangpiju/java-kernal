package com.hs3.web.auth;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
    boolean validate() default false;
}
