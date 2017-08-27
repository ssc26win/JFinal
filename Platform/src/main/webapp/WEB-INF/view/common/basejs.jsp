<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");

			(function($){  
				$.fn.serializeJson=function(){
					var serializeObj={};
					var array=this.serializeArray();
					var str=this.serialize();
					$(array).each(function(){
						if(serializeObj[this.name]){
							if($.isArray(serializeObj[this.name])){
								serializeObj[this.name].push(this.value);
							}else{
								serializeObj[this.name]=[serializeObj[this.name],this.value];  
							}
						}else{
							serializeObj[this.name]=this.value;
						}
					});
					return serializeObj;
				};
			})(jQuery);

		</script>
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery-ui.custom.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.ui.touch-punch.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/chosen.jquery.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/fuelux/fuelux.spinner.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-datepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-timepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/moment.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/daterangepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-datetimepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap-colorpicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.knob.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.autosize.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.inputlimiter.1.3.1.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.maskedinput.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap-tag.js"></script>

		<script src="${res_url}ace-1.3.3/assets/js/jqGrid/jquery.jqGrid.src.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jqGrid/i18n/grid.locale-en.js"></script>
		
		<!-- ace scripts -->
		
		<!-- ace scripts -->
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.scroller.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.fileinput.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.typeahead.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.spinner.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.treeview.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wizard.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.aside.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.ajax-content.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-box.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-skin.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.searchbox-autocomplete.js"></script>
		
		<script src="${res_url}ace-1.3.3/assets/js/jquery.validate.js"></script>
		<script src="${res_url}js/layer/layer.js"></script>
	<%--	<script src="${res_url}js/jquery.mytime.js"></script>--%>
		<script type="text/javascript">
		String.prototype.trim = function() {
				return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
			}
		</script>
        <script type="text/javascript">
            function openMap(company, address, waterUseNum) {
                layer.open({
                    title:company + '水量信息',
                    type: 2,
                    area: ['70%', '75%'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/map?company='+company+'&address='+address+'&waterUseNum='+waterUseNum
                });
            }
            $(function () {
                $(".form_datetime").datetimepicker({
                    format: 'YYYY-MM-DD hh:mm:ss',
                    locale: moment.locale('zh-cn', {
                        months: '一月_二月_三月_四月_五月_六月_七月_八月_九月_十月_十一月_十二月'.split('_'),
                        monthsShort: '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
                        weekdays: '星期日_星期一_星期二_星期三_星期四_星期五_星期六'.split('_'),
                        weekdaysShort: '周日_周一_周二_周三_周四_周五_周六'.split('_'),
                        weekdaysMin: '日_一_二_三_四_五_六'.split('_'),
                        longDateFormat: {
                            LT: 'Ah点mm分',
                            LTS: 'Ah点m分s秒',
                            L: 'YYYY-MM-DD',
                            LL: 'YYYY年MMMD日',
                            LLL: 'YYYY年MMMD日Ah点mm分',
                            LLLL: 'YYYY年MMMD日ddddAh点mm分',
                            l: 'YYYY-MM-DD',
                            ll: 'YYYY年MMMD日',
                            lll: 'YYYY年MMMD日Ah点mm分',
                            llll: 'YYYY年MMMD日ddddAh点mm分'
                        },
                        meridiemParse: /凌晨|早上|上午|中午|下午|晚上/,
                        meridiemHour: function (h, meridiem) {
                            let hour = h;
                            if (hour === 12) {
                                hour = 0;
                            }
                            if (meridiem === '凌晨' || meridiem === '早上' ||
                                    meridiem === '上午') {
                                return hour;
                            } else if (meridiem === '下午' || meridiem === '晚上') {
                                return hour + 12;
                            } else {
                                // '中午'
                                return hour >= 11 ? hour : hour + 12;
                            }
                        },
                        meridiem: function (hour, minute, isLower) {
                            const hm = hour * 100 + minute;
                            if (hm < 600) {
                                return '凌晨';
                            } else if (hm < 900) {
                                return '早上';
                            } else if (hm < 1130) {
                                return '上午';
                            } else if (hm < 1230) {
                                return '中午';
                            } else if (hm < 1800) {
                                return '下午';
                            } else {
                                return '晚上';
                            }
                        },
                        calendar: {
                            sameDay: function () {
                                return this.minutes() === 0 ? '[今天]Ah[点整]' : '[今天]LT';
                            },
                            nextDay: function () {
                                return this.minutes() === 0 ? '[明天]Ah[点整]' : '[明天]LT';
                            },
                            lastDay: function () {
                                return this.minutes() === 0 ? '[昨天]Ah[点整]' : '[昨天]LT';
                            },
                            nextWeek: function () {
                                let startOfWeek, prefix;
                                startOfWeek = moment().startOf('week');
                                prefix = this.diff(startOfWeek, 'days') >= 7 ? '[下]' : '[本]';
                                return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
                            },
                            lastWeek: function () {
                                let startOfWeek, prefix;
                                startOfWeek = moment().startOf('week');
                                prefix = this.unix() < startOfWeek.unix() ? '[上]' : '[本]';
                                return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
                            },
                            sameElse: 'LL'
                        },
                        ordinalParse: /\d{1,2}(日|月|周)/,
                        ordinal: function (number, period) {
                            switch (period) {
                                case 'd':
                                case 'D':
                                case 'DDD':
                                    return number + '日';
                                case 'M':
                                    return number + '月';
                                case 'w':
                                case 'W':
                                    return number + '周';
                                default:
                                    return number;
                            }
                        },
                        relativeTime: {
                            future: '%s内',
                            past: '%s前',
                            s: '几秒',
                            m: '1 分钟',
                            mm: '%d 分钟',
                            h: '1 小时',
                            hh: '%d 小时',
                            d: '1 天',
                            dd: '%d 天',
                            M: '1 个月',
                            MM: '%d 个月',
                            y: '1 年',
                            yy: '%d 年'
                        },
                        week: {
                            // GB/T 7408-1994《数据元和交换格式·信息交换·日期和时间表示法》与ISO 8601:1988等效
                            dow: 1, // Monday is the first day of the week.
                            doy: 4  // The week that contains Jan 4th is the first week of the year.
                        }
                    })
                });
                $(".form_date").datetimepicker({
                    format: 'YYYY-MM-DD',
                    locale: moment.locale('zh-cn', {
                        months: '一月_二月_三月_四月_五月_六月_七月_八月_九月_十月_十一月_十二月'.split('_'),
                        monthsShort: '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
                        weekdays: '星期日_星期一_星期二_星期三_星期四_星期五_星期六'.split('_'),
                        weekdaysShort: '周日_周一_周二_周三_周四_周五_周六'.split('_'),
                        weekdaysMin: '日_一_二_三_四_五_六'.split('_'),
                        longDateFormat: {
                            LT: 'Ah点mm分',
                            LTS: 'Ah点m分s秒',
                            L: 'YYYY-MM-DD',
                            LL: 'YYYY年MMMD日',
                            LLL: 'YYYY年MMMD日Ah点mm分',
                            LLLL: 'YYYY年MMMD日ddddAh点mm分',
                            l: 'YYYY-MM-DD',
                            ll: 'YYYY年MMMD日',
                            lll: 'YYYY年MMMD日Ah点mm分',
                            llll: 'YYYY年MMMD日ddddAh点mm分'
                        },
                        meridiemParse: /凌晨|早上|上午|中午|下午|晚上/,
                        meridiemHour: function (h, meridiem) {
                            let hour = h;
                            if (hour === 12) {
                                hour = 0;
                            }
                            if (meridiem === '凌晨' || meridiem === '早上' ||
                                    meridiem === '上午') {
                                return hour;
                            } else if (meridiem === '下午' || meridiem === '晚上') {
                                return hour + 12;
                            } else {
                                // '中午'
                                return hour >= 11 ? hour : hour + 12;
                            }
                        },
                        meridiem: function (hour, minute, isLower) {
                            const hm = hour * 100 + minute;
                            if (hm < 600) {
                                return '凌晨';
                            } else if (hm < 900) {
                                return '早上';
                            } else if (hm < 1130) {
                                return '上午';
                            } else if (hm < 1230) {
                                return '中午';
                            } else if (hm < 1800) {
                                return '下午';
                            } else {
                                return '晚上';
                            }
                        },
                        calendar: {
                            sameDay: function () {
                                return this.minutes() === 0 ? '[今天]Ah[点整]' : '[今天]LT';
                            },
                            nextDay: function () {
                                return this.minutes() === 0 ? '[明天]Ah[点整]' : '[明天]LT';
                            },
                            lastDay: function () {
                                return this.minutes() === 0 ? '[昨天]Ah[点整]' : '[昨天]LT';
                            },
                            nextWeek: function () {
                                let startOfWeek, prefix;
                                startOfWeek = moment().startOf('week');
                                prefix = this.diff(startOfWeek, 'days') >= 7 ? '[下]' : '[本]';
                                return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
                            },
                            lastWeek: function () {
                                let startOfWeek, prefix;
                                startOfWeek = moment().startOf('week');
                                prefix = this.unix() < startOfWeek.unix() ? '[上]' : '[本]';
                                return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
                            },
                            sameElse: 'LL'
                        },
                        ordinalParse: /\d{1,2}(日|月|周)/,
                        ordinal: function (number, period) {
                            switch (period) {
                                case 'd':
                                case 'D':
                                case 'DDD':
                                    return number + '日';
                                case 'M':
                                    return number + '月';
                                case 'w':
                                case 'W':
                                    return number + '周';
                                default:
                                    return number;
                            }
                        },
                        relativeTime: {
                            future: '%s内',
                            past: '%s前',
                            s: '几秒',
                            m: '1 分钟',
                            mm: '%d 分钟',
                            h: '1 小时',
                            hh: '%d 小时',
                            d: '1 天',
                            dd: '%d 天',
                            M: '1 个月',
                            MM: '%d 个月',
                            y: '1 年',
                            yy: '%d 年'
                        },
                        week: {
                            // GB/T 7408-1994《数据元和交换格式·信息交换·日期和时间表示法》与ISO 8601:1988等效
                            dow: 1, // Monday is the first day of the week.
                            doy: 4  // The week that contains Jan 4th is the first week of the year.
                        }
                    })
                });
            });
        </script>