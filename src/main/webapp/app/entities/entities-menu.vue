<template>
  <div>
    <b-dropdown-item to="/i-user">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iUser')">I User</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-role">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iRole')">I Role</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-user-role">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iUserRole')">I User Role</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-menu">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iMenu')">I Menu</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-role-menu">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iRoleMenu')">I Role Menu</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-blog">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iBlog')">I Blog</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-comment">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iComment')">I Comment</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-classify">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iClassify')">I Classify</span>
    </b-dropdown-item>
    <b-dropdown-item to="/i-label">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.iLabel')">I Label</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
