package io.skysail.server.utils;

import org.restlet.Request;
import org.restlet.data.Form;

import lombok.Getter;

//@NoArgsConstructor
public abstract class ParamsUtils {

	@Getter
	private String value;
	
	@Getter
	private Form form;

	protected String fieldname;
	protected Request request;

	public ParamsUtils(String fieldname, Request request) {
		this(fieldname, request, null);
	}

	public ParamsUtils(String fieldname, Request request, String value) {
		this.fieldname = fieldname;
		this.request = request;
		this.form = request.getOriginalRef().getQueryAsForm();
		this.value = value;
	}
	
	protected abstract Form handleQueryForm();

	protected String toggleLink(Request request, String fieldname) {
		Form queryForm = handleQueryForm();
		if (isEmpty(queryForm)) {
			return emptyQueryRef(request);
		}
		queryForm = stripEmptyParams(queryForm);
		return isEmpty(queryForm) ? request.getOriginalRef().getHierarchicalPart() : "?" + queryForm.getQueryString();
	}

	protected String emptyQueryRef(Request request) {
		return request.getOriginalRef().getHierarchicalPart();
	}

	protected boolean isEmpty(Form queryForm) {
		return "".equals(queryForm.getQueryString());
	}


	private Form stripEmptyParams(Form queryForm) {
		Form result = new Form();
		queryForm.forEach(param -> {
			if (param.getValue() != null && !"".equals(param.getValue().trim())) {
				result.add(param);
			}
		});
		return result;
	}
}
