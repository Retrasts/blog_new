<template>
  <div>
    <h2 id="page-heading" data-cy="ICommentHeading">
      <span v-text="$t('blogNewApp.iComment.home.title')" id="i-comment-heading">I Comments</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iComment.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ICommentCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-comment"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iComment.home.createLabel')"> Create a new I Comment </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iComments && iComments.length === 0">
      <span v-text="$t('blogNewApp.iComment.home.notFound')">No iComments found</span>
    </div>
    <div class="table-responsive" v-if="iComments && iComments.length > 0">
      <table class="table table-striped" aria-describedby="iComments">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createTime')">
              <span v-text="$t('blogNewApp.iComment.createTime')">Create Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createUserId')">
              <span v-text="$t('blogNewApp.iComment.createUserId')">Create User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('blogId')">
              <span v-text="$t('blogNewApp.iComment.blogId')">Blog Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'blogId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('content')">
              <span v-text="$t('blogNewApp.iComment.content')">Content</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'content'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('likes')">
              <span v-text="$t('blogNewApp.iComment.likes')">Likes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'likes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('parentId')">
              <span v-text="$t('blogNewApp.iComment.parentId')">Parent Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'parentId'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iComment in iComments" :key="iComment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ICommentView', params: { iCommentId: iComment.id } }">{{ iComment.id }}</router-link>
            </td>
            <td>{{ iComment.createTime }}</td>
            <td>{{ iComment.createUserId }}</td>
            <td>{{ iComment.blogId }}</td>
            <td>{{ iComment.content }}</td>
            <td>{{ iComment.likes }}</td>
            <td>{{ iComment.parentId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ICommentView', params: { iCommentId: iComment.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ICommentEdit', params: { iCommentId: iComment.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iComment)"
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
        ><span id="blogNewApp.iComment.delete.question" data-cy="iCommentDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iComment-heading" v-text="$t('blogNewApp.iComment.delete.question', { id: removeId })">
          Are you sure you want to delete this I Comment?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iComment"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIComment()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-comment.component.ts"></script>
