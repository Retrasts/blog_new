<template>
  <div>
    <h2 id="page-heading" data-cy="IBlogHeading">
      <span v-text="$t('blogNewApp.iBlog.home.title')" id="i-blog-heading">I Blogs</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('blogNewApp.iBlog.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'IBlogCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-i-blog"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('blogNewApp.iBlog.home.createLabel')"> Create a new I Blog </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iBlogs && iBlogs.length === 0">
      <span v-text="$t('blogNewApp.iBlog.home.notFound')">No iBlogs found</span>
    </div>
    <div class="table-responsive" v-if="iBlogs && iBlogs.length > 0">
      <table class="table table-striped" aria-describedby="iBlogs">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createUserId')">
              <span v-text="$t('blogNewApp.iBlog.createUserId')">Create User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span v-text="$t('blogNewApp.iBlog.title')">Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('label')">
              <span v-text="$t('blogNewApp.iBlog.label')">Label</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'label'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('classify')">
              <span v-text="$t('blogNewApp.iBlog.classify')">Classify</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'classify'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('content')">
              <span v-text="$t('blogNewApp.iBlog.content')">Content</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'content'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('likes')">
              <span v-text="$t('blogNewApp.iBlog.likes')">Likes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'likes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('replynumber')">
              <span v-text="$t('blogNewApp.iBlog.replynumber')">Replynumber</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'replynumber'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createTime')">
              <span v-text="$t('blogNewApp.iBlog.createTime')">Create Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('updateTime')">
              <span v-text="$t('blogNewApp.iBlog.updateTime')">Update Time</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updateTime'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iBlog in iBlogs" :key="iBlog.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IBlogView', params: { iBlogId: iBlog.id } }">{{ iBlog.id }}</router-link>
            </td>
            <td>{{ iBlog.createUserId }}</td>
            <td>{{ iBlog.title }}</td>
            <td>{{ iBlog.label }}</td>
            <td>{{ iBlog.classify }}</td>
            <td>{{ iBlog.content }}</td>
            <td>{{ iBlog.likes }}</td>
            <td>{{ iBlog.replynumber }}</td>
            <td>{{ iBlog.createTime }}</td>
            <td>{{ iBlog.updateTime }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'IBlogView', params: { iBlogId: iBlog.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IBlogEdit', params: { iBlogId: iBlog.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(iBlog)"
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
        ><span id="blogNewApp.iBlog.delete.question" data-cy="iBlogDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-iBlog-heading" v-text="$t('blogNewApp.iBlog.delete.question', { id: removeId })">
          Are you sure you want to delete this I Blog?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-iBlog"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeIBlog()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./i-blog.component.ts"></script>
