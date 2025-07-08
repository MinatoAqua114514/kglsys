package com.lin.kglsys.common.constant;

/**
 * Redis Key常量类
 */
public final class RedisConstants {

    private RedisConstants() {}

    /**
     * 项目Key前缀
     */
    private static final String PROJECT_PREFIX = "kglsys:";

    /**
     * 用户登录信息Key (后接用户ID)
     * 存储结构: String
     * Value: JWT Token
     */
    public static final String USER_LOGIN_TOKEN_KEY = PROJECT_PREFIX + "user:login:token:";

    /**
     * 代码提交任务状态Key (后接任务ID)
     * 存储结构: Hash
     * Fields: status, result, error_message
     */
    public static final String CODE_SUBMISSION_STATUS_KEY = PROJECT_PREFIX + "submission:status:";

    /**
     * 知识图谱节点缓存Key (后接节点ID)
     * 存储结构: Hash
     */
    public static final String KG_NODE_CACHE_KEY = PROJECT_PREFIX + "kg:node:";

    /**
     * 岗位推荐结果缓存Key (后接用户ID)
     * 存储结构: ZSet (score为匹配度，value为岗位ID)
     */
    public static final String JOB_RECOMMENDATION_CACHE_KEY = PROJECT_PREFIX + "recommend:job:";

    /**
     * 同一个IP地址，在60秒内只能请求一次验证码接口。
     */
    public static final String EMAIL_VERIFY_IP_LIMIT = PROJECT_PREFIX + "verify:ip:limit:";
}