import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import IUserRoleService from '@/entities/i-user-role/i-user-role.service';
import { IIUserRole } from '@/shared/model/i-user-role.model';

import IRoleMenuService from '@/entities/i-role-menu/i-role-menu.service';
import { IIRoleMenu } from '@/shared/model/i-role-menu.model';

import { IIRole, IRole } from '@/shared/model/i-role.model';
import IRoleService from './i-role.service';

const validations: any = {
  iRole: {
    roleName: {},
    remark: {},
    createTime: {},
    updateTime: {},
    createUserId: {},
    updateUserId: {},
  },
};

@Component({
  validations,
})
export default class IRoleUpdate extends Vue {
  @Inject('iRoleService') private iRoleService: () => IRoleService;
  @Inject('alertService') private alertService: () => AlertService;

  public iRole: IIRole = new IRole();

  @Inject('iUserRoleService') private iUserRoleService: () => IUserRoleService;

  public iUserRoles: IIUserRole[] = [];

  @Inject('iRoleMenuService') private iRoleMenuService: () => IRoleMenuService;

  public iRoleMenus: IIRoleMenu[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iRoleId) {
        vm.retrieveIRole(to.params.iRoleId);
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
    if (this.iRole.id) {
      this.iRoleService()
        .update(this.iRole)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iRole.updated', { param: param.id });
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
      this.iRoleService()
        .create(this.iRole)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iRole.created', { param: param.id });
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

  public retrieveIRole(iRoleId): void {
    this.iRoleService()
      .find(iRoleId)
      .then(res => {
        this.iRole = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.iUserRoleService()
      .retrieve()
      .then(res => {
        this.iUserRoles = res.data;
      });
    this.iRoleMenuService()
      .retrieve()
      .then(res => {
        this.iRoleMenus = res.data;
      });
  }
}
