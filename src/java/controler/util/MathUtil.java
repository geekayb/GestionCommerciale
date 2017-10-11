/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import java.math.BigDecimal;

/**
 *
 * @author Younes
 */
public class MathUtil {
     public static BigDecimal calculerMax(BigDecimal[][] resultas) {
        BigDecimal max = new BigDecimal(0);
        max = resultas[0][0];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (max.compareTo(resultas[i][j]) == -1) {
                    max = resultas[i][j];
                    System.out.println("ha max :::" + max);
                }
            }
        }
        return max;
    }
}
