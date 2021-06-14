package cn.edu.sustech.cs209a.ass1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shilong Li (Lori)
 * @project CS209A_ASS1_ForStudent
 * @filename Test
 * @date 2021/3/17 13:59
 */
public class Test {
    public static void main(String[] args) {

        String inputCSVPath = "test/test32.bin";
        StringBuilder sb = new StringBuilder();
        sb.append(inputCSVPath, 0, 1);
        System.out.println(sb.toString());

//        try {
//            String fileContent = Utils8.detectFromUnknownEncoding(inputCSVPath);
//            String[] a = fileContent.split("(\r\n)|\n");
//            System.out.println(a[0].charAt(0));//大端
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        System.out.println(a.get(0).charAt(0) == 0xFFFE);
    }
}
