package org.gradoop.model.impl.operators.summarization;

import org.gradoop.model.impl.pojo.DefaultEdgeData;
import org.gradoop.model.impl.pojo.DefaultGraphData;
import org.gradoop.model.impl.pojo.DefaultVertexData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LogicalGraphSummarizeGroupMapTest extends
  LogicalGraphSummarizeTestBase {

  public LogicalGraphSummarizeGroupMapTest(TestExecutionMode mode) {
    super(mode);
  }

  @Override
  public Summarization<DefaultVertexData, DefaultEdgeData, DefaultGraphData>
  getSummarizationImpl(
    String vertexGroupingKey, boolean useVertexLabel, String edgeGroupingKey,
    boolean useEdgeLabel) {
    return new SummarizationGroupMap<>(vertexGroupingKey, edgeGroupingKey,
      useVertexLabel, useEdgeLabel);
  }
}