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
 * 博文管理
 */
@ApiModel(description = "博文管理")
@Table("i_blog")
public class IBlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * 发布用户ID
     */
    @ApiModelProperty(value = "发布用户ID")
    @Column("create_user_id")
    private Long createUserId;

    /**
     * 博文标题
     */
    @ApiModelProperty(value = "博文标题")
    @Column("title")
    private String title;

    /**
     * 标签ID
     */
    @ApiModelProperty(value = "标签ID")
    @Column("label")
    private Long label;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    @Column("classify")
    private Long classify;

    /**
     * 博文内容
     */
    @ApiModelProperty(value = "博文内容")
    @Column("content")
    private String content;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数")
    @Column("likes")
    private Long likes;

    /**
     * 回复数
     */
    @ApiModelProperty(value = "回复数")
    @Column("replynumber")
    private Long replynumber;

    /**
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期")
    @Column("create_time")
    private LocalDate createTime;

    /**
     * 修改日期
     */
    @ApiModelProperty(value = "修改日期")
    @Column("update_time")
    private LocalDate updateTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IBlog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public IBlog createUserId(Long createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getTitle() {
        return this.title;
    }

    public IBlog title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLabel() {
        return this.label;
    }

    public IBlog label(Long label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(Long label) {
        this.label = label;
    }

    public Long getClassify() {
        return this.classify;
    }

    public IBlog classify(Long classify) {
        this.setClassify(classify);
        return this;
    }

    public void setClassify(Long classify) {
        this.classify = classify;
    }

    public String getContent() {
        return this.content;
    }

    public IBlog content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getLikes() {
        return this.likes;
    }

    public IBlog likes(Long likes) {
        this.setLikes(likes);
        return this;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getReplynumber() {
        return this.replynumber;
    }

    public IBlog replynumber(Long replynumber) {
        this.setReplynumber(replynumber);
        return this;
    }

    public void setReplynumber(Long replynumber) {
        this.replynumber = replynumber;
    }

    public LocalDate getCreateTime() {
        return this.createTime;
    }

    public IBlog createTime(LocalDate createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return this.updateTime;
    }

    public IBlog updateTime(LocalDate updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IBlog)) {
            return false;
        }
        return id != null && id.equals(((IBlog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IBlog{" +
            "id=" + getId() +
            ", createUserId=" + getCreateUserId() +
            ", title='" + getTitle() + "'" +
            ", label=" + getLabel() +
            ", classify=" + getClassify() +
            ", content='" + getContent() + "'" +
            ", likes=" + getLikes() +
            ", replynumber=" + getReplynumber() +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
