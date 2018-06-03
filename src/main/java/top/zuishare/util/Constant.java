package top.zuishare.util;

import java.io.File;

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
	public static final int ARTICLE_CONTENT_ONE_TYPE = 1;

	/**
	 * 上传文件的目录前缀
	 */
	public static final String UPLOAD_PRE = "upload";
	/**
	 * 主图文章图文类型的标题图片上传的目录前缀
	 */
	public static final String ARTICLE_PIC_TITLE_PRE = "articles";

	/**
	 * 主图文章图文类型的标题图片上传的路径前缀
	 */
	public static final String UPLOAD_ARTICLE_PIC_TITLE = UPLOAD_PRE + File.separator +ARTICLE_PIC_TITLE_PRE;

	public static final String AUTHOR = "TANFAN";
	
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

	/**
	 * redis 组成key之间单词的分隔符
	 * 对象类型:对象ID:对象属性”来命名一个键。对于多个单词则推荐使用"."分割
	 * eg:Article:list
	 * eg:Article:1:id
	 * eg:Article:1:categoryId
	 * eg:Article:list:viewNum
	 * eg:ArticleCategory:list
	 */
	public static final String KEYDELIMITER = ":";
	/**
	 *文章类型
	 */
	public static final String ARTICLECLASS = "Article";

	/**
	 * 文章分类类型
	 */
	public static final String ARTICLECATEGORYCLASS = "ArticleCategory";

	/**
	 *所有正常发布的文章
	 */
	public static final String REDIS_ARTICLES_KEY = "list";
	/**
	 *所有正常的文章分类
	 */
	public static final String REDIS_ARTICLE_CATEGORY_KEY = "list";
	/**
	 * 所有正常发布的文章总数，需要分页
	 */
	public static final String REDIS_ARTICLES_COUNT_KEY = "count";
	/**
	 *热门榜文章
	 */
	public static final String REDIS_HOT_ARTICLES_KEY = "viewNum";

	/**
	 * 过期时间30天
	 */
	public static final int TIMEOUTDAYS = 30;

	/**
	 * 发布的文章redis的key前缀
	 */
	public static final String REDIS_ARTICLE_PRE_KEY = "id";
	/**
	 * 发布的分类文章列表Key前缀
	 */
	public static final String REDIS_CATEGORY_ARTICLES_PRE_KEY = "categoryId";


	/**
	 * 滚动图片上传的目录前缀
	 */
	public static final String ADS_PIC_TITLE_PRE = "ads";

	/**
	 * 产品图片上传的目录前缀
	 */
	public static final String PRODUCTS_PIC_PRE = "products";
	/**
	 * 公司相关图片上传的目录前缀
	 */
	public static final String COMPANY_PIC_PRE = "company";

	/**
	 * 新闻图片上传的目录前缀
	 */
	public static final String NEWS_PIC_PRE = "news";

}
