import org.apache.commons.csv.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.List;

/**
 * @author Shilong Li (Lori)
 * @project CS209A
 * @filename Server
 * @date 2021/4/28 10:28
 */
public class Server {
    static Connection con = null;
    static String[] HEADERS = {"Name", "Grade"};
    static String FILENAME = "StudentsGrade.csv";
    public static void main(String[] args) throws Exception {
        openDB(args[0]);
        loadData();

        int portNumber = 8888;
        PrintWriter out = null;
        BufferedReader in = null;
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is OK, is waiting for connect...");
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Have a connect");

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine, outputLine;
                // Wait for input
                if ((inputLine = in.readLine()) != null) {
                    String[] command = inputLine.split(" ");
                    String b = command[0];
//                    String response = "";
                    System.out.println("Received "+ inputLine);
                    switch (b){
                        case "NAME":
                            String name = inputLine.substring(inputLine.indexOf(b) + b.length() + 1);
                            getRecordByName(name,out);
                            break;
                        case "GRADE":
                            getRecordByGradeInterval(Integer.parseInt(command[1]), Integer.parseInt(command[2]),out);
                            break;
                        case "TOP":
                            getTop(Integer.parseInt(command[1]),out);
                            break;
                        default:
                            out.println("GoodBye");
                            break;
                    }
                    System.out.println("Command proceed");
//                    out.println(response);
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println(
                    "Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }

        }

    }
    private static void openDB(String dbPath) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.err.println("Cannot find the driver.");
            System.exit(1);
        }
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            con.setAutoCommit(false);
            System.err.println("Successfully connected to the database.");
        } catch (Exception e) {
            System.err.println("openDB" + e.getMessage());
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception ignored) {
            }
        }
    }

    public static List<CSVRecord> getData(String fileName) throws Exception {
        Reader in = new FileReader(fileName);
        CSVParser parser = CSVFormat.RFC4180.withHeader(HEADERS).withFirstRecordAsHeader().parse(in);
        List<CSVRecord> records = parser.getRecords();
        in.close();
        return records;
    }

    public static void loadData() throws Exception {
        if (con != null) {
            try {
                Statement statement = con.createStatement();
                statement.setQueryTimeout(30); // set timeout to 30 sec.
                statement.executeUpdate("drop table if exists StudentsGrade;");
                // create table
                statement.executeUpdate("create table StudentsGrade(name string,grade int);");
                // insert records
                PreparedStatement preparedStatement = con.prepareStatement("insert into StudentsGrade values(?,?);");
                List<CSVRecord> records = getData(FILENAME);
                for (CSVRecord record : records) {
                    preparedStatement.setString(1, record.get(0));
                    preparedStatement.setInt(2, Integer.parseInt(record.get(1)));
                    preparedStatement.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                System.err.println("loadData:" + e.getMessage());
            }
        }
    }

    public static void getRecordByName(String name,PrintWriter out) {
        if (con != null) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("select * from StudentsGrade where name=?;");
                preparedStatement.setQueryTimeout(30); // set timeout to 30 sec.
                // insert records
                preparedStatement.setString(1, name);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    out.println(rs.getInt("grade"));
                }
                rs.close();
                con.commit();
            } catch (SQLException e) {
                out.println("select data" + e.getMessage());
            }
        }
    }

    public static void getRecordByGradeInterval(int lowerBound, int upperBound,PrintWriter out) {
        if (con != null) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("select * from StudentsGrade where grade>=? and grade <=?;");
                preparedStatement.setQueryTimeout(30); // set timeout to 30 sec.
                // insert records
                preparedStatement.setInt(1, lowerBound);
                preparedStatement.setInt(2, upperBound);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    out.printf("%s,%d\n", rs.getString("name"), rs.getInt("grade"));
                }
                rs.close();
                con.commit();
            } catch (SQLException e) {
                out.println("select data" + e.getMessage());
            }
        }
    }

    public static void getTop(int top,PrintWriter out) {
        if (con != null) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("select * from StudentsGrade order by grade desc;");
                preparedStatement.setQueryTimeout(30);
                ResultSet rs = preparedStatement.executeQuery();
                int max = 0;
                int k = 0;
                while (rs.next()) {
                    int grade = rs.getInt("grade");
                    if (max != grade) {
                        k++;
                        max = grade;
                        if (k > top) {
                            break;
                        } else {
                            out.printf("%s,%d\n", rs.getString("name"), grade);
                        }
                    } else {
                        out.printf("%s,%d\n", rs.getString("name"), grade);
                    }
                }
                rs.close();
                con.commit();
            } catch (SQLException e) {
                out.println("select data" + e.getMessage());
            }
        }
    }
}
