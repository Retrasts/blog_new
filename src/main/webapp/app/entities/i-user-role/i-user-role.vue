<template>
  <div>
    <h2 id="page-heading" data-cy="IUserRoleHeading">
      <span v-text="$t('blogNewApp.iUserRole.home.title')" id="i-user-role-heading">I User Roles</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iUserRole.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'IUserRoleCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-user-role"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iUserRole.home.createLabel')"> Create a new I User Role </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iUserRoles && iUserRoles.length === 0">
      <span v-text="$t('blogNewApp.iUserRole.home.notFound')">No iUserRoles found</span>
    </div>
    <div class="table-responsive" v-if="iUserRoles && iUserRoles.length > 0">
      <table class="table table-striped" aria-describedby="iUserRoles">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('blogNewApp.iUserRole.user')">User</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iUserRole in iUserRoles" :key="iUserRole.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IUserRoleView', params: { iUserRoleId: iUserRole.id } }">{{ iUserRole.id }}</router-link>
            </td>
            <td>
              <div v-if="iUserRole.user">
                <router-link :to="{ name: 'IUserView', params: { iUserId: iUserRole.user.id } }">{{ iUserRole.user.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'IUserRoleView', params: { iUserRoleId: iUserRole.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IUserRoleEdit', params: { iUserRoleId: iUserRole.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iUserRole)"
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
        ><span id="blogNewApp.iUserRole.delete.question" data-cy="iUserRoleDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iUserRole-heading" v-text="$t('blogNewApp.iUserRole.delete.question', { id: removeId })">
          Are you sure you want to delete this I User Role?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iUserRole"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIUserRole()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-user-role.component.ts"></script>
