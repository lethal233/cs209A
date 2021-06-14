import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shilong Li (Lori)
 * @project CS209A
 * @filename CodeBreaker_1
 * @date 2021/3/3 10:10
 */
public class CodeBreaker_1 {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Length of arguments is wrong.");
        } else {
            String content;
            Map<String, Integer> hm = new HashMap<>();
            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8)) {
                char[] cbuf = new char[65535];
                int file_len = isr.read(cbuf);
                content = String.valueOf(cbuf);
                for (int i = 0; i < file_len - 2; i++) {
                    if (!hm.containsKey(content.substring(i, i + 3))) {
                        hm.put(content.substring(i, i + 3), 1);
                    } else {
                        int tmp = hm.get(content.substring(i, i + 3));
                        hm.put(content.substring(i, i + 3), tmp + 1);
                    }
                }
                int b = 0;
                String c = "";
                for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                    if (entry.getValue() > b) {
                        b = entry.getValue();
                        c = entry.getKey();
                    }
                }
                System.out.println(c);

            } catch (IOException e) {
                System.out.println("The file name is wrong.");
            }
        }
    }
}
