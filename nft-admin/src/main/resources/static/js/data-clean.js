Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : {
		selectedDataTypes : [],
		dataTypes : [ {
			value : 'memberBalanceChangeLog',
			label : '会员资金流水'
		}, {
			value : 'smsSendRecord',
			label : '短信发送情况'
		}, {
			value : 'loginLog',
			label : '登录日志'
		}, {
			value : 'operLog',
			label : '操作日志'
		} ],
		startTime : '',
		endTime : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
	},
	methods : {

		dataClean : function() {
			var that = this;
			that.$q.dialog({
				title : '提示',
				message : '确定要清理数据吗?',
				cancel : true,
				persistent : true
			}).onOk(function() {
				that.dataCleanInner();
			});
		},

		dataCleanInner : function() {
			var that = this;
			if (that.selectedDataTypes.length == 0) {
				that.$q.notify({
					type : 'negative',
					message : '请选择数据类型'
				});
				return;
			}
			if (that.startTime === null || that.startTime === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择需要清理的日期范围'
				});
				return;
			}
			if (that.endTime === null || that.endTime === '') {
				that.$q.notify({
					type : 'negative',
					message : '请选择需要清理的日期范围'
				});
				return;
			}
			that.$http.post('/dataClean/clean', {
				dataTypes : that.selectedDataTypes,
				startTime : that.startTime,
				endTime : that.endTime
			}).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
			});
		}
	}
});