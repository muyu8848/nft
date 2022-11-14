Quasar.lang.set(Quasar.lang.zhHans)
var appVM = new Vue({
	el : '#q-app',
	data : function() {
		return {
			timeStart : dayjs().subtract(7, 'day').format('YYYY-MM-DD'),
			timeEnd : dayjs().format('YYYY-MM-DD'),
			memberStatisticData : '',
			collectionStatisticData : '',
			selfTradeStatisticData : '',
			marketTradeStatisticData : '',
			everydaySelfTradeData : [],
			everydayMarketTradeData : [],
			selfTradeChart : null,
			marketTradeChart : null
		}
	},
	mounted : function() {
		var that = this;
		that.getMemberStatisticData();
		that.getCollectionStatisticData();
		that.getSelfTradeStatisticData();
		that.getMarketTradeStatisticData();
		that.refreshData();
	},
	methods : {

		getMemberStatisticData : function() {
			var that = this;
			that.$http.get('/member/getMemberStatisticData', {
				params : {}
			}).then(function(res) {
				this.memberStatisticData = res.body.data;
			});
		},

		getSelfTradeStatisticData : function() {
			var that = this;
			that.$http.get('/tradeStatistic/getTradeStatisticData', {
				params : {
					bizMode : '1'
				}
			}).then(function(res) {
				that.selfTradeStatisticData = res.body.data;
			});
		},

		getMarketTradeStatisticData : function() {
			var that = this;
			that.$http.get('/tradeStatistic/getTradeStatisticData', {
				params : {
					bizMode : '2'
				}
			}).then(function(res) {
				that.marketTradeStatisticData = res.body.data;
			});
		},

		moneyFormat : function(money, len) {
			len = len || 2
			if (!money && money !== 0)
				return ''
			if (isNaN(+money))
				return ''
			if (money === 0 || money === '0')
				return '0.' + '0'.repeat(len)
			var arr = (money + '').split('.')
			var intStr = arr[0] ? arr[0] : 0
			var floatStr = arr[1] ? arr[1] : 0
			if (floatStr === 0) {
				floatStr = '0'.repeat(len)
			} else {
				floatStr = (+('0.' + floatStr)).toFixed(len).split('.')[1]
			}
			money = (intStr + '.' + floatStr).replace(/(\d{1,3})(?=(?:\d{3})+\.)/g, `$1,`);
			return money
		},

		getCollectionStatisticData : function() {
			var that = this;
			that.$http.get('/collection/getCollectionStatisticData', {
				params : {}
			}).then(function(res) {
				that.collectionStatisticData = res.body.data;
			});
		},

		refreshSelfTradeChart : function() {
			var that = this;
			if (that.receiptChart == null) {
				var chartDom = document.getElementById('selfTradeChart');
				that.selfTradeChart = echarts.init(chartDom);
				// that.selfTradeChart.on('click', function(event) {
				// if (event.data) {
				// }
				// });
			}
			var amounts = [];
			var everydays = [];
			that.everydaySelfTradeData.map(function(item, index, arr) {
				amounts.push(item.successAmount);
				everydays.push(item.everyday);
			});
			var option = {
				title : {
					text : '平台自营'
				},
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				xAxis : {
					type : 'category',
					data : everydays
				},
				yAxis : {
					type : 'value'
				},
				series : [ {
					data : amounts,
					type : 'bar',
					name : '交易金额',
				} ]
			};
			that.selfTradeChart.setOption(option);
		},

		refreshMarketTradeChart : function() {
			var that = this;
			if (that.marketTradeChart == null) {
				var chartDom = document.getElementById('marketTradeChart');
				that.marketTradeChart = echarts.init(chartDom);
				// that.marketTradeChart.on('click', function(event) {
				// if (event.data) {
				// }
				// });
			}
			var amounts = [];
			var everydays = [];
			that.everydayMarketTradeData.map(function(item, index, arr) {
				amounts.push(item.successAmount);
				everydays.push(item.everyday);
			});
			var option = {
				title : {
					text : '二级市场'
				},
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				xAxis : {
					type : 'category',
					data : everydays
				},
				yAxis : {
					type : 'value'
				},
				series : [ {
					data : amounts,
					type : 'bar',
					name : '交易金额',
					lineStyle : {
						color : '#6be6c1',
					},
					itemStyle : {
						borderColor : '#6be6c1',
						color : '#6be6c1'
					}
				} ]
			};
			that.marketTradeChart.setOption(option);
		},

		setDateRange : function(dateRange) {
			var timeStart = 'all';
			var timeEnd = 'all';
			if (dateRange == 'all') {
				timeStart = null;
				timeEnd = null;
			} else if (dateRange == 'year') {
				timeStart = dayjs().startOf('year').format('YYYY-MM-DD');
				timeEnd = dayjs().endOf('year').format('YYYY-MM-DD');
			} else if (dateRange == 'lastMonth') {
				timeStart = dayjs().subtract(1, 'month').startOf('month').format('YYYY-MM-DD');
				timeEnd = dayjs().subtract(1, 'month').endOf('month').format('YYYY-MM-DD');
			} else if (dateRange == 'month') {
				timeStart = dayjs().startOf('month').format('YYYY-MM-DD');
				timeEnd = dayjs().endOf('month').format('YYYY-MM-DD');
			} else if (dateRange == 'lately7day') {
				timeStart = dayjs().subtract(7, 'day').format('YYYY-MM-DD');
				timeEnd = dayjs().format('YYYY-MM-DD');
			}
			this.timeStart = timeStart;
			this.timeEnd = timeEnd;
			this.refreshData();
		},

		refreshData : function() {
			this.findEverydaySelfTradeData();
			this.findEverydayMarketTradeData();
		},

		findEverydaySelfTradeData : function() {
			var that = this;
			that.$http.get('/tradeStatistic/findEverydayTradeData', {
				params : {
					timeStart : that.timeStart,
					timeEnd : that.timeEnd,
					bizMode : '1'
				}
			}).then(function(res) {
				that.everydaySelfTradeData = res.body.data;
				that.refreshSelfTradeChart();
			});
		},

		findEverydayMarketTradeData : function() {
			var that = this;
			that.$http.get('/tradeStatistic/findEverydayTradeData', {
				params : {
					timeStart : that.timeStart,
					timeEnd : that.timeEnd,
					bizMode : '2'
				}
			}).then(function(res) {
				that.everydayMarketTradeData = res.body.data;
				that.refreshMarketTradeChart();
			});
		}

	},
});