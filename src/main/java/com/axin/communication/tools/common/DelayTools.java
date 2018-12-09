package com.axin.communication.tools.common;

/**
 * 计算时延工具
 */
public class DelayTools {


  /**
   * 计算解码后，累加通信成功的包的时延
   */
  public static int computeDelay(int[][] delayMPEM) {
    int number = delayMPEM.length - 1;
    int packet = delayMPEM[0].length;

    int res = 0;
    for (int j = 0; j < packet; j++) {
      int temp = 0;
      for (int i = 0; i < number; i++) {
        delayMPEM[i][j] += temp;
      }
      if (temp == 0) {
        res += delayMPEM[number][j];
      }
    }
    return res;
  }

  /**
   * 增加时延
   * @param delayMPEM
   */
  public static void addDelay(int[][] delayMPEM) {
    int number = delayMPEM.length - 1;
    int packet = delayMPEM[0].length;

    for (int j = 0; j < packet; j++) {
      delayMPEM[number][j]++;
    }
  }
}
