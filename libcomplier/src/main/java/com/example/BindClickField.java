package com.example;

import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

public class BindClickField {
    private Symbol.MethodSymbol mVariableElement;
    private int mResId;
    private ListenerClass mListenerClass;

    BindClickField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(String.format("Only methods can be annotated with @%s",
                    BindClick.class.getSimpleName()));
        }
        mVariableElement = (Symbol.MethodSymbol) element;

        BindClick bindClick = mVariableElement.getAnnotation(BindClick.class);
        mResId = bindClick.value();
        mListenerClass = bindClick.annotationType().getAnnotation(ListenerClass.class);
        if (mResId < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", BindClick.class.getSimpleName(),
                            mVariableElement.getSimpleName()));
        }
    }

    Name getFieldName() {
        return mVariableElement.getSimpleName();
    }
    /**
     * 获取变量ListenerClass
     *
     * @return
     */
    ListenerClass getListenerClass() {
        return mListenerClass;
    }

    /**
     * 标记了BindClick的id值
     */
    int getResId() {
        return mResId;
    }

    /**
     * 获取变量类型
     *
     * @return
     */
    TypeMirror getFieldType() {
        return mVariableElement.asType();
    }
}
