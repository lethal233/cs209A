package cn.edu.sustech.cs209a.ass1;

import org.apache.tika.parser.txt.CharsetDetector;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        // This part gives you a brief idea of how the text preprocessing program works.
        // You will not be able to compile and run the program now.
        // But don't worry! Check the methods one by one, and fill the empties with appropriate code,
        // then you can finish this task step by step.
        // Now go and start.
        // hint: You can also search for `TODO` to find all unfinished part.
        // hint: Before your start, read the `Example` class we provided.

        Properties properties;
        try {
            properties = Utils8.loadProperties(args[0]); // load the property file
        } catch (FileNotFoundException e) {
            // prompt user if no config file is given
            throw new IllegalArgumentException();
        }
        String inputCSVPath = properties.getProperty("InputCSVPath");
        if (inputCSVPath == null) {
            throw new NullPointerException("No CSV path!");
        }


        List<String> a = new ArrayList<>();
        String line;
        try (FileInputStream fis = new FileInputStream(new File(inputCSVPath));
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                a.add(line);
            }
        } catch (IOException ignored) {

        }
        String[] csvContents = a.toArray(new String[0]);


        // TODO: recursively create summaryPath File
        String summaryPath = properties.getProperty("SummaryPath", null);
        Utils8.createFileRecur(summaryPath);

        // TODO: recursively create errorPath File
        String errorPath = properties.getProperty("ErrorPath", null);
        Utils8.createFileRecur(errorPath);

        PrintWriter summaryPW = summaryPath == null ? null : new PrintWriter(summaryPath);
        PrintWriter errorPW = errorPath == null ? null : new PrintWriter(errorPath);
        if (errorPW != null) {
            errorPW.println("\"original file path\",\"abnormal line\"");
        }
        if (summaryPW != null) {
            summaryPW.printf("\"file path\",\"processed line count\",\"processed file size(Bytes)\",\"is align\"\n");
        }
        for (int i = 1; i < csvContents.length; i++) {
            List<String> l = Utils8.readCSVLine(csvContents[i], ',');
            String oriPath = Utils8.removeQuotes(l.get(0));
            try {
                processOneFile(properties, summaryPW, l, oriPath);
            } catch (MyFileException e) {
                if (errorPW != null) {
                    errorPW.printf("\"%s\",\"%d\"\n", oriPath, e.lineCnt);
                }
            }
        }
        if (summaryPW != null) {
            summaryPW.close();
        }
        if (errorPW != null) {
            errorPW.close();
        }
    }

    private static void processOneFile(Properties properties, PrintWriter summaryPW, List<String> l, String oriPath) throws MyFileException {
        String fileContent;
        char targetSeparator = properties.getProperty("TargetSeparator", ",").charAt(0);
        String targetLineSeparator = properties.getProperty("TargetLineSeparator", "\n");
        try {
            fileContent = Utils8.detectFromUnknownEncoding(oriPath);
            // all content of the file
        } catch (FileNotFoundException o) {
            throw new MyFileException(-1);
        }
        char separator = Utils8.removeQuotes(l.get(2)).charAt(0);// separator
        String[] fileLines = fileContent.split("(\r\n)|\n");// split the content

        int lineCnt = fileLines.length;
        int lastColumnCount = 0;
        int currentLine = 0;
        // used to detect align
        boolean isAlign = true;
        StringBuilder out = new StringBuilder();
        for (String line : fileLines) {
            ++currentLine;
            if (line.equals("")) {
                lineCnt--;
            } else {
                int currentColumnCount = 0;
                boolean inCell = false;
                for (int i = 0; i < line.length(); ) {
                    if (line.charAt(i) == '"') {
                        if (inCell) {
                            if (i + 1 < line.length()) {
                                if (line.charAt(i + 1) == '"') {// "...""..."
                                    out.append(line, i, i + 2);
                                    i += 2;
                                } else if (line.charAt(i + 1) == separator) {//"...."[seperator]
                                    out.append(line.charAt(i));
                                    out.append(targetSeparator);
                                    inCell = false;
                                    currentColumnCount += 1;
                                    i += 2;
                                } else { //"..".....
                                    throw new MyFileException(currentLine);
                                }
                            } else {
                                out.append(line.charAt(i++));
                                currentColumnCount += 1;
                                inCell = false;//"...."\n
                            }
                        } else {
                            if (i + 1 < line.length()) {
                                if (line.charAt(i + 1) == '"') {
                                    if (i + 2 < line.length() && line.charAt(i + 2) == '"') {//""".....
                                        out.append(line, i, i + 3);
                                        i += 3;
                                        inCell = true;
                                    } else {//""....
                                        throw new MyFileException(currentLine);
                                    }
                                } else {//".....
                                    out.append(line.charAt(i++));
                                    inCell = true;
                                }
                            } else {
                                throw new MyFileException(currentLine);
                            }
                        }
                    } else {
                        if (inCell) {
                            out.append(line.charAt(i));
                            i++;
                        } else {
                            throw new MyFileException(currentLine);
                        }
                    }
                }
                if (inCell) {
                    throw new MyFileException(currentLine);
                } else {
                    out.append(targetLineSeparator);
                }
                if (lastColumnCount != 0) {
                    isAlign = (lastColumnCount == currentColumnCount);
                }
                lastColumnCount = currentColumnCount;
            }
        }
        String outPath = Utils8.removeQuotes(l.get(1));
        Utils8.writeAsEncoding(out.toString(), outPath,
                Charset.forName(properties.getProperty("TargetEncoding", "utf-8")));
        if (summaryPW != null) {
            summaryPW.printf("\"%s\",\"%d\",\"%d\",\"%b\"\n", outPath, lineCnt, new File(outPath).length(), isAlign);
        }
    }
}
