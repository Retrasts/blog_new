package com.retrast.blog.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 分类管理
 */
@ApiModel(description = "分类管理")
@Table("i_classify")
public class IClassify implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @Column("name")
    private String name;

    /**
     * 别名
     */
    @ApiModelProperty(value = "别名")
    @Column("alias")
    private String alias;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Column("description")
    private String description;

    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IClassify id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IClassify name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return this.alias;
    }

    public IClassify alias(String alias) {
        this.setAlias(alias);
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return this.description;
    }

    public IClassify description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public IClassify parentId(Long parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IClassify)) {
            return false;
        }
        return id != null && id.equals(((IClassify) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IClassify{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alias='" + getAlias() + "'" +
            ", description='" + getDescription() + "'" +
            ", parentId=" + getParentId() +
            "}";
    }
}
