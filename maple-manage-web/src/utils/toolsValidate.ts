export function verifyNumberPercentage(val: string): string {

	let v = val.replace(/(^\s*)|(\s*$)/g, '');

	v = v.replace(/[^\d]/g, '');

	v = v.replace(/^0/g, '');

	v = v.replace(/^[1-9]\d\d{1,3}$/, '100');

	return v;
}

export function verifyNumberPercentageFloat(val: string): string {
	let v = verifyNumberIntegerAndFloat(val);

	v = v.replace(/^[1-9]\d\d{1,3}$/, '100');

	v = v.replace(/^100\.$/, '100');

	return v;
}

export function verifyNumberIntegerAndFloat(val: string) {

	let v = val.replace(/(^\s*)|(\s*$)/g, '');

	v = v.replace(/[^\d.]/g, '');

	v = v.replace(/^0{2}$/g, '0');

	v = v.replace(/^\./g, '');

	v = v.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');

	v = v.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');

	return v;
}

export function verifiyNumberInteger(val: string) {

	let v = val.replace(/(^\s*)|(\s*$)/g, '');

	v = v.replace(/[\.]*/g, '');

	v = v.replace(/(^0[\d]*)$/g, '0');

	v = v.replace(/^0\d$/g, '0');

	v = v.replace(/[^\d]/g, '');

	return v;
}

export function verifyCnAndSpace(val: string) {

	let v = val.replace(/[\u4e00-\u9fa5\s]+/g, '');

	v = v.replace(/(^\s*)|(\s*$)/g, '');

	return v;
}

export function verifyEnAndSpace(val: string) {

	let v = val.replace(/[a-zA-Z]+/g, '');

	v = v.replace(/(^\s*)|(\s*$)/g, '');

	return v;
}

export function verifyAndSpace(val: string) {

	let v = val.replace(/(^\s*)|(\s*$)/g, '');

	return v;
}

export function verifyNumberComma(val: string) {

	let v: any = verifyNumberIntegerAndFloat(val);

	v = v.toString().split('.');

	v[0] = v[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');

	v = v.join('.');

	return v;
}

export function verifyTextColor(val: string, text = '', color = 'red') {

	let v = text.replace(new RegExp(val, 'gi'), `<span style='color: ${color}'>${val}</span>`);

	return v;
}

export function verifyNumberCnUppercase(val: any, unit = '仟佰拾亿仟佰拾万仟佰拾元角分', v = '') {

	val += '00';

	let lookup = val.indexOf('.');

	if (lookup >= 0) val = val.substring(0, lookup) + val.substr(lookup + 1, 2);

	unit = unit.substr(unit.length - val.length);

	for (let i = 0; i < val.length; i++) {
		v += '零壹贰叁肆伍陆柒捌玖'.substr(val.substr(i, 1), 1) + unit.substr(i, 1);
	}

	v = v
		.replace(/零角零分$/, '整')
		.replace(/零[仟佰拾]/g, '零')
		.replace(/零{2,}/g, '零')
		.replace(/零([亿|万])/g, '$1')
		.replace(/零+元/, '元')
		.replace(/亿零{0,3}万/, '亿')
		.replace(/^元/, '零元');

	return v;
}

export function verifyPhone(val: string) {

	if (!/^((12[0-9])|(13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0|1,5-9]))\d{8}$/.test(val)) return false;

	else return true;
}

export function verifyTelPhone(val: string) {

	if (!/\d{3}-\d{8}|\d{4}-\d{7}/.test(val)) return false;

	else return true;
}

export function verifyAccount(val: string) {

	if (!/^[a-zA-Z][a-zA-Z0-9_]{4,15}$/.test(val)) return false;

	else return true;
}

export function verifyPassword(val: string) {

	if (!/^[a-zA-Z]\w{5,15}$/.test(val)) return false;

	else return true;
}

export function verifyPasswordPowerful(val: string) {

	if (!/^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&\.*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&\.*]+$)(?![\d!@#$%^&\.*]+$)[a-zA-Z\d!@#$%^&\.*]{6,16}$/.test(val))
		return false;

	else return true;
}

export function verifyPasswordStrength(val: string) {
	let v = '';

	if (/^(?:\d+|[a-zA-Z]+|[!@#$%^&\.*]+){6,16}$/.test(val)) v = '弱';

	if (/^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&\.*]+$)[a-zA-Z\d!@#$%^&\.*]{6,16}$/.test(val)) v = '中';

	if (/^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&\.*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&\.*]+$)(?![\d!@#$%^&\.*]+$)[a-zA-Z\d!@#$%^&\.*]{6,16}$/.test(val))
		v = '强';

	return v;
}

export function verifyIPAddress(val: string) {

	if (
		!/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(
			val
		)
	)
		return false;

	else return true;
}

export function verifyEmail(val: string) {

	if (
		!/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
			val
		)
	)
		return false;

	else return true;
}

export function verifyIdCard(val: string) {

	if (!/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(val)) return false;

	else return true;
}

export function verifyFullName(val: string) {

	if (!/^[\u4e00-\u9fa5]{1,6}(·[\u4e00-\u9fa5]{1,6}){0,2}$/.test(val)) return false;

	else return true;
}

export function verifyPostalCode(val: string) {

	if (!/^[1-9][0-9]{5}$/.test(val)) return false;

	else return true;
}

export function verifyUrl(val: string) {

	if (
		!/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test(
			val
		)
	)
		return false;

	else return true;
}

export function verifyCarNum(val: string) {

	if (
		!/^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$/.test(
			val
		)
	)
		return false;

	else return true;
}
