package top.zuishare.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String mark; // 权限分配时显示的字，可能跟name一致，也可能不一致，只是为了分配权限时看得明白，清楚。
	private String name; // 菜单名，展示在菜单栏上的信息
	private Integer parentId; // 父菜单
	private Integer priority; // 菜单展示的优先级
	private String url; // 点击菜单的链接
	
	//非table字段
	//父级菜单名称
	private transient String parentMenuName;
	private transient List<Menu> children;

	public Menu() {
	}

	public Menu(String name, boolean leafOrNo, String url, Integer priority) {
		this.name = name;
		this.url = url;
		this.priority = priority;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public Integer getId() {
		return id;
	}

	public String getMark() {
		return mark;
	}

	public String getName() {
		return name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public String getParentMenuName() {
		return parentMenuName;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getUrl() {
		return url;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
