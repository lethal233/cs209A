package cn.edu.sustech.cs209a.ass1;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MainTestForStudent {
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static String[] in_paths;
    private static String[] tested_paths;
    private static String[] no_paths;
    private static Charset[] charsets;
    private static String targetSeparators;
    private static String targetLineSeparator;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @BeforeClass
    public static void init() throws IOException {
        in_paths = new String[]{"./test/1.txt", "./test/2.txt"};
        tested_paths = new String[]{"./test/out/1.csv", "./test/out/2.csv"};
        no_paths = new String[]{"./test/out/3.csv", "./test/out/4.csv"};
        charsets = new Charset[]{Charset.forName("gb2312"), Charset.forName("utf-32BE")};
        targetLineSeparator = "\r\n";
        targetSeparators = ",";
        Main.main(new String[]{".properties"});
    }

    @Test
    public void test01_prob_not_exist() throws IOException {
        exceptionRule.expect(IllegalArgumentException.class);
        Main.main(new String[]{".properties_not_exist"});
    }

    @Test
    public void test02_InputCSVPath_not_exist() throws IOException {
        exceptionRule.expect(NullPointerException.class);
        Main.main(new String[]{".properties_no_inputCSV"});
    }

    @Test
    public void test11_output_path() {
        for (String tested_path : tested_paths) {
            File file = new File(tested_path);
            assertTrue(file.exists());
        }
    }

    @Test
    public void test12_no_output_path() {
        for (String no_path : no_paths) {
            File file = new File(no_path);
            assertFalse(file.exists());
        }
    }

    @Test
    public void test13_lineSeparator() throws IOException {
        for (String path: tested_paths) {
            String content = readString(path, UTF_8);
            String[] lines = content.split(targetLineSeparator);
            assertEquals("\"CS209A\",\"计算机系统设计与应用\"", lines[1]);
        }
    }

    @Test
    public void test14_separator() throws IOException {
        for (String path: tested_paths) {
            String content = readString(path, UTF_8);
            String[] lines = content.split(targetLineSeparator);
            String[] words = lines[1].split(targetSeparators);
            assertEquals("\"CS209A\"", words[0]);
            assertEquals("\"计算机系统设计与应用\"", words[1]);
        }
    }


    @Test
    public void test21_log_exist(){
        File error = new File("log/error.txt");
        assertTrue(error.exists());
        File summary = new File("log/summary.csv");
        assertTrue(summary.exists());
    }

    @Test
    public void test22_log_header() throws IOException {
        String error = readString("log/error.txt", UTF_8);
        String[] lines = error.split("\n|(\r\n)");
        String[] words = lines[0].split(",");
        assertEquals("\"original file path\"", words[0]);
        assertEquals("\"abnormal line\"", words[1]);

        String summary = readString("log/summary.csv", UTF_8);
        lines = summary.split("\n|(\r\n)");
        words = lines[0].split(",");
        assertEquals("\"file path\"", words[0]);
        assertEquals("\"processed line count\"", words[1]);
        assertEquals("\"processed file size(Bytes)\"", words[2]);
        assertEquals("\"is align\"", words[3]);
    }

    @Test
    public void test23_error() throws IOException {
        String error = readString("log/error.txt", UTF_8);
        String expected = "\"original file path\",\"abnormal line\"\r\n\"test/3.txt\",\"-1\"\r\n\"test/4.txt\",\"3\"\r\n";
        String[] lines1 = error.split("\n|(\r\n)");
        String[] lines2 = expected.split("\n|(\r\n)");
		assertEquals(lines2.length, lines1.length);
        for (int i = 0; i<lines1.length; i++){
            assertEquals(lines2[i], lines1[i]);
        }
    }

    @Test
    public void test24_summary() throws IOException {
        String summary = readString("log/summary.csv", UTF_8);
        String expected = "\"file path\",\"processed line count\",\"processed file size(Bytes)\",\"is align\"\r\n" +
                "\"test/out/1.csv\",\"3\",\"91\",\"true\"\r\n" +
                "\"test/out/2.csv\",\"3\",\"95\",\"false\"\r\n";
        String[] lines1 = summary.split("\n|(\r\n)");
        String[] lines2 = expected.split("\n|(\r\n)");
		assertEquals(lines2.length, lines1.length);
        for (int i = 0; i<lines1.length; i++){
            assertEquals(lines2[i], lines1[i]);
        }
    }

    private static String readString(String pathname, Charset charset) throws IOException {
        return new String(Files.readAllBytes(new File(pathname).toPath()), charset);
    }

}