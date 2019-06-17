/**
 * 
 * 需要每年维护
 * 
 * @param date
 * 
 * @returns
 * 
 */
function WeekDay(date) {
	var arys1 = new Array();
	arys1 = date.split('-'); // 日期为输入日期，格式为 2013-3-10
	var ssdate = new Date(arys1[0], parseInt(arys1[1] - 1), arys1[2]);
	return ssdate.getDay();
}

/**
 * 
 * 节假日（有包含周末）
 * 
 */
var holidays = ['2019-02-04', '2019-02-05', '2019-02-06', '2019-02-07',
	'2019-02-08', '2019-02-09', '2019-02-10', // 春节
	'2019-04-05', '2019-04-06', '2019-04-07', // 清明节
	'2019-05-01', // 劳动节
	'2019-06-07', '2019-06-08', '2019-06-09', // 端午节
	'2019-09-13', '2019-09-14', '2019-09-15', // 中秋节
	'2019-10-01', '2019-10-02', '2019-10-05', '2019-10-06', '2019-10-07' // 国庆节
];

/**
 * 
 * 有些需要工作的周末
 * 
 */
var workweekends = [
	'2019-09-29', '2019-10-12', // 国庆节调班
	'2019-02-02', '2019-02-03' // 春节调班
];

/**
 * 判断是否是法定节假日，是就返回true，不是就返回false
 * @param date
 * @returns
 */
function isLawHoliday(date) {
	for(day in holidays) {
		if(holidays[day] == date)
			return true;
	}
	return false;
}

/**
 * 判断周末是否需要上班，是就返回true，否就返回false
 * @param date
 * @returns
 */
function isWorkweekend(date) {
	for(day in workweekends) {
		if(workweekends[day] == date)
			return true;
	}
	return false;
}

/**
 * 判断是否是节假日
 * @param date
 * @returns
 */
function isHoliday(date) {
	if(isWorkweekend(date))
		return false;
	return(WeekDay(date) % 6 == 0) || isLawHoliday(date);
}
/**
 * 对时间格式进行重写
 * @param {Object} format
 */
Date.prototype.format = function(format) {
	var o = {
		"M+": this.getMonth() + 1, //month
		"d+": this.getDate(), //day
		"h+": this.getHours(), //hour
		"m+": this.getMinutes(), //minute
		"s+": this.getSeconds(), //second
		"q+": Math.floor((this.getMonth() + 3) / 3), //quarter
		"S": this.getMilliseconds() //millisecond
	}
	if(/(y+)/.test(format)) format = format.replace(RegExp.$1,
		(this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1,
				RegExp.$1.length == 1 ? o[k] :
				("00" + o[k]).substr(("" + o[k]).length));
	return format;
}