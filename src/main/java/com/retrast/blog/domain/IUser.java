package com.retrast.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 用户表
 */
@ApiModel(description = "用户表")
@Table("i_user")
public class IUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址")
    @Column("ip")
    private String ip;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @Column("username")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    @Column("nikename")
    private String nikename;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @Column("password")
    private String password;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    @Column("sex")
    private Integer sex;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Column("emaile")
    private String emaile;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    @Column("avatar")
    private String avatar;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column("create_time")
    private LocalDate createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @Column("update_time")
    private LocalDate updateTime;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    @Column("create_user_id")
    private Long createUserId;

    /**
     * 更新人id
     */
    @ApiModelProperty(value = "更新人id")
    @Column("update_user_id")
    private Long updateUserId;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @Column("birthday")
    private LocalDate birthday;

    /**
     * 公司
     */
    @ApiModelProperty(value = "公司")
    @Column("company")
    private String company;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @Column("phone")
    private Integer phone;

    @Transient
    @JsonIgnoreProperties(value = { "role", "user" }, allowSetters = true)
    private Set<IUserRole> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return this.ip;
    }

    public IUser ip(String ip) {
        this.setIp(ip);
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return this.username;
    }

    public IUser username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNikename() {
        return this.nikename;
    }

    public IUser nikename(String nikename) {
        this.setNikename(nikename);
        return this;
    }

    public void setNikename(String nikename) {
        this.nikename = nikename;
    }

    public String getPassword() {
        return this.password;
    }

    public IUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return this.sex;
    }

    public IUser sex(Integer sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmaile() {
        return this.emaile;
    }

    public IUser emaile(String emaile) {
        this.setEmaile(emaile);
        return this;
    }

    public void setEmaile(String emaile) {
        this.emaile = emaile;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public IUser avatar(String avatar) {
        this.setAvatar(avatar);
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDate getCreateTime() {
        return this.createTime;
    }

    public IUser createTime(LocalDate createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return this.updateTime;
    }

    public IUser updateTime(LocalDate updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public IUser createUserId(Long createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return this.updateUserId;
    }

    public IUser updateUserId(Long updateUserId) {
        this.setUpdateUserId(updateUserId);
        return this;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public IUser birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getCompany() {
        return this.company;
    }

    public IUser company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getPhone() {
        return this.phone;
    }

    public IUser phone(Integer phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Set<IUserRole> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<IUserRole> iUserRoles) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.setUser(null));
        }
        if (iUserRoles != null) {
            iUserRoles.forEach(i -> i.setUser(this));
        }
        this.roles = iUserRoles;
    }

    public IUser roles(Set<IUserRole> iUserRoles) {
        this.setRoles(iUserRoles);
        return this;
    }

    public IUser addRoles(IUserRole iUserRole) {
        this.roles.add(iUserRole);
        iUserRole.setUser(this);
        return this;
    }

    public IUser removeRoles(IUserRole iUserRole) {
        this.roles.remove(iUserRole);
        iUserRole.setUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IUser)) {
            return false;
        }
        return id != null && id.equals(((IUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IUser{" +
            "id=" + getId() +
            ", ip='" + getIp() + "'" +
            ", username='" + getUsername() + "'" +
            ", nikename='" + getNikename() + "'" +
            ", password='" + getPassword() + "'" +
            ", sex=" + getSex() +
            ", emaile='" + getEmaile() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", createUserId=" + getCreateUserId() +
            ", updateUserId=" + getUpdateUserId() +
            ", birthday='" + getBirthday() + "'" +
            ", company='" + getCompany() + "'" +
            ", phone=" + getPhone() +
            "}";
    }
}
