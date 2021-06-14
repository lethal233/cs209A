import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CharsetConvertor {

	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream(args[0]);
			InputStreamReader isr = new InputStreamReader(fis, args[1]);
			BufferedReader bReader = new BufferedReader(isr);
			StringBuilder sBuilder = new StringBuilder();
			while (true) {
				String str = bReader.readLine();
				if (null == str) {
					break;
				}
				byte[] bytes = str.getBytes(args[2]);
				int i = 0;
				if (args[2].equals("UTF-16")) {
					i = 2;
				}
				for (; i < bytes.length; i++) {
					sBuilder.append(String.format("%02X ", bytes[i]));
					System.out.printf("%02X ", bytes[i]);

				}

				sBuilder.append("\n");
			}

			isr.close();

			BufferedWriter bWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(args[2] + "_" + args[0])));
			bWriter.write(sBuilder.toString());

			bWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
