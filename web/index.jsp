<%--
  Created by IntelliJ IDEA.
  User: lingalone
  Date: 2016/6/12
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="shortcut icon" href="../../assets/ico/favicon.ico">

  <title>Search System</title>

  <!-- Bootstrap core CSS -->
  <link href="dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="dist/css/jumbotron-narrow.css" rel="stylesheet">

  <!-- Just for debugging purposes. Don't actually copy this line! -->
  <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
  <script type="text/javascript" src="dist/js/jquery-1.10.2.js"></script>
  <script type="text/javascript" src="dist/js/bootstrap.min.js"></script>
  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <![endif]-->
</head>

<body>

<div class="container">
  <div class="header">
    <ul class="nav nav-pills pull-right">
      <li class="active"><a href="#">Home</a></li>
      <li><a href="#">About</a></li>
      <li><a href="#">Contact</a></li>
    </ul>
    <h3 class="text-muted">Search System</h3>
  </div>


    <form class="bs-example bs-example-form" role="form" name="searchForm" method="post" action="#">
      <div class="row">
        <div class="col-lg-12">
          <div class="input-group">
            <input type="text" class="form-control" name="inputQuery" id="inputQuery" placeholder="search...." required>
               <span class="input-group-btn">
                  <button class="btn btn-primary"  type="button" onClick="onSearch()";>
                    Search
                  </button>
               </span>
          </div><!-- /input-group -->
        </div><!-- /.col-lg-12 -->
      </div><!-- /.row -->
    </form>
  <div class="row marketing">
      <table class="table text-center" id="resultList">
    </table>
  </div>

  <div class="footer">
    <p>&copy; lingalone@126.com</p>
  </div>

</div> <!-- /container -->

<div id="content" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="cheesyGoodJob">内容</h4>
      </div>
      <div class="modal-body" id="model_content">

      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-info" data-dismiss="modal">关闭</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->



<script>
  function onSearch(){
    //inputQuery
    var query = document.getElementById("inputQuery").value;
    //alert(query);
    if(query!=""){
      var xmlHttp;
      if (window.XMLHttpRequest)
      {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlHttp=new XMLHttpRequest();
      }
      else
      {// code for IE6, IE5
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlHttp.onreadystatechange=function()
      {
        if (xmlHttp.readyState==4 && xmlHttp.status==200)
        {
          //获得返回数据，更新列表
          document.getElementById("resultList").innerHTML=xmlHttp.responseText;
        }
      }
      //提交query
      xmlHttp.open("GET","ajaxServlet?query="+query,true);
      xmlHttp.send();
    }
    else
      inputQuery.value = "请输入要查找的 query";
  }

  function onContent(id) {
    if(id!=""){
      var xmlHttp;
      if (window.XMLHttpRequest)
      {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlHttp=new XMLHttpRequest();
      }
      else
      {// code for IE6, IE5
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlHttp.onreadystatechange=function()
      {
        if (xmlHttp.readyState==4 && xmlHttp.status==200)
        {
          document.getElementById("model_content").innerHTML=xmlHttp.responseText;
          $('#content').modal('show');
        }
      }
      xmlHttp.open("GET","ajaxServlet?ID="+id,true);
      xmlHttp.send();
    }
  }
</script>
<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
</body>
</html>
