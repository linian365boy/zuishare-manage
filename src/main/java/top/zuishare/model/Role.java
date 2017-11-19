package top.zuishare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Role implements Serializable {
	private static final long serialVersionUID = -3498056750436845009L;
	private String name;				//角色名
	private String describes;				//角色描述
	private boolean defaultOrNo;		//是否默认角色
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createDate;			//创建角色日期
	private int priority; // 菜单展示的优先级
	
	private transient List<Resource> resources;
	
	public Role(){
	}
	
	public Role(String name) {
		this.name = name;
	}
	
	public Role(String name, String describes) {
		this(name);
		this.describes = describes;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public boolean isDefaultOrNo() {
		return defaultOrNo;
	}
	public void setDefaultOrNo(boolean defaultOrNo) {
		this.defaultOrNo = defaultOrNo;
	}
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
