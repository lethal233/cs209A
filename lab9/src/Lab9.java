import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.csv.*;

/**
 * @author Shilong Li (Lori)
 * @project CS209A
 * @filename Lab9
 * @date 2021/4/14 10:44
 */
public class Lab9 {
    static Connection con = null;
    static String[] HEADERS = {"Name", "Grade"};
    static String FILENAME = "StudentsGrade.csv";

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

    public static void getRecordByName(String name) {
        if (con != null) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("select * from StudentsGrade where name=?;");
                preparedStatement.setQueryTimeout(30); // set timeout to 30 sec.
                // insert records
                preparedStatement.setString(1, name);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getInt("grade"));
                }
                rs.close();
                con.commit();
            } catch (SQLException e) {
                System.err.println("select data" + e.getMessage());
            }
        }
    }

    public static void getRecordByGradeInterval(int lowerBound, int upperBound) {
        if (con != null) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("select * from StudentsGrade where grade>=? and grade <=?;");
                preparedStatement.setQueryTimeout(30); // set timeout to 30 sec.
                // insert records
                preparedStatement.setInt(1, lowerBound);
                preparedStatement.setInt(2, upperBound);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    System.out.printf("%s,%d\n", rs.getString("name"), rs.getInt("grade"));
                }
                rs.close();
                con.commit();
            } catch (SQLException e) {
                System.err.println("select data" + e.getMessage());
            }
        }
    }

    public static void getTop(int top) {
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
                            System.out.printf("%s,%d\n", rs.getString("name"), grade);
                        }
                    } else {
                        System.out.printf("%s,%d\n", rs.getString("name"), grade);
                    }
                }
                rs.close();
                con.commit();
            } catch (SQLException e) {
                System.err.println("select data" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        openDB(args[0]);
        loadData();
        System.out.println("Input the mode: ");
        String a = in.nextLine();
        String b = a.split(" ")[0];
        switch (b) {
            case "NAME":
                String name = a.substring(a.indexOf(b) + b.length() + 1);
                getRecordByName(name);
                break;
            case "GRADE":
                getRecordByGradeInterval(Integer.parseInt(a.split(" ")[1]), Integer.parseInt(a.split(" ")[2]));
                break;
            case "TOP":
                getTop(Integer.parseInt(a.split(" ")[1]));
                break;
        }
        closeDB();
    }
}