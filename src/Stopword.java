import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

public class Stopword {
	public static void main(String[] args) throws IOException{
		String context = "Priyanka who has earned laurels for her character in the series Quantico will now make a Hollywood debut with ¡®Baywatch¡¯.";
		Stopword st=new Stopword();
		st.Deword(context);
	}
    private String sw=null;
	public void Deword(String context) throws IOException {
		// TODO Auto-generated method stub
		//loading the stopwords.
	  	File f1 = new File("G:\\tmp\\stopwords.txt");
	  	BufferedReader br1=new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
	  	List<String> stopword = new ArrayList<String>();	  	
	  	while ((sw = br1.readLine()) != null) {
	            stopword.add(new String(sw));		
	  		}
	  	List<String> list = new ArrayList<String>();
		    TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		    Tokenizer<CoreLabel> tok =tokenizerFactory.getTokenizer(new StringReader(context));
		    List<CoreLabel> sentence = tok.tokenize();
		   for (Label lab : sentence) {
		    	list.add(lab.value());
		    }

			for(int i = 0;i<list.size();i++){
				for(int j =0;j<stopword.size();j++){
					if(list.get(i).equals(stopword.get(j))){
//						System.out.println(list.get(i)+" "+stop[j]+"É¾³ý³É¹¦");
						list.remove(i);
						i--;
						break;	
					}
				}
			}
			for(int i = 0;i<list.size();i++){
			System.out.println(""+list.get(i));}
	}

}
