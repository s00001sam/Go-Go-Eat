package com.sam.gogoeat.api.resp.base;

import com.squareup.moshi.Json;

public class RespStatus {

    public static final int CODE_SUCCESS = StatusCode.STATUS_CODE;
    public static final int CODE_ERROR_JSON_ERROR = StatusCode.CODE_ERROR_JSON_ERROR;

    public static final String MSG_SUCCESS = "success.";
    public static final String MSG_ERROR_BRAND_ID = "initialization failed, wrong, or miss brand id";
    public static final String MSG_ERROR_DEVICE_ID = "initialization failed, can't get device id. call MobileApi.init() first.";
    public static final String MSG_ERROR_INTERNET = "bad internet.";
    public static final String MSG_ERROR_TIME_OUT = "connection time out.";
    public static final String MSG_ERROR_NOT_FOUND404 = "connect not found 404";
    public static final String MSG_ERROR_MISS_USERINFO = "miss user info, need login first.";
    public static final String MSG_ERROR_JSON_ERROR = "json data parser error.";

    public static RespStatus createJsonParserError() {
        RespStatus status = new RespStatus();
        status.setCode(CODE_ERROR_JSON_ERROR);
        status.setMsg(MSG_ERROR_JSON_ERROR);
        return status;
    }

    public static RespStatus success() {
        RespStatus status = new RespStatus();
        status.setCode(CODE_SUCCESS);
        status.setMsg(MSG_SUCCESS);
        return status;
    }

    public static RespStatus connectNotFount() {
        RespStatus status = new RespStatus();
        status.setCode(StatusCode.CODE_ERROR_NOT_FOUND404);
        status.setMsg(MSG_ERROR_NOT_FOUND404);
        return status;
    }

    public static RespStatus connectTimeOut() {
        RespStatus status = new RespStatus();
        status.setCode(StatusCode.CODE_ERROR_TIME_OUT);
        status.setMsg(MSG_ERROR_TIME_OUT);
        return status;
    }

    public static RespStatus exception(String msg) {
        RespStatus status = new RespStatus();
        status.setCode(StatusCode.CODE_ERROR_EXCEPTION);
        status.setMsg(msg);
        return status;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Json(name = "msg")
    protected String msg;

    @Json(name = "code")
    protected int code;

    public int taskId;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    /**
     * @return is base status success
     */
    public boolean isSuccess() {
        return code == CODE_SUCCESS || StatusCode.SESSION_RESULT_CONTAINS_ERRORS == code;
    }

    @Override
    public String toString() {
        return "RespStatus{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                "}";
    }
}
