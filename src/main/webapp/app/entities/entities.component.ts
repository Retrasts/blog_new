import { Component, Provide, Vue } from 'vue-property-decorator';

import IUserService from './i-user/i-user.service';
import IRoleService from './i-role/i-role.service';
import IUserRoleService from './i-user-role/i-user-role.service';
import IMenuService from './i-menu/i-menu.service';
import IRoleMenuService from './i-role-menu/i-role-menu.service';
import IBlogService from './i-blog/i-blog.service';
import ICommentService from './i-comment/i-comment.service';
import IClassifyService from './i-classify/i-classify.service';
import ILabelService from './i-label/i-label.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('iUserService') private iUserService = () => new IUserService();
  @Provide('iRoleService') private iRoleService = () => new IRoleService();
  @Provide('iUserRoleService') private iUserRoleService = () => new IUserRoleService();
  @Provide('iMenuService') private iMenuService = () => new IMenuService();
  @Provide('iRoleMenuService') private iRoleMenuService = () => new IRoleMenuService();
  @Provide('iBlogService') private iBlogService = () => new IBlogService();
  @Provide('iCommentService') private iCommentService = () => new ICommentService();
  @Provide('iClassifyService') private iClassifyService = () => new IClassifyService();
  @Provide('iLabelService') private iLabelService = () => new ILabelService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
