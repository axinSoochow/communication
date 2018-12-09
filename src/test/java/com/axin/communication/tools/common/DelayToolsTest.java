package com.axin.communication.tools.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class DelayToolsTest {

  @Test
  public void addDelay() {
    int[][] delayMPEM = NetworkCodeTools.creatDelayMPEM(5, 5, 0.5);
    MatrixTools.printMatrix(delayMPEM);

    DelayTools.addDelay(delayMPEM);
    MatrixTools.printMatrix(delayMPEM);

  }
}