const cssCdnUrlList: Array<string> = [
	'
	'
];

const jsCdnUrlList: Array<string> = [];

export function setCssCdn() {
	if (cssCdnUrlList.length <= 0) return false;
	cssCdnUrlList.map((v) => {
		let link = document.createElement('link');
		link.rel = 'stylesheet';
		link.href = v;
		link.crossOrigin = 'anonymous';
		document.getElementsByTagName('head')[0].appendChild(link);
	});
}

export function setJsCdn() {
	if (jsCdnUrlList.length <= 0) return false;
	jsCdnUrlList.map((v) => {
		let link = document.createElement('script');
		link.src = v;
		document.body.appendChild(link);
	});
}

const setIntroduction = {

	cssCdn: () => {
		setCssCdn();
	},

	jsCdn: () => {
		setJsCdn();
	},
};

export default setIntroduction;
