package io.skysail.server.app.notes.domain;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.PutView;
import io.skysail.server.forms.Visibility;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Note implements Identifiable {

    public enum BackupStatus {
        NONE, // status unknown
        SYNCING,
        SYNCED
    }

    @Id
    private String id;

    @Field(inputType = InputType.READONLY)
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
    @PostView(visibility = Visibility.HIDE)
    @PutView(visibility = Visibility.HIDE)
    private Date created;

    @Field(inputType = InputType.READONLY)
    @PostView(visibility = Visibility.HIDE)
    @PutView(visibility = Visibility.HIDE)
    private Date modified;

    @Field
    @PostView(visibility = Visibility.HIDE)
    @PutView(visibility = Visibility.HIDE)
    // @ListView(hide = true)
    private BackupStatus backupStatus = BackupStatus.NONE;

}