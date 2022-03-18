import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIRole } from '@/shared/model/i-role.model';
import IRoleService from './i-role.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class IRoleDetails extends Vue {
  @Inject('iRoleService') private iRoleService: () => IRoleService;
  @Inject('alertService') private alertService: () => AlertService;

  public iRole: IIRole = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iRoleId) {
        vm.retrieveIRole(to.params.iRoleId);
      }
    });
  }

  public retrieveIRole(iRoleId) {
    this.iRoleService()
      .find(iRoleId)
      .then(res => {
        this.iRole = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
