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
				name : 'userName',
				field : 'userName',
				label : '账号'
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
		}
	},
	mounted : function() {
		this.loadTableData({});
	},
	methods : {

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
				that.$http.post('/rbac/modifySuperAdminLoginPwd', {
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

		refreshTable : function() {
			this.loadTableData({});
		},

		loadTableData : function(param) {
			var that = this;
			that.tableDataLoading = true;
			that.$http.get('/rbac/findSuperAdmin', {
				params : {}
			}).then(function(res) {
				var data = res.body.data;
				that.tableData = data;
				that.tableDataLoading = false;
			});
		}

	},
});