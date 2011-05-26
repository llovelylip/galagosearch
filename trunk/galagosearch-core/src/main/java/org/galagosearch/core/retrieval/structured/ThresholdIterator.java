// BSD License (http://www.galagosearch.org/license)
package org.galagosearch.core.retrieval.structured;

import java.io.IOException;
import org.galagosearch.core.index.ValueIterator;
import org.galagosearch.tupleflow.Parameters;

/**
 * #threshold: raw=x.xx ( ScoreIterator ) 
 *
 * @author sjh
 */
@RequiredStatistics(statistics = {"raw", "prob", "logprob"})
public class ThresholdIterator implements IndicatorIterator {

  DocumentContext context;
  ScoreValueIterator iterator;
  int document;
  double threshold;

  public ThresholdIterator(Parameters parameters, ScoreValueIterator scorer) {
    this.iterator = scorer;
    this.document = scorer.currentCandidate();
    if (parameters.containsKey("raw")) {
      this.threshold = Double.parseDouble(parameters.get("raw"));
    
    } else if (parameters.containsKey("prob")) {
      this.threshold = Math.log(Double.parseDouble(parameters.get("prob")));
      assert this.threshold < 0;

    } else if (parameters.containsKey("logprob")) {
      this.threshold = Double.parseDouble(parameters.get("logprob"));
      assert this.threshold < 0;
    
    } else {
      throw new RuntimeException("#threshold operator requires a thresholding parameter: [raw|prob|logprob]");
    }
  }

  public void setContext(DocumentContext context) {
    this.context = context;
  }

  public DocumentContext getContext() {
    return this.context;
  }

  public void reset() throws IOException {
    iterator.reset();
    this.document = iterator.currentCandidate();
  }

  public boolean isDone() {
    return iterator.isDone();
  }

  public int currentCandidate() {
    return document;
  }

  public boolean hasMatch(int identifier) {
    // needs to check the score against a threshold.
    return ((this.document == identifier)
            && (iterator.score() >= threshold));
  }

  /* 
   *  BE VERY CAREFUL NOT TO CALL next() INTERNALLY
   */
  public boolean next() throws IOException {
    movePast(document);
    // use a new doccontext to iterate until the indicator is true
    while ((!isDone()) && (!this.hasMatch(document))) {
      movePast(document);
    }
    return (!isDone());
  }

  public boolean moveTo(int identifier) throws IOException {
    iterator.moveTo(identifier);
    this.document = iterator.currentCandidate();
    return hasMatch(identifier);
  }

  public void movePast(int identifier) throws IOException {
    moveTo(identifier + 1);
  }

  public String getEntry() throws IOException {
    throw new UnsupportedOperationException("Threshold nodes don't have singular values");
  }

  public long totalEntries() {
    return iterator.totalEntries();
  }

  public int compareTo(ValueIterator other) {
    if (isDone() && !other.isDone()) {
      return 1;
    }
    if (other.isDone() && !isDone()) {
      return -1;
    }
    if (isDone() && other.isDone()) {
      return 0;
    }
    return currentCandidate() - other.currentCandidate();
  }

  @Override
  public boolean getStatus() {
    return hasMatch(this.document);
  }

  public boolean getStatus(int document) {
    return hasMatch(document);
  }

  public int getIndicatorStatus() {
    if (hasMatch(this.document)) {
      return 1;
    }
    return 0;
  }
}
