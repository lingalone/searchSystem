import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by lingalone on 2016/5/30.
 */
public class Index {
    public Map<String, Term> index;        //index of collection
    public Integer collectionSize;         //the number of documents in collection;
    public ArrayList stopList;             //stop word list
    public ArrayList eachDocLength;
    public Stemmer stemmer;                //a stemmer obj
    public MyFile file;
    public String collectionPath;
    public ArrayList docTitle;
    Index(){}
    Index(String docDirPath, String stopWordFile){
        collectionPath = docDirPath;
        file = new MyFile();
        ArrayList fileList = file.getFileList(docDirPath);
        //System.out.println(fileList);
        stemmer = new Stemmer();
        createStop(stopWordFile);
        createIndex(fileList,fileList.size());
    }

    /*
    * (3)	使用倒排索引
    * function    :   create a index;
    *   parameter   :
    *                   filePath (ArrayList)    //file list in dir
    *                   numOfFile (Integer)     //list size
    * */
    public void createIndex(ArrayList filePath, Integer numOfFile){

        index = new TreeMap<String, Term>();
        eachDocLength = new ArrayList();
        docTitle = new ArrayList<String>();
        collectionSize = numOfFile;
        int docID = 0;
        try {
            for(docID = 0; docID < numOfFile; docID++){
                Integer wordIndex = 0;                              //the index of word in document
                File file = new File((String) filePath.get(docID)); //get a document file
                //System.out.println(filePath.get(i));
                InputStream is = new FileInputStream(file);
                BufferedReader read = new BufferedReader(new InputStreamReader(is));
                String temp = null;
                while ((temp = read.readLine()) != null) {
                    docTitle.add(temp);
                    /*deal with the words*/
                    //System.out.println(temp);
                    //Turn uppercase lowercase
                    temp = upperChangeLower(temp);
                    //System.out.println(temp);
                    temp = StringFilter(temp);
                    //System.out.println(temp);
                    String[] words = temp.split(" ");
                    for (String word : words) {
                        //Stop word choose
                        if(!isStop(word)) {
                            word = word.replaceAll("[^0-9a-zA-Z-.]+","");
                            //Stemmer
                            if(word.length()>1) {
                                word = stemming(word);
                                wordIndex++;
                                Term tempTerm;
                                ArrayList tempPostingList;
                                // term not in the index;
                                if (!index.containsKey(word)) {
                                    tempTerm = new Term();
                                    tempPostingList = new ArrayList();
                                    tempTerm.term = word;
                                } else {
                                    tempTerm = index.get(word);
                                    if (tempTerm.postings.containsKey(docID)) {
                                        tempPostingList = tempTerm.postings.get(docID);
                                    } else {
                                        tempPostingList = new ArrayList();
                                        tempTerm.term = word;
                                    }
                                }//if-else end;
                                tempPostingList.add(wordIndex);
                                tempTerm.postings.put(docID, tempPostingList);
                                //System.out.println(word);
                                index.put(word, tempTerm);
                            }
                        }//if isStop end
                    }//second for loop end;
                }//while loop end;
                eachDocLength.add(wordIndex);
                is.close();
                read.close();
            }//first for loop end;
        }catch (IOException e) {
            //System.out.println("error in read file");
        }//try-catch end;
    }//fun end;

    /*
    *   function    :   filter stop words create stop list
    *   parameter   :
    *                   filePath (ArrayList)    //file list in dir
    * */
    public void createStop(String filePath){
        stopList = new ArrayList<String>();
        try {
            File file = new File(filePath);
            //System.out.println(filePath);
            InputStream is = new FileInputStream(file);
            BufferedReader read = new BufferedReader(new InputStreamReader(is));
            String temp;
            while ((temp = read.readLine()) != null)
                stopList.add(temp);
            read.close();
            is.close();
        }catch (IOException e) {
            //System.out.println("error in read file");
        }
    }

    /*
    *  (2)和停用词（stop words）过滤
    *   function    :   filter stop words
    *   parameter   :
    *                   term (String)    //file list in dir
    *   return      :   true (is stopword) false(not)
    * */
    public Boolean isStop(String Term){
        if(stopList.contains(Term))
            return true;
        else
            return false;
    }

    /*

    *  (2)	支持对查询串和文档中的token的stemming操作
    *  function    :   Stemmer process
    *   parameter   :
    *                   term (String)    //file list in dir
    *   return      :   term after stemming
    * */
    public String stemming(String Term){
        if ((Term != null)) {
            stemmer.add(Term.toCharArray(), Term.length());
            stemmer.stem();
            return stemmer.toString();
        }
        return  Term;
    }

    /*
        *   function    :   change upper to lower
        *   parameter   :
        *                   term (String)    //file list in dir
        *   return      :   term after changing
        * */
    public String upperChangeLower(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }
    /*
    *   (2)	支持对查询串和文档中的token的大小写转换
    *   function    :   filter str like ;:., ...etc
    *   parameter   :
    *                   term (String)    //file list in dir
    *   return      :   term after filer
    * */
    public static String StringFilter(String str)throws PatternSyntaxException {
        String dest = "";
        if (str != null) {
            Pattern p= Pattern.compile("[!,\":?{}@#$%^&*();<>]|.$");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            p = Pattern.compile("\\s+");
            m = p.matcher(dest);
            dest = m.replaceAll(" ");
        }
        return dest;
    }
    /*
    *   function    :   show index table
    *   parameter   :
    *   return      :
    * */
    public  void showTable(){
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            // entry的输出结果如key0=value0等
            Map.Entry entry =(Map.Entry) it.next();
            Object key = entry.getKey();
           // System.out.println(key);
            Term term = (Term)entry.getValue();

            StringBuilder sb = new StringBuilder();
            sb.append("term    ：" + term.term + "\n");
            //sb.append("        freq：" + term.getDocFreq() + " ");
            sb.append("postings：");

            Iterator it2 = term.postings.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry entry2 =(Map.Entry) it2.next();
                Object index2 = entry2.getKey();
                sb.append(" "+index2+"<");
                ArrayList postingList = (ArrayList)entry2.getValue();
                for(int i=0;i<postingList.size();i++){
                    sb.append(postingList.get(i)+" ");
                }
                sb.append(">");
            }
            System.out.println(sb);
        }
    }
    /*
    *   (4)	使用TF-IDF作为权重的计算方式
    *   (5)	使用余弦相似度作为<query,doc>的相关性分值的计算方式
    *   return      :
    * */
    public ArrayList Tf_idf_Weight(String query){
        query = upperChangeLower(query);
        String[] queryWords = query.split(" ");
        //result
        ArrayList scoreSet = new ArrayList();
        //temp
        ArrayList[] tempList = new ArrayList[collectionSize];
        for(int j=0;j<collectionSize;j++)
            tempList[j] = new ArrayList();

        Double score = 0.0;
        for (String word : queryWords) {
            if(!isStop(word)) {
                word = word.replaceAll("[^0-9a-zA-Z-.]+", "");
                //Stemmer
                if (word.length() > 1) {
                    word = stemming(word);
                    if (index.containsKey(word)) {
                        Term tempTerm = index.get(word);
                        double idft = Math.log10((double) collectionSize / tempTerm.postings.size());
                        //System.out.println(idft);
                        for (int i = 0; i < collectionSize; i++) {
                            score = 0.0;
                            if (tempTerm.postings.containsKey(i)) {
                                ArrayList tempPosting = tempTerm.postings.get(i);
                                int tf = tempPosting.size();
                                score = idft * tf;
                            }
                            tempList[i].add(score);
                        }//for
                    }
                }
            }
        }
        for(int j=0;j<collectionSize;j++) {
            score = 0.0;
            for (int i = 0; i < tempList[j].size(); i++) {
                double temp = (double)tempList[j].get(i);
                score += temp;
            }
            //除以文档长度

            int len = (int)eachDocLength.get(j);
            scoreSet.add(score/len);
        }
        return scoreSet;
    }

    /*
    *   (6)	根据相关性分值大小对匹配到的文档进行排序
    *   (7)	对于检索结果为空的情况，显示提示信息，并返回一个默认或随机的结果(1~10)
    *   return      :
    * */
    public List compareRank(ArrayList temp, Integer size){
        Map tempMap = new TreeMap<Integer,Double>();
        for(int i=0;i<temp.size();i++)
            tempMap.put(i,temp.get(i));

        List<Map.Entry<Integer,Double>> list
                = new ArrayList<Map.Entry<Integer,Double>>(tempMap.entrySet());

        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Integer,Double>>() {
            @Override
            public int compare(Map.Entry<Integer,Double> o1, Map.Entry<Integer,Double> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return  o2.getValue().compareTo(o1.getValue());
            }
        });
        List TList = new ArrayList();
        Iterator it = list.iterator();
        int j=0;
        while (it.hasNext()&&j<size){
            Map.Entry entry =(Map.Entry) it.next();
            Object key = entry.getKey();
            Double value = (Double)entry.getValue();
            //System.out.println(key+"  "+value);
            TList.add(key);
            j++;
        }
        return TList;
    }

    /*public static void main(String[] argv){

        MyFile file = new MyFile();
        int docNum = 2000;
        //#* #o #c #t
        String selectString = "#*";
        String[] selectSet = selectString.split(" ");
        file.splitRCVDocument(".\\test\\AMiner-Paper.txt",docNum,selectSet);

        Index index = new Index(".\\test\\collection",".\\test\\english.stop.txt");
        //index.showTable();



        Scanner reader = new Scanner(System.in);
        int end = 1;
        while(end!=0){
            System.out.println("请输入要查找文档的关键字：");
            String query = reader.nextLine();
            ArrayList temp = index.Tf_idf_Weight(query);
            List rank = index.compareRank(temp,10);
            System.out.println("按相关度排序如下：");
            for(int i=1;i<rank.size();i++)
                System.out.println("Rank"+(i)+"："+rank.get(i));
            System.out.println("退出请按 0，继续查找请按 1");
            Integer xx = reader.nextInt();
            if(xx==0)
                end = 0;
        }


        //String name = sc.nextLine();



    }*/
}
