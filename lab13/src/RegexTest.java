import java.util.regex.*;
//Online regex test website: https://regex101.com
public class RegexTest {
	public static void main(String args[]) {
		String searchString = "", searchPattern = "";
		if (args.length == 2) {
			searchPattern = args[0];
			searchString = args[1];
			System.out.println(args[0]);
			System.out.println(args[1]);
		} else {
			System.exit(0);
		}
		Pattern p = Pattern.compile(searchPattern);
		Matcher m = p.matcher(searchString);
		boolean b = m.find();
		System.out.println("\nMatch found : " + b);
		while (b) {
			System.out.println("Match start : " + m.start());
			System.out.println("Match end : " + m.end());
			System.out.println("Match content : " + m.group(0));
			if (m.groupCount() != 0) {
				for (int i = 1; i <= m.groupCount(); i++) {
					System.out.println("Group " + i + " : " + m.group(i));
				}
			}
			b = m.find();
			if (b)
				System.out.println("\nMatch found : " + b);
		}
	}

}