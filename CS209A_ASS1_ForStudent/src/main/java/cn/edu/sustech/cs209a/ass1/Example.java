package cn.edu.sustech.cs209a.ass1;

import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Example {
    public static void main(String[] args) throws IOException {
        // Properties example
        Properties properties = new Properties();
        properties.load(new FileInputStream(".properties"));
        System.out.println(properties.getProperty("ErrorPath"));
        System.out.println(properties.getProperty("NotFound","default"));

        // tika example
        CharsetDetector charsetDetector = new CharsetDetector();
        charsetDetector.setText(new BufferedInputStream(new FileInputStream("test/1.txt")));
        CharsetMatch charsetMatch = charsetDetector.detect();
        System.out.println(charsetMatch.getName());
        System.out.println(charsetMatch.getString());


    }
}
