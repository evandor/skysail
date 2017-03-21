package io.skysail.api.doc.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import io.skysail.api.doc.ApiDescription;
import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.doc.ApiMetadata.ApiMetadataBuilder;
import io.skysail.api.doc.ApiSummary;
import io.skysail.api.doc.ApiTags;

public class ApiMetadataTest {

    private ApiMetadataBuilder builder;

    private class AnEntity {
        @ApiSummary("getSummary")
        @ApiDescription("getDescription")
        @ApiTags("getTags")
        public String get() {return "";}

        @ApiSummary("putSummary")
        @ApiDescription("putDescription")
        @ApiTags("putTags")
        public String put() {return "";}

        @ApiSummary("postSummary")
        @ApiDescription("postDescription")
        @ApiTags("postTags")
        public String post() {return "";}

        @ApiSummary("deleteSummary")
        @ApiDescription("deleteDescription")
        @ApiTags("deleteTags")
        public String delete() {return "";}
    }

    @Before
    public void setUp(){
        builder = ApiMetadata.builder();
    }

    @Test
    public void check_get_annotations() {
        assertThat(builder.summaryForGet(AnEntity.class, "get").build().getSummaryForGet(),is("getSummary"));
        assertThat(builder.descriptionForGet(AnEntity.class, "get").build().getDescriptionForGet(),is("getDescription"));
        assertThat(Arrays.asList(builder.tagsForGet(AnEntity.class, "get").build().getTagsForGet()),hasItem("getTags"));
    }

    @Test
    public void check_put_annotations() {
        assertThat(builder.summaryForGet(AnEntity.class, "put").build().getSummaryForGet(),is("putSummary"));
        assertThat(builder.descriptionForGet(AnEntity.class, "put").build().getDescriptionForGet(),is("putDescription"));
        assertThat(Arrays.asList(builder.tagsForGet(AnEntity.class, "put").build().getTagsForGet()),hasItem("putTags"));
    }

    @Test
    public void check_post_annotations() {
        assertThat(builder.summaryForGet(AnEntity.class, "post").build().getSummaryForGet(),is("postSummary"));
        assertThat(builder.descriptionForGet(AnEntity.class, "post").build().getDescriptionForGet(),is("postDescription"));
        assertThat(Arrays.asList(builder.tagsForGet(AnEntity.class, "post").build().getTagsForGet()),hasItem("postTags"));
    }

    @Test
    public void check_delete_annotations() {
        assertThat(builder.summaryForGet(AnEntity.class, "delete").build().getSummaryForGet(),is("deleteSummary"));
        assertThat(builder.descriptionForGet(AnEntity.class, "delete").build().getDescriptionForGet(),is("deleteDescription"));
        assertThat(Arrays.asList(builder.tagsForGet(AnEntity.class, "delete").build().getTagsForGet()),hasItem("deleteTags"));
    }

}
