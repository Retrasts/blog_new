<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iComment">
        <h2 class="jh-entity-heading" data-cy="iCommentDetailsHeading">
          <span v-text="$t('blogNewApp.iComment.detail.title')">IComment</span> {{ iComment.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iComment.createTime')">Create Time</span>
          </dt>
          <dd>
            <span>{{ iComment.createTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iComment.createUserId')">Create User Id</span>
          </dt>
          <dd>
            <span>{{ iComment.createUserId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iComment.blogId')">Blog Id</span>
          </dt>
          <dd>
            <span>{{ iComment.blogId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iComment.content')">Content</span>
          </dt>
          <dd>
            <span>{{ iComment.content }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iComment.likes')">Likes</span>
          </dt>
          <dd>
            <span>{{ iComment.likes }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iComment.parentId')">Parent Id</span>
          </dt>
          <dd>
            <span>{{ iComment.parentId }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="iComment.id" :to="{ name: 'ICommentEdit', params: { iCommentId: iComment.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-comment-details.component.ts"></script>
