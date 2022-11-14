Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			orderNo : '',
			state : '',
			stateDictItems : [],
			submitTimeStart : dayjs().format('YYYY-MM-DD'),
			submitTimeEnd : dayjs().format('YYYY-MM-DD'),
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'order_no',
				label : '订单号'
			}, {
				align : 'left',
				name : 'member_info',
				label : '会员信息'
			}, {
				align : 'left',
				name : 'amount_info',
				label : '金额'
			}, {
				align : 'left',
				name : 'bank_card_info',
				label : '结算账户'
			}, {
				align : 'left',
				name : 'state_info',
				label : '状态'
			}, {
				align : 'left',
				name : 'submitTime',
				field : 'submitTime',
				label : '提交时间'
			}, {
				align : 'left',
				name : 'action',
				label : '操作'
			} ],
			tableData : [],
		}
	},
	mounted : function() {
		var that = this;
		this.findStateDictItem();
		that.loadTableData({
			pagination : this.tablePagination
		});
		new ClipboardJS('.copy-btn', {
			text : function(trigger) {
				return trigger.getAttribute('data-clipboard-text');
			}
		}).on('success', function(e) {
			that.$q.notify({
				type : 'positive',
				message : '复制成功'
			});
			return;
		});
	},
	methods : {
		
		findStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'withdrawRecordState'
				}
			}).then(function(res) {
				this.stateDictItems = res.body.data;
			});
		},
		
		reject : function(id) {
			var that = this;
			that.$q.dialog({
				title : '提示',
				message : '确定要驳回吗?',
				cancel : true,
				persistent : true
			}).onOk(function() {
				that.$http.get('/withdraw/reject', {
					params : {
						id : id
					}
				}).then(function(res) {
					that.$q.notify({
						progress : true,
						timeout : 1000,
						type : 'positive',
						message : '操作成功',
					});
					that.refreshTable();
				});
			});
		},
		
		confirmCredited : function(id) {
			var that = this;
			that.$q.dialog({
				title : '提示',
				message : '确定打款了吗?',
				cancel : true,
				persistent : true
			}).onOk(function() {
				that.$http.get('/withdraw/confirmCredited', {
					params : {
						id : id
					}
				}).then(function(res) {
					that.$q.notify({
						progress : true,
						timeout : 1000,
						type : 'positive',
						message : '操作成功',
					});
					that.refreshTable();
				});
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
			that.$http.get('/withdraw/findByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					orderNo : that.orderNo,
					state : that.state,
					submitTimeStart : that.submitTimeStart,
					submitTimeEnd : that.submitTimeEnd,
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