<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="blogNewApp.iUser.home.createOrEditLabel"
          data-cy="IUserCreateUpdateHeading"
          v-text="$t('blogNewApp.iUser.home.createOrEditLabel')"
        >
          Create or edit a IUser
        </h2>
        <div>
          <div class="form-group" v-if="iUser.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="iUser.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.ip')" for="i-user-ip">Ip</label>
            <input
              type="text"
              class="form-control"
              name="ip"
              id="i-user-ip"
              data-cy="ip"
              :class="{ valid: !$v.iUser.ip.$invalid, invalid: $v.iUser.ip.$invalid }"
              v-model="$v.iUser.ip.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.username')" for="i-user-username">Username</label>
            <input
              type="text"
              class="form-control"
              name="username"
              id="i-user-username"
              data-cy="username"
              :class="{ valid: !$v.iUser.username.$invalid, invalid: $v.iUser.username.$invalid }"
              v-model="$v.iUser.username.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.nikename')" for="i-user-nikename">Nikename</label>
            <input
              type="text"
              class="form-control"
              name="nikename"
              id="i-user-nikename"
              data-cy="nikename"
              :class="{ valid: !$v.iUser.nikename.$invalid, invalid: $v.iUser.nikename.$invalid }"
              v-model="$v.iUser.nikename.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.password')" for="i-user-password">Password</label>
            <input
              type="text"
              class="form-control"
              name="password"
              id="i-user-password"
              data-cy="password"
              :class="{ valid: !$v.iUser.password.$invalid, invalid: $v.iUser.password.$invalid }"
              v-model="$v.iUser.password.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.sex')" for="i-user-sex">Sex</label>
            <input
              type="number"
              class="form-control"
              name="sex"
              id="i-user-sex"
              data-cy="sex"
              :class="{ valid: !$v.iUser.sex.$invalid, invalid: $v.iUser.sex.$invalid }"
              v-model.number="$v.iUser.sex.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.emaile')" for="i-user-emaile">Emaile</label>
            <input
              type="text"
              class="form-control"
              name="emaile"
              id="i-user-emaile"
              data-cy="emaile"
              :class="{ valid: !$v.iUser.emaile.$invalid, invalid: $v.iUser.emaile.$invalid }"
              v-model="$v.iUser.emaile.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.avatar')" for="i-user-avatar">Avatar</label>
            <input
              type="text"
              class="form-control"
              name="avatar"
              id="i-user-avatar"
              data-cy="avatar"
              :class="{ valid: !$v.iUser.avatar.$invalid, invalid: $v.iUser.avatar.$invalid }"
              v-model="$v.iUser.avatar.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.createTime')" for="i-user-createTime">Create Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-user-createTime"
                  v-model="$v.iUser.createTime.$model"
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
                id="i-user-createTime"
                data-cy="createTime"
                type="text"
                class="form-control"
                name="createTime"
                :class="{ valid: !$v.iUser.createTime.$invalid, invalid: $v.iUser.createTime.$invalid }"
                v-model="$v.iUser.createTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.updateTime')" for="i-user-updateTime">Update Time</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-user-updateTime"
                  v-model="$v.iUser.updateTime.$model"
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
                id="i-user-updateTime"
                data-cy="updateTime"
                type="text"
                class="form-control"
                name="updateTime"
                :class="{ valid: !$v.iUser.updateTime.$invalid, invalid: $v.iUser.updateTime.$invalid }"
                v-model="$v.iUser.updateTime.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.createUserId')" for="i-user-createUserId">Create User Id</label>
            <input
              type="number"
              class="form-control"
              name="createUserId"
              id="i-user-createUserId"
              data-cy="createUserId"
              :class="{ valid: !$v.iUser.createUserId.$invalid, invalid: $v.iUser.createUserId.$invalid }"
              v-model.number="$v.iUser.createUserId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.updateUserId')" for="i-user-updateUserId">Update User Id</label>
            <input
              type="number"
              class="form-control"
              name="updateUserId"
              id="i-user-updateUserId"
              data-cy="updateUserId"
              :class="{ valid: !$v.iUser.updateUserId.$invalid, invalid: $v.iUser.updateUserId.$invalid }"
              v-model.number="$v.iUser.updateUserId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.birthday')" for="i-user-birthday">Birthday</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="i-user-birthday"
                  v-model="$v.iUser.birthday.$model"
                  name="birthday"
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
                id="i-user-birthday"
                data-cy="birthday"
                type="text"
                class="form-control"
                name="birthday"
                :class="{ valid: !$v.iUser.birthday.$invalid, invalid: $v.iUser.birthday.$invalid }"
                v-model="$v.iUser.birthday.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.company')" for="i-user-company">Company</label>
            <input
              type="text"
              class="form-control"
              name="company"
              id="i-user-company"
              data-cy="company"
              :class="{ valid: !$v.iUser.company.$invalid, invalid: $v.iUser.company.$invalid }"
              v-model="$v.iUser.company.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('blogNewApp.iUser.phone')" for="i-user-phone">Phone</label>
            <input
              type="number"
              class="form-control"
              name="phone"
              id="i-user-phone"
              data-cy="phone"
              :class="{ valid: !$v.iUser.phone.$invalid, invalid: $v.iUser.phone.$invalid }"
              v-model.number="$v.iUser.phone.$model"
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
            :disabled="$v.iUser.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./i-user-update.component.ts"></script>
