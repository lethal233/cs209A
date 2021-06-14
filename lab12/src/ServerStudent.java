import javafx.util.Pair;
// Please run the code under Java 8
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

// Please run the code under Java 8
public class ServerStudent {
    final static String INVALID = "Invalid command";
    final static String gradeRegex = "^[0-9]+ [0-9]+$";
    final static String topRegex = "^[0-9]+$";
    String FILE_NAME = "StudentsGrade.csv";
    HashMap<String, Integer> gradeMap = new HashMap<>();
    List<Pair<String, Integer>> orderedList = new ArrayList<>();

    public void readFile() throws Exception {
        File file = new File(FILE_NAME);
        BufferedReader  reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        reader.readLine();
        String s = reader.readLine();
        while (s != null) {
            String[] strs = s.replace("\n", "").split(",");
            gradeMap.put(strs[0], Integer.valueOf(strs[1]));
            orderedList.add(new Pair<>(strs[0], Integer.parseInt(strs[1])));
            s = reader.readLine();
        }

       orderedList.sort(Comparator.comparing(Pair::getValue));

    }

    public String handleCommand(String s) {
        s =  s.replace("\n", "");
        s = s.trim();

        int idx = s.indexOf(' ');
        if (idx == -1) {
            return INVALID;
        }

        String command = s.substring(0, idx);
        String content = s.substring(idx + 1);

        String result = "";
        switch (command) {
            case "NAME" : result = handleNameCommand(content); break;
            case "GRADE" : result = handleGradeCommand(content); break;
            case "TOP" : result = handleTopCommand(content); break;
            default: result = "Invalid command";
        }
        return result;
    }

    public String handleTopCommand(String s) {
        boolean match = Pattern.matches(topRegex, s);
        if (match) {
            int top = Integer.parseInt(s);
            if (top > 0 && orderedList.size() > 0) {
                StringBuilder sb = new StringBuilder();

                int t = top;
                int index = orderedList.size() - 1;
                int value = orderedList.get(index).getValue();
                while (t > 0 && index >= 0) {

                    Pair<String, Integer> pair = orderedList.get(index);
                    int currentValue = pair.getValue();

                    if (currentValue == value) {
                        sb.append(pair.getKey()).append(",").append(pair.getValue()).append("\n");
                        index--;
                    }
                    else {
                        value = currentValue;
                        t--;
                    }
                }
                return sb.toString();
            }
            else {
                return INVALID;
            }
        }

        return INVALID;
    }

    public String handleNameCommand(String s) {

        Integer grade = gradeMap.get(s);
        if (grade == null)
            return "Student does not exist";
        return grade.toString();
    }

    public String handleGradeCommand(String s) {
        boolean match = Pattern.matches(gradeRegex, s);

        if (match) {
            String[] range = s.split(" ");
            StringBuilder sb = new StringBuilder();
            int lower = Integer.parseInt(range[0]);
            int upper = Integer.parseInt(range[1]);

            if (lower > upper)
                return INVALID;
            for (Map.Entry<String, Integer>  entry: gradeMap.entrySet()) {
                int grade = entry.getValue();
                if ( lower <= grade && grade <= upper) {
                    sb.append(entry.getKey() + "," + grade + "\n");
                }
            }
            return sb.toString();
        }

        return INVALID;
    }


    public static void main(String[] args) throws IOException {
        ServerStudent server = new ServerStudent();
        try {
                server.readFile();
        } catch (IOException e) {
            System.out.println("Failed to load file");
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ServerSocket socket = new ServerSocket(6324);
        while (true) {
            try {
                Socket clientSocket = socket.accept();
                System.out.println("Accepted");
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

                String s = reader.readLine();

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println(server.handleCommand(s));

                System.out.println("Command processed");
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
