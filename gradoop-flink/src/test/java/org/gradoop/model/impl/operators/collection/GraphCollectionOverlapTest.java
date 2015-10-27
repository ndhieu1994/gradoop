/*
 * This file is part of Gradoop.
 *
 * Gradoop is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gradoop is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Gradoop.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gradoop.model.impl.operators.collection;

import com.google.common.collect.Lists;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.io.LocalCollectionOutputFormat;
import org.gradoop.model.FlinkTestBase;
import org.gradoop.model.impl.GraphCollection;
import org.gradoop.model.impl.LogicalGraph;
import org.gradoop.model.impl.operators.collection.OverlapCollection;
import org.gradoop.model.impl.pojo.DefaultEdgeData;
import org.gradoop.model.impl.pojo.DefaultGraphData;
import org.gradoop.model.impl.pojo.DefaultVertexData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class GraphCollectionOverlapTest extends FlinkTestBase {
  public GraphCollectionOverlapTest(TestExecutionMode mode) {
    super(mode);
  }

  @Test
  public void overlapCollectionTest() throws Exception {
    GraphCollection<DefaultVertexData, DefaultEdgeData, DefaultGraphData> coll =
      getGraphStore().getCollection().getGraphs(1L, 2L, 3L);
    LogicalGraph<DefaultVertexData, DefaultEdgeData, DefaultGraphData>
      newGraph = coll.callForGraph(
      new OverlapCollection<DefaultVertexData, DefaultEdgeData,
              DefaultGraphData>());

    List<DefaultVertexData> oldVertices = Lists.newArrayList();
    List<DefaultEdgeData> oldEdges = Lists.newArrayList();
    List<Long> oldGraphs = Lists.newArrayList();
    List<DefaultVertexData> newVertices = Lists.newArrayList();
    List<DefaultEdgeData> newEdges = Lists.newArrayList();

    coll.getVertices().output(new LocalCollectionOutputFormat<>(oldVertices));
    coll.getEdges().output(new LocalCollectionOutputFormat<>(oldEdges));
    coll.getGraphHeads().map(new MapFunction<DefaultGraphData, Long>() {
      @Override
      public Long map(DefaultGraphData graphData) throws Exception {
        return graphData.getId();
      }
    }).output(new LocalCollectionOutputFormat<>(oldGraphs));

    newGraph.getVertices()
      .output(new LocalCollectionOutputFormat<>(newVertices));
    newGraph.getEdges().output(new LocalCollectionOutputFormat<>(newEdges));
    getExecutionEnvironment().execute();

    assertNotNull("graph was null", newGraph);
    for (DefaultVertexData vertex : newVertices) {
      assertTrue(oldVertices.contains(vertex));
      assertTrue(oldGraphs.containsAll(vertex.getGraphs()));
    }
    for (DefaultEdgeData edge : newEdges) {
      assertTrue(oldEdges.contains(edge));
      assertTrue(oldGraphs.containsAll(edge.getGraphs()));
    }
  }
}
