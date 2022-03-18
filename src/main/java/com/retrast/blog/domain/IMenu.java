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
 * 菜单表
 */
@ApiModel(description = "菜单表")
@Table("i_menu")
public class IMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * 菜单路由
     */
    @ApiModelProperty(value = "菜单路由")
    @Column("url")
    private String url;

    /**
     * 菜单名字
     */
    @ApiModelProperty(value = "菜单名字")
    @Column("menu_name")
    private String menuName;

    /**
     * 父级菜单id
     */
    @ApiModelProperty(value = "父级菜单id")
    @Column("parent_id")
    private Long parentId;

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
    @JsonIgnoreProperties(value = { "role", "menu" }, allowSetters = true)
    private Set<IRoleMenu> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IMenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public IMenu url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public IMenu menuName(String menuName) {
        this.setMenuName(menuName);
        return this;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public IMenu parentId(Long parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public LocalDate getCreateTime() {
        return this.createTime;
    }

    public IMenu createTime(LocalDate createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return this.updateTime;
    }

    public IMenu updateTime(LocalDate updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public IMenu createUserId(Long createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return this.updateUserId;
    }

    public IMenu updateUserId(Long updateUserId) {
        this.setUpdateUserId(updateUserId);
        return this;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Set<IRoleMenu> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<IRoleMenu> iRoleMenus) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.setMenu(null));
        }
        if (iRoleMenus != null) {
            iRoleMenus.forEach(i -> i.setMenu(this));
        }
        this.roles = iRoleMenus;
    }

    public IMenu roles(Set<IRoleMenu> iRoleMenus) {
        this.setRoles(iRoleMenus);
        return this;
    }

    public IMenu addRoles(IRoleMenu iRoleMenu) {
        this.roles.add(iRoleMenu);
        iRoleMenu.setMenu(this);
        return this;
    }

    public IMenu removeRoles(IRoleMenu iRoleMenu) {
        this.roles.remove(iRoleMenu);
        iRoleMenu.setMenu(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IMenu)) {
            return false;
        }
        return id != null && id.equals(((IMenu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IMenu{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", menuName='" + getMenuName() + "'" +
            ", parentId=" + getParentId() +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", createUserId=" + getCreateUserId() +
            ", updateUserId=" + getUpdateUserId() +
            "}";
    }
}
