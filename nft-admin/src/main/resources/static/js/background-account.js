Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			userName : '',
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
				label : '账号'
			}, {
				align : 'left',
				name : 'stateName',
				field : 'stateName',
				label : '状态',
			}, {
				align : 'left',
				name : 'registeredTime',
				field : 'registeredTime',
				label : '注册时间'
			}, {
				align : 'left',
				name : 'latelyLoginTime',
				field : 'latelyLoginTime',
				label : '最近登录时间'
			}, {
				align : 'left',
				name : 'action',
				label : '操作'
			} ],
			tableData : [],
			showAddDialogFlag : false,
			selectedAccount : '',
			selectedAccountId : '',
			functionStateDictItems : [],
			showEditDialogFlag : false,
			showAssignRoleDialogFlag : false,
			roles : [],
		}
	},
	mounted : function() {
		this.findStateItem();
		this.findAllRole();
		this.loadTableData({
			pagination : this.tablePagination
		});
	},
	methods : {
		
		assignRole : function() {
			var that = this;
			var roleIds = [];
			for (var i = 0; i < that.roles.length; i++) {
				if (that.roles[i].selectdFlag) {
					roleIds.push(that.roles[i].id);
				}
			}
			that.$http.post('/rbac/assignRole', {
				accountId : that.selectedAccountId,
				roleIds : roleIds
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showAssignRoleDialogFlag = false;
				that.refreshTable();
			});
		},
		
		showAssignRoleDialog : function(id) {
			var that = this;
			that.showAssignRoleDialogFlag = true;
			that.selectedAccountId = id;
			for (var i = 0; i < that.roles.length; i++) {
				that.roles[i].selectdFlag = false;
			}
			that.$http.get('/rbac/findRoleByAccountId', {
				params : {
					accountId : id
				}
			}).then(function(res) {
				var selectedRole = res.body.data;
				for (var i = 0; i < that.roles.length; i++) {
					var role = that.roles[i];
					for (var j = 0; j < selectedRole.length; j++) {
						if (role.id == selectedRole[j].id) {
							role.selectdFlag = true;
							break;
						}
					}
				}
			});
		},
		
		findAllRole : function() {
			var that = this;
			that.$http.get('/rbac/findAllRole').then(function(res) {
				var roles = res.body.data;
				for (var i = 0; i < roles.length; i++) {
					roles[i].selectdFlag = false;
				}
				that.roles = roles;
			});
		},
		
		updateLoginPwd : function(id) {
			var that = this;
			that.$q.dialog({
				title : '请输入修改后的登录密码',
				ok : '确定',
				cancel : '取消',
				prompt : {
					model : '',
					isValid : function(val) {
						return val != null && val != '';
					},
					type : 'text'
				},
				persistent : true
			}).onOk(function(data) {
				that.$http.post('/rbac/modifyLoginPwd', {
					id : id,
					newPwd : data
				}, {
					emulateJSON : true
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

		delAccount : function(id) {
			var that = this;
			that.$q.dialog({
				title : '提示',
				message : '确定要删除吗?',
				cancel : true,
				persistent : true
			}).onOk(function() {
				that.$http.get('/rbac/delBackgroundAccount', {
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

		updateAccount : function() {
			var that = this;
			var selectedAccount = that.selectedAccount
			if (selectedAccount.userName === null || selectedAccount.userName === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入账号'
				});
				return;
			}
			if (selectedAccount.state === null || selectedAccount.state === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入状态'
				});
				return;
			}
			that.$http.post('/rbac/updateBackgroundAccount', selectedAccount, {
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
			that.$http.get('/rbac/findBackgroundAccountById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedAccount = res.body.data;
				that.showEditDialogFlag = true;
			});
		},

		findStateItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'functionState'
				}
			}).then(function(res) {
				this.functionStateDictItems = res.body.data;
			});
		},

		addAccount : function() {
			var that = this;
			var selectedAccount = that.selectedAccount;
			if (selectedAccount.userName === null || selectedAccount.userName === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入账号'
				});
				return;
			}
			if (selectedAccount.loginPwd === null || selectedAccount.loginPwd === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入登录密码'
				});
				return;
			}
			that.$http.post('/rbac/addBackgroundAccount', selectedAccount, {
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
			this.selectedAccount = {
				userName : '',
				loginPwd : ''
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
			that.$http.get('/rbac/findBackgroundAccountByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					userName : that.userName
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