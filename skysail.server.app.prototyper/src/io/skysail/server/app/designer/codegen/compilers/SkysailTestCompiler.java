package io.skysail.server.app.designer.codegen.compilers;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.JavaCompiler;
import io.skysail.server.app.designer.codegen.STGroupBundleDir;
import io.skysail.server.app.designer.codegen.SkysailCompiler;
import io.skysail.server.app.designer.codegen.templates.TemplateProvider;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.RouteModel;

public class SkysailTestCompiler extends SkysailCompiler {

    private String applicationClassName;
    private TemplateProvider templateProvider;

    public SkysailTestCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup,
            Bundle bundle, JavaCompiler compiler, TemplateProvider templateProvider) {
        super(applicationModel, stGroup, compiler);
        this.templateProvider = templateProvider;
    }

    public List<CompiledCode> createTests(List<RouteModel> routeModels) {
        ST template = templateProvider.templateFor("abstractAppResourceTest");
        List<CompiledCode> compiledCode = setupTestsForCompilation(template, applicationModel, routeModels);
        return compiledCode;
    }

    private List<CompiledCode> setupTestsForCompilation(ST template, DesignerApplicationModel applicationModel,
            List<RouteModel> routeModels) {

        List<CompiledCode> result = new ArrayList<>();
        applicationClassName = applicationModel.getPackageName() + ".Abstract" + applicationModel.getName() + "ResourceTest";

        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
                + "/test/" + classNameToPath(applicationClassName);
        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");


        template.add("application", applicationModel);
        String entityCode = template.render();

        result.add(collectSource(applicationClassName/** + "Gen"*/, entityCode, "test-gen"));

        return result;
    }
}
