Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			mobile : '',
			inviterMobile : '',
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'account_info',
				label : '账号信息'
			}, {
				align : 'left',
				name : 'fund_info',
				label : '资金信息'
			}, {
				align : 'left',
				name : 'wallet_addr',
				label : '区块链地址'
			}, {
				align : 'left',
				name : 'real_name_info',
				label : '实名信息'
			}, {
				align : 'left',
				name : 'security_setting',
				label : '账号安全'
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
			collections : [],
			collectionId : '',
			showAirDropDialogFlag : false,
		}
	},
	mounted : function() {
		var that = this;
		that.findFunctionStateItem();
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

		syncChain : function(id) {
			var that = this;
			that.$http.get('/member/createBlockChainAddr', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.refreshTable();
				that.$q.dialog({
					title : '上链返回结果',
					ok : '确定',
					prompt : {
						model : res.body.data,
						type : 'textarea'
					},
					persistent : true
				}).onOk(function(data) {
					that.refreshTable();
				});
			});
		},

		airDrop : function() {
			var that = this;
			if (that.collectionId === null || that.collectionId === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择空投的艺术品'
				});
				return;
			}
			that.$http.post('/airDrop/airDrop', {
				collectionId : that.collectionId,
				memberId : that.selectedAccountId
			}, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.showAirDropDialogFlag = false;
				that.refreshTable();
			});
		},

		showAirDropDialog : function(id) {
			this.showAirDropDialogFlag = true;
			this.selectedAccountId = id;
			this.collectionId = '';
		},

		findAllCollection : function() {
			var that = this;
			that.$http.get('/collection/findAllCollection', {
				params : {}
			}).then(function(res) {
				this.collections = res.body.data;
			});
		},

		findFunctionStateItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'functionState'
				}
			}).then(function(res) {
				this.functionStateDictItems = res.body.data;
			});
		},

		getAvatar : function(avatar) {
			return avatar ? '/storage/fetch/' + avatar : '/images/avatar.png';
		},

		balanceChangeLog : function(member) {
			window.location.href = '/page/member-balance-change-log?mobile=' + member.mobile;
		},

		reduceBalance : function(id) {
			var that = this;
			that.$q.dialog({
				title : '请输入要减少的金额(余额)',
				ok : '确定',
				cancel : '取消',
				prompt : {
					model : '',
					isValid : function(val) {
						return val != null && val != '' && val >= 0;
					},
					type : 'number'
				},
				persistent : true
			}).onOk(function(data) {
				that.$http.post('/member/reduceBalance', {
					id : id,
					amount : data
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

		addBalance : function(id) {
			var that = this;
			that.$q.dialog({
				title : '请输入要增加的金额(余额)',
				ok : '确定',
				cancel : '取消',
				prompt : {
					model : '',
					isValid : function(val) {
						return val != null && val != '' && val >= 0;
					},
					type : 'number'
				},
				persistent : true
			}).onOk(function(data) {
				that.$http.post('/member/addBalance', {
					id : id,
					amount : data
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

		updatePayPwd : function(id) {
			var that = this;
			that.$q.dialog({
				title : '请输入修改后的支付密码',
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
				that.$http.post('/member/modifyPayPwd', {
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
				that.$http.get('/member/delMember', {
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
			if (selectedAccount.nickName === null || selectedAccount.nickName === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入昵称'
				});
				return;
			}
			if (selectedAccount.mobile === null || selectedAccount.mobile === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入手机号'
				});
				return;
			}
			if (selectedAccount.state === null || selectedAccount.state === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择状态'
				});
				return;
			}
			that.$http.post('/member/updateMember', selectedAccount, {
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
			that.$http.get('/member/findMemberById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedAccount = res.body.data;
				that.showEditDialogFlag = true;
			});
		},

		findFunctionStateItem : function() {
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
			if (selectedAccount.nickName === null || selectedAccount.nickName === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入昵称'
				});
				return;
			}
			if (selectedAccount.mobile === null || selectedAccount.mobile === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入手机号'
				});
				return;
			}
			that.$http.post('/member/addMember', selectedAccount, {
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
				nickName : '',
				mobile : '',
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
			that.$http.get('/member/findMemberByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					mobile : that.mobile,
					inviterMobile : that.inviterMobile
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