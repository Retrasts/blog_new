<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="blogNewApp.iLabel.home.createOrEditLabel"
          data-cy="ILabelCreateUpdateHeading"
          v-text="$t('blogNewApp.iLabel.home.createOrEditLabel')"
        >
          Create or edit a ILabel
        </h2>
        <div>
          <div class="form-group" v-if="iLabel.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="iLabel.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iLabel.name')" for="i-label-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="i-label-name"
              data-cy="name"
              :class="{ valid: !$v.iLabel.name.$invalid, invalid: $v.iLabel.name.$invalid }"
              v-model="$v.iLabel.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iLabel.alias')" for="i-label-alias">Alias</label>
            <input
              type="text"
              class="form-control"
              name="alias"
              id="i-label-alias"
              data-cy="alias"
              :class="{ valid: !$v.iLabel.alias.$invalid, invalid: $v.iLabel.alias.$invalid }"
              v-model="$v.iLabel.alias.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iLabel.description')" for="i-label-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="i-label-description"
              data-cy="description"
              :class="{ valid: !$v.iLabel.description.$invalid, invalid: $v.iLabel.description.$invalid }"
              v-model="$v.iLabel.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iLabel.parentId')" for="i-label-parentId">Parent Id</label>
            <input
              type="number"
              class="form-control"
              name="parentId"
              id="i-label-parentId"
              data-cy="parentId"
              :class="{ valid: !$v.iLabel.parentId.$invalid, invalid: $v.iLabel.parentId.$invalid }"
              v-model.number="$v.iLabel.parentId.$model"
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
            :disabled="$v.iLabel.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./i-label-update.component.ts"></script>
