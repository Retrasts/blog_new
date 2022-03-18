import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IIComment, IComment } from '@/shared/model/i-comment.model';
import ICommentService from './i-comment.service';

const validations: any = {
  iComment: {
    createTime: {},
    createUserId: {},
    blogId: {},
    content: {},
    likes: {},
    parentId: {},
  },
};

@Component({
  validations,
})
export default class ICommentUpdate extends Vue {
  @Inject('iCommentService') private iCommentService: () => ICommentService;
  @Inject('alertService') private alertService: () => AlertService;

  public iComment: IIComment = new IComment();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iCommentId) {
        vm.retrieveIComment(to.params.iCommentId);
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
    if (this.iComment.id) {
      this.iCommentService()
        .update(this.iComment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iComment.updated', { param: param.id });
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
      this.iCommentService()
        .create(this.iComment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iComment.created', { param: param.id });
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

  public retrieveIComment(iCommentId): void {
    this.iCommentService()
      .find(iCommentId)
      .then(res => {
        this.iComment = res;
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
