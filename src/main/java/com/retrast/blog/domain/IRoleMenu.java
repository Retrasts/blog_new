package com.retrast.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 菜单角色中间表
 */
@ApiModel(description = "菜单角色中间表")
@Table("i_role_menu")
public class IRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    private IRole role;

    @Transient
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private IMenu menu;

    @Column("menu_id")
    private Long menuId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IRoleMenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IRole getRole() {
        return this.role;
    }

    public void setRole(IRole iRole) {
        if (this.role != null) {
            this.role.setMenus(null);
        }
        if (iRole != null) {
            iRole.setMenus(this);
        }
        this.role = iRole;
    }

    public IRoleMenu role(IRole iRole) {
        this.setRole(iRole);
        return this;
    }

    public IMenu getMenu() {
        return this.menu;
    }

    public void setMenu(IMenu iMenu) {
        this.menu = iMenu;
        this.menuId = iMenu != null ? iMenu.getId() : null;
    }

    public IRoleMenu menu(IMenu iMenu) {
        this.setMenu(iMenu);
        return this;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public void setMenuId(Long iMenu) {
        this.menuId = iMenu;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IRoleMenu)) {
            return false;
        }
        return id != null && id.equals(((IRoleMenu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IRoleMenu{" +
            "id=" + getId() +
            "}";
    }
}
