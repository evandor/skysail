package io.skysail.converter

import org.restlet.representation.Variant
import io.skysail.api.responses.SkysailResponse
import io.skysail.core.resources.SkysailServerResource
import org.restlet.representation.StringRepresentation
import org.restlet.resource.Resource

class StringTemplateRenderer(htmlConverter: ScalaHtmlConverter , resource: Resource ) {
   def createRepresenation(entity: SkysailResponse[_] ,  target: Variant,resource:  SkysailServerResource[_] ):StringRepresentation = {

//        styling = Styling.determineForm(resource); // e.g. bootstrap,
//                                                   // semanticui, jquerymobile
//        theme = Theme.determineFrom(resource, target);
//
//        STGroupBundleDir.clearUsedTemplates();
//        STGroup stGroup = createStringTemplateGroup(resource, styling, theme);
//        ST index = getStringTemplateIndex(resource, styling, stGroup);
//
//        ResourceModel<SkysailServerResource<?>, ?> resourceModel = createResourceModel(entity, target, resource);
//
//        addSubstitutions(resourceModel, index);
//
//        checkForInspection(resource, index);

        return new StringRepresentation("hi");//createRepresentation(index, stGroup);
    }
}