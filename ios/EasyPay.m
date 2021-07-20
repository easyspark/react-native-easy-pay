#import "EasyPay.h"
#import <AlipaySDK/AlipaySDK.h>

@implementation EasyPay
{
    NSString *wxOpenId;
    NSString *alipayScheme;
    RCTResponseSenderBlock wxCallBack;
    RCTResponseSenderBlock alipayCallBack;
}

RCT_EXPORT_MODULE(EasyPay)
- (instancetype)init
{
    self = [super init];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleOpenURL:) name:@"RCTOpenURLNotification" object:nil];
    }
    return self;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (BOOL)handleOpenURL:(NSNotification *)aNotification
{
    NSString * aURLString =  [aNotification userInfo][@"url"];
    NSURL * aURL = [NSURL URLWithString:aURLString];
    if ([aURL.host isEqualToString:@"safepay"]) {
        [[AlipaySDK defaultService] processOrderWithPaymentResult:aURL standbyCallback:^(NSDictionary *resultDic) {
            if (alipayCallBack != nil) {
                alipayCallBack([[NSArray alloc] initWithObjects:resultDic, nil]);
                alipayCallBack = nil;
            }
            NSLog(@"result = %@",resultDic);
        }];
    }
    if ([WXApi handleOpenURL:aURL delegate:self])
       {
           return YES;
       } else {
           return NO;
       }
}

RCT_EXPORT_METHOD(setAlipayScheme:(NSString *)scheme){
    alipayScheme = scheme;
}

RCT_EXPORT_METHOD(alipay:(NSString *)info callback:(RCTResponseSenderBlock)callback)
{
    alipayCallBack = callback;
    dispatch_async(dispatch_get_main_queue(), ^{
        
        [[AlipaySDK defaultService] payOrder:info fromScheme:alipayScheme callback:^(NSDictionary *resultDic) {
            NSLog(@"alipay:callback");
            
            callback([[NSArray alloc] initWithObjects:resultDic, nil]);
        }];
    });
}


@end
