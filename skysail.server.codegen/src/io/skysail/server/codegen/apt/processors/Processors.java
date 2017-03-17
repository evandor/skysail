package io.skysail.server.codegen.apt.processors;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

public abstract class Processors extends AbstractProcessor {

	public abstract boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	        throws Exception;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (annotations.isEmpty()) {
			return true;
		}

		try {
			return doProcess(annotations, roundEnv);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	protected Set<? extends Element> getElements(RoundEnvironment roundEnv, Class<? extends Annotation> gpr) {
		return roundEnv.getElementsAnnotatedWith(gpr);
	}

	protected void printMessage(String msg) {
		processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.NOTE, msg);
	}

	protected String getTypeName(Element e) {
		return e.getEnclosingElement().toString() + "." + e.getSimpleName();
	}

	protected JavaFileObject createSourceFile(String name) throws IOException {
	    Filer filer = processingEnv.getFiler();
	    Map<String, String> options = processingEnv.getOptions();
	    printMessage("env: " + processingEnv.getClass().getName());
	    printMessage("options: " + options);
		JavaFileObject createdSourceFile = processingEnv.getFiler().createSourceFile(name);
		printMessage("Created source file: " + createdSourceFile.toUri() + " for name " + name);
		return createdSourceFile;
	}

}
