import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class Stemmer {
	public static void main(String[] args) throws Exception{
		String file="G:\\tmp\\test\\test.txt";
		Stemmer stem=new Stemmer();
		ArrayList<String> stemword=new ArrayList<String>();
		stem.stemming(file,stemword);
		stem.print(stemword);
	}
	public void stemming(String file,ArrayList<String> stemword) throws Exception{
		Class stemClass = Class.forName("englishStemmer");
	    SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();
		Reader reader= new InputStreamReader(new FileInputStream(file));

		StringBuffer input = new StringBuffer();
		int repeat = 1;
		int character;
		while ((character = reader.read()) != -1) {
		    char ch = (char) character;
		    if (Character.isWhitespace((char) ch)) {
			if (input.length() > 0) {
			    stemmer.setCurrent(input.toString());
			    for (int i = repeat; i != 0; i--) {
				stemmer.stem();
			    }
			    stemword.add(stemmer.getCurrent());
			    input.delete(0, input.length());
			}
		    } else {
			input.append(Character.toLowerCase(ch));
		    }
		}
	    }
	public void print(ArrayList<String> stemword){
		for(int i = 0;i<stemword.size();i++){
			System.out.println(""+stemword.get(i));
			}
	}
}
