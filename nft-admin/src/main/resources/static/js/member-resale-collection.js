Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			memberMobile : '',
			collectionName : '',
			stateDictItems : [],
			state : '',
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'member_info',
				label : '会员信息'
			}, {
				align : 'left',
				name : 'collection_info',
				label : '艺术品'
			}, {
				align : 'left',
				name : 'tech_info',
				label : '技术信息'
			}, {
				align : 'left',
				name : 'stateName',
				field : 'stateName',
				label : '状态',
			}, {
				align : 'left',
				name : 'resalePrice',
				field : 'resalePrice',
				label : '售价',
			}, {
				align : 'left',
				name : 'time_info',
				label : '时间',
			} ],
			tableData : [],
		}
	},
	mounted : function() {
		var that = this;
		that.findStateDictItem();
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
					dictTypeCode : 'memberResaleCollectionState'
				}
			}).then(function(res) {
				this.stateDictItems = res.body.data;
			});
		},

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
			that.$http.get('/memberResaleCollection/findMemberResaleCollectionByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					memberMobile : that.memberMobile,
					collectionName : that.collectionName,
					state : that.state,
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