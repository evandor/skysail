//package jettywebapptest;
//
//import java.util.List;
//
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.osgi.service.component.annotations.Activate;
//import org.osgi.service.component.annotations.Component;
//import org.restlet.Server;
//import org.restlet.engine.Engine;
//import org.restlet.engine.adapter.HttpServerHelper;
//import org.restlet.engine.connector.ConnectorHelper;
//
//@Component
//public class RegisterServlet {
//
//    @Activate
//    public void register() {
//
//        List<ConnectorHelper<Server>> registeredServers = Engine.getInstance().getRegisteredServers();
//        ConnectorHelper<Server> jettyHelper = registeredServers.get(0);
//        HttpServerHelper httpJettyHelper = (HttpServerHelper)jettyHelper;
//
//        System.out.println(httpJettyHelper.getAdapter());
//
//        TestServlet myServlet = new TestServlet();
//
//        ServletHolder holder = new ServletHolder( myServlet );
//        //holder.setInitParameter( Constants.CONF_INIT_STRING_PARAM, configString );
//        //holder.setInitParameter( Constants.CONF_INIT_NAME_PARAM, myName );
//        ServletContextHandler handler = new ServletContextHandler();
//        /*if( details != null ) {
//            details.sch = handler;
//        }*/
//        String contextPath = "apath";//MyConfig.getContextPath( myProps, myName );
//        handler.setContextPath( contextPath );
//        handler.setAllowNullPathInfo( true );
//
//        // bind the servlet to the connectors it should be listening to
//        /*if( isSsl ) {
//            if( isSecureOnly ) {
//                connectors = new String[ 1 ];
//                connectors[0] = Constants.CONF_HTTPS_CONNECTOR;
//            } else {
//                connectors = new String[ 2 ];
//                connectors[0] = Constants.CONF_HTTPS_CONNECTOR;
//                connectors[1] = Constants.CONF_HTTP_CONNECTOR;
//            }
//        } else {
//            if( isSecureOnly ) {
//            throw new ConfigException( 50051 );
//            }*/
//            //connectors = new String[ 1 ];
//            //connectors[0] = Constants.CONF_HTTP_CONNECTOR;
//        //}
////        handler.setConnectorNames( connectors );
////
////        if( myName != null ) {
////            handler.setDisplayName( MyMessage.message( 10025, myName ) );
////        } else {
////            handler.setDisplayName( MyMessage.message( 10001 ) );
////        }
////        handler.addServlet( holder, "/*" );
////        contextHandlers.addHandler( handler );
////
////        contextHandlers.mapContexts();
////        Exception initEx = holder.getUnavailableException();
////        if( initEx != null ) {
////            // deal with error
////        }
////
//
//
//    }
//
//}
