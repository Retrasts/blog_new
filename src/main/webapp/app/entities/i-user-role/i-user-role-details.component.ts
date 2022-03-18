import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIUserRole } from '@/shared/model/i-user-role.model';
import IUserRoleService from './i-user-role.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class IUserRoleDetails extends Vue {
  @Inject('iUserRoleService') private iUserRoleService: () => IUserRoleService;
  @Inject('alertService') private alertService: () => AlertService;

  public iUserRole: IIUserRole = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iUserRoleId) {
        vm.retrieveIUserRole(to.params.iUserRoleId);
      }
    });
  }

  public retrieveIUserRole(iUserRoleId) {
    this.iUserRoleService()
      .find(iUserRoleId)
      .then(res => {
        this.iUserRole = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
