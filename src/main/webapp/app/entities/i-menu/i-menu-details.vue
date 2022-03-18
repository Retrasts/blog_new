<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iMenu">
        <h2 class="jh-entity-heading" data-cy="iMenuDetailsHeading">
          <span v-text="$t('blogNewApp.iMenu.detail.title')">IMenu</span> {{ iMenu.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iMenu.url')">Url</span>
          </dt>
          <dd>
            <span>{{ iMenu.url }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iMenu.menuName')">Menu Name</span>
          </dt>
          <dd>
            <span>{{ iMenu.menuName }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iMenu.parentId')">Parent Id</span>
          </dt>
          <dd>
            <span>{{ iMenu.parentId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iMenu.createTime')">Create Time</span>
          </dt>
          <dd>
            <span>{{ iMenu.createTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iMenu.updateTime')">Update Time</span>
          </dt>
          <dd>
            <span>{{ iMenu.updateTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iMenu.createUserId')">Create User Id</span>
          </dt>
          <dd>
            <span>{{ iMenu.createUserId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iMenu.updateUserId')">Update User Id</span>
          </dt>
          <dd>
            <span>{{ iMenu.updateUserId }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="iMenu.id" :to="{ name: 'IMenuEdit', params: { iMenuId: iMenu.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-menu-details.component.ts"></script>
