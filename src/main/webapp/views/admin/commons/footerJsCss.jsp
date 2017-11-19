<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- jQuery 2.2.3 -->
<script src="/resources/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- jQuery form plugin -->
<script src="/resources/plugins/jQueryForm/jquery.form.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="/resources/bootstrap/js/bootstrap.min.js"></script>
<!-- Bootstrap table -->
<script src="/resources/plugins/bootstrap-table/bootstrap-table.js"></script>
<!-- put your locale files after bootstrap-table.js -->
<script src="/resources/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
<!-- SlimScroll -->
<script src="/resources/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="/resources/plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="/resources/dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="/resources/dist/js/demo.js"></script>
<!-- page script -->
<script>
  function runningFormatter(value, row, index){
	  return (index+1)+parseInt($(".page-size").text())*(parseInt($(".page-number.active").text())-1);
  }
  function actionFormatter(value, row, index) {
	    return [
	        '<a class="label label-info edit" href="javascript:void(0)" title="修改">修改</a>',
			'<a class="label label-danger ml10 remove" href="javascript:void(0)" title="删除">删除</a>'
	    ].join('');
	}
  window.actionEvents = {
		    'click .edit': function (e, value, row, index) {
		    	update(row.id);
		    },
		    'click .remove': function (e, value, row, index) {
		    	del(row.id);
		    }
		};
</script>