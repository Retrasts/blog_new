<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="blogNewApp.iRole.home.createOrEditLabel"
          data-cy="IRoleCreateUpdateHeading"
          v-text="$t('blogNewApp.iRole.home.createOrEditLabel')"
        >
          Create or edit a IRole
        </h2>
        <div>
          <div class="form-group" v-if="iRole.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="iRole.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.roleName')" for="i-role-roleName">Role Name</label>
            <input
              type="text"
              class="form-control"
              name="roleName"
              id="i-role-roleName"
              data-cy="roleName"
              :class="{ valid: !$v.iRole.roleName.$invalid, invalid: $v.iRole.roleName.$invalid }"
              v-model="$v.iRole.roleName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.remark')" for="i-role-remark">Remark</label>
            <input
              type="text"
              class="form-control"
              name="remark"
              id="i-role-remark"
              data-cy="remark"
              :class="{ valid: !$v.iRole.remark.$invalid, invalid: $v.iRole.remark.$invalid }"
              v-model="$v.iRole.remark.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.createTime')" for="i-role-createTime">Create Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-role-createTime"
                  v-model="$v.iRole.createTime.$model"
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
                id="i-role-createTime"
                data-cy="createTime"
                type="text"
                class="form-control"
                name="createTime"
                :class="{ valid: !$v.iRole.createTime.$invalid, invalid: $v.iRole.createTime.$invalid }"
                v-model="$v.iRole.createTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.updateTime')" for="i-role-updateTime">Update Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-role-updateTime"
                  v-model="$v.iRole.updateTime.$model"
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
                id="i-role-updateTime"
                data-cy="updateTime"
                type="text"
                class="form-control"
                name="updateTime"
                :class="{ valid: !$v.iRole.updateTime.$invalid, invalid: $v.iRole.updateTime.$invalid }"
                v-model="$v.iRole.updateTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.createUserId')" for="i-role-createUserId">Create User Id</label>
            <input
              type="number"
              class="form-control"
              name="createUserId"
              id="i-role-createUserId"
              data-cy="createUserId"
              :class="{ valid: !$v.iRole.createUserId.$invalid, invalid: $v.iRole.createUserId.$invalid }"
              v-model.number="$v.iRole.createUserId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.updateUserId')" for="i-role-updateUserId">Update User Id</label>
            <input
              type="number"
              class="form-control"
              name="updateUserId"
              id="i-role-updateUserId"
              data-cy="updateUserId"
              :class="{ valid: !$v.iRole.updateUserId.$invalid, invalid: $v.iRole.updateUserId.$invalid }"
              v-model.number="$v.iRole.updateUserId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.users')" for="i-role-users">Users</label>
            <select class="form-control" id="i-role-users" data-cy="users" name="users" v-model="iRole.users">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="iRole.users && iUserRoleOption.id === iRole.users.id ? iRole.users : iUserRoleOption"
                v-for="iUserRoleOption in iUserRoles"
                :key="iUserRoleOption.id"
              >
                {{ iUserRoleOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iRole.menus')" for="i-role-menus">Menus</label>
            <select class="form-control" id="i-role-menus" data-cy="menus" name="menus" v-model="iRole.menus">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="iRole.menus && iRoleMenuOption.id === iRole.menus.id ? iRole.menus : iRoleMenuOption"
                v-for="iRoleMenuOption in iRoleMenus"
                :key="iRoleMenuOption.id"
              >
                {{ iRoleMenuOption.id }}
              </option>
            </select>
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
            :disabled="$v.iRole.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./i-role-update.component.ts"></script>
