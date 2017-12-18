<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/views/commons/include.jsp" %>
<script type="text/javascript">
		var add = function(){
			var url = '${ctx}/admin/article/category/add';
			art.dialog.open(url,{
            				title:'添加主题分类',
            				id:'addUI',
            				width: 768,
            				height: 260,
            				resize: false
            });
		};
		var update = function(obj){
			var url = '${ctx}/admin/article/category/'+obj.id+'/update';
			art.dialog.open(url,{
            			title:'编辑主题分类',
            			id:'updateUI',
            			width:768,
            			height:260,
            			resize: false
            });
		};
		var del = function(obj){
			art.dialog.confirm('确定删除此【'+obj.name+'】主题分类？',function(){
				var url = '${ctx}/admin/article/category/'+obj.id+'/delete';
				$.getJSON(url,function(json){
		    		if(json.code == 200){
		    			$("button[name='refresh']",window.document).click();
		    		}else{
		    			art.dialog.tips(json.message, 1.5);
		    		}
				});
			});
		};

		var commonFormatter = function(value, row, index){
			 return [
				        '<a class="label label-info edit" href="javascript:void(0)" title="修改">修改</a>',
						'<a class="label label-danger ml10 remove" href="javascript:void(0)" title="删除">删除</a>'
				    ].join('');
		};

		var timeFormatter = function(value, row, index){
		    var unixTimestamp = new Date(row.createTime * 1000)
		    return unixTimestamp.toLocaleString();
		};

        var articleStatusFormatter = function(value, row, index){
            return row.status?"<a id='status_"+row.id+"' class='label label-info editStatus' title='点击修改状态' href='javascript:void(0)'>正常</a>"
				:"<a id='status_"+row.id+"' class='label label-default editStatus' title='点击修改状态' href='javascript:void(0)'>锁定</a>";
        };

        window.changeStatusEvent = {
            'click .editStatus': function(e, value, row, index){
				var statusStr = "正常";
				if(row.status){
					statusStr = "锁定";
				}
				art.dialog.confirm('确定修改【'+row.name+'】状态为'+statusStr+'？',function(){
					$.post("${ctx}/admin/article/category/"+row.id+"/changeStatus",
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

		window.commonEvents = {
				'click .edit': function (e, value, row, index) {
			    	update(row);
			    },
			    'click .remove': function (e, value, row, index) {
			    	del(row);
			    }
		};
		$("#table").bootstrapTable();
</script>
	<!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        	主题分类管理
        <small>更轻松管理您的主题分类</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/admin/index"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="javascript:void(0);">主题管理</a></li>
        <li class="active">主题分类</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">主题分类列表</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
            	<div id="toolbar">
			        <button class="btn btn-block btn-primary" onclick="add();">
			            <i class="glyphicon glyphicon-plus icon-plus"></i> 新增
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
					<th data-field="name">名称</th>
					<th data-field="priority">优先值</th>
					<th data-field="status" data-formatter="articleStatusFormatter" data-events="changeStatusEvent">状态</th>
					<th data-field="createTime" data-formatter="timeFormatter">创建日期</th>
					<th data-formatter="commonFormatter" data-events="commonEvents">操作</th>
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