<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/views/commons/include.jsp" %>
<title>主题新增</title>
<script type="text/javascript" src="${ctx }/resources/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx }/resources/plugins/ckeditor/lang/zh-cn.js"></script>
<script type="text/javascript" src="/resources/plugins/jQueryValidate/jquery.validate.js"></script>
<script type="text/javascript" src="/resources/plugins/jQueryValidate/jquery.metadata.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/dist/css/customUse.css" />

<script type="text/javascript">
	$(document).ready(function(){
		$("#form").validate({
			rules:{
				"title":{
					required:true
				},
				"content":{
					required:true
				}
			},
			messages:{
				"title":{
					required:"标题不能为空！"
				},
				"content":{
					required:"详情不能为空！"
				}
			},
			highlight: function(element) {
			      jQuery(element).closest('.form-group').removeClass('has-success').addClass('has-error');
			},
			success: function(element) {
			      jQuery(element).closest('.form-group').removeClass('has-error');
			},
			submitHandler: function(form){
				CKEDITOR.instances.content.updateElement();
				$(form).ajaxSubmit({
					dataType:'json',
					success:function(json) {
			    		if(json.code==200){
			    			$("ul.treeview-menu.menu-open li.active a").click();
			    		}else{
			    			$(".box-header .error").removeClass("hide").html(json.message);
			    		}
			        }
				});
			}
		});
		CKEDITOR.replace('content');
	});

	function changeContentType(type){
	    if(type == 1){
            $("div.imgTitle").hide();
	    }else{
            $("div.imgTitle").show();
	    }
	};
	</script>

	<section class="content-header">
      <h1>
        	主题文章管理
        <small>更轻松管理您的主题文章</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/admin/index"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">主题文章管理</a></li>
        <li class="active">主题文章管理</li>
      </ol>
    </section>

	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
                <div class="box-header with-border text-center">
					<h3 class="box-title pull-left">新增主题文章</h3>
					<label class="error hide"></label>
				</div>
                <!-- form start -->
                <form id="form" class="form-horizontal content" action="${ctx }/admin/article/add" method="post" enctype="multipart/form-data">
                  <div class="box-body">
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="title">标题<code>*</code></label>
                      <div class="col-sm-10">
                        <input type="text" placeholder="标题" name="title" id="title" class="form-control">
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="parentCs">主题分类<code>*</code></label>
                      <div class="col-sm-10">
                        <select name="categoryName" id="categoryName" class="col-xs-5 selectpicker">
                            <c:forEach var="articleCategory" items="${articleCategorys}">
                                <option value="${articleCategory.id}_${articleCategory.name}">${articleCategory.name}</option>
                            </c:forEach>
                        </select>
                      </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">内容分类<code>*</code></label>
                        <div class="col-sm-10">
                          <label class="radio-inline">
                            <input type="radio" name="contentType" id="contentType1" value="0" checked onclick="changeContentType(0);">图文类型
                          </label>
                          <label class="radio-inline">
                            <input type="radio" name="contentType" id="contentType2" value="1" onclick="changeContentType(1);">快讯类型
                          </label>
                        </div>
                    </div>
                    <div class="form-group imgTitle">
                      <label class="col-sm-2 control-label" for="photo">图片标题</label>
                      <div class="col-sm-10">
                        <input type="file" name="photo" id="photo" accept="image/*"/>
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="content">主题内容<code>*</code></label>
                      <div class="col-sm-10">
                        <textarea id="content" rows="16" name="content" class="form-control ckeditor"></textarea>
                      </div>
                    </div>
                  </div><!-- /.box-body -->
                  <div class="box-footer">
                    <button class="btn btn-default" type="reset">重置</button>
                    <button class="btn btn-info pull-right" type="submit">保存</button>
                  </div><!-- /.box-footer -->
                </form>
              </div>
			</div>
		</div>
       </section>