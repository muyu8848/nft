Vue.http.interceptors.push(function(request) {
	var tokenName = window.sessionStorage.getItem('tokenName');
	var tokenValue = window.sessionStorage.getItem('tokenValue');
	if (tokenName != null && tokenName != '') {
		request.headers.set(tokenName, tokenValue);
	}
	return function(response) {
		if (response.body.code == 999) {
			appVM.$q.notify({
				progress : true,
				timeout : 1000,
				type : 'negative',
				message : '系统检测到你已离线,请重新登录!',
				onDismiss : function() {
					window.sessionStorage.removeItem('token');
					window.location.href = '/page/login';
				}
			});
		} else if (response.body.code != null && response.body.code != 200) {
			response.ok = false;
			appVM.$q.dialog({
				title : '提示',
				message : response.body.msg
			}).onOk(function() {
			});
		}
	};
});

/**
 * 由于js存在精度丢失的问题,需要对其进行四舍五入处理
 * 
 * @param num
 * @param digit
 *            小数位数, 不填则默认4为小数
 * @returns
 */
function numberFormat(num, digit) {
	if (digit == null) {
		digit = 4;
	}
	return parseFloat(Number(num).toFixed(digit));
}

/**
 * 获取url参数
 * 
 * @param name
 * @returns
 */
function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
