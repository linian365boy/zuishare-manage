package top.zuishare.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class User implements UserDetails{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -2414711442165502235L;
	private Integer id;
	private String realName;			//真实姓名
	private String username;			//用户名
	private String email;				//邮箱
	private String password;			//密码
	private boolean enabled;			//账号是否可用  true为可用，false不可用
	private boolean accountNonLocked;	//账号是否被锁！true为没锁，false为已锁
	private Date lastCloseDate;			//最近一次禁用或者注销时间
	private Date createDate;			//创建日期
	private String telphone;			//手机号码
	
	/**
	 * 所属角色
	 */
	private transient List<Role> roles;
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthoritys = new ArrayList<GrantedAuthority>();
		if(roles != null){
			for(Role r : roles){
				grantedAuthoritys.add(new SimpleGrantedAuthority(r.getName()));
			}
		}
		return grantedAuthoritys;
	}

	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean isAccountNonExpired() {
		return true;
	}
	
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User){
			User u = (User)obj;
			return (u.id==id)?true:false;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if(id != null) {
			return id.intValue();
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	
	public Date getLastCloseDate() {
		return lastCloseDate;
	}
	public void setLastCloseDate(Date lastCloseDate) {
		this.lastCloseDate = lastCloseDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

}
