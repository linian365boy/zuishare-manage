<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="../../commons/include.jsp" %>
<script type="text/javascript">
		var add = function(){
			var url = '${ctx}/admin/article/add';
			$.get(url,function(data){
				$("div.content-wrapper").html(data);
			});
		};
		var update = function(obj){
			var url = '${ctx}/admin/article/'+obj.id+'/update';
			$.get(url,function(data){
				$("div.content-wrapper").html(data);
			});
		};
		//del
		var del = function(obj){
			art.dialog.confirm('确定删除此【'+obj.title+'】主题？',function(){
				var url = '${ctx}/admin/article/'+obj.id+'/delete';
				$.getJSON(url,function(json){
		    		if(json.code==200){
		    			$("button[name='refresh']",window.document).click();
		    		}else{
		    			art.dialog.tips(json.message, 1.5);
		    		}
				});
			});
		};
		//publish
		var publish = function(obj){
			var articleId = obj.id;
            art.dialog.confirm('确定发布此【'+obj.title+'】主题文章？',function(){
                $.post("${ctx}/admin/article/"+articleId+"/release",function(data){
                    var dialog = art.dialog({
                        id:"publish",
                        lock:true
                    });
                     if(data.code==200){
                        dialog.content(data.message).time(2.5);
                        $("button[name='refresh']",window.document).click();
                    }else{
                        dialog.content(data.message).time(2.5);
                    }
                },"json");
            });
		};

		var publishAllNormal = function(){
		    art.dialog.confirm('确定一键发布所有正常状态的主题文章？',function(){
                $.post("${ctx}/admin/article/releaseAll",function(data){
                    var dialog = art.dialog({
                        id:"publishAll",
                        lock:true
                    });
                     if(data.code==200){
                        dialog.content(data.message).time(2.5);
                        $("button[name='refresh']",window.document).click();
                    }else{
                        dialog.content(data.message).time(2.5);
                    }
                },"json");
            });
		};

		var reflushArticles = function(){
		    art.dialog.confirm('确定一键刷新主题文章缓存？', function(){
		        $.post("${ctx}/admin/article/reflush",function(data){
                    var dialog = art.dialog({
                        id:"reflush",
                        lock:true
                    });
                     if(data.code==200){
                        dialog.content(data.message).time(2.5);
                    }else{
                        dialog.content(data.message).time(2.5);
                    }
                },"json");
		    });
		};

		var timeFormatter = function(date){
		    return "<span class='label label-primary'>"+date.toLocaleString()+"</span>";
		};

		var publishTimeFormatter = function(value, row, index){
		    if(row.publishTime == 0){
                return "<span class='label label-warning'>未发布</span>";
		    }
		    return timeFormatter(new Date(row.publishTime * 1000));
		};

		var articleActionFormatter = function(value, row, index){
			 return [
				        '<a class="label label-info edit" href="javascript:void(0)" title="修改">修改</a>',
				        '<a class="label label-info publish ml10" href="javascript:void(0)" title="发布">发布</a>',
						'<a class="label label-danger ml10 remove" href="javascript:void(0)" title="删除">删除</a>'
				    ].join('');
		};

		var contentTypeFormatter = function(value, row, index){
            if(row.contentType){
                return "<span class='label label-danger'>快讯类型</span>";
            }
            return "<span class='label label-success'>图文类型<span>";
		};

		var pathFormatter = function(value, row, index){
		    if(row.contentType){
		        return "<span class='label label-danger'>no image</span>";
		    }
            return "<img src='"+row.imgTitlePath+"' width='100px' height='70px'/>";
		};

		var articleStatusFormatter = function(value, row, index){
		    return row.status?"<a id='status_"+row.id+"' class='label label-info editStatus' title='点击修改状态' href='javascript:void(0)'>正常</a>"
            				:"<a id='status_"+row.id+"' class='label label-default editStatus' title='点击修改状态' href='javascript:void(0)'>锁定</a>";
		};

		window.articleActionEvents = {
				'click .publish':function(e, value, row, index){
					publish(row);
				},
				'click .edit': function (e, value, row, index) {
			    	update(row);
			    },
			    'click .remove': function (e, value, row, index) {
			    	del(row);
			    }
		};
		window.changeStatusEvent = {
                    'click .editStatus': function(e, value, row, index){
        				var statusStr = "正常";
        				if(row.status){
        					statusStr = "锁定";
        				}
        				art.dialog.confirm('确定修改【'+row.title+'】状态为'+statusStr+'？',function(){
        					$.post("${ctx}/admin/article/"+row.id+"/changeStatus",
        							{status:row.status},function(data){
        						if(data.code == "200"){
        							if(row.status){
        								$("#status_"+row.id).text("锁定").removeClass("label-info").addClass("label-default");
        							}else{
        								$("#status_"+row.id).text("正常").removeClass("label-default").addClass("label-info");
        							}
        							$("button[name='refresh']",window.document).click();
        						}else{
        							art.dialog.tips("修改状态失败！", 1.5);
        						}
        					},"json");
        				});
        			}
                };
		$("#table").bootstrapTable();
</script>
	<!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        	主题管理
        <small>更轻松管理您的主题</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/admin/index"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="javascript:void(0);">主题管理</a></li>
        <li class="active">主题</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">主题列表</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
            	<div id="toolbar">
			        <button class="btn btn-primary" onclick="add();">
			            <i class="glyphicon glyphicon-plus icon-plus"></i> 新增
			        </button>
			        <button class="btn btn-warning" onclick="publishAllNormal();" title="一键发布所有正常状态的主题文章">
                    	<i class="glyphicon"></i> 一键发布
                    </button>
                    <button class="btn btn-danger" onclick="reflushArticles();" title="一键刷新所有正常状态且发布的主题文章缓存">
                        <i class="glyphicon"></i> 一键刷新文章缓存
                    </button>
			    </div>
              <table id="table" data-toolbar="#toolbar"
              data-toggle="table" class="table table-striped" data-search="true" data-show-refresh="true"
              data-show-columns="true"
              data-show-export="true"
              data-show-pagination-switch="true"
              data-pagination="true"
              data-id-field="id"
              data-page-list="[10, 25, 50]"
              data-show-footer="false"
              data-side-pagination="server" data-url="${ctx }/${ajaxListUrl}">
                <thead>
                <tr>
    				<th data-formatter="runningFormatter">序号</th>
					<th data-field="title">标题</th>
					<th data-field="imgTitlePath" data-formatter="pathFormatter">标题图片</th>
					<th data-field="categoryName">主题分类</th>
					<th data-field="contentType" data-formatter="contentTypeFormatter">内容类别</th>
					<th data-field="status" data-formatter="articleStatusFormatter" data-events="changeStatusEvent">状态</th>
					<th data-field="viewNum">点击量</th>
					<th data-field="publishTime" data-formatter="publishTimeFormatter" >发布时间</th>
					<th data-formatter="articleActionFormatter" data-events="articleActionEvents">操作</th>
				</tr>
                </thead>
              </table>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
