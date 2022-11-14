Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			type : '',
			typeDictItems : [],
			title : '',
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'notice_title',
				label : '公告信息'
			}, {
				align : 'left',
				name : 'publishTime',
				field : 'publishTime',
				label : '发布时间'
			}, {
				align : 'left',
				name : 'lastModifyTime',
				field : 'lastModifyTime',
				label : '最后修改'
			}, {
				align : 'left',
				name : 'action',
				label : '操作'
			} ],
			tableData : [],
			showAddOrUpdateDialogFlag : false,
			addOrUpdateAction : '',
			selectedNotice : '',
			showViewNoticeFlag : false
		}
	},
	mounted : function() {
		var that = this;
		that.findTypeItem();
		that.loadTableData({
			pagination : this.tablePagination
		});
	},
	methods : {

		findTypeItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'noticeType'
				}
			}).then(function(res) {
				this.typeDictItems = res.body.data;
			});
		},

		viewNotice : function(notice) {
			this.selectedNotice = notice;
			this.showViewNoticeFlag = true;
		},

		delNotice : function(id) {
			var that = this;
			that.$q.dialog({
				title : '提示',
				message : '确定要删除吗?',
				cancel : true,
				persistent : true
			}).onOk(function() {
				that.$http.get('/notice/delById', {
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

		showEditDialog : function(id) {
			var that = this;
			that.$http.get('/notice/findById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedNotice = res.body.data;
				that.showAddOrUpdateDialogFlag = true;
				that.addOrUpdateAction = 'update';
			});
		},

		addOrUpdateNotice : function() {
			var that = this;
			var selectedNotice = that.selectedNotice;
			if (selectedNotice.title === null || selectedNotice.title === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入公告标题'
				});
				return;
			}
			if (selectedNotice.type === null || selectedNotice.type === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择公告类型'
				});
				return;
			}
			if (selectedNotice.publishTime === null || selectedNotice.publishTime === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择发布时间'
				});
				return;
			}
			if (selectedNotice.content === null || selectedNotice.content === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入公告内容'
				});
				return;
			}
			that.$http.post('/notice/addOrUpdateNotice', selectedNotice, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showAddOrUpdateDialogFlag = false;
				that.refreshTable();
			});
		},

		showAddDialog : function() {
			this.showAddOrUpdateDialogFlag = true;
			this.addOrUpdateAction = 'add';
			this.selectedNotice = {
				title : '',
				content : '',
				type : '',
				publishTime : dayjs().format('YYYY-MM-DD HH:mm')
			};
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
			that.$http.get('/notice/findByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					type : that.type,
					title : that.title
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