import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IIBlog, IBlog } from '@/shared/model/i-blog.model';
import IBlogService from './i-blog.service';

const validations: any = {
  iBlog: {
    createUserId: {},
    title: {},
    label: {},
    classify: {},
    content: {},
    likes: {},
    replynumber: {},
    createTime: {},
    updateTime: {},
  },
};

@Component({
  validations,
})
export default class IBlogUpdate extends Vue {
  @Inject('iBlogService') private iBlogService: () => IBlogService;
  @Inject('alertService') private alertService: () => AlertService;

  public iBlog: IIBlog = new IBlog();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iBlogId) {
        vm.retrieveIBlog(to.params.iBlogId);
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
    if (this.iBlog.id) {
      this.iBlogService()
        .update(this.iBlog)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iBlog.updated', { param: param.id });
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
      this.iBlogService()
        .create(this.iBlog)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iBlog.created', { param: param.id });
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

  public retrieveIBlog(iBlogId): void {
    this.iBlogService()
      .find(iBlogId)
      .then(res => {
        this.iBlog = res;
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
