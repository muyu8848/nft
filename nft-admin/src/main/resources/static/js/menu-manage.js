Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			showAddDialogFlag : false,
			selectedMenu : '',
			selectedMenuId : '',
			showEditDialogFlag : false,
			showDelDialogFlag : false,
			menuTrees : [],
		}
	},
	mounted : function() {
		this.findMenuTree();
	},
	methods : {

		findMenuTree : function() {
			var that = this;
			that.$http.get('/rbac/findMenuTree').then(function(res) {
				that.menuTrees = res.body.data;
			});
		},

		delMenu : function() {
			var that = this;
			that.$http.get('/rbac/delMenu', {
				params : {
					id : that.selectedMenuId
				}
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showDelDialogFlag = false;
				that.findMenuTree();
			});
		},

		showDelDialog : function(id) {
			this.showDelDialogFlag = true;
			this.selectedMenuId = id;
		},

		updateMenu : function() {
			var that = this;
			var selectedMenu = that.selectedMenu
			if (selectedMenu.name === null || selectedMenu.name === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入菜单名'
				});
				return;
			}
//			if (selectedMenu.url === null || selectedMenu.url === '') {
//				that.$q.notify({
//					type : 'negative',
//					message : '请输入url'
//				});
//				return;
//			}
			if (selectedMenu.orderNo === null || selectedMenu.orderNo === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入排序号'
				});
				return;
			}
			that.$http.post('/rbac/addOrUpdateMenu', selectedMenu, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showEditDialogFlag = false;
				that.findMenuTree();
			});
		},

		showEditDialog : function(id) {
			var that = this;
			that.$http.get('/rbac/findMenuById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedMenu = res.body.data;
				that.showEditDialogFlag = true;
			});
		},

		addMenu : function() {
			var that = this;
			var selectedMenu = that.selectedMenu;
			if (selectedMenu.name === null || selectedMenu.name === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入菜单名'
				});
				return;
			}
//			if (selectedMenu.url === null || selectedMenu.url === '') {
//				that.$q.notify({
//					type : 'negative',
//					message : '请输入url'
//				});
//				return;
//			}
			if (selectedMenu.orderNo === null || selectedMenu.orderNo === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入排序号'
				});
				return;
			}
			that.$http.post('/rbac/addOrUpdateMenu', selectedMenu, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showAddDialogFlag = false;
				that.findMenuTree();
			});
		},

		showAddDialog : function(parentId, parentName) {
			this.showAddDialogFlag = true;
			this.selectedMenu = {
				name : '',
				url : '',
				orderNo : '',
				parentId : parentId,
				parentName : parentName,
				type : parentId === null ? 'menu_1' : 'menu_2'
			};
		}

	},
});