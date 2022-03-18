<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iUserRole">
        <h2 class="jh-entity-heading" data-cy="iUserRoleDetailsHeading">
          <span v-text="$t('blogNewApp.iUserRole.detail.title')">IUserRole</span> {{ iUserRole.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iUserRole.user')">User</span>
          </dt>
          <dd>
            <div v-if="iUserRole.user">
              <router-link :to="{ name: 'IUserView', params: { iUserId: iUserRole.user.id } }">{{ iUserRole.user.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="iUserRole.id"
          :to="{ name: 'IUserRoleEdit', params: { iUserRoleId: iUserRole.id } }"
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

<script lang="ts" src="./i-user-role-details.component.ts"></script>
