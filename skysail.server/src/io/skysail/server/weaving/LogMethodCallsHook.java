//package io.skysail.server.weaving;
//
//import org.osgi.framework.hooks.weaving.WeavingHook;
//import org.osgi.framework.hooks.weaving.WovenClass;
//import org.osgi.service.component.annotations.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
////@Component(immediate = true)
//public class LogMethodCallsHook implements WeavingHook {
//
//        
//    @Override
//    public void weave(WovenClass wovenClass) {
//        if(isInstrumented(wovenClass.getClassName())) {
////            log.info("Instrumenting {}", wovenClass.getClassName());
////            final ClassReader cr = new ClassReader(wovenClass.getBytes());
////            final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
////            final ClassVisitor v = new LMCClassVisitor(cw);
////            cr.accept(v, 0);
////            wovenClass.setBytes(cw.toByteArray());
//        }
//    }
//    
//    private boolean isInstrumented(String className) {
//    	if (className.startsWith("io.skysail.server.weaving")) {
//    		return false;
//    	}
//        return className.contains(".skysail.");
//}
//
//}
