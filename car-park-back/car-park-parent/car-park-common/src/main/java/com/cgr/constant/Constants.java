package com.cgr.constant;

/**
 * 通用常量信息
 *
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * JSON ContentType
     */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key:";

    /**
     * 验证码缓存前缀
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    public static final Long USER_INFO_TTL = 10L;

    public static final String AUTHORIZATION = "Authorization";


    // 临时用户id
    public static final Long TEMP_USER_ID = 99999L;
}
