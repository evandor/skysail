package io.skysail.server.app.ref.singleentity;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class HtmlPage implements Entity {

	@Id
    private String id = "7";

	private String html;

	public HtmlPage(String finalHTML) {
		this.html = finalHTML;
	}
   
}
