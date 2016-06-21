import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by lingalone on 2016/6/12.
 */
public class ajaxServlet extends HttpServlet{
    //private static final String CONTENT_TYPE = "text/xml; charset=UTF-8";//这里最好统一用UTF-8进行编码
    public Index index;
    private String testPath;
    public ajaxServlet() {
        super();
        //testPath = this.getServletConfig().getServletContext().getRealPath("/");
        //固定test文件夹在c盘
        testPath = "C:";
        index = new Index(testPath +"\\test\\collection", testPath +"\\test\\english.stop.txt");
        //String filePath=this.getServletConfig().getServletContext().getRealPath("/");
        //System.out.println(filePath);
        //index = new Index(".\\test\\collection",".\\test\\english.stop.txt");
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filePath=this.getServletConfig().getServletContext().getRealPath("/");
        System.out.println(filePath);
        // 设置响应内容类型
        String query = request.getParameter("query");
        String ID = request.getParameter("ID");
        if(query!=null) {
            ArrayList temp = index.Tf_idf_Weight(query);
            //获得文档排名
            List rank = index.compareRank(temp, 11);
            //提取前10文档id
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //out.write(i);
            out.write("<tr class=\"info\">" + "<td>序号</td>" + "<td>标题</td>" +
                    "<td>文档ID</td>" + "</tr>");
            for (int i = 1; i < rank.size(); i++) {
                if (i % 4 == 0)
                    out.write("<tr class=\"success\">");
                else if (i % 4 == 1)
                    out.write("<tr class=\"warning\">");
                else if (i % 4 == 2)
                    out.write("<tr class=\"info\">");
                else if (i % 4 == 3)
                    out.write("<tr class=\"danger\">");
                out.write("<td>" + i + "</td>");
                int tt = (int) rank.get(i);
                out.write("<td><a href=\"#\" onclick=\"onContent(" + tt + ")\">" +
                        index.docTitle.get(tt - 1) + "</a></td>");
                out.write("<td>" + tt + "</td>");
            }
        }
        else if(ID!=null){
            for(int i=ID.length();i<=6;i++)
                ID = "0"+ID;
            System.out.println(ID);
            ArrayList con = new ArrayList();
            try {
                File file = new File(testPath +"\\test\\doc\\doc"+ID+".txt"); //get a document file
                //System.out.println(filePath.get(i));
                InputStream is = new FileInputStream(file);
                BufferedReader read = new BufferedReader(new InputStreamReader(is));
                String temp = null;
                while ((temp = read.readLine()) != null) {
                    con.add(temp);
                }
            }catch (Exception e){}
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.write("<p>题目："+con.get(0)+"</p>");
            out.write("<p>单位："+con.get(1)+"</p>");
            out.write("<p>时间："+con.get(2)+"</p>");
            out.write("<p>摘要："+con.get(3)+"</p>");
        }
    }
}
