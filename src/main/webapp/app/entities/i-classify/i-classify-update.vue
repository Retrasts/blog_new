<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="blogNewApp.iClassify.home.createOrEditLabel"
          data-cy="IClassifyCreateUpdateHeading"
          v-text="$t('blogNewApp.iClassify.home.createOrEditLabel')"
        >
          Create or edit a IClassify
        </h2>
        <div>
          <div class="form-group" v-if="iClassify.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="iClassify.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iClassify.name')" for="i-classify-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="i-classify-name"
              data-cy="name"
              :class="{ valid: !$v.iClassify.name.$invalid, invalid: $v.iClassify.name.$invalid }"
              v-model="$v.iClassify.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iClassify.alias')" for="i-classify-alias">Alias</label>
            <input
              type="text"
              class="form-control"
              name="alias"
              id="i-classify-alias"
              data-cy="alias"
              :class="{ valid: !$v.iClassify.alias.$invalid, invalid: $v.iClassify.alias.$invalid }"
              v-model="$v.iClassify.alias.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iClassify.description')" for="i-classify-description"
              >Description</label
            >
            <input
              type="text"
              class="form-control"
              name="description"
              id="i-classify-description"
              data-cy="description"
              :class="{ valid: !$v.iClassify.description.$invalid, invalid: $v.iClassify.description.$invalid }"
              v-model="$v.iClassify.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iClassify.parentId')" for="i-classify-parentId">Parent Id</label>
            <input
              type="number"
              class="form-control"
              name="parentId"
              id="i-classify-parentId"
              data-cy="parentId"
              :class="{ valid: !$v.iClassify.parentId.$invalid, invalid: $v.iClassify.parentId.$invalid }"
              v-model.number="$v.iClassify.parentId.$model"
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
            :disabled="$v.iClassify.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./i-classify-update.component.ts"></script>
