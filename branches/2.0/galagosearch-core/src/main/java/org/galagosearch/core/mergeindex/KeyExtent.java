// BSD License (http://www.galagosearch.org/license)
package org.galagosearch.core.mergeindex;

import org.galagosearch.core.retrieval.structured.Extent;

public class KeyExtent {
  byte[] key;
  Extent extent;
  int document;
  
  public KeyExtent(byte[] key, int document, Extent e) {
    this.key = key;
    this.document = document;
    this.extent = e;
  }
  
}