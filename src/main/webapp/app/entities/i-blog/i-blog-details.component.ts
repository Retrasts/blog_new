import { Component, Vue, Inject } from 'vue-property-decorator';

import { IIBlog } from '@/shared/model/i-blog.model';
import IBlogService from './i-blog.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class IBlogDetails extends Vue {
  @Inject('iBlogService') private iBlogService: () => IBlogService;
  @Inject('alertService') private alertService: () => AlertService;

  public iBlog: IIBlog = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iBlogId) {
        vm.retrieveIBlog(to.params.iBlogId);
      }
    });
  }

  public retrieveIBlog(iBlogId) {
    this.iBlogService()
      .find(iBlogId)
      .then(res => {
        this.iBlog = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
