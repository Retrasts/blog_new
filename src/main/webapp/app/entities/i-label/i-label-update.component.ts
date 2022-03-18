import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IILabel, ILabel } from '@/shared/model/i-label.model';
import ILabelService from './i-label.service';

const validations: any = {
  iLabel: {
    name: {},
    alias: {},
    description: {},
    parentId: {},
  },
};

@Component({
  validations,
})
export default class ILabelUpdate extends Vue {
  @Inject('iLabelService') private iLabelService: () => ILabelService;
  @Inject('alertService') private alertService: () => AlertService;

  public iLabel: IILabel = new ILabel();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iLabelId) {
        vm.retrieveILabel(to.params.iLabelId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.iLabel.id) {
      this.iLabelService()
        .update(this.iLabel)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iLabel.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.iLabelService()
        .create(this.iLabel)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iLabel.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveILabel(iLabelId): void {
    this.iLabelService()
      .find(iLabelId)
      .then(res => {
        this.iLabel = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
