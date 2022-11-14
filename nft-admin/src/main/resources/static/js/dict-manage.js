Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			dictTypeCode : '',
			dictTypeName : '',
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'dictTypeCode',
				field : 'dictTypeCode',
				label : '字典code'
			}, {
				align : 'left',
				name : 'dictTypeName',
				field : 'dictTypeName',
				label : '字典名称'
			}, {
				align : 'left',
				name : 'note',
				field : 'note',
				label : '备注',
			}, {
				align : 'left',
				name : 'lastModifyTime',
				field : 'lastModifyTime',
				label : '最后修改时间'
			}, {
				align : 'left',
				name : 'action',
				label : '操作'
			} ],
			tableData : [],
			showAddDialogFlag : false,
			selectedDictType : '',
			selectedDictTypeId : '',
			showEditDialogFlag : false,
			showDelDialogFlag : false,
			showEditDictDataDialogFlag : false,
			dictItems : []
		}
	},
	mounted : function() {
		this.loadTableData({
			pagination : this.tablePagination
		});
	},
	methods : {
		
		updateDictData : function() {
			var that = this;
			var dictItemCodeMap = new Map();
			var dictItems = that.dictItems;
			for (var i = 0; i < dictItems.length; i++) {
				var dictItem = dictItems[i];
				if (dictItem.dictItemCode == null || dictItem.dictItemCode == '') {
					that.$q.notify({
						type : 'negative',
						message : '请输入字典项code'
					});
					return;
				}
				if (dictItem.dictItemName == null || dictItem.dictItemName == '') {
					that.$q.notify({
						type : 'negative',
						message : '请输入字典项名称'
					});
					return;
				}
				if (dictItemCodeMap.get(dictItem.dictItemCode) != null) {
					that.$q.notify({
						type : 'negative',
						message : '字典项code不能重复'
					});
					return;
				}
				dictItemCodeMap.set(dictItem.dictItemCode, dictItem.dictItemCode);
			}
			that.$http.post('/dictconfig/updateDictData', {
				dictTypeId : that.selectedDictTypeId,
				dictDatas : dictItems
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showEditDictDataDialogFlag = false;
				that.refreshTable();
			});
		},
		
		moveUpDictItem : function(index) {
			var temp = this.dictItems[index - 1];
			Vue.set(this.dictItems, index - 1, this.dictItems[index]);
			Vue.set(this.dictItems, index, temp);
		},

		moveDownDictItem : function(index) {
			var i = this.dictItems[index + 1];
			Vue.set(this.dictItems, index + 1, this.dictItems[index]);
			Vue.set(this.dictItems, index, i);
		},
		
		addDictItem : function() {
			this.dictItems.push({
				dictItemCode : '',
				dictItemName : ''
			});
		},

		delDictItem : function(index) {
			this.dictItems.splice(index, 1);
		},
		
		showEditDictDataDialog : function(id) {
			var that = this;
			that.selectedDictTypeId = id;
			that.$http.get('/dictconfig/findDictItemByDictTypeId', {
				params : {
					dictTypeId : id
				}
			}).then(function(res) {
				that.dictItems = res.body.data;
				this.showEditDictDataDialogFlag = true;
			});
		},

		delDictType : function() {
			var that = this;
			that.$http.get('/dictconfig/delDictType', {
				params : {
					id : that.selectedDictTypeId
				}
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showDelDialogFlag = false;
				that.refreshTable();
			});
		},

		showDelDialog : function(id) {
			this.showDelDialogFlag = true;
			this.selectedDictTypeId = id;
		},

		updateDictType : function() {
			var that = this;
			var selectedDictType = that.selectedDictType;
			if (selectedDictType.dictTypeCode === null || selectedDictType.dictTypeCode === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入字典code'
				});
				return;
			}
			if (selectedDictType.dictTypeName === null || selectedDictType.dictTypeName === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入字典名称'
				});
				return;
			}
			that.$http.post('/dictconfig/addOrUpdateDictType', selectedDictType, {
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
			that.$http.get('/dictconfig/findDictTypeById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedDictType = res.body.data;
				that.showEditDialogFlag = true;
			});
		},

		addDictType : function() {
			var that = this;
			var selectedDictType = that.selectedDictType;
			if (selectedDictType.dictTypeCode === null || selectedDictType.dictTypeCode === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入字典code'
				});
				return;
			}
			if (selectedDictType.dictTypeName === null || selectedDictType.dictTypeName === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入字典名称'
				});
				return;
			}
			that.$http.post('/dictconfig/addOrUpdateDictType', selectedDictType, {
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
			this.selectedDictType = {
				dictTypeCode : '',
				dictTypeName : '',
				note : ''
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
			that.$http.get('/dictconfig/findDictTypeByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					dictTypeCode : that.dictTypeCode,
					dictTypeName : that.dictTypeName
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