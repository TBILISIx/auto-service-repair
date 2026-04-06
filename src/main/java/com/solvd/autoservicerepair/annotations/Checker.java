package com.solvd.autoservicerepair.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // runtime makes it visible during runtime when I have IntelliJ opened
@Target(ElementType.METHOD)

public @interface Checker {

    String description() default "This annotation is used on methods where some condition is checked, this is default description";

}
