import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIRoleMenu } from '@/shared/model/i-role-menu.model';
import IRoleMenuService from './i-role-menu.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class IRoleMenuDetails extends Vue {
  @Inject('iRoleMenuService') private iRoleMenuService: () => IRoleMenuService;
  @Inject('alertService') private alertService: () => AlertService;

  public iRoleMenu: IIRoleMenu = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iRoleMenuId) {
        vm.retrieveIRoleMenu(to.params.iRoleMenuId);
      }
    });
  }

  public retrieveIRoleMenu(iRoleMenuId) {
    this.iRoleMenuService()
      .find(iRoleMenuId)
      .then(res => {
        this.iRoleMenu = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
