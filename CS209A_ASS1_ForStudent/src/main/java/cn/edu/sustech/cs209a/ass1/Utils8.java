package cn.edu.sustech.cs209a.ass1;

import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Utils8 {
    /**
     * write content to path (if not exist, then create it recursively) with specified encoding.
     *
     * @param content
     * @param path
     * @param charset
     */
    public static void writeAsEncoding(String content, String path, Charset charset) {
        //if not exist, then create it recursively
        File tmp = new File(path);
        if (!tmp.exists()) {
            createFileRecur(path);
        }
        // TODO:  write content to path with specified encoding DONE
        try (FileOutputStream fos = new FileOutputStream(tmp);
             OutputStreamWriter osw = new OutputStreamWriter(fos, charset.name());
             BufferedWriter bufferedWriter = new BufferedWriter(osw);) {
            bufferedWriter.write(content);
        } catch (FileNotFoundException e) {
            System.out.println("The pathname does not exist.");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("The Character Encoding is not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed or interrupted when doing the I/O operations");
            e.printStackTrace();
        }


    }

    public static void createFileRecur(String path) {
        // TODO:  if not exist, then create it recursively
        if (path == null) {
            return;
        }
        try {
            File a = new File(path);
            File parent = a.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (a.exists()) {
                a.delete();
            }
            a.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * detect the encoding of file and return the content as a string
     *
     * @param path the original file path
     * @return the content of file
     * @throws FileNotFoundException
     */
    public static String detectFromUnknownEncoding(String path) throws FileNotFoundException {
        // TODO: detect the encoding, read the original file, and return the content as a string
        String a;
        try {
            CharsetDetector charsetDetector = new CharsetDetector();
            charsetDetector.setText(new BufferedInputStream(new FileInputStream(path)));
            CharsetMatch charsetMatch = charsetDetector.detect();
            a = charsetMatch.getString();
        } catch (IOException e) {
            throw new FileNotFoundException();
        }

        return a;
    }

    /**
     * load the properties
     *
     * @param path the path of properties file
     * @return The Properties
     * @throws FileNotFoundException
     */
    public static Properties loadProperties(String path) throws FileNotFoundException {
        // TODO: load the properties Done
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        return properties;
    }

    /**
     * remove the additional quotes of a string.
     * this method is used to process the csv.
     *
     * @param ori
     * @return real content
     */
    public static String removeQuotes(String ori) {
        StringBuilder sb = new StringBuilder();
        // TODO: remove the additional quotes of a string.
        for (int i = 1; i < ori.length() - 1; i++) {
            if (ori.charAt(i) == '"') {
                if (i + 1 < ori.length() && ori.charAt(i + 1) == '"') {
                    sb.append("\"");
                    ++i;
                }
            } else {
                sb.append(ori.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * the reverse method of {@link Utils8#removeQuotes(String)}
     *
     * @param ori
     * @return real content after add quotes
     */
    public static String addQuotes(String ori) {
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (char c : ori.toCharArray()) {
            if (c == '"') {
                sb.append("\"\"");
            } else {
                sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * read a line of the input csv file
     *
     * @param line
     * @param separator
     * @return
     */
    public static List<String> readCSVLine(String line, char separator) {
        // TODO: read a line of the input csv file,
        List<String> a = new ArrayList<>();

        boolean inCell = false;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < line.length(); ) {
            if (line.charAt(i) == '"') {
                if (inCell) {
                    if (i + 1 < line.length()) {
                        if (line.charAt(i + 1) == '"') {// "...""..."
                            b.append("\"\"");
                            i += 2;
                        } else if (line.charAt(i + 1) == separator) {//"...."[seperator]
                            b.append("\"");
                            a.add(b.toString());
                            b = new StringBuilder();
                            inCell = false;
                            i += 2;
                        }
                    } else {
                        b.append("\"");
                        a.add(b.toString());//"...."\n
                        b = new StringBuilder();
                        inCell = false;
                        i++;
                    }
                } else {
                    if (i + 1 < line.length()) {
                        if (line.charAt(i+1) == '"'){
                            if (i + 2 < line.length() && line.charAt(i + 2) == '"') {//""".....
                                b.append("\"\"\"");
                                i += 3;
                                inCell = true;
                            }
                        } else {//".....
//                            out.append(line.charAt(i++));
                            b.append("\"");
                            inCell = true; // ["]
                            i++;
                        }
                    }
                }
            } else {
                b.append(line.charAt(i++));
            }
        }
        return a;
    }
}
