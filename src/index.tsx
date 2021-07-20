import { NativeModules } from 'react-native';

type EasyPayType = {
  multiply(a: number, b: number): Promise<number>;
};

const { EasyPay } = NativeModules;

export default EasyPay as EasyPayType;
