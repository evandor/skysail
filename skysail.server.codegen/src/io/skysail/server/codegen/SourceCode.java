package io.skysail.server.codegen;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class SourceCode extends SimpleJavaFileObject {

    private String contents = null;

    public SourceCode(String className, String contents) {
        super(URI.create(calcUri(className)), Kind.SOURCE);
        this.contents = contents;
    }

    private static String calcUri(String className) {
        return "string:///" + className.replace('.', '/') + Kind.SOURCE.extension;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return contents;
    }
}