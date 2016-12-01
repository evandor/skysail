//package io.skysail.server.converter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import org.osgi.service.component.annotations.Component;
//import org.restlet.data.MediaType;
//import org.restlet.engine.resource.VariantInfo;
//import org.restlet.ext.jackson.JacksonConverter;
//import org.restlet.representation.Representation;
//import org.restlet.representation.StringRepresentation;
//import org.restlet.representation.Variant;
//import org.restlet.resource.Resource;
//
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.app.SkysailApplication;
//import io.skysail.server.services.OsgiConverterHelper;
//import lombok.extern.slf4j.Slf4j;
//
//@Component(immediate = true)
//@Slf4j
//public class DataConverter extends JacksonConverter implements OsgiConverterHelper {
//
//	private static final float DEFAULT_MATCH_VALUE = 0.3f;
//	private static Map<MediaType, Float> mediaTypesMatch = new HashMap<MediaType, Float>();
//	
//	private Pattern keysPattern = Pattern.compile("(\"[a-zA-Z_0-9]*\":)");
//
//	static {
//		mediaTypesMatch.put(SkysailApplication.SKYSAIL_DATA, 1.0F);
//	}
//
//	@Override
//	public List<Class<?>> getObjectClasses(Variant source) {
//		return null;
//	}
//
//	@Override
//	public List<VariantInfo> getVariants(Class<?> source) {
//		return Arrays.asList(new VariantInfo(SkysailApplication.SKYSAIL_DATA));
//	}
//
//	@Override
//	public float score(Object source, Variant target, Resource resource) {
//		if (target == null) {
//			return 0.0f;
//		}
//		for (MediaType mediaType : mediaTypesMatch.keySet()) {
//			if (target.getMediaType().equals(mediaType)) {
//				log.debug("converter '{}' matched '{}' with threshold {}", new Object[] {
//						this.getClass().getSimpleName(), mediaTypesMatch, mediaTypesMatch.get(mediaType) });
//				return mediaTypesMatch.get(mediaType);
//			}
//		}
//		return DEFAULT_MATCH_VALUE;
//	}
//
//	@Override
//	protected boolean isCompatible(Variant variant) {
//		return true;
//	}
//
//	@Override
//	public Representation toRepresentation(Object source, Variant target, Resource resource) {
//        if (source instanceof SkysailResponse) {
//            Object entity = ((SkysailResponse<?>) source).getEntity();
//            Representation jsonRep = super.toRepresentation(entity, target, resource);
//    		try {
//    			String data = jsonRep.getText().replace("{", "[").replace("}","]");
//    			return new StringRepresentation("{\"data\":" + removeKeys(data) + "}");
//    		} catch (IOException e) {
//    			log.error(e.getMessage(), e);
//    			return jsonRep;
//    		}
//        }
//        return super.toRepresentation(source, target, resource);
//	}
//
//	private String removeKeys(String data) {
//		return keysPattern.matcher(data).replaceAll("");
//	}
//
//}
