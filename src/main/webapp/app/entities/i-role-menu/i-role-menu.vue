<template>
  <div>
    <h2 id="page-heading" data-cy="IRoleMenuHeading">
      <span v-text="$t('blogNewApp.iRoleMenu.home.title')" id="i-role-menu-heading">I Role Menus</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iRoleMenu.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'IRoleMenuCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-role-menu"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iRoleMenu.home.createLabel')"> Create a new I Role Menu </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iRoleMenus && iRoleMenus.length === 0">
      <span v-text="$t('blogNewApp.iRoleMenu.home.notFound')">No iRoleMenus found</span>
    </div>
    <div class="table-responsive" v-if="iRoleMenus && iRoleMenus.length > 0">
      <table class="table table-striped" aria-describedby="iRoleMenus">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('blogNewApp.iRoleMenu.menu')">Menu</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iRoleMenu in iRoleMenus" :key="iRoleMenu.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IRoleMenuView', params: { iRoleMenuId: iRoleMenu.id } }">{{ iRoleMenu.id }}</router-link>
            </td>
            <td>
              <div v-if="iRoleMenu.menu">
                <router-link :to="{ name: 'IMenuView', params: { iMenuId: iRoleMenu.menu.id } }">{{ iRoleMenu.menu.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'IRoleMenuView', params: { iRoleMenuId: iRoleMenu.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IRoleMenuEdit', params: { iRoleMenuId: iRoleMenu.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iRoleMenu)"
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
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="blogNewApp.iRoleMenu.delete.question" data-cy="iRoleMenuDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iRoleMenu-heading" v-text="$t('blogNewApp.iRoleMenu.delete.question', { id: removeId })">
          Are you sure you want to delete this I Role Menu?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iRoleMenu"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIRoleMenu()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-role-menu.component.ts"></script>
