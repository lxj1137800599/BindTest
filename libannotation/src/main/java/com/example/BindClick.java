package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@ListenerClass(
        targetType = "android.view.View",
        setter = "setOnClickListener",
        type = "com.company.libapi.util.DebouncingOnClickListener",
        method = @ListenerMethod(
                name = "doClick",
                parameters = "android.view.View"
        )
)
public @interface BindClick {
    int value();
}
