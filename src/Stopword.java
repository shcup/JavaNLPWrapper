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
		String context = "Euro 2016 Final: Yuvraj Singh, Chris Gayle, others celebrate Portugal's victory";
		Stopword st=new Stopword();
		ArrayList<String> wordlist=new ArrayList<String>();
		st.Deword(context,wordlist);
		st.print(wordlist);
	}
    private String sw=null;
	public void Deword(String context,ArrayList<String> wordlist) throws IOException {
		// TODO Auto-generated method stub
		//loading the stopwords.
	  	File f1 = new File("src\\stopwords.txt");
	  	BufferedReader br1=new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
	  	List<String> stopword = new ArrayList<String>();	  	
	  	while ((sw = br1.readLine()) != null) {
	            stopword.add(new String(sw));		
	  		}
		    TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		    Tokenizer<CoreLabel> tok =tokenizerFactory.getTokenizer(new StringReader(context));
		    List<CoreLabel> sentence = tok.tokenize();
		   for (Label lab : sentence) {
		    	wordlist.add(lab.value());
		    }

			for(int i = 0;i<wordlist.size();i++){
				for(int j =0;j<stopword.size();j++){
					if(wordlist.get(i).equals(stopword.get(j))){
//						System.out.println(list.get(i)+" "+stop[j]+"ɾ���ɹ�");
						wordlist.remove(i);
						i--;
						break;	
					}
				}
			}
			
	}
public void print(ArrayList<String> list){
	for(int i = 0;i<list.size();i++){
		System.out.println(""+list.get(i));}
}
}
