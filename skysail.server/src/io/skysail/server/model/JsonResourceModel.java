//package io.skysail.server.model;
//
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import org.restlet.representation.Variant;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.node.JsonNodeFactory;
//import com.fasterxml.jackson.databind.node.NullNode;
//import com.fasterxml.jackson.databind.node.POJONode;
//import com.fasterxml.jackson.databind.node.ValueNode;
//
//import io.skysail.api.responses.ConstraintViolationsResponse;
//import io.skysail.api.responses.EntityServerResponse;
//import io.skysail.api.responses.FormResponse;
//import io.skysail.api.responses.ListServerResponse;
//import io.skysail.api.responses.RelationTargetResponse;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.api.um.UserManagementProvider;
//import io.skysail.server.rendering.Theme;
//import io.skysail.server.restlet.resources.ListServerResource;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//import io.skysail.server.utils.FormfieldUtils;
//import io.skysail.server.utils.ResourceUtils;
//import lombok.Getter;
//import lombok.ToString;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * The model of the resource from which the html representation is derived.
// *
// * <p>
// * The typical setup for a skysail client is something like single-page
// * application, i.e the client initiates all the request to the resources it
// * needs and builds up the GUI from that. Subsequent requests might alter only
// * parts of the GUI.
// * </p>
// *
// * <p>
// * The purpose of this class is a little bit different, as it aims to be more
// * generic. All relevant rawData (links, the current user, pagination
// * information, the entities fields, their metadata and associated entities and
// * so on) can be accessed, so that a complete "one-time-request" representation
// * of the current resource can be generated from this information.
// * </p>
// *
// * @param <R>
// * @param <T>
// */
//@Getter
//@ToString
//@Slf4j
//public class JsonResourceModel<S extends SkysailServerResource<T>, T> extends ResourceModel<S, T> {
//
//	private static JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
//
//	public JsonResourceModel(S resource, SkysailResponse<?> response) {
//		super(resource, response);
//	}
//
//	public JsonResourceModel(S resource, SkysailResponse<?> entity, UserManagementProvider userManagementProvider,
//			Variant target, Theme theme) {
//		super(resource, entity, userManagementProvider, target, theme);
//	}
//
//	public void process() {
//		rawJsonData = getJsonData(response, resource);
//
//		if (resource instanceof ListServerResource<?>) {
//			facets = ((ListServerResource<?>) resource).getFacets();
//		}
//
//		parameterizedType = resource.getParameterizedType();
//
//		fields = FormfieldUtils.determineFormfields(response, resource);
//
//		rootEntity = new EntityModel<>(response.getEntity(), resource);
//
//		String identifierName = "id";// TODO getIdentifierFormField(rawData);
//		jsonData = rawJsonData;//convertJson(identifierName, resource);
//
//		addAssociatedLinks(rawJsonData);
//		// TODO addAssociatedLinks(rawData);
//	}
//	
//	private void addAssociatedLinks(ValueNode theData) {
//		if (!(getResource() instanceof ListServerResource)) {
//			return;
//		}
//		ListServerResource<?> listServerResource = (ListServerResource<?>) getResource();
//		List<io.skysail.api.links.Link> links = listServerResource.getLinks();
//		List<Class<? extends SkysailServerResource<?>>> entityResourceClass = listServerResource
//				.getAssociatedServerResources();
//		if (entityResourceClass != null) {
////			List<Map<String, Object>> sourceAsList = theData;
////			for (Map<String, Object> dataRow : sourceAsList) {
////				addLinks(links, dataRow);
////			}
//			
//			POJONode pn = ((POJONode)theData);
//			
//			
//			
//			Iterator<JsonNode> elements = theData.elements();
//			while(elements.hasNext()) {
//				JsonNode next = elements.next();
//				System.out.println(next);
//			}
//			theData.forEach(e -> {
//				System.out.println(e.toString());
//			});
//			
//		}
//	}
//
//	protected List<Map<String, Object>> convertJson(String identifierName, S resource) {
//		List<Map<String, Object>> result = new ArrayList<>();
////		rawData.stream().filter(row -> row != null).forEach(row -> {
////			Map<String, Object> newRow = new HashMap<>();
////			result.add(newRow);
////			row.keySet().stream().forEach(columnName -> {
////				Object identifier = row.get(identifierName);
////				if (identifier != null) {
////					apply(newRow, row, columnName, identifier, resource);
////				} else {
////					// for now, for Gatling
////					log.debug("identifier was null");
////				}
////			});
////		});
//		return result;
//	}
//
//	private ValueNode getJsonData(Object source, S theResource) {
//		if (source instanceof ListServerResponse) {
//			List<?> list = ((ListServerResponse<?>) source).getEntity();
//			return jsonNodeFactory.pojoNode(list);
//		} else if (source instanceof RelationTargetResponse) {
//		} else if (source instanceof EntityServerResponse) {
//		} else if (source instanceof FormResponse) {
//		} else if (source instanceof ConstraintViolationsResponse) {
//		}
//		return NullNode.getInstance();
//	}
//
//}
