import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIClassify } from '@/shared/model/i-classify.model';
import IClassifyService from './i-classify.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class IClassifyDetails extends Vue {
  @Inject('iClassifyService') private iClassifyService: () => IClassifyService;
  @Inject('alertService') private alertService: () => AlertService;

  public iClassify: IIClassify = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iClassifyId) {
        vm.retrieveIClassify(to.params.iClassifyId);
      }
    });
  }

  public retrieveIClassify(iClassifyId) {
    this.iClassifyService()
      .find(iClassifyId)
      .then(res => {
        this.iClassify = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
