import java.io.*;
import java.util.ArrayList;

/**
 * Created by lingalone on 2016/5/30.
 */
/*
* file output and input
* */
public class MyFile {

    /*
    *   function    :   get file list from directory(not contain another directory)
    *   parameter   :
    *                   Directory path  (String)
    *   return      :   file list       (ArrayList)
    * */
    public ArrayList<String> getFileList(String dirPath){
        ArrayList<String> fileList;
        fileList = new ArrayList<String>();
        File f = new File(dirPath);
        if (!f.exists()) {
            System.out.println(dirPath + " not exists");
            return null;
        }
        File files[] = f.listFiles();
        for (int i = 0; i < files.length; i++){
            if(files[i].isFile()){
                String absolutePath = files[i].getAbsolutePath();
                fileList.add(absolutePath);         // add in ArrayList
                //System.out.println(absolutePath);
            }else if(files[i].isDirectory())
                continue;
        }//for loop end;
        return  fileList;
    }
    /*
    *   function    :   save file list from ArrayList
    *   parameter   :
    *                   fileList    (ArrayList)
    *                   filePath    (String)
    *   return      :   null
    * */
    public void saveFileList(ArrayList<String> fileList, String filePath){
        try{
            PrintWriter out = new PrintWriter(filePath);
            for(String aline:fileList){
                //System.out.println(aline);
                out.println(aline);
            }
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    *   function    :   Split Document
    *   parameter   :
    *                   subDocNumber    (Integer)
    *                   filePath        (String)
    *                   selectSet       (String[])
    *   return      :   null
    * */
    public void splitRCVDocument(String filePath, Integer subDocNumber, String[] selectSet){

        try{
            File file = new File(filePath);         //get a document file
            InputStream is = new FileInputStream(file);
            BufferedReader read = new BufferedReader(new InputStreamReader(is));
            String temp = null;
            Integer docID = 0;
            for(; docID <= subDocNumber; docID++){
                String tempId = docID.toString();
                for(int xx=tempId.length();xx<=6;xx++)
                    tempId = "0"+tempId;
                String subDocPath = ".\\test\\collection\\"+tempId+".txt";
                PrintWriter out = new PrintWriter(subDocPath);
                while ((temp = read.readLine()) != null) {
                    /*deal with the words*/
                    String[] words = temp.split(" ");
                    if(words[0].equals("#index")){
                        //System.out.println(temp);
                        break;
                    }
                    /*select */
                    for(String select:selectSet){
                        if(select.equals(words[0]))
                            out.println(temp.substring(3));//cut string like "#.."
                    }
                }//while loop end;
                out.close();
            }//for loop end;
        }catch (IOException e) {
            e.printStackTrace();
        }//try-catch end;
    }//function end;


    public static void main(String[] argv){

        MyFile myFile = new MyFile();
        //ArrayList<String> fileList = myFile.getFileList(".\\test\\cc");
        //myFile.saveFileList(fileList,".\\test\\pathList.txt");
        //System.out.println(fileList);
        /*Split exp*/
        /*
                String selectString = "#* #c";
                String[] selectSet = selectString.split(" ");
                myFile.splitRCVDocument(".\\test\\AMiner-Paper.txt",10,selectSet);
        */

        /*
        try{
            File file = new File(".\\test\\AMiner-Paper.txt"); //get a document file
            //System.out.println(filePath.get(i));
            InputStream is = new FileInputStream(file);
            BufferedReader read = new BufferedReader(new InputStreamReader(is));
            String temp = null;
            int i =200;
            while ((temp = read.readLine()) != null && i>0) {
                System.out.println(temp);
                i--;
            }
        }catch (IOException e) {
            //System.out.println("error in read file");
        }//try-catch end;
        */

    }

}
