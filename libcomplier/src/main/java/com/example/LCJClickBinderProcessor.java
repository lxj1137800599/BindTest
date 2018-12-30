package com.example;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class LCJClickBinderProcessor extends AbstractProcessor {
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String, AnnotatedClick> mAnnotatedClickMap;//String就是bind的类，这个例子是MainActivity

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mAnnotatedClickMap = new TreeMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClickMap.clear();
        try {
            processBindClick(roundEnv);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
        }

        for (AnnotatedClick annotatedClick : mAnnotatedClickMap.values()) {
            try {
                annotatedClick.generateFile().writeTo(mFiler);
            } catch (Exception e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    /**
     * 找到注释了BindView的类并且保存起来
     * @param roundEnv
     * @throws IllegalArgumentException
     */
    private void processBindClick(RoundEnvironment roundEnv) throws IllegalArgumentException {

        for (Element element : roundEnv.getElementsAnnotatedWith(BindClick.class)) {
            AnnotatedClick annotatedClass = getAnnotatedClick(element);
            BindClickField bindViewField = new BindClickField(element);
            annotatedClass.addField(bindViewField);
        }
    }

    private AnnotatedClick getAnnotatedClick(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        AnnotatedClick annotatedClass = mAnnotatedClickMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClick(typeElement, mElementUtils);
            mAnnotatedClickMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindClick.class.getCanonicalName());
        return types;
    }
}