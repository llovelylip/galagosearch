/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.galagosearch.core.retrieval.extents;

import junit.framework.TestCase;
import org.galagosearch.core.retrieval.structured.DocumentContext;
import org.galagosearch.core.retrieval.structured.ScaleIterator;
import org.galagosearch.core.retrieval.structured.ThresholdIterator;
import org.galagosearch.tupleflow.Parameters;

/**
 *
 * @author marc
 */
public class ThresholdIteratorTest extends TestCase {

  int[] docsA = new int[]{5, 10, 15, 20};
  double[] scoresA = new double[]{1.0, 2.0, 3.0, 4.0};
  int[] docsB = new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20};
  double[] scoresB = new double[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20};

  public ThresholdIteratorTest(String testName) {
    super(testName);
  }

  public void testA() throws Exception {
    FakeScoreIterator inner = new FakeScoreIterator(docsA, scoresA);
    DocumentContext dc = new DocumentContext();
    inner.setContext(dc);
    
    Parameters dummyParameters = new Parameters();
    dummyParameters.add("raw", "2.5");
    ThresholdIterator iterator = new ThresholdIterator(dummyParameters, inner);

    assertFalse(iterator.isDone());
    dc.document = iterator.currentCandidate();
    assertFalse(iterator.hasMatch(docsA[0]));
    iterator.movePast(docsA[0]);

    assertFalse(iterator.isDone());
    dc.document = iterator.currentCandidate();
    assertFalse(iterator.hasMatch(docsA[1]));
    iterator.movePast(docsA[1]);

    assertFalse(iterator.isDone());
    dc.document = iterator.currentCandidate();
    assertTrue(iterator.hasMatch(docsA[2]));
    iterator.movePast(docsA[2]);

    assertFalse(iterator.isDone());
    dc.document = iterator.currentCandidate();
    assertTrue(iterator.hasMatch(docsA[3]));
    iterator.movePast(docsA[3]);

    assertTrue(iterator.isDone());
    iterator.reset();

    dc.document = iterator.currentCandidate();
    assertFalse(iterator.hasMatch(docsA[0]));
    iterator.movePast(docsA[2]);
    
    assertFalse(iterator.isDone());
    dc.document = iterator.currentCandidate();
    assertTrue(iterator.hasMatch(docsA[3]));
  }
}
