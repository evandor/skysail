package io.skysail.server.codegen.apt.processors;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.STGroup;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.codegen.ResourceType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.codegen.apt.stringtemplate.MySTGroupFile;
import io.skysail.server.codegen.model.JavaApplication;
import io.skysail.server.codegen.model.JavaEntity;

@SupportedAnnotationTypes("io.skysail.server.codegen.annotations.GenerateResources")
@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
public class EntityProcessor extends Processors {

    public static final String GENERATED_ANNOTATION = "@Generated(\"" + EntityProcessor.class.getName() + "\")";

    @Override
    public boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        Set<? extends Element> generateResourceElements = roundEnv.getElementsAnnotatedWith(GenerateResources.class);
        String applicationName = getOneAndOnlyApplicationName(generateResourceElements);
        JavaApplication application = new JavaApplication(applicationName);
        analyse(application, roundEnv, generateResourceElements);
        application.getEntityIds().stream()
                .map(application::getEntity)
                .forEach(entity -> {
                    try {
                        //createRepository((JavaEntity) entity);
                        createEntityResource(roundEnv, (JavaEntity) entity);
                        createListResource(roundEnv, (JavaEntity) entity);
                        createPostResource(roundEnv, (JavaEntity) entity);
                        createPutResource(roundEnv, (JavaEntity) entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        return true;
    }

    private String getOneAndOnlyApplicationName(Set<? extends Element> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalStateException("no elements found for code generation");
        }
        Set<String> result = new HashSet<>();
        elements.stream().forEach(element -> {
            result.add(element.getAnnotation(GenerateResources.class).application());
        });
        if (result.size() != 1) {
            throw new IllegalStateException("two many (" + result.size() + ") applications defined");
        }
        return result.iterator().next();
    }

    private void analyse(JavaApplication application, RoundEnvironment roundEnv,
            Set<? extends Element> generateResourceElements) {
        printHeadline("Analysing project for code generation: first iteration");
        for (Element entityElement : generateResourceElements) {
            printMessage("adding entity: " + entityElement.toString());
            application.addOnce(new JavaEntity(application, entityElement));
        }
        printHeadline("Analysing project for code generation: second interation");
        for (EntityModel<?> entity : application.getEntityValues()) {
            JavaEntity javaEntity = (JavaEntity)entity;
            javaEntity.determineLinks(entity, application.getEntityValues());
        }

        printMessage("");
    }

    private void createRepository(JavaEntity entity) throws IOException {
        String repositoryName = entity.getPackageName() + ".repositories." + entity.getName() + "Repository";
        String templateFile = "repository/Repository.stg";
        create(entity, repositoryName, templateFile);
    }

    private void createEntityResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        if (methodIncluded(entity.getGenerateResourcesAnnotation(), ResourceType.GET)) {
            String resourceName = entity.getId() + "Resource";
            String templateName = "entityResource/EntityResource.stg";
            create(entity, resourceName, templateName);
        }
    }

    private void createListResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        if (methodIncluded(entity.getGenerateResourcesAnnotation(), ResourceType.LIST)) {
            create(entity, entity.getId() + "sResource", "listResource/ListResource.stg");
        }
    }

    private void createPostResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        if (methodIncluded(entity.getGenerateResourcesAnnotation(), ResourceType.POST)) {
            String resourceName = entity.getPackageName() + ".Post" + entity.getSimpleName() + "Resource";
            String templateName = "postResource/PostResource2.stg";
            create(entity, resourceName, templateName);
        }
    }

    private void createPutResource(RoundEnvironment roundEnv, JavaEntity entity) throws Exception {
        if (methodIncluded(entity.getGenerateResourcesAnnotation(), ResourceType.PUT)) {
            String resourceName = entity.getPackageName() + ".Put" + entity.getSimpleName() + "Resource";
            String templateName = "putResource/PutResource.stg";
            create(entity, resourceName, templateName);
        }
    }

    private void printHeadline(String msg) {
        printMessage("");
        printMessage(msg);
        printMessage(StringUtils.repeat("-", msg.length()));
        printMessage("");
    }

    private void create(JavaEntity entity, String resourceName, String templateName) throws IOException {
        JavaFileObject jfo = createSourceFile(resourceName);
        Writer writer = jfo.openWriter();
        STGroup group = new MySTGroupFile(templateName, '$', '$');
        writer.append(group.getInstanceOf("template").add("entity", entity).render());
        writer.close();
    }

    private boolean methodIncluded(GenerateResources annotation, ResourceType resourceType) {
        return !Arrays.asList(annotation.exclude()).contains(resourceType);
    }

}
