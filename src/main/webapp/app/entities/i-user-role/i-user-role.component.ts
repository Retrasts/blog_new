import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IIUserRole } from '@/shared/model/i-user-role.model';

import IUserRoleService from './i-user-role.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class IUserRole extends Vue {
  @Inject('iUserRoleService') private iUserRoleService: () => IUserRoleService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public iUserRoles: IIUserRole[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllIUserRoles();
  }

  public clear(): void {
    this.retrieveAllIUserRoles();
  }

  public retrieveAllIUserRoles(): void {
    this.isFetching = true;
    this.iUserRoleService()
      .retrieve()
      .then(
        res => {
          this.iUserRoles = res.data;
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

  public prepareRemove(instance: IIUserRole): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeIUserRole(): void {
    this.iUserRoleService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('blogNewApp.iUserRole.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllIUserRoles();
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
