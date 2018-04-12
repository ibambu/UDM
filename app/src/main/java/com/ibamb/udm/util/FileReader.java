/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luotao
 */
public class FileReader {
        /**
     * 读取文本文件内容
     *
     * @param filename
     * @param doDistinct 是否要去掉重复行
     * @return
     */
    public static List<String> readTxtFileToList(String filename, boolean doDistinct) {
        List dslist = new ArrayList(1500);
        try {
            FileInputStream instream = new FileInputStream(filename);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(instream, "UTF-8"), 50 * 1024 * 1024);
            String readline = null;
            while ((readline = bufreader.readLine()) != null) {
                if (!doDistinct) {
                    dslist.add(readline);
                } else if (!dslist.contains(readline)) {
                    dslist.add(readline);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dslist;
    }
}
