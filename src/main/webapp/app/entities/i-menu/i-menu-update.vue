<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="blogNewApp.iMenu.home.createOrEditLabel"
          data-cy="IMenuCreateUpdateHeading"
          v-text="$t('blogNewApp.iMenu.home.createOrEditLabel')"
        >
          Create or edit a IMenu
        </h2>
        <div>
          <div class="form-group" v-if="iMenu.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="iMenu.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iMenu.url')" for="i-menu-url">Url</label>
            <input
              type="text"
              class="form-control"
              name="url"
              id="i-menu-url"
              data-cy="url"
              :class="{ valid: !$v.iMenu.url.$invalid, invalid: $v.iMenu.url.$invalid }"
              v-model="$v.iMenu.url.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iMenu.menuName')" for="i-menu-menuName">Menu Name</label>
            <input
              type="text"
              class="form-control"
              name="menuName"
              id="i-menu-menuName"
              data-cy="menuName"
              :class="{ valid: !$v.iMenu.menuName.$invalid, invalid: $v.iMenu.menuName.$invalid }"
              v-model="$v.iMenu.menuName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iMenu.parentId')" for="i-menu-parentId">Parent Id</label>
            <input
              type="number"
              class="form-control"
              name="parentId"
              id="i-menu-parentId"
              data-cy="parentId"
              :class="{ valid: !$v.iMenu.parentId.$invalid, invalid: $v.iMenu.parentId.$invalid }"
              v-model.number="$v.iMenu.parentId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iMenu.createTime')" for="i-menu-createTime">Create Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-menu-createTime"
                  v-model="$v.iMenu.createTime.$model"
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
                id="i-menu-createTime"
                data-cy="createTime"
                type="text"
                class="form-control"
                name="createTime"
                :class="{ valid: !$v.iMenu.createTime.$invalid, invalid: $v.iMenu.createTime.$invalid }"
                v-model="$v.iMenu.createTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iMenu.updateTime')" for="i-menu-updateTime">Update Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-menu-updateTime"
                  v-model="$v.iMenu.updateTime.$model"
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
                id="i-menu-updateTime"
                data-cy="updateTime"
                type="text"
                class="form-control"
                name="updateTime"
                :class="{ valid: !$v.iMenu.updateTime.$invalid, invalid: $v.iMenu.updateTime.$invalid }"
                v-model="$v.iMenu.updateTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iMenu.createUserId')" for="i-menu-createUserId">Create User Id</label>
            <input
              type="number"
              class="form-control"
              name="createUserId"
              id="i-menu-createUserId"
              data-cy="createUserId"
              :class="{ valid: !$v.iMenu.createUserId.$invalid, invalid: $v.iMenu.createUserId.$invalid }"
              v-model.number="$v.iMenu.createUserId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iMenu.updateUserId')" for="i-menu-updateUserId">Update User Id</label>
            <input
              type="number"
              class="form-control"
              name="updateUserId"
              id="i-menu-updateUserId"
              data-cy="updateUserId"
              :class="{ valid: !$v.iMenu.updateUserId.$invalid, invalid: $v.iMenu.updateUserId.$invalid }"
              v-model.number="$v.iMenu.updateUserId.$model"
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
            :disabled="$v.iMenu.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./i-menu-update.component.ts"></script>
