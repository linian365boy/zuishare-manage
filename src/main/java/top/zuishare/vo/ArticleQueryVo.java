package top.zuishare.vo;

/**
 * @author niange
 * @ClassName: ArticleQueryVo
 * @desp:
 * @date: 2017/12/25 下午11:08
 * @since JDK 1.7
 */
public class ArticleQueryVo {
    /**
     * 状态
     * 1表示正常的
     * 0表示锁定的
     * 其他所有
     */
    private Integer status;
    /**
     * 发布状态
     * 1表示需要发布的
     * 其他表示需要没有发布过得
     */
    private Integer publishStatus;
    /**
     * 分类id
     */
    private Integer categoryId;

    public ArticleQueryVo(){}

    public ArticleQueryVo(Integer status){
        this.status = status;
    }

    public ArticleQueryVo(Integer status, Integer publishStatus){
        this.status = status;
        this.publishStatus = publishStatus;
    }

    public ArticleQueryVo(Integer status, Integer publishStatus, Integer categoryId){
        this.status = status;
        this.publishStatus = publishStatus;
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}

