Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			name : '',
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'name_info',
				label : '创作者名称'
			}, {
				align : 'left',
				name : 'avatar_info',
				label : '头像',
			}, {
				align : 'left',
				name : 'createTime',
				field : 'createTime',
				label : '创建时间',
			}, {
				align : 'left',
				name : 'lastModifyTime',
				field : 'lastModifyTime',
				label : '最后修改',
			}, {
				align : 'left',
				name : 'action',
				label : '操作'
			} ],
			tableData : [],
			showAddDialogFlag : false,
			selectedRecord : '',
			selectedRecordId : '',
			showEditDialogFlag : false,
		}
	},
	mounted : function() {
		var that = this;
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

		viewImage : function(imagePath) {
			var image = new Image();
			image.src = imagePath;
			var viewer = new Viewer(image, {
				hidden : function() {
					viewer.destroy();
				},
			});
			viewer.show();
		},

		del : function(id) {
			var that = this;
			that.$q.dialog({
				title : '提示',
				message : '确定要删除吗?',
				cancel : true,
				persistent : true
			}).onOk(function() {
				that.$http.get('/creator/delCreator', {
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

		update : function() {
			var that = this;
			var selectedRecord = that.selectedRecord;
			if (selectedRecord.name === null || selectedRecord.name === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入创作者名称'
				});
				return;
			}
			if (selectedRecord.avatar === null || selectedRecord.avatar === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入头像链接'
				});
				return;
			}
			that.$http.post('/creator/addOrUpdateCreator', selectedRecord, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showEditDialogFlag = false;
				that.refreshTable();
			});
		},

		showEditDialog : function(id) {
			var that = this;
			that.$http.get('/creator/findCreatorById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedRecord = res.body.data;
				that.showEditDialogFlag = true;
			});
		},

		add : function() {
			var that = this;
			var selectedRecord = that.selectedRecord;
			if (selectedRecord.name === null || selectedRecord.name === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入创作者名称'
				});
				return;
			}
			if (selectedRecord.avatar === null || selectedRecord.avatar === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入头像链接'
				});
				return;
			}
			that.$http.post('/creator/addOrUpdateCreator', selectedRecord, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showAddDialogFlag = false;
				that.refreshTable();
			});
		},

		showAddDialog : function() {
			this.showAddDialogFlag = true;
			this.selectedRecord = {
				name : '',
				avatar : '',
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
			that.$http.get('/creator/findCreatorByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					name : that.name
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