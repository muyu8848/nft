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
				name : 'title',
				field : 'title',
				label : '活动标题',
			}, {
				align : 'left',
				name : 'collection_info',
				label : '合成后的艺术品'
			}, {
				align : 'left',
				name : 'material_info',
				label : '所需原料'
			}, {
				align : 'left',
				name : 'quantity',
				field : 'quantity',
				label : '数量',
			}, {
				align : 'left',
				name : 'stock',
				field : 'stock',
				label : '库存',
			}, {
				align : 'left',
				name : 'activity_time_range',
				label : '活动时间范围'
			}, {
				align : 'left',
				name : 'createTime',
				field : 'createTime',
				label : '创建时间',
			}, {
				align : 'left',
				name : 'action',
				label : '操作'
			} ],
			tableData : [],
			collections : [],
			showAddDialogFlag : false,
			selectedRecord : '',
			selectedRecordId : '',
			updateComposeMaterialDialogFlag : false,
			materials : [],
			showComposeRecordDialogFlag : false,
			composeRecords : [],
			memberMobile : '',
		}
	},
	mounted : function() {
		var that = this;
		that.findAllCollection();
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

		showComposeRecordDialog : function(id) {
			var that = this;
			that.memberMobile = '';
			that.selectedRecordId = id;
			that.findComposeRecord();
			that.showComposeRecordDialogFlag = true;
		},

		findComposeRecord : function() {
			var that = this;
			that.$http.get('/composeActivity/findComposeRecord', {
				params : {
					composeActivityId : that.selectedRecordId,
					memberMobile : that.memberMobile
				}
			}).then(function(res) {
				that.composeRecords = res.body.data;
			});
		},

		friendlyActivityTimeRange : function(result) {
			if (dayjs().isAfter(dayjs(result.activityTimeEnd))) {
				return '已结束';
			}
			if (dayjs().isBefore(dayjs(result.activityTimeStart))) {
				return '未开始';
			}
			return '进行中';
		},

		updateComposeMaterial : function() {
			var that = this;
			var materials = that.materials;
			for (var i = 0; i < materials.length; i++) {
				var materialId = materials[i].materialId;
				if (materialId === null || materialId === '') {
					that.$q.notify({
						type : 'negative',
						message : '请选择原料'
					});
					return;
				}
				var quantity = materials[i].quantity;
				if (quantity === null || quantity === '') {
					that.$q.notify({
						type : 'negative',
						message : '请输入数量'
					});
					return;
				}
				if (quantity <= 0) {
					that.$q.notify({
						type : 'negative',
						message : '数量必须大于0'
					});
					return;
				}
			}
			if (materials.length == 0) {
				that.$q.notify({
					type : 'negative',
					message : '最少添加一种原料'
				});
				return;
			}
			that.$http.post('/composeActivity/updateComposeMaterial', {
				activityId : that.selectedRecordId,
				materials : materials,
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.updateComposeMaterialDialogFlag = false;
				that.refreshTable();
			});
		},

		addMaterial : function() {
			this.materials.push({
				materialId : '',
				quantity : '',
			});
		},

		removeMaterial : function(index) {
			this.materials.splice(index, 1);
		},

		showUpdateComposeMaterialDialog : function(id) {
			var that = this;
			that.$http.get('/composeActivity/findComposeActivityById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedRecordId = id;
				that.materials = res.body.data.materials;
				that.updateComposeMaterialDialogFlag = true;
			});
		},

		viewImage : function(path) {
			var viewer = new Viewer(document.getElementById(path), {
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
				that.$http.get('/composeActivity/del', {
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

		findAllCollection : function() {
			var that = this;
			that.$http.get('/collection/findAllCollection', {
				params : {}
			}).then(function(res) {
				this.collections = res.body.data;
			});
		},

		add : function() {
			var that = this;
			var selectedRecord = that.selectedRecord;
			if (selectedRecord.title === null || selectedRecord.title === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入活动标题'
				});
				return;
			}
			if (selectedRecord.collectionId === null || selectedRecord.collectionId === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择合成后的艺术品'
				});
				return;
			}
			if (selectedRecord.quantity === null || selectedRecord.quantity === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入数量'
				});
				return;
			}
			if (selectedRecord.activityTimeStart === null || selectedRecord.activityTimeStart === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入活动开始时间'
				});
				return;
			}
			if (selectedRecord.activityTimeEnd === null || selectedRecord.activityTimeEnd === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入活动结束时间'
				});
				return;
			}
			that.$http.post('/composeActivity/addComposeActivity', selectedRecord, {
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
				title : '',
				activityTimeStart : '',
				activityTimeEnd : '',
				collectionId : '',
				quantity : ''
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
			that.$http.get('/composeActivity/findComposeActivityByPage', {
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