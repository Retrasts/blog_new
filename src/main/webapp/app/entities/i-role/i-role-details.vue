<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iRole">
        <h2 class="jh-entity-heading" data-cy="iRoleDetailsHeading">
          <span v-text="$t('blogNewApp.iRole.detail.title')">IRole</span> {{ iRole.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iRole.roleName')">Role Name</span>
          </dt>
          <dd>
            <span>{{ iRole.roleName }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iRole.remark')">Remark</span>
          </dt>
          <dd>
            <span>{{ iRole.remark }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iRole.createTime')">Create Time</span>
          </dt>
          <dd>
            <span>{{ iRole.createTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iRole.updateTime')">Update Time</span>
          </dt>
          <dd>
            <span>{{ iRole.updateTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iRole.createUserId')">Create User Id</span>
          </dt>
          <dd>
            <span>{{ iRole.createUserId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iRole.updateUserId')">Update User Id</span>
          </dt>
          <dd>
            <span>{{ iRole.updateUserId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iRole.users')">Users</span>
          </dt>
          <dd>
            <div v-if="iRole.users">
              <router-link :to="{ name: 'IUserRoleView', params: { iUserRoleId: iRole.users.id } }">{{ iRole.users.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iRole.menus')">Menus</span>
          </dt>
          <dd>
            <div v-if="iRole.menus">
              <router-link :to="{ name: 'IRoleMenuView', params: { iRoleMenuId: iRole.menus.id } }">{{ iRole.menus.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="iRole.id" :to="{ name: 'IRoleEdit', params: { iRoleId: iRole.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-role-details.component.ts"></script>
