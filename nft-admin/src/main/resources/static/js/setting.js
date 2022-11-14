Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			tabs : [ {
				name : 'system',
				label : '系统设置',
			}, {
				name : 'chain',
				label : '区块链',
			} ],
			tab : 'system',
			systemSetting : '',
			currentInUseChain : '',
			chainTypes : [ {
				name : '暂不上链',
				value : 'noneChain'
			}],
		}
	},
	watch : {
		tab : {
			handler : function(newVal, oldVal) {
				if (newVal == 'system') {
					this.getSystemSetting();
				} else if (newVal == 'chain') {
					this.getCurrentInUseChain();
				}
			},
			immediate : false,
			deep : true
		}
	},
	mounted : function() {
		this.getSystemSetting();
	},
	methods : {

		updateCurrentInUseChain : function() {
			var that = this;
			if (that.currentInUseChain === null || that.currentInUseChain === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择要上的链'
				});
				return;
			}
			that.$http.post('/setting/updateCurrentInUseChain', {
				currentInUseChain : that.currentInUseChain
			}, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.getCurrentInUseChain();
			});
		},

		getCurrentInUseChain : function() {
			var that = this;
			that.$http.get('/setting/getCurrentInUseChain').then(function(res) {
				that.currentInUseChain = res.body.data;
			});
		},

		updateSystemSetting : function() {
			var that = this;
			var systemSetting = that.systemSetting;
			if (systemSetting.appVersion === null || systemSetting.appVersion === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入APP最新版本号'
				});
				return;
			}
			if (systemSetting.appUrl === null || systemSetting.appUrl === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入APP下载地址'
				});
				return;
			}
			if (systemSetting.appSchema === null || systemSetting.appSchema === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入APP Schema'
				});
				return;
			}
			if (systemSetting.h5Gateway === null || systemSetting.h5Gateway === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入h5网关'
				});
				return;
			}
			if (systemSetting.localStoragePath === null || systemSetting.localStoragePath === '') {
				that.$q.notify({
					type : 'negative',
					message : '请输入图片资源存放目录'
				});
				return;
			}
			that.$http.post('/setting/updateSystemSetting', systemSetting, {
				emulateJSON : true
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				that.getSystemSetting();
			});
		},

		getSystemSetting : function() {
			var that = this;
			that.$http.get('/setting/getSystemSetting').then(function(res) {
				that.systemSetting = res.body.data;
			});
		}

	},
});