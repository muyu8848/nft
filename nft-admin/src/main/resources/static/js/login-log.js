Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			userName : '',
			ipAddr : '',
			state : null,
			loginStateDictItems : [],
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
				name : 'userName',
				field : 'userName',
				label : '登录账号'
			}, {
				align : 'left',
				name : 'ipAddr',
				field : 'ipAddr',
				label : 'ip地址'
			}, {
				align : 'left',
				name : 'subSystemName',
				field : 'subSystemName',
				label : '子系统'
			}, {
				align : 'left',
				name : 'browser',
				field : 'browser',
				label : '浏览器'
			}, {
				align : 'left',
				name : 'os',
				field : 'os',
				label : '操作系统'
			}, {
				align : 'left',
				name : 'state_name',
				label : '登录状态'
			}, {
				align : 'left',
				name : 'loginTime',
				field : 'loginTime',
				label : '登录时间'
			} ],
			tableData : []
		}
	},
	mounted : function() {
		this.findLoginStateDictItem();
		this.loadTableData({
			pagination : this.tablePagination
		});
	},
	methods : {

		findLoginStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'loginState'
				}
			}).then(function(res) {
				this.loginStateDictItems = res.body.data;
			});
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
			that.$http.get('/loginLog/findLoginLogByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					userName : that.userName,
					ipAddr : that.ipAddr,
					state : that.state,
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