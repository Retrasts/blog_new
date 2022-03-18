import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIComment } from '@/shared/model/i-comment.model';
import ICommentService from './i-comment.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ICommentDetails extends Vue {
  @Inject('iCommentService') private iCommentService: () => ICommentService;
  @Inject('alertService') private alertService: () => AlertService;

  public iComment: IIComment = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iCommentId) {
        vm.retrieveIComment(to.params.iCommentId);
      }
    });
  }

  public retrieveIComment(iCommentId) {
    this.iCommentService()
      .find(iCommentId)
      .then(res => {
        this.iComment = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
