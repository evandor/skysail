//package io.skysail.server.weaving;
//
//import java.io.Serializable;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.LongAdder;
//
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceReference;
//
//public class LoggerProxy implements InvocationHandler, Serializable {
//
//	private static final long serialVersionUID = 5708100421373684743L;
//	
//	private static final ConcurrentHashMap<String, LongAdder> invocount = new ConcurrentHashMap<>();
//
//	private ServiceReference serviceReference;
//    private BundleContext bundleContext;
//
//	private Class<?> cls;
//
//    public LoggerProxy(BundleContext bundleContext,
//            ServiceReference serviceReference) {
//        this.serviceReference = serviceReference;
//        this.bundleContext = bundleContext;
//    }
//    
//    public LoggerProxy(Class<?> cls) {
//		this.cls = cls;
//	}
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//    	if (this.cls != null) {
//    		System.out.println("Hier!");
//    	}
//    	
//    	invocount.computeIfAbsent(method.getDeclaringClass().getCanonicalName() + ":" + method.getName(), k -> new LongAdder()).increment();
//    	
//        System.out.println("-->Methode : [" + method.getName() + "] ");
////        System.out.println("-->Parameters : ");
////        for (Object object : args) {
////            System.out.print("->"+object + " : ");
////        }
////        System.out.println("");
//        Object invoke = method.invoke(bundleContext.getService(serviceReference),
//                args);
//
//        //System.out.println("-->Return : " + invoke);
//        System.out.println("*** " + invocount);
//        return invoke;
//    }
//}
