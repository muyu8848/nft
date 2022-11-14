Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			name : '',
			commodityTypeDictItems : [],
			commodityType : '',
			tableDataLoading : false,
			tablePagination : {
				page : 1,
				rowsPerPage : 10,
				rowsNumber : 10
			},
			tableColumns : [ {
				align : 'left',
				name : 'name_info',
				label : '艺术品'
			}, {
				align : 'left',
				name : 'story_info',
				label : '作品故事'
			}, {
				align : 'left',
				name : 'price',
				field : 'price',
				label : '价格',
			}, {
				align : 'left',
				name : 'quantity',
				field : 'quantity',
				label : '发行数量',
			}, {
				align : 'left',
				name : 'stock',
				field : 'stock',
				label : '库存',
			}, {
				align : 'left',
				name : 'sale_time',
				label : '发售时间',
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
			creators : [],
			showAddDialogFlag : false,
			selectedRecord : '',
			selectedRecordId : '',
			updateCollectionStoryDialogFlag : false,
			collectionStorys : [],
			viewCollectionStoryDialogFlag : false,

			exchangeCodes : [],
			exchangeCodeDialogFlag : false,

			issuedCollections : [],
			issuedCollectionDialogFlag : false,

			updateMysteryBoxCommodityDialogFlag : false,
			subCommoditys : [],
			allCollections : []
		}
	},
	mounted : function() {
		var that = this;
		this.findCommodityTypeDictItem();
		that.findAllCreator();
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

		findCommodityTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'commodityType'
				}
			}).then(function(res) {
				this.commodityTypeDictItems = res.body.data;
			});
		},

		findAllCollection : function() {
			var that = this;
			that.$http.get('/collection/findAllCollection', {
				params : {}
			}).then(function(res) {
				this.allCollections = res.body.data;
			});
		},

		updateMysteryBoxCommodity : function() {
			var that = this;
			var totalProbability = 0;
			var subCommoditys = that.subCommoditys;
			for (var i = 0; i < subCommoditys.length; i++) {
				var commodityId = subCommoditys[i].commodityId;
				if (commodityId === null || commodityId === '') {
					that.$q.notify({
						type : 'negative',
						message : '请选择藏品'
					});
					return;
				}
				var probability = subCommoditys[i].probability;
				if (probability === null || probability === '') {
					that.$q.notify({
						type : 'negative',
						message : '请输入概率'
					});
					return;
				}
				if (probability <= 0) {
					that.$q.notify({
						type : 'negative',
						message : '概率必须大于0'
					});
					return;
				}
				totalProbability = totalProbability + probability;
			}
			if (subCommoditys.length == 0) {
				that.$q.notify({
					type : 'negative',
					message : '最少添加一个藏品'
				});
				return;
			}
			if (totalProbability != 100) {
				that.$q.notify({
					type : 'negative',
					message : '概率之和必须等于100'
				});
				return;
			}
			that.$http.post('/collection/updateMysteryBoxCommodity', {
				collectionId : that.selectedRecordId,
				subCommoditys : subCommoditys,
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.updateMysteryBoxCommodityDialogFlag = false;
				that.refreshTable();
			});
		},

		addSubCommodity : function() {
			this.subCommoditys.push({
				commodityId : '',
				probability : '',
			});
		},

		removeSubCommodity : function(index) {
			this.subCommoditys.splice(index, 1);
		},

		showUpdateMysteryBoxCommodityDialog : function(id) {
			var that = this;
			that.findAllCollection();
			that.$http.get('/collection/findMysteryBoxCommodity', {
				params : {
					collectionId : id
				}
			}).then(function(res) {
				that.selectedRecordId = id;
				that.subCommoditys = res.body.data;
				that.updateMysteryBoxCommodityDialogFlag = true;
			});
		},

		findIssuedCollection : function() {
			var that = this;
			that.$http.get('/collection/findIssuedCollection', {
				params : {
					collectionId : that.selectedRecordId,
				}
			}).then(function(res) {
				that.issuedCollections = res.body.data;
			});
		},

		showIssuedCollectionDialog : function(id) {
			var that = this;
			that.selectedRecordId = id;
			that.issuedCollectionDialogFlag = true;
			that.findIssuedCollection();
		},

		syncChain : function(id) {
			var that = this;
			that.$http.get('/collection/syncChain', {
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

		generateExchangeCode : function() {
			var that = this;
			that.$q.dialog({
				title : '生成兑换码',
				message : '请输入要生成的个数',
				prompt : {
					model : '',
					isValid : function(val) {
						return val != null && val != '';
					},
					type : 'number'
				},
				cancel : true,
				persistent : true
			}).onOk(function(data) {
				that.$http.post('/exchangeCode/generateExchangeCode', {
					collectionId : that.selectedRecordId,
					quantity : data
				}, {
					emulateJSON : true
				}).then(function(res) {
					that.$q.notify({
						progress : true,
						timeout : 1000,
						type : 'positive',
						message : '操作成功',
					});
					that.findExchangeCode();
				});
			});
		},

		findExchangeCode : function() {
			var that = this;
			that.$http.get('/exchangeCode/findExchangeCode', {
				params : {
					collectionId : that.selectedRecordId,
				}
			}).then(function(res) {
				that.exchangeCodes = res.body.data;
			});
		},

		showExchangeCodeDialog : function(id) {
			var that = this;
			that.selectedRecordId = id;
			that.exchangeCodeDialogFlag = true;
			that.findExchangeCode();
		},

		showViewCollectionStoryDialog : function(selectedRecord) {
			this.collectionStorys = selectedRecord.collectionStorys;
			this.viewCollectionStoryDialogFlag = true;
		},

		updateCollectionStory : function() {
			var that = this;
			var picLinks = [];
			var collectionStorys = that.collectionStorys;
			for (var i = 0; i < collectionStorys.length; i++) {
				var picLink = collectionStorys[i].picLink;
				if (picLink === null || picLink === '') {
					that.$q.notify({
						type : 'negative',
						message : '请输入链接'
					});
					return;
				}
				picLinks.push(picLink);
			}
			if (picLinks.length == 0) {
				that.$q.notify({
					type : 'negative',
					message : '最少上传一张图片'
				});
				return;
			}
			that.$http.post('/collection/updateCollectionStory', {
				collectionId : that.selectedRecordId,
				picLinks : picLinks,
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.updateCollectionStoryDialogFlag = false;
				that.refreshTable();
			});
		},

		addStoryPicLink : function() {
			this.collectionStorys.push({
				picLink : '',
			});
		},

		removeStoryPicLink : function(index) {
			this.collectionStorys.splice(index, 1);
		},

		showUpdateCollectionStoryDialog : function(id) {
			var that = this;
			that.$http.get('/collection/findCollectionById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedRecordId = id;
				that.collectionStorys = res.body.data.collectionStorys;
				that.updateCollectionStoryDialogFlag = true;
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
				that.$http.get('/collection/delCollection', {
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

		findAllCreator : function() {
			var that = this;
			that.$http.get('/creator/findAllCreator', {
				params : {}
			}).then(function(res) {
				this.creators = res.body.data;
			});
		},

		add : function() {
			var that = this;
			var selectedRecord = that.selectedRecord;
			if (selectedRecord.commodityType === null || selectedRecord.commodityType === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择类型'
				});
				return;
			}
			if (selectedRecord.name === null || selectedRecord.name === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入艺术品名称'
				});
				return;
			}
			if (selectedRecord.cover === null || selectedRecord.cover === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入封面链接'
				});
				return;
			}
			if (selectedRecord.price === null || selectedRecord.price === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入价格'
				});
				return;
			}
			if (selectedRecord.quantity === null || selectedRecord.quantity === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入发行数量'
				});
				return;
			}
			if (!selectedRecord.notExternalSaleFlag) {
				if (selectedRecord.saleTime === null || selectedRecord.saleTime === '') {
					that.$q.notify({
						type : 'negative',
						message : '请输入发售时间'
					});
					return;
				}
			}
			if (selectedRecord.creatorId === null || selectedRecord.creatorId === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择创作者'
				});
				return;
			}
			that.$http.post('/collection/addCollection', selectedRecord, {
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
				commodityType : '',
				name : '',
				cover : '',
				price : '',
				quantity : '',
				saleTime : '',
				notExternalSaleFlag : false,
				creatorId : ''
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
			that.$http.get('/collection/findCollectionByPage', {
				params : {
					pageSize : param.pagination.rowsPerPage,
					pageNum : param.pagination.page,
					commodityType : that.commodityType,
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