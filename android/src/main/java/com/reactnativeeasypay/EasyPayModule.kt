package com.reactnativeeasypay

import com.facebook.react.bridge.*
import java.lang.Runnable
import com.facebook.react.bridge.WritableMap

import com.alipay.sdk.app.PayTask


class EasyPayModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "EasyPay"
    }

    @ReactMethod
    fun setAlipayScheme(scheme:String) {
      return
    }

    @ReactMethod
    fun alipay(orderInfo:String, promise: Callback) {
      val payRunnable = Runnable {
        val alipay = PayTask(currentActivity)
        val result = alipay.payV2(orderInfo, true)
        val map = Arguments.createMap()
        map.putString("memo", result["memo"])
        map.putString("result", result["result"])
        map.putString("resultStatus", result["resultStatus"])
        promise.invoke(map)
      }
      val payThread = Thread(payRunnable)
      payThread.start()
    }
}
