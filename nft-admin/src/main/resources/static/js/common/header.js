Quasar.lang.set(Quasar.lang.zhHans)
var headerVM = new Vue({
	el : '#header',
	data : function() {
		return {
			userName : '',
			currentPathName : '',
			menus : [],
		}
	},
	mounted : function() {
		var that = this;
		that.currentPathName = window.location.pathname;
		that.findLoginAccountMenuTree();
		that.getLoginAccountInfo();
	},
	methods : {
		
		logout : function() {
			var that = this;
			that.$http.post('/logout').then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '退出成功',
					onDismiss : function() {
						window.sessionStorage.removeItem('token');
						window.location.href = '/page/login';
					}
				});
			});
		},

		getLoginAccountInfo : function() {
			var that = this;
			that.$http.get('/rbac/getAccountInfo').then(function(res) {
				that.userName = res.body.data.userName;
			});
		},

		refreshCache : function() {
			var that = this;
			var cacheItems = [];
			cacheItems.push('dict*');
			cacheItems.push('findMenuTreeByAccountId_*');
			that.$http.post('/setting/refreshCache', cacheItems).then(function(res) {
				that.$q.notify({
					progress : true,
					timeout : 1000,
					type : 'positive',
					message : '操作成功',
				});
				window.location.reload(); 
			});
		},

		findLoginAccountMenuTree : function() {
			var that = this;
			that.$http.get('/rbac/findMyMenuTree').then(function(res) {
				that.menus = res.body.data;
			});
		},

		hasSubMenu : function(menu) {
			return menu.subMenus.length != 0;
		},

		parentMenuActive : function(subMenus) {
			for (var i = 0; i < subMenus.length; i++) {
				if (subMenus[i].url == this.currentPathName) {
					return true;
				}
			}
			return false;
		},

		navTo : function(url) {
			window.location.href = url;
		}
	},
});