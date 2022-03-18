import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIMenu } from '@/shared/model/i-menu.model';
import IMenuService from './i-menu.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class IMenuDetails extends Vue {
  @Inject('iMenuService') private iMenuService: () => IMenuService;
  @Inject('alertService') private alertService: () => AlertService;

  public iMenu: IIMenu = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iMenuId) {
        vm.retrieveIMenu(to.params.iMenuId);
      }
    });
  }

  public retrieveIMenu(iMenuId) {
    this.iMenuService()
      .find(iMenuId)
      .then(res => {
        this.iMenu = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
