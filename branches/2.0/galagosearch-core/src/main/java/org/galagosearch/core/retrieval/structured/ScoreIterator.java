// BSD License (http://www.galagosearch.org/license)
package org.galagosearch.core.retrieval.structured;

import gnu.trove.TObjectDoubleHashMap;
import java.io.IOException;
import org.galagosearch.core.index.ValueIterator;

/**
 * 2/24/2010 (irmarc): Refactored to represent anything that
 * iterates and produces scores.
 *
 * 12/19/2010 (irmarc): This should've been done before - methods add to return
 * estimates of the maximum and minimum scores the iterator. If it doesn't know,
 * the estimates are awful (Double.MAX and Double.MIN), otherwise they're useful.
 *
 *
 * @author trevor, irmarc
 */
public interface ScoreIterator extends StructuredIterator {

  public int currentIdentifier();

  public void next();

  /**
   * Produce a score for the current candidate
   * @return
   */
  public double score();

  /**
   * Estimate the maximum possible score to be produced by this iterator.
   * If a useful estimate cannot be formed, returns Double.MAX_VALUE
   */
  public double maximumScore();

  /**
   * Estimate the minimum possible score to be produced by this iterator.
   * If useful estimate cannot be formed, returns Double.MIN_VALUE
   *
   */
  public double minimumScore();

  /**
   * Produce a set of scores for the current candidate
   *  - the set of scores correspond to the set of parameters input by the user.
   *
   */
  public TObjectDoubleHashMap<String> parameterSweepScore();
}
