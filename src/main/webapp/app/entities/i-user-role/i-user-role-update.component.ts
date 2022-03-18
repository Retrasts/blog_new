import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import IRoleService from '@/entities/i-role/i-role.service';
import { IIRole } from '@/shared/model/i-role.model';

import IUserService from '@/entities/i-user/i-user.service';
import { IIUser } from '@/shared/model/i-user.model';

import { IIUserRole, IUserRole } from '@/shared/model/i-user-role.model';
import IUserRoleService from './i-user-role.service';

const validations: any = {
  iUserRole: {},
};

@Component({
  validations,
})
export default class IUserRoleUpdate extends Vue {
  @Inject('iUserRoleService') private iUserRoleService: () => IUserRoleService;
  @Inject('alertService') private alertService: () => AlertService;

  public iUserRole: IIUserRole = new IUserRole();

  @Inject('iRoleService') private iRoleService: () => IRoleService;

  public iRoles: IIRole[] = [];

  @Inject('iUserService') private iUserService: () => IUserService;

  public iUsers: IIUser[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iUserRoleId) {
        vm.retrieveIUserRole(to.params.iUserRoleId);
      }
      vm.initRelationships();
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
    if (this.iUserRole.id) {
      this.iUserRoleService()
        .update(this.iUserRole)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iUserRole.updated', { param: param.id });
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
      this.iUserRoleService()
        .create(this.iUserRole)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iUserRole.created', { param: param.id });
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

  public retrieveIUserRole(iUserRoleId): void {
    this.iUserRoleService()
      .find(iUserRoleId)
      .then(res => {
        this.iUserRole = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.iRoleService()
      .retrieve()
      .then(res => {
        this.iRoles = res.data;
      });
    this.iUserService()
      .retrieve()
      .then(res => {
        this.iUsers = res.data;
      });
  }
}
