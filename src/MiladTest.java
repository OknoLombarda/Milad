import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.milad.MiladTools;
import com.milad.Phrase;
import com.milad.Word;

public class MiladTest {
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		MiladTools.readData();
		String s = "";
		Scanner in = new Scanner(System.in);
	//	s = in.nextLine();
		List<Word> results = MiladTools.find(s);
		results.forEach(System.out::println);
		in.close();
	}
}
