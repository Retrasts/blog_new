<template>
  <div>
    <h2 id="page-heading" data-cy="IUserHeading">
      <span v-text="$t('blogNewApp.iUser.home.title')" id="i-user-heading">I Users</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iUser.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'IUserCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-user"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iUser.home.createLabel')"> Create a new I User </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iUsers && iUsers.length === 0">
      <span v-text="$t('blogNewApp.iUser.home.notFound')">No iUsers found</span>
    </div>
    <div class="table-responsive" v-if="iUsers && iUsers.length > 0">
      <table class="table table-striped" aria-describedby="iUsers">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('ip')">
              <span v-text="$t('blogNewApp.iUser.ip')">Ip</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ip'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('username')">
              <span v-text="$t('blogNewApp.iUser.username')">Username</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'username'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nikename')">
              <span v-text="$t('blogNewApp.iUser.nikename')">Nikename</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nikename'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('password')">
              <span v-text="$t('blogNewApp.iUser.password')">Password</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'password'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('sex')">
              <span v-text="$t('blogNewApp.iUser.sex')">Sex</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sex'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('emaile')">
              <span v-text="$t('blogNewApp.iUser.emaile')">Emaile</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'emaile'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('avatar')">
              <span v-text="$t('blogNewApp.iUser.avatar')">Avatar</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'avatar'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createTime')">
              <span v-text="$t('blogNewApp.iUser.createTime')">Create Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('updateTime')">
              <span v-text="$t('blogNewApp.iUser.updateTime')">Update Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updateTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createUserId')">
              <span v-text="$t('blogNewApp.iUser.createUserId')">Create User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('updateUserId')">
              <span v-text="$t('blogNewApp.iUser.updateUserId')">Update User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updateUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('birthday')">
              <span v-text="$t('blogNewApp.iUser.birthday')">Birthday</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'birthday'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('company')">
              <span v-text="$t('blogNewApp.iUser.company')">Company</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'company'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('phone')">
              <span v-text="$t('blogNewApp.iUser.phone')">Phone</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phone'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iUser in iUsers" :key="iUser.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IUserView', params: { iUserId: iUser.id } }">{{ iUser.id }}</router-link>
            </td>
            <td>{{ iUser.ip }}</td>
            <td>{{ iUser.username }}</td>
            <td>{{ iUser.nikename }}</td>
            <td>{{ iUser.password }}</td>
            <td>{{ iUser.sex }}</td>
            <td>{{ iUser.emaile }}</td>
            <td>{{ iUser.avatar }}</td>
            <td>{{ iUser.createTime }}</td>
            <td>{{ iUser.updateTime }}</td>
            <td>{{ iUser.createUserId }}</td>
            <td>{{ iUser.updateUserId }}</td>
            <td>{{ iUser.birthday }}</td>
            <td>{{ iUser.company }}</td>
            <td>{{ iUser.phone }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'IUserView', params: { iUserId: iUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IUserEdit', params: { iUserId: iUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iUser)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
        <infinite-loading
          ref="infiniteLoading"
          v-if="totalItems > itemsPerPage"
          :identifier="infiniteId"
          slot="append"
          @infinite="loadMore"
          force-use-infinite-wrapper=".el-table__body-wrapper"
          :distance="20"
        >
        </infinite-loading>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="blogNewApp.iUser.delete.question" data-cy="iUserDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iUser-heading" v-text="$t('blogNewApp.iUser.delete.question', { id: removeId })">
          Are you sure you want to delete this I User?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iUser"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIUser()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-user.component.ts"></script>
