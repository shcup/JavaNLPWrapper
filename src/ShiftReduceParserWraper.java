
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.shiftreduce.ShiftReduceParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;

public class ShiftReduceParserWraper {
	
	public static void main(String[] args) {
		String context = "Euro 2016 Final: Yuvraj Singh, Chris Gayle, others celebrate Portugal's victory";
		ShiftReduceParserWraper srparser = new ShiftReduceParserWraper();
		HashMap<String, TagIndex> np_hashmap = new HashMap<String, TagIndex>(); 
		HashMap<String, TagIndex> nnp_hashmap = new HashMap<String, TagIndex>(); 
		HashMap<String, TagIndex> vb_hashmap = new HashMap<String, TagIndex>();
		
		srparser.ParagraphPhraseParse(context, 0, np_hashmap, nnp_hashmap, vb_hashmap);
		System.out.println("np");
		srparser.Print(np_hashmap);
		System.out.println("nnp");
		srparser.Print(nnp_hashmap);
		System.out.println("vb");
		srparser.Print(vb_hashmap);
	}
	
    private String modelPath = "edu/stanford/nlp/models/srparser/englishSR.ser.gz";
    private String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
    
    private MaxentTagger tagger = new MaxentTagger(taggerPath);
    private ShiftReduceParser model = ShiftReduceParser.loadModel(modelPath);
    
    public class TagIndex {
    	public ArrayList<Pair<Integer, Integer>> index;
    	public TagIndex() {
    		index = new ArrayList<Pair<Integer, Integer>>();
    	}
    }
    
    public void Print(HashMap<String, TagIndex> hashmap) {
    	Iterator iter = hashmap.keySet().iterator();
    	while (iter.hasNext()) {
    		Object key = iter.next();
    		TagIndex index = hashmap.get(key);
    		System.out.println(key);
    		for (Pair<Integer, Integer> pair : index.index) {
    			System.out.print(pair.first + "_" + pair.second + " ");
    		}
    		System.out.println();
    	}
    }
    
    public void ParagraphPhraseParse(String text, int paragraph_idx,
    														HashMap<String, TagIndex> np_hashmap, 
    														HashMap<String, TagIndex> nnp_hashmap, 
    														HashMap<String, TagIndex> vb_hashmap) {
	    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));

	    int idx = 0;
	    for (List<HasWord> sentence : tokenizer) {
	      List<TaggedWord> tagged = tagger.tagSentence(sentence);
	      Tree tree = model.apply(tagged);
	      System.out.println(tree);
	      ArrayList<String> np_array = new ArrayList<String>();
	      ArrayList<String> nnp_array = new ArrayList<String>();
	      ArrayList<String> vb_array = new ArrayList<String>();
	      SentencePhraseParse(tree, np_array, nnp_array, vb_array);
	      
	      
	      for (String np : np_array) {
	    	  if (np_hashmap.containsKey(np)) {
	    		  np_hashmap.get(np).index.add(new Pair(paragraph_idx, idx));
	    	  } else {
	    		  np_hashmap.put(np, new TagIndex());
	    		  np_hashmap.get(np).index.add(new Pair(paragraph_idx, idx));
	    	  }
	      }
	      
	      for (String nnp : nnp_array) {
	    	  if (nnp_hashmap.containsKey(nnp)) {
	    		  nnp_hashmap.get(nnp).index.add(new Pair(paragraph_idx, idx));
	    	  } else {
	    		  nnp_hashmap.put(nnp, new TagIndex());
	    		  nnp_hashmap.get(nnp).index.add(new Pair(paragraph_idx, idx));
	    	  }
	      }
	      
	      for (String vb : vb_array) {
	    	  if (vb_hashmap.containsKey(vb)) {
	    		  vb_hashmap.get(vb).index.add(new Pair(paragraph_idx, idx));
	    	  } else {
	    		  vb_hashmap.put(vb, new TagIndex());
	    		  vb_hashmap.get(vb).index.add(new Pair(paragraph_idx, idx));
	    	  }
	      }
	      idx++;
	    }
    }
    
    private static String NP = "NP";
    private static String NNP = "NNP";
    private static String VBZ = "VBZ";
    private static String VBN = "VBN";
    private static String VBG = "VBG";
    private static String VB = "VB";
    
    private void SentencePhraseParse(Tree tree, ArrayList<String> np, ArrayList<String> nnp, ArrayList<String> vb) {
    	//ArrayList<Pair<String, Integer>> res = new ArrayList<Pair<String, Integer>>();
    	
    	if (tree.label().value().equals(NP)) {
    		ArrayList<Pair<ArrayList<String>, Boolean>>  words = new ArrayList<Pair<ArrayList<String>, Boolean>>();
    		words.add(new Pair(new ArrayList<String>(), false));
    		GenerateNP(tree, words, vb);
    		ConvertWordsToString(words, np, nnp);
    		return ;
    	}
    	
    	if (tree.label().value().equals(VBZ) || tree.label().value().equals(VBN) || 
    			tree.label().value().equals(VBG) || tree.label().value().equals(VB)) {
    		if (!tree.isLeaf() && tree.getChild(0).label() != null) {
    			vb.add(tree.getChild(0).label().value().toString());
    		}
    	}
    	
    	Tree[] kids = tree.children();
    	if (kids != null) {
    		for (Tree kid : kids) {
    			SentencePhraseParse(kid, np, nnp, vb);
    		}
    	}
    	
     	return;
    }
    
    // if in NP there are some other sub NP, it will only generate the words for lowest NP
    // and also it will seperate the NP and NNP
    private void GenerateNP(Tree tree, ArrayList<Pair<ArrayList<String>, Boolean>> words, ArrayList<String> vb) {
    	if (tree.isLeaf()) {
    		if (tree.label() != null) {
    			words.get(words.size() - 1).first.add(tree.label().value());
    		}
    		return;
    	}
    	
    	if (tree.label().value().equals(NP)) {
    		words.get(words.size() - 1).first.clear();
    		words.get(words.size() - 1).second = false;
    	}
    	
    	if (tree.label().value().equals(NNP)) {
    		words.get(words.size() - 1).second = true;
    	}
    	
    	if (tree.label().value().equals(VBZ) || tree.label().value().equals(VBN) || 
    			tree.label().value().equals(VBG) || tree.label().value().equals(VB)) {
    		if (!tree.isLeaf() && tree.getChild(0).label() != null) {
    			vb.add(tree.getChild(0).label().value().toString());
    		}
    	}
    	
    	Tree[] kids = tree.children();
    	if (kids != null) {
    		for (Tree kid : kids) {
    			GenerateNP(kid, words, vb);
    		}
    	}
    	
    	if (tree.label().value().equals(NP)) {
    		words.add(new Pair(new ArrayList<String>(), false));
    	}
    }
    
    private void ConvertWordsToString(ArrayList<Pair<ArrayList<String>, Boolean>> words, ArrayList<String> np, ArrayList<String> nnp) {
    	for (Pair<ArrayList<String>, Boolean> arraylist : words) {
    		if (arraylist.first.size() != 0) {
		    	StringBuilder sb = new StringBuilder();
		    	boolean has_charactor = false;
		    	for (int i = 0; i < arraylist.first.size(); ++i) {
		    		sb.append(arraylist.first.get(i));
		    		if (i != arraylist.first.size() - 1) {
		    			sb.append(" ");
		    		}
		    	}
		    	String phrase = sb.toString();
		    	if (phrase.length() == 1 && !Character.isLetter(phrase.charAt(0))) {
		    		continue;
		    	}
		    	if (arraylist.second == true) {
		    		nnp.add(phrase);
		    	} else {
		    		np.add(phrase);
		    	}
    		}
    	}
    }
}
