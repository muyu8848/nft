Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			mobile : '',
			bizOrderNo : '',
			typeDictItems : [],
			type : '',
			timeStart : dayjs().format('YYYY-MM-DD'),
			timeEnd : dayjs().format('YYYY-MM-DD'),
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'account_info',
				label : '账号信息'
			}, {
				align : 'left',
				name : 'bizOrderNo',
				field : 'bizOrderNo',
				label : '业务订单号'
			}, {
				align : 'left',
				name : 'changeTypeName',
				field : 'changeTypeName',
				label : '变动类型',
			}, {
				align : 'left',
				name : 'changeTime',
				field : 'changeTime',
				label : '变动时间'
			}, {
				align : 'left',
				name : 'balance_change',
				label : '金额变化'
			} ],
			tableData : [],
			requestParam : '',
		}
	},
	mounted : function() {
		var mobile = getQueryString('mobile');
		if (mobile != null && mobile != '') {
			this.mobile = mobile;
			this.timeStart = '';
			this.timeEnd = '';
		}
		this.findTypeDictItem();
		this.loadTableData({
			pagination : this.tablePagination
		});
	},
	methods : {

		findTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'memberBalanceChangeType'
				}
			}).then(function(res) {
				this.typeDictItems = res.body.data;
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
			that.$http.get('/memberBalanceChangeLog/findByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					mobile : that.mobile,
					bizOrderNo : that.bizOrderNo,
					changeType : that.type,
					timeStart : that.timeStart,
					timeEnd : that.timeEnd
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