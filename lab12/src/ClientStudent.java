import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// Please run the code under Java 8
public class ClientStudent{

    public static void main (String[] args) throws IOException {


            while (true) {
                try {
                    System.out.print("Enter the command: ");
                    Scanner scan = new Scanner(System.in);
                    String s = scan.nextLine();
                    Socket socket = new Socket("localhost", 6324);
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                    writer.println(s);
                    String res;
                    while ((res = reader.readLine()) != null) {
                        System.out.println(res);
                    }
                } catch (IOException e) {
                    System.out.println("IO Exception happened");
                    System.exit(-1);
                }

            }

    }
}
