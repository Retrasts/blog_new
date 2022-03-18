package com.retrast.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 用户角色中间表
 */
@ApiModel(description = "用户角色中间表")
@Table("i_user_role")
public class IUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    private IRole role;

    @Transient
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private IUser user;

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IUserRole id(Long id) {
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
            this.role.setUsers(null);
        }
        if (iRole != null) {
            iRole.setUsers(this);
        }
        this.role = iRole;
    }

    public IUserRole role(IRole iRole) {
        this.setRole(iRole);
        return this;
    }

    public IUser getUser() {
        return this.user;
    }

    public void setUser(IUser iUser) {
        this.user = iUser;
        this.userId = iUser != null ? iUser.getId() : null;
    }

    public IUserRole user(IUser iUser) {
        this.setUser(iUser);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long iUser) {
        this.userId = iUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IUserRole)) {
            return false;
        }
        return id != null && id.equals(((IUserRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IUserRole{" +
            "id=" + getId() +
            "}";
    }
}
