package com.reactnativeeasypay

import com.facebook.react.bridge.*
import java.lang.Runnable
import com.facebook.react.bridge.WritableMap

import com.alipay.sdk.app.PayTask
import com.facebook.react.bridge.ReactMethod
import com.tencent.mm.opensdk.modelpay.PayReq

import com.tencent.mm.opensdk.openapi.WXAPIFactory

import com.tencent.mm.opensdk.openapi.IWXAPI

import com.facebook.react.bridge.ReadableMap








class EasyPayModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    var WX_APPID = ""

    override fun getName(): String {
        return "EasyPay"
    }

    @ReactMethod
    fun setWxId(id: String) {
      WX_APPID = id
    }

    @ReactMethod
    fun wxPay(params: ReadableMap, callback: Callback) {
      val api = WXAPIFactory.createWXAPI(currentActivity, WX_APPID)
      val req = PayReq()
      req.appId = WX_APPID
      req.partnerId = params.getString("partnerId")
      req.prepayId = params.getString("prepayId")
      req.packageValue = params.getString("packageValue")
      req.nonceStr = params.getString("nonceStr")
      req.timeStamp = params.getString("timeStamp")
      req.sign = params.getString("sign")
      api.registerApp(WX_APPID)
      XWXPayEntryActivity.callback = WXPayCallBack { result -> callback.invoke(result) }
      //发起请求
      api.sendReq(req)
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

  companion object {
    lateinit var WX_APPID: String
  }


}
