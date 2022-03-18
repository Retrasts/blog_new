<template>
  <div>
    <h2 id="page-heading" data-cy="IClassifyHeading">
      <span v-text="$t('blogNewApp.iClassify.home.title')" id="i-classify-heading">I Classifies</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iClassify.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'IClassifyCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-classify"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iClassify.home.createLabel')"> Create a new I Classify </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iClassifies && iClassifies.length === 0">
      <span v-text="$t('blogNewApp.iClassify.home.notFound')">No iClassifies found</span>
    </div>
    <div class="table-responsive" v-if="iClassifies && iClassifies.length > 0">
      <table class="table table-striped" aria-describedby="iClassifies">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('blogNewApp.iClassify.name')">Name</span></th>
            <th scope="row"><span v-text="$t('blogNewApp.iClassify.alias')">Alias</span></th>
            <th scope="row"><span v-text="$t('blogNewApp.iClassify.description')">Description</span></th>
            <th scope="row"><span v-text="$t('blogNewApp.iClassify.parentId')">Parent Id</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iClassify in iClassifies" :key="iClassify.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IClassifyView', params: { iClassifyId: iClassify.id } }">{{ iClassify.id }}</router-link>
            </td>
            <td>{{ iClassify.name }}</td>
            <td>{{ iClassify.alias }}</td>
            <td>{{ iClassify.description }}</td>
            <td>{{ iClassify.parentId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'IClassifyView', params: { iClassifyId: iClassify.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IClassifyEdit', params: { iClassifyId: iClassify.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iClassify)"
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
        ><span id="blogNewApp.iClassify.delete.question" data-cy="iClassifyDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iClassify-heading" v-text="$t('blogNewApp.iClassify.delete.question', { id: removeId })">
          Are you sure you want to delete this I Classify?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iClassify"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIClassify()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-classify.component.ts"></script>
