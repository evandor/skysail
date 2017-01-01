//package io.skysail.server.app.ref.fields.test;
//
//import java.io.File;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Set;
//
//import javax.tools.JavaCompiler;
//import javax.tools.JavaCompiler.CompilationTask;
//import javax.tools.JavaFileManager;
//import javax.tools.JavaFileObject;
//import javax.tools.JavaFileObject.Kind;
//import javax.tools.StandardJavaFileManager;
//import javax.tools.StandardLocation;
//import javax.tools.ToolProvider;
//
//import org.junit.Test;
//
//import io.skysail.server.codegen.apt.processors.EntityProcessor;
//
//
//public class FieldsDemoApplicationTest {
//
//    @Test
//    public void runAnnoationProcessor() throws Exception {
//        String source = "/Users/carsten/git/skysail/skysail.server.app.ref.fields/src";
//
//        Iterable<JavaFileObject> files = getSourceFiles(source);
//
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//
//        JavaFileManager fileManager = ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null);
//        CompilationTask task = compiler.getTask(new PrintWriter(System.out), fileManager, null, null, null, files);
//        task.setProcessors(Arrays.asList(new EntityProcessor()));
//
//        task.call();
//    }
//
//    private Iterable<JavaFileObject> getSourceFiles(String p_path) throws Exception {
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);
//
//        files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(p_path)));
//
//        Set<Kind> fileKinds = Collections.singleton(Kind.SOURCE);
//        return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
//    }
//
//}
