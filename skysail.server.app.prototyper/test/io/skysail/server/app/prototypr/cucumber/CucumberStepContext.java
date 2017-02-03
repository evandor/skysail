package io.skysail.server.app.prototypr.cucumber;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class CucumberStepContext {

    private static final String RANDOM_IDENT = "<random>";

    private static final VariantInfo VARIANT = new VariantInfo(MediaType.TEXT_HTML);

    private Class<?> entityClass;

    private Map<String, String> randoms = new HashMap<>();

    @Getter
    @Setter
    private SkysailResponse<? extends Entity> lastResponse;

    public CucumberStepContext(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public boolean handleKey(Map<String, String> data, Form form, String key) {
        String value = data.get(key);
        if (value.contains(RANDOM_IDENT)) {
            String randomString = new BigInteger(130, new SecureRandom()).toString(32);
            randoms.put(key, randomString);
            value = value.replace(RANDOM_IDENT, randomString);
        }
        return form.add(key, value);
    }

    public Variant getVariant() {
        return VARIANT;
    }

    public Form formFor(String... str) {
        Form form = new Form();
        Arrays.stream(str).forEach(input -> {
            String[] split = input.split(":", 2);
            form.add(split[0], split[1]);
        });
        return form;
    }

    public Map<String, String> substitute(Map<String, String> data) {
        Map<String, String> result = new HashMap<>();
        data.entrySet().stream().forEach(e -> substitute(result, e));
        return result;
    }

    private void substitute(Map<String,String> result, Entry<String, String> entry) {
        String value = entry.getValue();
        if (value.contains(RANDOM_IDENT)) {
            value = value.replace(RANDOM_IDENT, randoms.get(entry.getKey()));
        }
        result.put(entry.getKey(), value);
    }

    public Form toForm(Map<String, String> data) {
        Form form = new Form();
        data.keySet().stream().forEach(key -> handleKey(data, form, key));
        log.info("setting form to {}",form);
        return form;
    }

    public void post(PostEntityServerResource<?> resource, Map<String, String> data) {
        Form form = toForm(data);
        SkysailResponse<? extends Entity> response = resource.post(form, getVariant());
		setLastResponse(response);
    }
}
