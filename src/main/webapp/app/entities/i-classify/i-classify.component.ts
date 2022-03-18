import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IIClassify } from '@/shared/model/i-classify.model';

import IClassifyService from './i-classify.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class IClassify extends Vue {
  @Inject('iClassifyService') private iClassifyService: () => IClassifyService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public iClassifies: IIClassify[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllIClassifys();
  }

  public clear(): void {
    this.retrieveAllIClassifys();
  }

  public retrieveAllIClassifys(): void {
    this.isFetching = true;
    this.iClassifyService()
      .retrieve()
      .then(
        res => {
          this.iClassifies = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IIClassify): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeIClassify(): void {
    this.iClassifyService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('blogNewApp.iClassify.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllIClassifys();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
