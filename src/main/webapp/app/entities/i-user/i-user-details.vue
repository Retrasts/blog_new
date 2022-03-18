<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iUser">
        <h2 class="jh-entity-heading" data-cy="iUserDetailsHeading">
          <span v-text="$t('blogNewApp.iUser.detail.title')">IUser</span> {{ iUser.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iUser.ip')">Ip</span>
          </dt>
          <dd>
            <span>{{ iUser.ip }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.username')">Username</span>
          </dt>
          <dd>
            <span>{{ iUser.username }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.nikename')">Nikename</span>
          </dt>
          <dd>
            <span>{{ iUser.nikename }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.password')">Password</span>
          </dt>
          <dd>
            <span>{{ iUser.password }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.sex')">Sex</span>
          </dt>
          <dd>
            <span>{{ iUser.sex }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.emaile')">Emaile</span>
          </dt>
          <dd>
            <span>{{ iUser.emaile }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.avatar')">Avatar</span>
          </dt>
          <dd>
            <span>{{ iUser.avatar }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.createTime')">Create Time</span>
          </dt>
          <dd>
            <span>{{ iUser.createTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.updateTime')">Update Time</span>
          </dt>
          <dd>
            <span>{{ iUser.updateTime }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.createUserId')">Create User Id</span>
          </dt>
          <dd>
            <span>{{ iUser.createUserId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.updateUserId')">Update User Id</span>
          </dt>
          <dd>
            <span>{{ iUser.updateUserId }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.birthday')">Birthday</span>
          </dt>
          <dd>
            <span>{{ iUser.birthday }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.company')">Company</span>
          </dt>
          <dd>
            <span>{{ iUser.company }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iUser.phone')">Phone</span>
          </dt>
          <dd>
            <span>{{ iUser.phone }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="iUser.id" :to="{ name: 'IUserEdit', params: { iUserId: iUser.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-user-details.component.ts"></script>
