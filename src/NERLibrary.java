import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.javatuples.Quartet;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.Triple;

public class NERLibrary {
	
	public static void main(String[] args) throws ClassCastException, ClassNotFoundException, IOException{
		String context = "New Delhi: Around 89 per cent of the country has received normal and excess rainfall, owing to a good amount of monsoon in several parts, while large parts of Gujarat have recorded deficiency of more than half.Overall, the country has recorded 254 mm of rainfall from June 1 to July 10, as against 251 mm, which is one per cent more.";
		NERLibrary nl=new NERLibrary();
		nl.NERLibrary(7);		
//		ArrayList<NameEntity> wordIndex=new ArrayList<NameEntity>();
		List<Object> ner=new ArrayList<Object>();
		nl.GetEntity(context,ner);
		nl.Print(ner);
	}
	private AbstractSequenceClassifier<CoreLabel> classifier;
	private List<Triple<String,Integer,Integer>> triples;
	private int length;
	public void NERLibrary(int option) throws ClassCastException, ClassNotFoundException, IOException {
		if (option == 3) {
			classifier = CRFClassifier.getClassifier("english.nowiki.3class.distsim.crf.ser.gz");
		} else if (option==4){
			classifier=CRFClassifier.getClassifier("english.conll.4class.distsim.crf.ser.gz");
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
	public void GetEntity(String context,List<Object> list){
       classifier.classifyToString(context, "slashTags", false);
       classifier.classifyWithInlineXML(context);
       length=context.length();
	       triples = classifier.classifyToCharacterOffsets(context);
	        for (Triple<String, Integer, Integer> trip : triples) {
	        	Quartet<String,String,Integer,Integer> four
	        	=new Quartet<String,String,Integer,Integer>(trip.first(), context.substring(trip.second(), trip.third()), trip.second(), trip.third()) ;
	        	list.add(four);
	        }
	}
	public void GetEntity(String context, int paragraph_idx, HashMap<String, MatchType> entitys_people, HashMap<String, MatchType> entitys_location, HashMap<String, MatchType> entitys_organization) {
       classifier.classifyToString(context, "slashTags", false);
       classifier.classifyWithInlineXML(context);
       length = context.length();
       triples = classifier.classifyToCharacterOffsets(context);
       for (Triple<String, Integer, Integer> trip : triples) {
    	   if (trip.first == "Person") {
    		   String name = context.substring(trip.second(), trip.third());
    		   if (entitys_people.containsKey(name)) {
    			   entitys_people.get(name).match.add(new Pair(paragraph_idx, 0));
    		   } else {
    			   MatchType matchType = new MatchType();
    			   matchType.match.add(new Pair(paragraph_idx, 0));
    			   entitys_people.put(name, matchType);
    		   }
    	   } else if (trip.first == "Location") {
    		   String name = context.substring(trip.second(), trip.third());
    		   if (entitys_location.containsKey(name)) {
    			   entitys_location.get(name).match.add(new Pair(paragraph_idx, 0));
    		   } else {
    			   MatchType matchType = new MatchType();
    			   matchType.match.add(new Pair(paragraph_idx, 0));
    			   entitys_location.put(name, matchType);
    		   }   		   
    	   } else if (trip.first == "Organization") {
    		   String name = context.substring(trip.second(), trip.third());
    		   if (entitys_organization.containsKey(name)) {
    			   entitys_organization.get(name).match.add(new Pair(paragraph_idx, 0));
    		   } else {
    			   MatchType matchType = new MatchType();
    			   matchType.match.add(new Pair(paragraph_idx, 0));
    			   entitys_organization.put(name, matchType);
    		   }     		   
    	   }
       }
	}
	public void Print(List<Object> list){
//		index.iterator().toString();
		System.out.println("全文共"+length+"个字符");	
		for(int i=0;i<list.size();i++){
		System.out.println(list.get(i));}
	}
}