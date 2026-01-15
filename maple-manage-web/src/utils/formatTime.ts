export function formatDate(date: Date, format: string): string {
	let we = date.getDay(); 
	let z = getWeek(date); 
	let qut = Math.floor((date.getMonth() + 3) / 3).toString(); 
	const opt: { [key: string]: string } = {
		'Y+': date.getFullYear().toString(), 
		'm+': (date.getMonth() + 1).toString(), 
		'd+': date.getDate().toString(), 
		'H+': date.getHours().toString(), 
		'M+': date.getMinutes().toString(), 
		'S+': date.getSeconds().toString(), 
		'q+': qut, 
	};

	const week: { [key: string]: string } = {
		'0': '日',
		'1': '一',
		'2': '二',
		'3': '三',
		'4': '四',
		'5': '五',
		'6': '六',
	};

	const quarter: { [key: string]: string } = {
		'1': '一',
		'2': '二',
		'3': '三',
		'4': '四',
	};
	if (/(W+)/.test(format))
		format = format.replace(RegExp.$1, RegExp.$1.length > 1 ? (RegExp.$1.length > 2 ? '星期' + week[we] : '周' + week[we]) : week[we]);
	if (/(Q+)/.test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 4 ? '第' + quarter[qut] + '季度' : quarter[qut]);
	if (/(Z+)/.test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 3 ? '第' + z + '周' : z + '');
	for (let k in opt) {
		let r = new RegExp('(' + k + ')').exec(format);

		if (r) format = format.replace(r[1], RegExp.$1.length == 1 ? opt[k] : opt[k].padStart(RegExp.$1.length, '0'));
	}
	return format;
}

export function getWeek(dateTime: Date): number {
	let temptTime = new Date(dateTime.getTime());

	let weekday = temptTime.getDay() || 7;

	temptTime.setDate(temptTime.getDate() - weekday + 1 + 5);
	let firstDay = new Date(temptTime.getFullYear(), 0, 1);
	let dayOfWeek = firstDay.getDay();
	let spendDay = 1;
	if (dayOfWeek != 0) spendDay = 7 - dayOfWeek + 1;
	firstDay = new Date(temptTime.getFullYear(), 0, 1 + spendDay);
	let d = Math.ceil((temptTime.valueOf() - firstDay.valueOf()) / 86400000);
	let result = Math.ceil(d / 7);
	return result;
}

export function formatPast(param: string | Date, format: string = 'YYYY-mm-dd'): string {

	let t: any, s: number;

	let time: number = new Date().getTime();

	typeof param === 'string' || 'object' ? (t = new Date(param).getTime()) : (t = param);

	time = Number.parseInt(`${time - t}`);
	if (time < 10000) {

		return '刚刚';
	} else if (time < 60000 && time >= 10000) {

		s = Math.floor(time / 1000);
		return `${s}秒前`;
	} else if (time < 3600000 && time >= 60000) {

		s = Math.floor(time / 60000);
		return `${s}分钟前`;
	} else if (time < 86400000 && time >= 3600000) {

		s = Math.floor(time / 3600000);
		return `${s}小时前`;
	} else if (time < 259200000 && time >= 86400000) {

		s = Math.floor(time / 86400000);
		return `${s}天前`;
	} else {

		let date = typeof param === 'string' || 'object' ? new Date(param) : param;
		return formatDate(date, format);
	}
}

export function formatAxis(param: Date): string {
	let hour: number = new Date(param).getHours();
	if (hour < 6) return '凌晨好';
	else if (hour < 9) return '早上好';
	else if (hour < 12) return '上午好';
	else if (hour < 14) return '中午好';
	else if (hour < 17) return '下午好';
	else if (hour < 19) return '傍晚好';
	else if (hour < 22) return '晚上好';
	else return '夜里好';
}

export function parseDate(cellValue: any) {
	if (cellValue == null || cellValue == "") return "";
	let date = new Date(cellValue)
	let year = date.getFullYear()
	let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
	let day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
	return year + '-' + month + '-' + day
}

export function parseDateTime(cellValue: any) {
	if (cellValue == null || cellValue == "") return "";
	let date = new Date(cellValue)
	let year = date.getFullYear()
	let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
	let day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
	let hour = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
	let minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
	let second = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
	return year + '-' + month + '-' + day + " " + hour + ":" + minutes + ":" + second
}
