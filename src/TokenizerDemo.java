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
		String context = "Priyanka who has earned laurels for her character in the series Quantico will now make a Hollywood debut with ¡®Baywatch¡¯.";
		TokenizerDemo token=new TokenizerDemo();
		token.Token(context);
	}
  public void Token (String context) {
	    TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    Tokenizer<CoreLabel> tok =tokenizerFactory.getTokenizer(new StringReader(context));
	    List<CoreLabel> sentence = tok.tokenize();
//	   System.err.println("The words of the sentence:");
	   for (Label lab : sentence) {
	      if (lab instanceof CoreLabel) {
	        System.out.println(((CoreLabel) lab).toString());
	      } else {
	        System.out.println(lab);
	      }
  }
	   return;
  }
}