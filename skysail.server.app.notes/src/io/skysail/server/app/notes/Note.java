package io.skysail.server.app.notes;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
		NONE,     // status unknown   
		CREATING, // about to create the entity, should be replaced by CREATED once AWS backup is completed.
		CREATED,  // successfully created and backuped.
		UPDATING, // about to update entity 
		UPDATED,  // successfully updated entity
		DELETING, // about to delete entity
		DELETED   // successfully deleted entity
	}
	
	@Id
    private String id;

	private String uuid;

    @Field(description = "the notes title, derived from the beginning of the content if left empty")
    private String title;

    @Field(description = "a note belongs to a (non-empty) category")
    @Size(min = 1)
    private String category;

    @Field(inputType = InputType.TEXTAREA, description = "non-empty notes content")
    @ListView(hide = true)
    @NotNull
    @Size(min=1)
    private String content;

    @Field(inputType = InputType.READONLY)
    private Date created;
    
    @Field(description = "this field tracks the status of the AWS backup.")
    @PostView(visibility = Visibility.HIDE)
    @ListView(hide = true)
    private BackupStatus backupStatus = BackupStatus.NONE;

}