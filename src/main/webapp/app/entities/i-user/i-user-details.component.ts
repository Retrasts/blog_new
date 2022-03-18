import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIUser } from '@/shared/model/i-user.model';
import IUserService from './i-user.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class IUserDetails extends Vue {
  @Inject('iUserService') private iUserService: () => IUserService;
  @Inject('alertService') private alertService: () => AlertService;

  public iUser: IIUser = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iUserId) {
        vm.retrieveIUser(to.params.iUserId);
      }
    });
  }

  public retrieveIUser(iUserId) {
    this.iUserService()
      .find(iUserId)
      .then(res => {
        this.iUser = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
