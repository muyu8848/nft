var accountManageVM = new Vue({
	el : '#account-manage',
	data : {
		userName : '',
		googleVerCode : '',
		googleAuthBindTime : null,
		googleSecretKey : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.getAccountInfo();
	},
	methods : {

		getAccountInfo : function() {
			var that = this;
			that.$http.get('/rbac/getAccountInfo').then(function(res) {
				that.userName = res.body.data.userName;
				that.googleVerCode = null;
				that.googleAuthBindTime = res.body.data.googleAuthBindTime;
				if (that.googleAuthBindTime == null) {
					layer.alert('首次绑定,系统自动分配谷歌密钥', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					that.generateGoogleSecretKey();
				}
			});
		},

		generateGoogleSecretKey : function() {
			var that = this;
			that.$http.get('/rbac/generateGoogleSecretKey').then(function(res) {
				that.googleSecretKey = res.body.data;
				that.generateGoogleQRcode();
			});
		},

		generateGoogleQRcode : function() {
			var that = this;
			that.$nextTick(function() {
				$('.code-content').html('');
				jQuery('.code-content').qrcode({
					width : 200,
					height : 200,
					text : 'otpauth://totp/' + that.toUtf8(that.userName) + '?secret=' + that.googleSecretKey
				});
			});
		},

		toUtf8 : function(str) {
			var out, i, len, c;
			out = '';
			len = str.length;
			for (i = 0; i < len; i++) {
				c = str.charCodeAt(i);
				if ((c >= 0x0001) && (c <= 0x007F)) {
					out += str.charAt(i);
				} else if (c > 0x07FF) {
					out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
					out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
					out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
				} else {
					out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
					out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
				}
			}
			return out;
		},

		bindGoogleAuth : function() {
			var that = this;
			if (that.googleVerCode === null || that.googleVerCode === '') {
				layer.alert('请输入谷歌验证码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/rbac/bindGoogleAuth', {
				googleSecretKey : that.googleSecretKey,
				googleVerCode : that.googleVerCode
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('绑定成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.getAccountInfo();
			});
		},

	}
});