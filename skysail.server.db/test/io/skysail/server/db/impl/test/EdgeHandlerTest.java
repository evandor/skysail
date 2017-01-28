package io.skysail.server.db.impl.test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.Entity;
import io.skysail.server.db.impl.EdgeHandler;
import io.skysail.server.db.impl.test.entities.SomeRole;
import io.skysail.server.db.impl.test.entities.SomeUser;

public class EdgeHandlerTest {

    private OrientGraph db;
    private SomeUser theUser;
    private SomeRole theRole;
    private OrientVertex vertex;
    private EdgeHandler persistEdgeHandler;
    private EdgeHandler updateEdgeHandler;

    @Before
    public void setUp() throws Exception {
        vertex = Mockito.mock(OrientVertex.class);
        db = Mockito.mock(OrientGraph.class);
        theUser = new SomeUser("aUser");
        theRole = new SomeRole("aRole");
        theUser.getRoles().add(theRole);
        persistEdgeHandler = new EdgeHandler((identifiable) -> executePersist(identifiable),db);
        updateEdgeHandler = new EdgeHandler((identifiable) -> executeUpdate(identifiable),db);
    }

    private OrientVertex executeUpdate(Entity identifiable) {
        return vertex;
    }

    private OrientVertex executePersist(Entity identifiable) {
        return vertex;
    }

    @Test
    @Ignore
    public void new_vertex_with_new_edge_is_added_to_collection_field_for_reference_without_id() throws Exception {
        theRole.setId(null);

        persistEdgeHandler.handleEdges(theUser, vertex, Collections.emptyMap(), "roles");

        Mockito.verify(db).addVertex(theRole);
        Mockito.verify(db).addEdge(null, vertex, null, "roles");
    }

    @Test
    @Ignore
    public void existing_vertex_with_new_edge_is_added_to_collection_field_for_reference_with_id() throws Exception {
        theRole.setId("theRoleId");

        persistEdgeHandler.handleEdges(theUser, vertex, Collections.emptyMap(), "roles");

        Mockito.verify(db).getVertex(theRole.getId());
        Mockito.verify(db).addEdge(null, vertex, null, "roles");
    }

    @Test
    @Ignore
    public void id_field_is_added_as_edge_to_provided_vertex() throws Exception {
        Edge edge = Mockito.mock(Edge.class);
        Iterable<Edge> edges = Arrays.asList(edge);
        Mockito.when(vertex.getEdges(Direction.OUT, "roleId")).thenReturn(edges);
        Mockito.when(db.getVertex(edge.toString())).thenReturn(vertex);
//        persistEdgeHandler.handleEdges(theUser, vertex, Collections.emptyMap(), "roleId");
//        Mockito.verify(db).removeEdge(edge);
//        Mockito.verify(db).addEdge(null, vertex, null, "roleId");
    }
}
