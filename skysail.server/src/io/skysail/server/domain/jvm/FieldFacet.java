package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.restlet.data.Form;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.facets.FacetType;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.forms.FormField;
import io.skysail.server.forms.helper.CellRendererHelper;
import io.skysail.server.restlet.resources.FacetBuckets;
import io.skysail.server.restlet.resources.Facets;
import io.skysail.server.utils.ParamsUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A FieldFacet describes a field-related way to filter data in a list.
 *
 * For example, given a list of transactions (including a date), you could
 * define a facet based on the date's year. When the transaction list is
 * retrieved, you'll get one "bucket" of entries per year.
 *
 * Extensions of this class provide various ways to define such filters; they
 * are configured in a file called "facets.cfg" which is read by the
 * {@link FacetsProvider}.
 *
 * When a {@link SkysailApplication} is started, its
 * {@link SkysailApplicationModel} is created and each {@link SkysailFieldModel}
 * is checked if it has any facet-related setup.
 *
 * If the data list in question is being rendered, the {@link XXXParamsUtils}
 * classes is used to determine links that will trigger the selection or
 * unselection of a specific bucket by setting the appropriate filter parameter
 * (see SkysailServerResource.FILTER_PARAM_NAME).
 *
 * @see {@link Facets}
 * @see Facet
 * @see FacetType
 * @see FacetsProvider
 * @see ParamsUtils
 * @see FormField
 * @see CellRendererHelper
 *
 */
@ToString
public abstract class FieldFacet {

    private static final String DOT = ".";

    /**
     * a full qualified field name like "i.am.a.package.Transaction.amount",
     * never null.
     */
    @Getter
    private final String id;

    /**
     * the field name part of a full qualified field name like "amount", never
     * null.
     */
    @Getter
    private final String name;

    /**
     * Creates the name as the last part of the full qualified field name.
     *
     * @param id
     *            non-null full qualified field name.
     */
    public FieldFacet(@NonNull String id) {
        if ("".equals(id.trim())) {
            throw new IllegalStateException("trying to create a FieldFacet with empty id");
        }
        this.id = id.trim();
        this.name = this.id.substring(1 + this.id.lastIndexOf(DOT));
    }

    /**
     * The provided lists elements are put into buckets according to this facets
     * logic.
     *
     * @param field
     *            if the provided lists elements have a field like this, the
     *            element is put into the appropriate bucket.
     * @param list
     *            the list of elements to be analysed.
     * @return a FacetBuckets object describing all the created buckets with
     *         their associated entries and count.
     */
    public abstract FacetBuckets bucketsFrom(Field field, List<?> list);

    /**
     * @param value
     * @param operatorSign
     * @return
     */
    public abstract String sqlFilterExpression(String value, String operatorSign);

    /**
     * @param node
     * @param gotten
     * @param value
     * @return
     */
    public abstract boolean match(ExprNode node, Object gotten, String value);

    public abstract Form addFormParameters(Form form, String fieldname, String format, String string);

    /**
     * Default implementation.
     *
     * @param value
     * @return
     */
    public Set<String> getSelected(String value) {
        Set<String> result = new HashSet<>();
        result.add(value);
        return result;
    }

    public String getXXX(String value) {
        return value;
    }


}
