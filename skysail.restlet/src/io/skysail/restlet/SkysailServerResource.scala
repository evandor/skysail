//package io.skysail.restlet
//
//import org.restlet.resource.ServerResource
//import io.skysail.core.app.SkysailApplication
//import io.skysail.core.utils.ReflectionUtils
//
//abstract class SkysailServerResource extends ServerResource {
//
//  //   DateTimeConverter dateConverter = new DateConverter(null);
//  //        dateConverter.setPattern("yyyy-MM-dd");
//  //        dateConverter.setUseLocaleFormat(true);
//  //        ConvertUtils.deregister(Date.class);
//  //        ConvertUtils.register(dateConverter, Date.class);
//
//  //        defaultMediaTypes.add("xml");
//  //        defaultMediaTypes.add("json");
//  //        defaultMediaTypes.add("x-yaml");
//  //        defaultMediaTypes.add("csv");
//  //        defaultMediaTypes.add("mailto");
//
//  //val app = getApplication();
//
//  def getMetricsCollector() = getApplication().asInstanceOf[SkysailApplication].getMetricsCollector()
//
//  def getParameterizedType() = ReflectionUtils.getParameterizedType(getClass());
//}