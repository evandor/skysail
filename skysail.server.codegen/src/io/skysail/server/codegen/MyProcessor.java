package io.skysail.server.codegen;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.skysail.server.entities.GenerateResources"})
public class MyProcessor extends AbstractProcessor {

	@Override
	public synchronized void init(ProcessingEnvironment env){
		System.out.println("yeah");
	}

	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) { 
		System.out.println("yeah!!!");
		return true;
	}
}