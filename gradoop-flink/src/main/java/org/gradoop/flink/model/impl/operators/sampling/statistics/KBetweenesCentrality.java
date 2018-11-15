package org.gradoop.flink.model.impl.operators.sampling.statistics;

import org.gradoop.flink.model.api.operators.UnaryGraphToGraphOperator;
import org.gradoop.flink.model.impl.epgm.LogicalGraph;

public class KBetweenesCentrality implements UnaryGraphToGraphOperator {

  public  KBetweenesCentrality(int k) {}

  @Override
  public LogicalGraph execute(LogicalGraph graph) {
    return null;
  }

  @Override
  public String getName() {
    return KBetweenesCentrality.class.getName();
  }
}
