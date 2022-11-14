Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			operName : '',
			ipAddr : '',
			startTime : dayjs().format('YYYY-MM-DD'),
			endTime : dayjs().format('YYYY-MM-DD'),
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'operName',
				field : 'operName',
				label : '操作账号'
			}, {
				align : 'left',
				name : 'ipAddr',
				field : 'ipAddr',
				label : 'ip地址'
			}, {
				align : 'left',
				name : 'subSystemName',
				field : 'subSystemName',
				label : '子系统',
			}, {
				align : 'left',
				name : 'module',
				field : 'module',
				label : '模块',
			}, {
				align : 'left',
				name : 'operate',
				field : 'operate',
				label : '操作'
			}, {
				align : 'left',
				name : 'requestMethod',
				field : 'requestMethod',
				label : '请求方式'
			}, {
				align : 'left',
				name : 'requestUrl',
				field : 'requestUrl',
				label : 'url'
			}, {
				align : 'left',
				name : 'request_param',
				field : 'requestParam',
				label : '入参'
			}, {
				align : 'left',
				name : 'operTime',
				field : 'operTime',
				label : '操作时间'
			} ],
			tableData : [],
			showRequestParamDialogFlag : false,
			requestParam : '',
		}
	},
	mounted : function() {
		this.loadTableData({
			pagination : this.tablePagination
		});
	},
	methods : {

		showRequestParamDialog : function(requestParam) {
			this.requestParam = requestParam;
			this.showRequestParamDialogFlag = true;
		},

		refreshTable : function() {
			this.loadTableData({
				pagination : {
					page : 1,
					rowsPerPage : this.tablePagination.rowsPerPage,
					rowsNumber : 10
				}
			});
		},

		loadTableData : function(param) {
			var that = this;
			that.tableDataLoading = true;
			that.$http.get('/operLog/findOperLogByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					operName : that.operName,
					ipAddr : that.ipAddr,
					startTime : that.startTime,
					endTime : that.endTime
				}
			}).then(function(res) {
				var data = res.body.data;
				that.tablePagination.rowsNumber = data.total;
				that.tableData = data.content;
				that.tablePagination.page = param.pagination.page;
				that.tablePagination.rowsPerPage = param.pagination.rowsPerPage;
				that.tableDataLoading = false;
			});
		}

	},
});