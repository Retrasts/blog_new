import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IIClassify, IClassify } from '@/shared/model/i-classify.model';
import IClassifyService from './i-classify.service';

const validations: any = {
  iClassify: {
    name: {},
    alias: {},
    description: {},
    parentId: {},
  },
};

@Component({
  validations,
})
export default class IClassifyUpdate extends Vue {
  @Inject('iClassifyService') private iClassifyService: () => IClassifyService;
  @Inject('alertService') private alertService: () => AlertService;

  public iClassify: IIClassify = new IClassify();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iClassifyId) {
        vm.retrieveIClassify(to.params.iClassifyId);
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
    if (this.iClassify.id) {
      this.iClassifyService()
        .update(this.iClassify)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iClassify.updated', { param: param.id });
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
      this.iClassifyService()
        .create(this.iClassify)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iClassify.created', { param: param.id });
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

  public retrieveIClassify(iClassifyId): void {
    this.iClassifyService()
      .find(iClassifyId)
      .then(res => {
        this.iClassify = res;
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
