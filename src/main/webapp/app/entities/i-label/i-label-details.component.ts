import { Component, Vue, Inject } from 'vue-property-decorator';

import { IILabel } from '@/shared/model/i-label.model';
import ILabelService from './i-label.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ILabelDetails extends Vue {
  @Inject('iLabelService') private iLabelService: () => ILabelService;
  @Inject('alertService') private alertService: () => AlertService;

  public iLabel: IILabel = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iLabelId) {
        vm.retrieveILabel(to.params.iLabelId);
      }
    });
  }

  public retrieveILabel(iLabelId) {
    this.iLabelService()
      .find(iLabelId)
      .then(res => {
        this.iLabel = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
