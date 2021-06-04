// 报错语言
const LangKey = 'User-Lang'
export function setLang(data) {
  const LangValue = data
  localStorage.setItem(LangKey, LangValue)
}

export function getLang() {
  let lang = localStorage.getItem(LangKey)
  if (lang === null) {
    return 'zh'
  } else {
    return lang
  }
}

// 保存交易对
const tradeKey = 'trade-name'
export function setTrade(data) {
  localStorage.setItem(tradeKey, data)
}

export function getTrade() {
  return localStorage.getItem(tradeKey)
}


// 保存是否显示资产
const moneyShowKey = 'money-show'
export function setMoneyShow(data) {
  localStorage.setItem(moneyShowKey, data)
}

export function getMoneyShow() {
  return localStorage.getItem(moneyShowKey)
}

export function chooseBackgroundColor(uid) {
  let colorName = '';
  switch (parseInt(uid, 0) % 10) {
    case 0:
      colorName = '#6198d7';
      break;
    case 1:
      colorName = '#e55e4b';
      break;
    case 2:
      colorName = '#5f736a';
      break;
    case 3:
      colorName = '#b4cb28';
      break;
    case 4:
      colorName = '#8848b0';
      break;
    case 5:
      colorName = '#37bb7b';
      break;
    case 6:
      colorName = '#c19865';
      break;
    case 7:
      colorName = '#7692ab';
      break;
    case 8:
      colorName = '#a35d8f';
      break;
    case 9:
      colorName = '#7373b4';
      break;
    default:
      colorName = '#fff';
      break;
  }
  return colorName;
}
const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}
export function formDate(time) {
  const date = new Date(time)
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()
  return (
    [year, month, day].map(formatNumber).join('/') +
    ' ' +
    [hour, minute, second].map(formatNumber).join(':')
  )
}