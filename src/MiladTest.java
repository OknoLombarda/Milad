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
		List<Word> v = MiladTools.find("funnel");
		for (int i = 0; i < v.size(); i++) {
			System.out.println("(" + i + ")" + v.get(i));
		}
	}
}
