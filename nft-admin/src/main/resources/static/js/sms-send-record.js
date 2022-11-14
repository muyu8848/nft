Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			mobile : '',
			typeDictItems : [],
			type : '',
			stateDictItems : [],
			state : '',
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
				name : 'mobile',
				field : 'mobile',
				label : '手机号'
			}, {
				align : 'left',
				name : 'sms_type',
				label : '短信类型',
			}, {
				align : 'left',
				name : 'stateName',
				field : 'stateName',
				label : '发送状态'
			}, {
				align : 'left',
				name : 'time_info',
				label : '时间'
			}, ],
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
		this.findStateDictItem();
		this.loadTableData({
			pagination : this.tablePagination
		});
	},
	methods : {

		findTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'smsType'
				}
			}).then(function(res) {
				this.typeDictItems = res.body.data;
			});
		},
		
		findStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'smsSendState'
				}
			}).then(function(res) {
				this.stateDictItems = res.body.data;
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
			that.$http.get('/sms/findSendRecordByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					mobile : that.mobile,
					smsType : that.type,
					state : that.state,
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