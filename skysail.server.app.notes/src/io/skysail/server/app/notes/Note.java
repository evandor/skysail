package io.skysail.server.app.notes;

import java.util.Date;

import javax.persistence.Id;
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

    @Field
    private String title;

    @Field
    @Size(min = 1)
    private String category;

    @Field(inputType = InputType.TEXTAREA)
    @ListView(hide = true)
    private String content;

    @Field(inputType = InputType.READONLY)
    private Date created;
    
    @Field
    @PostView(visibility = Visibility.HIDE)
    @ListView(hide = true)
    private BackupStatus backupStatus = BackupStatus.NONE;

}