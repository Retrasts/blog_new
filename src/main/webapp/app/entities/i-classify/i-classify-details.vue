<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iClassify">
        <h2 class="jh-entity-heading" data-cy="iClassifyDetailsHeading">
          <span v-text="$t('blogNewApp.iClassify.detail.title')">IClassify</span> {{ iClassify.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iClassify.name')">Name</span>
          </dt>
          <dd>
            <span>{{ iClassify.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iClassify.alias')">Alias</span>
          </dt>
          <dd>
            <span>{{ iClassify.alias }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iClassify.description')">Description</span>
          </dt>
          <dd>
            <span>{{ iClassify.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iClassify.parentId')">Parent Id</span>
          </dt>
          <dd>
            <span>{{ iClassify.parentId }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="iClassify.id"
          :to="{ name: 'IClassifyEdit', params: { iClassifyId: iClassify.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-classify-details.component.ts"></script>
