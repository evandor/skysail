package io.skysail.server.forms.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.Identifiable;
import io.skysail.domain.Nameable;
import io.skysail.domain.core.FieldModel;
import io.skysail.domain.html.InputType;
import io.skysail.server.domain.jvm.ResourceClass;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.domain.jvm.SkysailEntityModel;
import io.skysail.server.domain.jvm.SkysailFieldModel;
import io.skysail.server.domain.jvm.facets.MatcherFacet;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.forms.ListView;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.LinkUtils;
import io.skysail.server.utils.PathSubstitutions;
import io.skysail.server.utils.params.FilterParamUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * For the given field definition (formField) and (skysail) response, the
 * provided cellData object is being rendered as a "meaningful" string according
 * to the following logic:
 *
 * - if the cellData is null, "" is returned. - otherwise, the cellData is
 * turned into a string, applying some logic depending on the cellDatas type.
 *
 *
 */
@Slf4j
public class CellRendererHelper {

    private SkysailResponse<?> response;
    private SkysailFieldModel field;
    private FilterParser parser;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public CellRendererHelper(SkysailFieldModel field, SkysailResponse<?> response) {
        this(field, response,null);
    }

    public CellRendererHelper(SkysailFieldModel field, SkysailResponse<?> response, FilterParser parser) {
        this.field = field;
        this.response = response;
        this.parser = parser;
    }

    public String render(Object cellData, String columnName, Object identifier, SkysailServerResource<?> resource) {
        if (cellData == null) {
            return "";
        }
        String string = toString(cellData, resource, columnName);
        if (field != null && field.getFacet() != null && field.getFacet() instanceof MatcherFacet) {
            String matchFilterLink = new FilterParamUtils(columnName, resource.getRequest(),parser).setMatchFilter(string);
            String filterParam = resource.getRequest().getOriginalRef().getQueryAsForm().getFirstValue("_f");
            if (filterParam != null && columnName.contains(filterParam)) { // TODO
                string = "<a href='"+matchFilterLink+"'><b>"+string+"</b></a>";
            } else {
                string = "<a href='"+matchFilterLink+"'>"+string+"</a>";
            }
        }
        if (response instanceof ListServerResponse) {
            return handleListView(cellData, string, field, identifier, resource);
        }
        return string;
    }

    private String handleListView(Object cellData, String string, SkysailFieldModel f, Object identifier, SkysailServerResource<?> r) {
        if (URL.class.equals(f.getType())) {
            return "<a href='" + string + "' target=\"_blank\">" + truncate(f, string, true) + "</a>";
        } else if (f.getInputType().equals(InputType.URL.name())) {
            return "<a href='" + string + "'>" + truncate(f, string, true) + "</a>";
        } else if (hasListViewLink(f)) {
            return renderListViewLink(string, f, identifier, r);
        } else if (f.getFormat() != null && !"".equals(f.getFormat().trim())) {
            return String.format(f.getFormat(), cellData);
        }
        return truncate(f, string, false);
    }

    private boolean hasListViewLink(SkysailFieldModel f) {
        if (f.getListViewLink() == null) {
            return false;
        }
        return !f.getListViewLink().equals(ListView.DEFAULT.class);
    }

    private String renderListViewLink(String string, SkysailFieldModel f, Object id, SkysailServerResource<?> r) {
        Class<? extends SkysailServerResource<?>> linkedResource = f.getListViewLink();
        List<Link> links = r.getLinks();
        if (links != null && id != null) {
            Optional<Link> findFirst = links.stream().filter(l -> {
                String idAsString = id != null ? id.toString().replace("#", "") : "";
                return linkedResource.equals(l.getCls()) && idAsString.equals(l.getRefId());
            }).findFirst();
            if (findFirst.isPresent()) {
                string = "<a href='" + findFirst.get().getUri() + "'><b>" + truncate(f, string, false) + "</b></a>";
            }
        }
        return string;
    }

    private static String truncate(FieldModel f, String string, boolean withoutHtml) {
        if (f.getTruncateTo() == null) {
            return string;
        }
        if (f.getTruncateTo() > 3) {
            String oldValue = string;
            if (string != null && string.length() > f.getTruncateTo()) {
                if (withoutHtml) {
                    return oldValue.substring(0, f.getTruncateTo() - 3) + "...";
                }
                return "<span title='" + oldValue + "'>" + oldValue.substring(0, f.getTruncateTo() - 3) + "...</span>";
            }
        }
        return string;
    }

    private static String toString(Object cellData, SkysailServerResource<?> resource, String columnName) {
        String result = analyseEntity(resource, columnName);
        if (result != null) {
            return result;
        }

        if (cellData instanceof List) {
            List<?> list = (List<?>) cellData;
            return list.stream().map(l -> toString(l, resource, columnName)).collect(Collectors.joining("<hr>"));
        }
        if (cellData instanceof Set) {
            return Integer.toString(((Set<?>) cellData).size());
        }
        if (cellData instanceof Long && ((Long) cellData) > 1000000000) {
            // assuming timestamp for now
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date((Long) cellData));
        }
        if (cellData instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) cellData;
            return "<ul>"
                    + map.keySet().stream().map(key -> "<li><b>" + key + "</b>:" + map.get(key).toString() + "</li>")
                            .collect(Collectors.joining())
                    + "</ul>";
        }
        if (cellData instanceof Double) {
            String formattedValue = df.format(cellData);
            if ((Double)cellData >= 0.0) {
                return "<font color='green'>" + formattedValue + "</font>";
            } else {
                return "<font color='red'>" + formattedValue + "</font>";
            }
        }
        if (!(cellData instanceof String)) {
            return cellData.toString();
        }
        String string = (String) cellData;
        return string.replace("\r", "&#13;").replace("\n", "&#10;");
    }

    private static String analyseEntity(SkysailServerResource<?> resource, String columnName) {
        if (resource == null) {
            return null;
        }
        Object currentEntity = resource.getCurrentEntity();
        try {
            return createLinkFromAssociatedResource(resource, columnName, currentEntity);
        } catch (Exception e) { // NOSONAR
            log.debug(e.getMessage());
        }
        return null;
    }

    private static String createLinkFromAssociatedResource(SkysailServerResource<?> resource, String columnName,
            Object entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method getter = entity.getClass().getDeclaredMethod(getterNameFor(columnName));
        Object property = getter.invoke(entity);
        if (property instanceof Nameable) {
            return handleNameable((Nameable) property, resource);
        } else if (property instanceof Identifiable) {
            return handleIdentifiable((Identifiable) property);
        }
        return null;
    }

    private static String handleIdentifiable(Identifiable property) {
        return "<a href='#'>" + property.getId() + "</a>";
    }

    private static String handleNameable(Nameable nameable, SkysailServerResource<?> resource) {
        SkysailEntityModel<? extends Identifiable> entity = (SkysailEntityModel<? extends Identifiable>) resource
                .getApplicationModel().getEntity(nameable.getClass().getName());
        ResourceClass associatedEntityResource = entity.getAssociatedResource(ResourceType.ENTITY);
        Link link = LinkUtils.fromResource(resource.getApplication(), associatedEntityResource.getResourceClass());

        // combine with io.skysail.server.utils.LinkUtils.addLink(Link, Object,
        // ListServerResource<?>, List<Link>) into Model?
        List<RouteBuilder> routeBuilders = resource.getApplication()
                .getRouteBuildersForResource(associatedEntityResource.getResourceClass());
        PathSubstitutions pathUtils = new PathSubstitutions(resource.getRequestAttributes(), routeBuilders);
        Map<String, String> substitutions = pathUtils.getFor(nameable);

        String href = link.getUri();
        for (Entry<String, String> entry : substitutions.entrySet()) {
            String substitutable = new StringBuilder("{").append(entry.getKey()).append("}").toString();
            if (link.getUri().contains(substitutable)) {
                href = href.replace(substitutable, entry.getValue());
            }
        }
        return "<a href='" + href + "'>" + nameable.getName() + "</a>";
    }

    private static String getterNameFor(String columnName) {
        return "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
    }

}
