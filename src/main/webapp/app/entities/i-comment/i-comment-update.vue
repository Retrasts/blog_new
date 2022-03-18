<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="blogNewApp.iComment.home.createOrEditLabel"
          data-cy="ICommentCreateUpdateHeading"
          v-text="$t('blogNewApp.iComment.home.createOrEditLabel')"
        >
          Create or edit a IComment
        </h2>
        <div>
          <div class="form-group" v-if="iComment.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="iComment.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iComment.createTime')" for="i-comment-createTime">Create Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-comment-createTime"
                  v-model="$v.iComment.createTime.$model"
                  name="createTime"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="i-comment-createTime"
                data-cy="createTime"
                type="text"
                class="form-control"
                name="createTime"
                :class="{ valid: !$v.iComment.createTime.$invalid, invalid: $v.iComment.createTime.$invalid }"
                v-model="$v.iComment.createTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iComment.createUserId')" for="i-comment-createUserId"
              >Create User Id</label
            >
            <input
              type="number"
              class="form-control"
              name="createUserId"
              id="i-comment-createUserId"
              data-cy="createUserId"
              :class="{ valid: !$v.iComment.createUserId.$invalid, invalid: $v.iComment.createUserId.$invalid }"
              v-model.number="$v.iComment.createUserId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iComment.blogId')" for="i-comment-blogId">Blog Id</label>
            <input
              type="number"
              class="form-control"
              name="blogId"
              id="i-comment-blogId"
              data-cy="blogId"
              :class="{ valid: !$v.iComment.blogId.$invalid, invalid: $v.iComment.blogId.$invalid }"
              v-model.number="$v.iComment.blogId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iComment.content')" for="i-comment-content">Content</label>
            <input
              type="text"
              class="form-control"
              name="content"
              id="i-comment-content"
              data-cy="content"
              :class="{ valid: !$v.iComment.content.$invalid, invalid: $v.iComment.content.$invalid }"
              v-model="$v.iComment.content.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iComment.likes')" for="i-comment-likes">Likes</label>
            <input
              type="number"
              class="form-control"
              name="likes"
              id="i-comment-likes"
              data-cy="likes"
              :class="{ valid: !$v.iComment.likes.$invalid, invalid: $v.iComment.likes.$invalid }"
              v-model.number="$v.iComment.likes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iComment.parentId')" for="i-comment-parentId">Parent Id</label>
            <input
              type="number"
              class="form-control"
              name="parentId"
              id="i-comment-parentId"
              data-cy="parentId"
              :class="{ valid: !$v.iComment.parentId.$invalid, invalid: $v.iComment.parentId.$invalid }"
              v-model.number="$v.iComment.parentId.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.iComment.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./i-comment-update.component.ts"></script>
