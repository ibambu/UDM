/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.main;

import com.ibamb.udm.util.DataTypeConvert;
import java.util.Arrays;

/**
 *
 * @author Luo Tao
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        byte a = -1;

        int d = DataTypeConvert.toUnsignedByte(a);
        System.out.println(d);
        int c = Byte.MAX_VALUE-Byte.MIN_VALUE +1 +(int)a;
        System.out.println("ccccc==="+c);
        byte[] b = new byte[1];
        b[0]=a;
        String cc = "12re";
        byte[] ccArray = cc.getBytes();
        System.out.println(Arrays.toString(cc.getBytes()));
        System.out.println(new String(ccArray,0,ccArray.length));
        String test2 = DataTypeConvert.hexStringToString("12");
        System.out.println(test2);
    }
    
}
