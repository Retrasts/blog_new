<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iRoleMenu">
        <h2 class="jh-entity-heading" data-cy="iRoleMenuDetailsHeading">
          <span v-text="$t('blogNewApp.iRoleMenu.detail.title')">IRoleMenu</span> {{ iRoleMenu.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iRoleMenu.menu')">Menu</span>
          </dt>
          <dd>
            <div v-if="iRoleMenu.menu">
              <router-link :to="{ name: 'IMenuView', params: { iMenuId: iRoleMenu.menu.id } }">{{ iRoleMenu.menu.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="iRoleMenu.id"
          :to="{ name: 'IRoleMenuEdit', params: { iRoleMenuId: iRoleMenu.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-role-menu-details.component.ts"></script>
