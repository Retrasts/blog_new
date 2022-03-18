package com.retrast.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 角色表
 */
@ApiModel(description = "角色表")
@Table("i_role")
public class IRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * 角色名
     */
    @ApiModelProperty(value = "角色名")
    @Column("role_name")
    private String roleName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Column("remark")
    private String remark;

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

    @Transient
    private IUserRole users;

    @Transient
    private IRoleMenu menus;

    @Column("users_id")
    private Long usersId;

    @Column("menus_id")
    private Long menusId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public IRole roleName(String roleName) {
        this.setRoleName(roleName);
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return this.remark;
    }

    public IRole remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDate getCreateTime() {
        return this.createTime;
    }

    public IRole createTime(LocalDate createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return this.updateTime;
    }

    public IRole updateTime(LocalDate updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public IRole createUserId(Long createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return this.updateUserId;
    }

    public IRole updateUserId(Long updateUserId) {
        this.setUpdateUserId(updateUserId);
        return this;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public IUserRole getUsers() {
        return this.users;
    }

    public void setUsers(IUserRole iUserRole) {
        this.users = iUserRole;
        this.usersId = iUserRole != null ? iUserRole.getId() : null;
    }

    public IRole users(IUserRole iUserRole) {
        this.setUsers(iUserRole);
        return this;
    }

    public IRoleMenu getMenus() {
        return this.menus;
    }

    public void setMenus(IRoleMenu iRoleMenu) {
        this.menus = iRoleMenu;
        this.menusId = iRoleMenu != null ? iRoleMenu.getId() : null;
    }

    public IRole menus(IRoleMenu iRoleMenu) {
        this.setMenus(iRoleMenu);
        return this;
    }

    public Long getUsersId() {
        return this.usersId;
    }

    public void setUsersId(Long iUserRole) {
        this.usersId = iUserRole;
    }

    public Long getMenusId() {
        return this.menusId;
    }

    public void setMenusId(Long iRoleMenu) {
        this.menusId = iRoleMenu;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IRole)) {
            return false;
        }
        return id != null && id.equals(((IRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IRole{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", remark='" + getRemark() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", createUserId=" + getCreateUserId() +
            ", updateUserId=" + getUpdateUserId() +
            "}";
    }
}
