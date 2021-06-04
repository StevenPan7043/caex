// 报错语言
const LangKey = 'User-Lang';
function setLang(data) {
  const LangValue = data;
  localStorage.setItem(LangKey, LangValue);
}

function getLang() {
  return localStorage.getItem(LangKey);
}

// 币种精度
const USDTPrecision = 6;
const CNYPrecision = 2;
const BTCPrecision = 8;
const ZCPrecision = 4;
const ETHPrecision = 6;
const PWRCPrecision = 6;

function customToFixed(num, coinType) {
  let res = '';
  if (coinType === 'USDT') {
    const pow = 10 ** USDTPrecision;
    res = (Math.floor(parseFloat(num) * pow) / pow).toFixed(USDTPrecision);
  }
  if (coinType === 'CNY') {
    const pow = 10 ** CNYPrecision;
    res = (Math.floor(parseFloat(num) * pow) / pow).toFixed(CNYPrecision);
  }
  if (coinType === 'BTC') {
    const pow = 10 ** BTCPrecision;
    res = (Math.floor(parseFloat(num) * pow) / pow).toFixed(BTCPrecision);
  }
  if (coinType === 'ZC') {
    const pow = 10 ** ZCPrecision;
    res = (Math.floor(parseFloat(num) * pow) / pow).toFixed(ZCPrecision);
  }
  if (coinType === 'ETH') {
    const pow = 10 ** ETHPrecision;
    res = (Math.floor(parseFloat(num) * pow) / pow).toFixed(ETHPrecision);
  }
  if (coinType === 'PWRC') {
    const pow = 10 ** PWRCPrecision;
    res = (Math.floor(parseFloat(num) * pow) / pow).toFixed(PWRCPrecision);
  }
  return res;
}


function chooseBackgroundColor(uid) {
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

export {
  setLang, getLang, customToFixed, chooseBackgroundColor,
};
