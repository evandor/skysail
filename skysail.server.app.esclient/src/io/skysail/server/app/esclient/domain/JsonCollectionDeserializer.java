package io.skysail.server.app.esclient.domain;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JsonCollectionDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {

	private final BeanProperty property;

	/**
	 * Default constructor needed by Jackson to be able to call
	 * 'createContextual'. Beware, that the object created here will cause a NPE
	 * when used for deserializing!
	 */
	public JsonCollectionDeserializer() {
		super(Collection.class);
		this.property = null;
	}

	/**
	 * Constructor for the actual object to be used for deserializing.
	 * 
	 * @param property
	 *            this is the property/field which is to be serialized
	 */
	private JsonCollectionDeserializer(BeanProperty property) {
		super(property.getType());
		this.property = property;
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
			throws JsonMappingException {
		return new JsonCollectionDeserializer(property);
	}

	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		switch (jp.getCurrentToken()) {
		case VALUE_STRING:
			// value is a string but we want it to be something else: unescape
			// the string and convert it
			return "hi";// JacksonUtil.MAPPER.readValue(StringUtil.unescapeXml(jp.getText()),
						// property.getType());
		default:
			// continue as normal: find the correct deserializer for the type
			// and call it
			return "there";// ctxt.findContextualValueDeserializer(property.getType(),
							// property).deserialize(jp, ctxt);
		}
	}
}