import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by lingalone on 2016/5/30.
 */
/*
* information of term and posting
* */
public class Term {
    public String                   term;               //term value;
    public Integer                  termFreqCol;        //tne number of term t in collection;
    public Map<Integer,ArrayList>   postings;           //the posting of term in each document;
    //     Map<docID  ,postingList>
    //public Map<Integer,Integer>     termFreqDoc;      //the number of term t in document d;
    //equal to postingList Length;
    //public Integer                  docFreq;          //the number of documents int collection that contain a term t;
    //equal to postings size;
    Term(){
        postings = new TreeMap<Integer, ArrayList>();
    }
}
