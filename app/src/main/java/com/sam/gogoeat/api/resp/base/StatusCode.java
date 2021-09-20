package com.sam.gogoeat.api.resp.base;

public interface StatusCode {

    int STATUS_CODE_0 = 0;

    int UN_KNOW = 99999999;

    int STATUS_CODE = 100000;

    int STATUS_CODE_NO_12 = 100012;

    // Token Expired http://km.tutorabc.com/display/TAR/Error+Handling
    int TOKEN_EXPIRED = 100013;

    int ERROR_CODE_100208 = 100208;

    int ERROR_CODE_100209 = 100209;

    int ERROR_CODE_100210 = 100210;

    int ERROR_CODE_100101 = 100101;

    // 手机号暂未绑定，请用邮箱登录
    int ERROR_NOT_EXIST = 100103;

    int ERROR_CODE_200011 = 200011;

    int ERROR_CODE_200012 = 200012;

    int ERROR_CODE_200013 = 200013;

    int ERROR_CODE_200014 = 200014;

    int ERROR_CODE_200015 = 200015;

    int ERROR_CODE_200002 = 200002;

    int ERROR_CODE_200003 = 200003;
    int ERROR_CODE_200004 = 200004;
    int ERROR_CODE_200005 = 200005;
    int ERROR_CODE_200006 = 200006;
    int ERROR_CODE_200007 = 200007;

    int ERROR_CODE_200010 = 200010;
    int ERROR_CODE_200017 = 200017;
    int ERROR_CODE_200018 = 200018;
    int ERROR_CODE_200019 = 200019;

    // andrew http://km.tutorabc.com/pages/viewpage.action?pageId=11863687
    int ERROR_CODE_601001 = 601001;
    int ERROR_CODE_601002 = 601002;
    int ERROR_CODE_601003 = 601003;
    int ERROR_CODE_602001 = 602001;
    int ERROR_CODE_602002 = 602002;
    int ERROR_CODE_602003 = 602003;
    int ERROR_CODE_603001 = 603001;
    int ERROR_CODE_603002 = 603002;
    int ERROR_CODE_603003 = 603003;

    // SDK level
    int CODE_ERROR_EXCEPTION = 90000;
    int CODE_ERROR_BRAND_ID = 90001;
    int CODE_ERROR_DEVICE_ID = 90002;
    int CODE_ERROR_INTERNET = 90003;
    int CODE_ERROR_TIME_OUT = 90004;
    int CODE_ERROR_NOT_FOUND404 = 90005;
    int CODE_ERROR_MISS_USERINFO = 90006;
    int CODE_ERROR_JSON_ERROR = 90007;
    int SESSION_RESULT_CONTAINS_ERRORS = 100202;
}
