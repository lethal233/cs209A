//
//   Lab1.java
//
//   You should use this program for testing your Translit class.
//   To run it in a console:
//      $ java Lab1 <name of the file to convert>
//
//   To run it from Eclipse you need first to go to
//       Run/Run Configurations...
//   then click on the tab "(x)= Arguments" and enter the full access
//   path to the file in the "Program arguments:" entry field.
//

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// Define class Translit here.
// You can also define it as a public class in a separate file named
// Translit.java
class Translit {
    private Map<Character, String> hm = new HashMap<>();


    public Translit() {
    }

    public String convert(String russian_text) {
        StringBuffer sb = new StringBuffer();
//        System.out.println(russian_text.length());

        readMap();
        for (int j = 0; j < russian_text.length(); j++) {
            if (russian_text.charAt(j) == 0) {
                break;
            }
            if (hm.containsKey(russian_text.charAt(j))) {
                sb.append(hm.get(russian_text.charAt(j)));
            } else {
                sb.append(russian_text.charAt(j));
            }
        }
        return sb.toString();
    }

    private void readMap() {

        String content;
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream("translit_table.txt"), StandardCharsets.UTF_8)) {
            char[] cbuf = new char[65535];
            int file_len = isr.read(cbuf);
            content = String.valueOf(cbuf);
            String[] st = content.split("\n");
            for (String s : st) {
                String value = s.substring(11, s.length() - 1);
                hm.put(s.charAt(1), value);
                hm.put(s.charAt(6), value);
            }
        } catch (IOException ignored) {
        }
    }
}

public class Lab1 {
    static String fileContent = new String("");

    // This method reads the contents of a file into a String.
    // It specifies that the characters in the file are encoded
    // with the UTF-8 encoding scheme (this is the standard on the Web
    // and on Linux machines; Windows machines use a different default
    // encoding scheme)
    // We will see files in detail later in the course.
    private static void readFile(String fileName) {
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8)) {
            char[] cbuf = new char[65535];
            int file_len = isr.read(cbuf);
            fileContent = String.valueOf(cbuf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // The program takes the name of the file from the command line.
        // Wen it runs, it finds command line parameters into the args array.
        if (args.length > 0) {
            try {
                // Load the content of the file in memory
                readFile(args[0]);
                // Display what has been read for control.
                System.out.println("Input:");
                System.out.println(fileContent);
                // Create a Translit object
                Translit tr = new Translit();
                // Convert and display. It will all be in lowercase.
                System.out.println("Output:");
                System.out.println(tr.convert(fileContent));
            } catch (Exception e) {
                // If anything goes wrong


            }
        }

    }

}
