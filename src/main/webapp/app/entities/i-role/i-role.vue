<template>
  <div>
    <h2 id="page-heading" data-cy="IRoleHeading">
      <span v-text="$t('blogNewApp.iRole.home.title')" id="i-role-heading">I Roles</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iRole.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'IRoleCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-role"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iRole.home.createLabel')"> Create a new I Role </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iRoles && iRoles.length === 0">
      <span v-text="$t('blogNewApp.iRole.home.notFound')">No iRoles found</span>
    </div>
    <div class="table-responsive" v-if="iRoles && iRoles.length > 0">
      <table class="table table-striped" aria-describedby="iRoles">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('roleName')">
              <span v-text="$t('blogNewApp.iRole.roleName')">Role Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'roleName'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('remark')">
              <span v-text="$t('blogNewApp.iRole.remark')">Remark</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'remark'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createTime')">
              <span v-text="$t('blogNewApp.iRole.createTime')">Create Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('updateTime')">
              <span v-text="$t('blogNewApp.iRole.updateTime')">Update Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updateTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createUserId')">
              <span v-text="$t('blogNewApp.iRole.createUserId')">Create User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('updateUserId')">
              <span v-text="$t('blogNewApp.iRole.updateUserId')">Update User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updateUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('users.id')">
              <span v-text="$t('blogNewApp.iRole.users')">Users</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'users.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('menus.id')">
              <span v-text="$t('blogNewApp.iRole.menus')">Menus</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'menus.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iRole in iRoles" :key="iRole.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IRoleView', params: { iRoleId: iRole.id } }">{{ iRole.id }}</router-link>
            </td>
            <td>{{ iRole.roleName }}</td>
            <td>{{ iRole.remark }}</td>
            <td>{{ iRole.createTime }}</td>
            <td>{{ iRole.updateTime }}</td>
            <td>{{ iRole.createUserId }}</td>
            <td>{{ iRole.updateUserId }}</td>
            <td>
              <div v-if="iRole.users">
                <router-link :to="{ name: 'IUserRoleView', params: { iUserRoleId: iRole.users.id } }">{{ iRole.users.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="iRole.menus">
                <router-link :to="{ name: 'IRoleMenuView', params: { iRoleMenuId: iRole.menus.id } }">{{ iRole.menus.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'IRoleView', params: { iRoleId: iRole.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IRoleEdit', params: { iRoleId: iRole.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iRole)"
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
        ><span id="blogNewApp.iRole.delete.question" data-cy="iRoleDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iRole-heading" v-text="$t('blogNewApp.iRole.delete.question', { id: removeId })">
          Are you sure you want to delete this I Role?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iRole"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIRole()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-role.component.ts"></script>
