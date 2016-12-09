package io.skysail.server.app.notes;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.Visibility;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Note implements Identifiable {

	public enum BackupStatus {
		NONE, CREATED, UPDATED, DELETED
	}
	
	@Id
    private String id;

	private String uuid;

    @Field
    private String title;

    @Field
    private String category;

    @Field(inputType = InputType.TEXTAREA)
    @ListView(hide = true)
    private String content;

    @Field
    @PostView(visibility = Visibility.HIDE)
    @ListView(hide = true)
    private BackupStatus backupStatus = BackupStatus.NONE;

}