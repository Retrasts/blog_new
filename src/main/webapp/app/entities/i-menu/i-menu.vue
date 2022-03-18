<template>
  <div>
    <h2 id="page-heading" data-cy="IMenuHeading">
      <span v-text="$t('blogNewApp.iMenu.home.title')" id="i-menu-heading">I Menus</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iMenu.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'IMenuCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-menu"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iMenu.home.createLabel')"> Create a new I Menu </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iMenus && iMenus.length === 0">
      <span v-text="$t('blogNewApp.iMenu.home.notFound')">No iMenus found</span>
    </div>
    <div class="table-responsive" v-if="iMenus && iMenus.length > 0">
      <table class="table table-striped" aria-describedby="iMenus">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('url')">
              <span v-text="$t('blogNewApp.iMenu.url')">Url</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'url'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('menuName')">
              <span v-text="$t('blogNewApp.iMenu.menuName')">Menu Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'menuName'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('parentId')">
              <span v-text="$t('blogNewApp.iMenu.parentId')">Parent Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'parentId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createTime')">
              <span v-text="$t('blogNewApp.iMenu.createTime')">Create Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('updateTime')">
              <span v-text="$t('blogNewApp.iMenu.updateTime')">Update Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updateTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createUserId')">
              <span v-text="$t('blogNewApp.iMenu.createUserId')">Create User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('updateUserId')">
              <span v-text="$t('blogNewApp.iMenu.updateUserId')">Update User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updateUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iMenu in iMenus" :key="iMenu.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IMenuView', params: { iMenuId: iMenu.id } }">{{ iMenu.id }}</router-link>
            </td>
            <td>{{ iMenu.url }}</td>
            <td>{{ iMenu.menuName }}</td>
            <td>{{ iMenu.parentId }}</td>
            <td>{{ iMenu.createTime }}</td>
            <td>{{ iMenu.updateTime }}</td>
            <td>{{ iMenu.createUserId }}</td>
            <td>{{ iMenu.updateUserId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'IMenuView', params: { iMenuId: iMenu.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IMenuEdit', params: { iMenuId: iMenu.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iMenu)"
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
        ><span id="blogNewApp.iMenu.delete.question" data-cy="iMenuDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iMenu-heading" v-text="$t('blogNewApp.iMenu.delete.question', { id: removeId })">
          Are you sure you want to delete this I Menu?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iMenu"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIMenu()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="iMenus && iMenus.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-menu.component.ts"></script>
