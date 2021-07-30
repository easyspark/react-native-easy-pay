import { NativeModules } from 'react-native';

type EasyPayType = {
  wxPay(params: object, cb: (result: any) => void): void;
  alipay(orderInfo: string, cb: (result: any) => void): void;
  setAlipayScheme(scheme: string): void;
  setWxId(wxid: string, universalLink: string): void;
};

const { EasyPay } = NativeModules;

export default EasyPay as EasyPayType;
