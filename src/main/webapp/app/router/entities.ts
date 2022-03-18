import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const IUser = () => import('@/entities/i-user/i-user.vue');
// prettier-ignore
const IUserUpdate = () => import('@/entities/i-user/i-user-update.vue');
// prettier-ignore
const IUserDetails = () => import('@/entities/i-user/i-user-details.vue');
// prettier-ignore
const IRole = () => import('@/entities/i-role/i-role.vue');
// prettier-ignore
const IRoleUpdate = () => import('@/entities/i-role/i-role-update.vue');
// prettier-ignore
const IRoleDetails = () => import('@/entities/i-role/i-role-details.vue');
// prettier-ignore
const IUserRole = () => import('@/entities/i-user-role/i-user-role.vue');
// prettier-ignore
const IUserRoleUpdate = () => import('@/entities/i-user-role/i-user-role-update.vue');
// prettier-ignore
const IUserRoleDetails = () => import('@/entities/i-user-role/i-user-role-details.vue');
// prettier-ignore
const IMenu = () => import('@/entities/i-menu/i-menu.vue');
// prettier-ignore
const IMenuUpdate = () => import('@/entities/i-menu/i-menu-update.vue');
// prettier-ignore
const IMenuDetails = () => import('@/entities/i-menu/i-menu-details.vue');
// prettier-ignore
const IRoleMenu = () => import('@/entities/i-role-menu/i-role-menu.vue');
// prettier-ignore
const IRoleMenuUpdate = () => import('@/entities/i-role-menu/i-role-menu-update.vue');
// prettier-ignore
const IRoleMenuDetails = () => import('@/entities/i-role-menu/i-role-menu-details.vue');
// prettier-ignore
const IBlog = () => import('@/entities/i-blog/i-blog.vue');
// prettier-ignore
const IBlogUpdate = () => import('@/entities/i-blog/i-blog-update.vue');
// prettier-ignore
const IBlogDetails = () => import('@/entities/i-blog/i-blog-details.vue');
// prettier-ignore
const IComment = () => import('@/entities/i-comment/i-comment.vue');
// prettier-ignore
const ICommentUpdate = () => import('@/entities/i-comment/i-comment-update.vue');
// prettier-ignore
const ICommentDetails = () => import('@/entities/i-comment/i-comment-details.vue');
// prettier-ignore
const IClassify = () => import('@/entities/i-classify/i-classify.vue');
// prettier-ignore
const IClassifyUpdate = () => import('@/entities/i-classify/i-classify-update.vue');
// prettier-ignore
const IClassifyDetails = () => import('@/entities/i-classify/i-classify-details.vue');
// prettier-ignore
const ILabel = () => import('@/entities/i-label/i-label.vue');
// prettier-ignore
const ILabelUpdate = () => import('@/entities/i-label/i-label-update.vue');
// prettier-ignore
const ILabelDetails = () => import('@/entities/i-label/i-label-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'i-user',
      name: 'IUser',
      component: IUser,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-user/new',
      name: 'IUserCreate',
      component: IUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-user/:iUserId/edit',
      name: 'IUserEdit',
      component: IUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-user/:iUserId/view',
      name: 'IUserView',
      component: IUserDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role',
      name: 'IRole',
      component: IRole,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role/new',
      name: 'IRoleCreate',
      component: IRoleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role/:iRoleId/edit',
      name: 'IRoleEdit',
      component: IRoleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role/:iRoleId/view',
      name: 'IRoleView',
      component: IRoleDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-user-role',
      name: 'IUserRole',
      component: IUserRole,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-user-role/new',
      name: 'IUserRoleCreate',
      component: IUserRoleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-user-role/:iUserRoleId/edit',
      name: 'IUserRoleEdit',
      component: IUserRoleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-user-role/:iUserRoleId/view',
      name: 'IUserRoleView',
      component: IUserRoleDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-menu',
      name: 'IMenu',
      component: IMenu,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-menu/new',
      name: 'IMenuCreate',
      component: IMenuUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-menu/:iMenuId/edit',
      name: 'IMenuEdit',
      component: IMenuUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-menu/:iMenuId/view',
      name: 'IMenuView',
      component: IMenuDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role-menu',
      name: 'IRoleMenu',
      component: IRoleMenu,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role-menu/new',
      name: 'IRoleMenuCreate',
      component: IRoleMenuUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role-menu/:iRoleMenuId/edit',
      name: 'IRoleMenuEdit',
      component: IRoleMenuUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-role-menu/:iRoleMenuId/view',
      name: 'IRoleMenuView',
      component: IRoleMenuDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-blog',
      name: 'IBlog',
      component: IBlog,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-blog/new',
      name: 'IBlogCreate',
      component: IBlogUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-blog/:iBlogId/edit',
      name: 'IBlogEdit',
      component: IBlogUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-blog/:iBlogId/view',
      name: 'IBlogView',
      component: IBlogDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-comment',
      name: 'IComment',
      component: IComment,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-comment/new',
      name: 'ICommentCreate',
      component: ICommentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-comment/:iCommentId/edit',
      name: 'ICommentEdit',
      component: ICommentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-comment/:iCommentId/view',
      name: 'ICommentView',
      component: ICommentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-classify',
      name: 'IClassify',
      component: IClassify,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-classify/new',
      name: 'IClassifyCreate',
      component: IClassifyUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-classify/:iClassifyId/edit',
      name: 'IClassifyEdit',
      component: IClassifyUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-classify/:iClassifyId/view',
      name: 'IClassifyView',
      component: IClassifyDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-label',
      name: 'ILabel',
      component: ILabel,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-label/new',
      name: 'ILabelCreate',
      component: ILabelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-label/:iLabelId/edit',
      name: 'ILabelEdit',
      component: ILabelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'i-label/:iLabelId/view',
      name: 'ILabelView',
      component: ILabelDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
