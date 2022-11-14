Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'name',
				field : 'name',
				label : '角色名'
			}, {
				align : 'left',
				name : 'createTime',
				field : 'createTime',
				label : '创建时间'
			}, {
				align : 'left',
				name : 'action',
				label : '操作'
			} ],
			tableData : [],
			showAddDialogFlag : false,
			selectedRole : '',
			selectedRoleId : '',
			showEditDialogFlag : false,
			showAssignRoleDialogFlag : false,
			menuTrees : [],
			selectedMenuIds : [],
			selectedAllMenu : false,
		}
	},
	mounted : function() {
		this.findMenuTree();
		this.loadTableData({});
	},
	methods : {
		
		toggleSelectedAllMenu : function() {
			if (this.selectedAllMenu) {
				var selectedMenuIds = [];
				for (var i = 0; i < this.menuTrees.length; i++) {
					var menuTree = this.menuTrees[i];
					for (var j = 0; j < menuTree.subMenus.length; j++) {
						var subMenu = menuTree.subMenus[j];
						selectedMenuIds.push(subMenu.id);
					}
					selectedMenuIds.push(menuTree.id);
				}
				this.selectedMenuIds = selectedMenuIds;
			} else {
				this.selectedMenuIds = [];
			}
		},

		assignMenu : function() {
			var that = this;
			that.$http.post('/rbac/assignMenu', {
				roleId : that.selectedRoleId,
				menuIds : that.selectedMenuIds
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

		findMenuTree : function() {
			var that = this;
			that.$http.get('/rbac/findMenuTree').then(function(res) {
				that.menuTrees = res.body.data;
			});
		},

		showAssignMenuDialog : function(id) {
			var that = this;
			that.showAssignRoleDialogFlag = true;
			that.selectedAllMenu = false;
			that.selectedRoleId = id;
			that.$http.get('/rbac/findMenuByRoleId', {
				params : {
					roleId : id
				}
			}).then(function(res) {
				var selectedMenus = res.body.data;
				var selectedMenuIds = [];
				for (var i = 0; i < selectedMenus.length; i++) {
					var selectedMenu = selectedMenus[i];
					for (var j = 0; j < selectedMenu.subMenus.length; j++) {
						var subMenu = selectedMenu.subMenus[j];
						selectedMenuIds.push(subMenu.id);
					}
					selectedMenuIds.push(selectedMenu.id);
				}
				that.selectedMenuIds = selectedMenuIds;
			});
		},

		delRole : function(id) {
			var that = this;
			that.$q.dialog({
				title : '提示',
				message : '确定要删除吗?',
				cancel : true,
				persistent : true
			}).onOk(function() {
				that.$http.get('/rbac/delRole', {
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

		updateRole : function() {
			var that = this;
			var selectedRole = that.selectedRole
			if (selectedRole.name === null || selectedRole.name === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入角色名'
				});
				return;
			}
			that.$http.post('/rbac/addOrUpdateRole', selectedRole, {
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
			that.$http.get('/rbac/findRoleById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedRole = res.body.data;
				that.showEditDialogFlag = true;
			});
		},

		addRole : function() {
			var that = this;
			var selectedRole = that.selectedRole;
			if (selectedRole.name === null || selectedRole.name === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入角色名'
				});
				return;
			}
			that.$http.post('/rbac/addOrUpdateRole', selectedRole, {
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
			this.selectedRole = {
				name : ''
			};
		},

		refreshTable : function() {
			this.loadTableData({});
		},

		loadTableData : function(param) {
			var that = this;
			that.tableDataLoading = true;
			that.$http.get('/rbac/findAllRole', {
				params : {}
			}).then(function(res) {
				var data = res.body.data;
				that.tableData = data;
				that.tableDataLoading = false;
			});
		}

	},
});