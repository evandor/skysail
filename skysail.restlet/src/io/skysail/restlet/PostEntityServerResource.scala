//package io.skysail.restlet
//
//import org.restlet.resource.Post
//import org.restlet.representation.Variant
//import org.restlet.data.Form
//import io.skysail.api.metrics.TimerMetric
//import java.util.LinkedHashMap
//
//import org.json4s._
//import org.json4s.jackson.JsonMethods._
////import org.json4s.JsonAST._
//
//
//import scala.collection.JavaConversions._
//import scala.collection.mutable.MutableList
//
//abstract class PostEntityServerResource[Note] extends SkysailServerResource {
//
//  implicit val formats = DefaultFormats 
//
//  class FormDeserializer[T](cls: Class[_]) {
//    def createFrom(form: Form): Unit= {
//      //      if (form == null) {
//      //            return null;
//      //        }
//
//      val elements =  MutableList[JField]()
//      form.getNames().foreach(key => elements += JField(key, JString("a")))
//
//      //        for (key:String <- form.getNames()) {
//      //            if (!"submit".equals(key)) {
//      //            val theKey = key;
//      //            if (key.startsWith(parameterizedTypeName)) {
//      //                theKey = key.substring(parameterizedTypeName.length()+1);
//      //            }
//      //            //rootMap.put(theKey, form.getFirstValue(key));
//      //                
//      //            }
//      //        }
//      val jValue: org.json4s.JsonAST.JValue = JObject(elements.toList)
//      val json = parse("""{"name"="joe"}""")
//      json.extract[Note]
//    }
//  }
//
//  @Post("x-www-form-urlencoded:html")
//  def post(form: Form, variant: Variant) = {
//    val timerMetric = getMetricsCollector().timerFor(this.getClass(), "posthtml");
//    val entity = new FormDeserializer[T](getParameterizedType()).createFrom(form);
//    //post(entity, variant);
//    timerMetric.stop();
//    
//  }
//}