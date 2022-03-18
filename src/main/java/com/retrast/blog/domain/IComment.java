package com.retrast.blog.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 评论管理
 */
@ApiModel(description = "评论管理")
@Table("i_comment")
public class IComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期")
    @Column("create_time")
    private LocalDate createTime;

    /**
     * 发布用户ID
     */
    @ApiModelProperty(value = "发布用户ID")
    @Column("create_user_id")
    private Long createUserId;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    @Column("blog_id")
    private Long blogId;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    @Column("content")
    private String content;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数")
    @Column("likes")
    private Long likes;

    /**
     * 父级菜单id
     */
    @ApiModelProperty(value = "父级菜单id")
    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreateTime() {
        return this.createTime;
    }

    public IComment createTime(LocalDate createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public IComment createUserId(Long createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getBlogId() {
        return this.blogId;
    }

    public IComment blogId(Long blogId) {
        this.setBlogId(blogId);
        return this;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getContent() {
        return this.content;
    }

    public IComment content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getLikes() {
        return this.likes;
    }

    public IComment likes(Long likes) {
        this.setLikes(likes);
        return this;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public IComment parentId(Long parentId) {
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
        if (!(o instanceof IComment)) {
            return false;
        }
        return id != null && id.equals(((IComment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IComment{" +
            "id=" + getId() +
            ", createTime='" + getCreateTime() + "'" +
            ", createUserId=" + getCreateUserId() +
            ", blogId=" + getBlogId() +
            ", content='" + getContent() + "'" +
            ", likes=" + getLikes() +
            ", parentId=" + getParentId() +
            "}";
    }
}
