<template>
  <div>
    <h2 id="page-heading" data-cy="ILabelHeading">
      <span v-text="$t('blogNewApp.iLabel.home.title')" id="i-label-heading">I Labels</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iLabel.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ILabelCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-label"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iLabel.home.createLabel')"> Create a new I Label </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iLabels && iLabels.length === 0">
      <span v-text="$t('blogNewApp.iLabel.home.notFound')">No iLabels found</span>
    </div>
    <div class="table-responsive" v-if="iLabels && iLabels.length > 0">
      <table class="table table-striped" aria-describedby="iLabels">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('blogNewApp.iLabel.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('alias')">
              <span v-text="$t('blogNewApp.iLabel.alias')">Alias</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'alias'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="$t('blogNewApp.iLabel.description')">Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('parentId')">
              <span v-text="$t('blogNewApp.iLabel.parentId')">Parent Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'parentId'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iLabel in iLabels" :key="iLabel.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ILabelView', params: { iLabelId: iLabel.id } }">{{ iLabel.id }}</router-link>
            </td>
            <td>{{ iLabel.name }}</td>
            <td>{{ iLabel.alias }}</td>
            <td>{{ iLabel.description }}</td>
            <td>{{ iLabel.parentId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ILabelView', params: { iLabelId: iLabel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ILabelEdit', params: { iLabelId: iLabel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iLabel)"
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
        ><span id="blogNewApp.iLabel.delete.question" data-cy="iLabelDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iLabel-heading" v-text="$t('blogNewApp.iLabel.delete.question', { id: removeId })">
          Are you sure you want to delete this I Label?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iLabel"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeILabel()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-label.component.ts"></script>
