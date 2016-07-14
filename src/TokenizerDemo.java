import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

public class TokenizerDemo {
	public static void main(String[] args){
		String context = "Euro 2016 Final: Yuvraj Singh, Chris Gayle, others celebrate Portugal's victory";
		TokenizerDemo token=new TokenizerDemo();
		ArrayList<String> tokenword=new ArrayList<String>();
		token.Token(context,tokenword);
		token.print(tokenword);
	}
  public void Token (String context,ArrayList<String> tokenword) {
	    TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    Tokenizer<CoreLabel> tok =tokenizerFactory.getTokenizer(new StringReader(context));
	    List<CoreLabel> sentence = tok.tokenize();
//	   System.err.println("The words of the sentence:");
	   for (Label lab : sentence) {
	      if (lab instanceof CoreLabel) {
	    	  tokenword.add(lab.value());
//	        System.out.println(((CoreLabel) lab).toString());
	      } 
  }
	   return;
  }
  public void print(ArrayList<String> tokenword){
	  for(int i = 0;i<tokenword.size();i++){
			System.out.println(""+tokenword.get(i));}
//	  System.out.println(tokenword);
  }
}