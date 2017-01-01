package io.skysail.server.codegen.model;

import javax.lang.model.element.Element;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.codegen.apt.processors.EntityProcessor;

public class JavaEntity extends EntityModel implements JavaModel {

    private String elementName;

    private Element element;

    private String applicationName;

    private String applicationPackage;

    public JavaEntity(JavaApplication application, Element element) {
        super(element.toString());
        this.element = element;
        this.elementName = element.toString();
        this.applicationName = application.getName();
        this.applicationPackage = application.getPackageName();
    }

    public GenerateResources getGenerateResourcesAnnotation() {
        return element.getAnnotation(GenerateResources.class);
    }

    public String getClassAnnotations() {
        return EntityProcessor.GENERATED_ANNOTATION;
    }

    public String getElementName() {
        return elementName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationPackage() {
        return applicationPackage;
    }


//    public String getLinkedResources() {
//        return getPostResourceClassName() + ".class";
//    }

}
