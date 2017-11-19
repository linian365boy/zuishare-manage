package top.zuishare.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

public class Resource implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String descn;		//资源在授权时显示的中文汉字	
	private Boolean display;	//是否显示在授权页面
	private Integer id;
	private Integer menuId;			//资源所属的菜单
	private String name;		//资源名
	private Integer priority;	//资源显示的优先值
	private String resString;	//资源字符串
	private String resType;	//资源类型，目前有两种资源类型。即METHOD与URL两种
	
	//临时变量，满足业务需求
	private transient String roleName;
	
	public String getDescn() {
		return descn;
	}
	public Boolean getDisplay() {
		return display;
	}
	public Integer getId() {
		return id;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public String getName() {
		return name;
	}
	public Integer getPriority() {
		return priority;
	}
	public String getResString() {
		return resString;
	}
	public String getResType() {
		return resType;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public void setDisplay(Boolean display) {
		this.display = display;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public void setResString(String resString) {
		this.resString = resString;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
