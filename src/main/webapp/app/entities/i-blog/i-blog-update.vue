<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="blogNewApp.iBlog.home.createOrEditLabel"
          data-cy="IBlogCreateUpdateHeading"
          v-text="$t('blogNewApp.iBlog.home.createOrEditLabel')"
        >
          Create or edit a IBlog
        </h2>
        <div>
          <div class="form-group" v-if="iBlog.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="iBlog.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.createUserId')" for="i-blog-createUserId">Create User Id</label>
            <input
              type="number"
              class="form-control"
              name="createUserId"
              id="i-blog-createUserId"
              data-cy="createUserId"
              :class="{ valid: !$v.iBlog.createUserId.$invalid, invalid: $v.iBlog.createUserId.$invalid }"
              v-model.number="$v.iBlog.createUserId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.title')" for="i-blog-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="i-blog-title"
              data-cy="title"
              :class="{ valid: !$v.iBlog.title.$invalid, invalid: $v.iBlog.title.$invalid }"
              v-model="$v.iBlog.title.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.label')" for="i-blog-label">Label</label>
            <input
              type="number"
              class="form-control"
              name="label"
              id="i-blog-label"
              data-cy="label"
              :class="{ valid: !$v.iBlog.label.$invalid, invalid: $v.iBlog.label.$invalid }"
              v-model.number="$v.iBlog.label.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.classify')" for="i-blog-classify">Classify</label>
            <input
              type="number"
              class="form-control"
              name="classify"
              id="i-blog-classify"
              data-cy="classify"
              :class="{ valid: !$v.iBlog.classify.$invalid, invalid: $v.iBlog.classify.$invalid }"
              v-model.number="$v.iBlog.classify.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.content')" for="i-blog-content">Content</label>
            <input
              type="text"
              class="form-control"
              name="content"
              id="i-blog-content"
              data-cy="content"
              :class="{ valid: !$v.iBlog.content.$invalid, invalid: $v.iBlog.content.$invalid }"
              v-model="$v.iBlog.content.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.likes')" for="i-blog-likes">Likes</label>
            <input
              type="number"
              class="form-control"
              name="likes"
              id="i-blog-likes"
              data-cy="likes"
              :class="{ valid: !$v.iBlog.likes.$invalid, invalid: $v.iBlog.likes.$invalid }"
              v-model.number="$v.iBlog.likes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.replynumber')" for="i-blog-replynumber">Replynumber</label>
            <input
              type="number"
              class="form-control"
              name="replynumber"
              id="i-blog-replynumber"
              data-cy="replynumber"
              :class="{ valid: !$v.iBlog.replynumber.$invalid, invalid: $v.iBlog.replynumber.$invalid }"
              v-model.number="$v.iBlog.replynumber.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.createTime')" for="i-blog-createTime">Create Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-blog-createTime"
                  v-model="$v.iBlog.createTime.$model"
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
                id="i-blog-createTime"
                data-cy="createTime"
                type="text"
                class="form-control"
                name="createTime"
                :class="{ valid: !$v.iBlog.createTime.$invalid, invalid: $v.iBlog.createTime.$invalid }"
                v-model="$v.iBlog.createTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iBlog.updateTime')" for="i-blog-updateTime">Update Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-blog-updateTime"
                  v-model="$v.iBlog.updateTime.$model"
                  name="updateTime"
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
                id="i-blog-updateTime"
                data-cy="updateTime"
                type="text"
                class="form-control"
                name="updateTime"
                :class="{ valid: !$v.iBlog.updateTime.$invalid, invalid: $v.iBlog.updateTime.$invalid }"
                v-model="$v.iBlog.updateTime.$model"
              />
            </b-input-group>
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
            :disabled="$v.iBlog.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./i-blog-update.component.ts"></script>
