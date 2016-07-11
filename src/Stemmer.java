import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class Stemmer {
	public static void main(String[] args) throws Exception{
		String file="G:\\tmp\\test\\test.txt";
		Stemmer stem=new Stemmer();
		stem.stemming(file);
	}
	private String classname="englishStemmer";
//	@SuppressWarnings("resource")
	public void stemming(String file) throws Exception{
		Class stemClass = Class.forName(classname);
		 SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();
		Reader reader;
		reader = new InputStreamReader(new FileInputStream(file));
		reader = new BufferedReader(reader);
		StringBuffer input = new StringBuffer();

	        OutputStream outstream;
	String fout="G:\\tmp\\test\\out.txt";
		if (file.length()> 2) {
	            if (file.length() >= 4 && file.equals(fout)) {
	              outstream = new FileOutputStream(fout);
	            } else {
	                usage();
	                return;
	            }
		} else {
		    outstream = System.out;
		}
		Writer output = new OutputStreamWriter(outstream);
		output = new BufferedWriter(output);

		int repeat = 1;
		if (file.length() > 4) {
		    repeat = Integer.parseInt(file);
		}

		Object [] emptyArgs = new Object[0];
		int character;
		while ((character = reader.read()) != -1) {
		    char ch = (char) character;
		    if (Character.isWhitespace((char) ch)) {
			if (input.length() > 0) {
			    stemmer.setCurrent(input.toString());
			    for (int i = repeat; i != 0; i--) {
				stemmer.stem();
			    }
			    output.write(stemmer.getCurrent());
			    output.write('\n');
			    input.delete(0, input.length());
			}
		    } else {
			input.append(Character.toLowerCase(ch));
		    }
		}
		output.flush();	
		    }
	private void usage() {
		// TODO Auto-generated method stub
		System.err.println("Usage: TestApp <algorithm> <input file> [-o <output file>]"); 
	}

}
