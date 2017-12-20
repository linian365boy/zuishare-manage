package top.zuishare.util;

public class Constant {
	/**
	 * 一般用于表示状态为锁定
	 */
	public static final int C_ZERO = 0;
	/**
	 * 一般用于表示状态为正常
	 */
	public static final int C_ONE = 1;
	/**
	 * 主题的内容类型，1=快讯类型，没有图片
	 */
	public static final int ARTICLE_CONTENT_TYPE = 1;

	public static final String AUTHOR = "tanfan";
	
	/**
	 * 栏目路径前缀
	 */
	public static final String COLUMNPATHPRE = "/col";
	/**
	 * 生成的产品相关页面路径
	 */
	public static final String PRODUCTPRE = "/product";
	/**
	 * 生成的产品页面路径，页面展示需要ssi嵌入
	 */
	public static final String PRODUCTPATH = PRODUCTPRE + "/detail";
	/**
	 * 生成的新闻相关页面路径前缀
	 */
	public static final String NEWSPRE = "/news";
	/**
	 * 生成的新闻页面路径，页面展示需要ssi嵌入
	 */
	public static final String NEWSPATH = NEWSPRE + "/detail";
	/**
	 * 生成的分类产品列表页面路径，页面展示需要ssi嵌入
	 */
	public static final String CATEGORYPRODUCTPATH = "/col";
	
	/**
	 * 生成的分类产品详情页面路径
	 */
	public static final String CATEGORYDETAILPRODUCTPATH = "/product";
	
	/**
	 * 生成的分类列表页面路径
	 */
	public static final String CATEPRE = "/cat";
	
	public static final int PAGE_INDEX_SIZE = 16;
	/**
	 * 成功返回码
	 */
	public static final int SUCCESS_CODE = 200;
	/**
	 * 验证码输入错误返回码
	 */
	public static final int CODE_201 = 201;
	/**
	 * 必填项未填错误返回码
	 */
	public static final int CODE_202 = 202;
	/**
	 * 失败返回码
	 */
	public static final int ERROR_CODE = 500;
	/**
	 * success
	 */
	public static final String SUCCESS_STR = "success";
	/**
	 * error
	 */
	public static final String ERROR_STR = "error";
	/**
	 * 验证码key
	 */
	public static final String VERIFY_CODE_KEY = "verifyCode";

	/**
	 * 登录验证码key
	 */
	public static final String LOGIN_VERIFY_CODE_KEY = "loginVerifyCode";

	/**
	 * 登录表单验证码输入框名称
	 */
	public static final String FORM_CAPTCHA_NAME = "captcha";

}
