<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iBlog">
        <h2 class="jh-entity-heading" data-cy="iBlogDetailsHeading">
          <span v-text="$t('blogNewApp.iBlog.detail.title')">IBlog</span> {{ iBlog.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iBlog.createUserId')">Create User Id</span>
          </dt>
          <dd>
            <span>{{ iBlog.createUserId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.title')">Title</span>
          </dt>
          <dd>
            <span>{{ iBlog.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.label')">Label</span>
          </dt>
          <dd>
            <span>{{ iBlog.label }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.classify')">Classify</span>
          </dt>
          <dd>
            <span>{{ iBlog.classify }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.content')">Content</span>
          </dt>
          <dd>
            <span>{{ iBlog.content }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.likes')">Likes</span>
          </dt>
          <dd>
            <span>{{ iBlog.likes }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.replynumber')">Replynumber</span>
          </dt>
          <dd>
            <span>{{ iBlog.replynumber }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.createTime')">Create Time</span>
          </dt>
          <dd>
            <span>{{ iBlog.createTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iBlog.updateTime')">Update Time</span>
          </dt>
          <dd>
            <span>{{ iBlog.updateTime }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="iBlog.id" :to="{ name: 'IBlogEdit', params: { iBlogId: iBlog.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-blog-details.component.ts"></script>
