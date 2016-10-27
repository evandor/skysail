package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.facets.FacetType;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.Facets;
import io.skysail.server.utils.ParamsUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A FieldFacet describes a field-related way to filter data in a list.
 *
 * For example, given a list of transactions (including a date), you could define a facet based
 * on the date's year. When the transaction list is retrieved, you'll get one "bucket" of entries
 * per year.
 *
 * Extensions of this class provide various ways to define such filters; they are configured in
 * a file called "facets.cfg" which is read by the {@link FacetsProvider}.
 *
 * When a {@link SkysailApplication} is started, its {@link SkysailApplicationModel} is created
 * and each {@link SkysailFieldModel} is checked if it has any facet-related setup.
 *
 * If the data list in question is being rendered, the {@link ParamsUtils} class is used to determine
 * links that will trigger the selection or unselection of a specific bucket by setting the appropriate
 * filter parameter (see SkysailServerResource.FILTER_PARAM_NAME).
 *
 * @see {@link Facets}
 * @see Facet
 * @see FacetType
 * @see FacetsProvider
 * @see ParamsUtils
 * @see FormField
 * @See CellRenderingHelper
 *
 */
@ToString
public abstract class FieldFacet {

    @Getter
    private String id;

    @Getter
    private String name;

    public FieldFacet(@NonNull String id, @NonNull Map<String, String> config) {
        this.id = id;
        this.name = id.substring(1 + id.lastIndexOf("."));
    }

    public abstract Map<String, AtomicInteger> bucketsFrom(Field field, List<?> list);

    public abstract String sqlFilterExpression(String value);

}
