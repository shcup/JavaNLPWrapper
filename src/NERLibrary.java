import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;

public class NERLibrary {
	
	public static void main(String[] args) throws ClassCastException, ClassNotFoundException, IOException{
		String context = "Priyanka who has earned laurels for her character in the series Quantico will now make a Hollywood debut with ‘Baywatch’.";
		NERLibrary nl=new NERLibrary();
		nl.NERLibrary(7);		
		HashMap<String,String> word=new HashMap<String,String>();
		ArrayList<Triple<String, Integer, Integer>> wordIndex=new ArrayList<Triple<String, Integer, Integer>>();
		nl.GetEntity(context,wordIndex,word);
		nl.Print(wordIndex,word);
	}
	private AbstractSequenceClassifier<CoreLabel> classifier;
	private List<Triple<String,Integer,Integer>> triples;
	private int length;
	public void NERLibrary(int option) throws ClassCastException, ClassNotFoundException, IOException {
		if (option == 3) {
			classifier = CRFClassifier.getClassifier("english.all.3class.distsim.crf.ser.gz");
		} else if (option==4){
			classifier=CRFClassifier.getClassifier("english.all.4class.distsim.crf.ser.gz");
		}else if (option==7){
			classifier=CRFClassifier.getClassifier("english.muc.7class.distsim.crf.ser.gz");
		}	
	} 
	public class TagIndex {
    	public ArrayList<Triple<String,Integer, Integer>> index;
    	public TagIndex() {
    		index = new ArrayList<Triple<String,Integer,Integer>>();
    	}
    }
	
	public void GetEntity(String context,ArrayList<Triple<String,Integer, Integer>> index,HashMap<String,String> word){
       classifier.classifyToString(context, "slashTags", false);
       classifier.classifyWithInlineXML(context);
       length=context.length();
	       triples = classifier.classifyToCharacterOffsets(context);
	        for (Triple<String, Integer, Integer> trip : triples) {
	        	index.add(trip);
	        	word.put(trip.first(),context.substring(trip.second(),trip.third()));
//	          System.out.printf("%s over character offsets [%d, %d] in sentence is :"+context.substring(trip.second(), trip.third())+".%n",
//	                  trip.first(), trip.second(), trip.third);//(分类字段，起始位置，终止位置，第几个句子)
	        }
	}
	public void Print(ArrayList<Triple<String,Integer, Integer>> index,HashMap<String,String> word){
//		index.iterator().toString();
		System.out.println("全文共"+length+"个字符");
		System.out.println(index);
		System.out.println(word);
	}
}