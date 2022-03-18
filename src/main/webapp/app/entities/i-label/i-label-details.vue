<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="iLabel">
        <h2 class="jh-entity-heading" data-cy="iLabelDetailsHeading">
          <span v-text="$t('blogNewApp.iLabel.detail.title')">ILabel</span> {{ iLabel.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('blogNewApp.iLabel.name')">Name</span>
          </dt>
          <dd>
            <span>{{ iLabel.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iLabel.alias')">Alias</span>
          </dt>
          <dd>
            <span>{{ iLabel.alias }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iLabel.description')">Description</span>
          </dt>
          <dd>
            <span>{{ iLabel.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('blogNewApp.iLabel.parentId')">Parent Id</span>
          </dt>
          <dd>
            <span>{{ iLabel.parentId }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="iLabel.id" :to="{ name: 'ILabelEdit', params: { iLabelId: iLabel.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./i-label-details.component.ts"></script>
