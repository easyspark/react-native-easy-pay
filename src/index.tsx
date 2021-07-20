import { NativeModules } from 'react-native';

type EasyPayType = {
  alipay(orderInfo: string, cb: (result: any) => void): void;
  setAlipayScheme(scheme: string): void;
};

const { EasyPay } = NativeModules;

export default EasyPay as EasyPayType;
