import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IIRoleMenu } from '@/shared/model/i-role-menu.model';

import IRoleMenuService from './i-role-menu.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class IRoleMenu extends Vue {
  @Inject('iRoleMenuService') private iRoleMenuService: () => IRoleMenuService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public iRoleMenus: IIRoleMenu[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllIRoleMenus();
  }

  public clear(): void {
    this.retrieveAllIRoleMenus();
  }

  public retrieveAllIRoleMenus(): void {
    this.isFetching = true;
    this.iRoleMenuService()
      .retrieve()
      .then(
        res => {
          this.iRoleMenus = res.data;
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

  public prepareRemove(instance: IIRoleMenu): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeIRoleMenu(): void {
    this.iRoleMenuService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('blogNewApp.iRoleMenu.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllIRoleMenus();
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
