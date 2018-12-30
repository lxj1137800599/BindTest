package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AnnotatedClick {
    private static class TypeUtil {
        static final ClassName BINDER = ClassName.get("com.company.libapi", "ClickBinder");
        //static final ClassName PROVIDER = ClassName.get("com.company.libapi", "ClickFinder");
    }

    private TypeElement mTypeElement;
    private ArrayList<BindClickField> mFields;
    private Elements mElements;
    private static final ClassName VIEW = ClassName.get("android.view", "View");

    AnnotatedClick(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mFields = new ArrayList<>();
    }

    void addField(BindClickField field) {
        mFields.add(field);
    }

    /**
     *
     * @return
     */
    JavaFile generateFile() throws Exception{
        //generateMethod
        MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("bindClick")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source");
                //.addParameter(AnnotatedClick.TypeUtil.PROVIDER, "finder");

        for (BindClickField field : mFields) {
            ListenerClass listenerClass = field.getListenerClass();
            ListenerMethod method = listenerClass.annotationType().getAnnotation(ListenerMethod.class);
            String name = field.getFieldName().toString();
            //匿名内部类
            TypeSpec onCLick = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ParameterizedTypeName.get(Class.forName(listenerClass.type())))
                    .addMethod(MethodSpec.methodBuilder(method.name())
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(VIEW, "view")
                            .returns(void.class)
                            .addStatement("host.$L();", name)
                            .build())
                    .build();
            bindViewMethod.addStatement("host.findViewById($L).setOnClickListener($L)", field.getResId(), onCLick);

//            bindViewMethod.addStatement("host.findViewById($L).setOnClickListener(new View.OnClickListener() { public void onClick($T v) { host.$L(); }})",
//                    field.getResId(), VIEW, name);

//            TypeSpec onCLick = TypeSpec.anonymousClassBuilder("")
//                    .addSuperinterface(ParameterizedTypeName.get(Student.class))
//                    .addMethod(MethodSpec.methodBuilder("run")
//                            .addAnnotation(Override.class)
//                            .addModifiers(Modifier.PUBLIC)
//                            .returns(void.class)
//                            .addStatement("int a = 5")
//                            .build()).build();
//            bindViewMethod.addStatement("new Thread($L).start()", onCLick);
        }

        MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindClick")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addAnnotation(Override.class);
        for (BindClickField field : mFields) {
            unBindViewMethod.addStatement("host.findViewById($L).setOnClickListener(null)", field.getResId());
        }

        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ClickBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(AnnotatedClick.TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))
                .addMethod(bindViewMethod.build())
                .addMethod(unBindViewMethod.build())
                .build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, injectClass).build();
    }
}
