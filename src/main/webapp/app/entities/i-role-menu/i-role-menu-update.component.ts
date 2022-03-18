import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import IRoleService from '@/entities/i-role/i-role.service';
import { IIRole } from '@/shared/model/i-role.model';

import IMenuService from '@/entities/i-menu/i-menu.service';
import { IIMenu } from '@/shared/model/i-menu.model';

import { IIRoleMenu, IRoleMenu } from '@/shared/model/i-role-menu.model';
import IRoleMenuService from './i-role-menu.service';

const validations: any = {
  iRoleMenu: {},
};

@Component({
  validations,
})
export default class IRoleMenuUpdate extends Vue {
  @Inject('iRoleMenuService') private iRoleMenuService: () => IRoleMenuService;
  @Inject('alertService') private alertService: () => AlertService;

  public iRoleMenu: IIRoleMenu = new IRoleMenu();

  @Inject('iRoleService') private iRoleService: () => IRoleService;

  public iRoles: IIRole[] = [];

  @Inject('iMenuService') private iMenuService: () => IMenuService;

  public iMenus: IIMenu[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iRoleMenuId) {
        vm.retrieveIRoleMenu(to.params.iRoleMenuId);
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
    if (this.iRoleMenu.id) {
      this.iRoleMenuService()
        .update(this.iRoleMenu)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iRoleMenu.updated', { param: param.id });
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
      this.iRoleMenuService()
        .create(this.iRoleMenu)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iRoleMenu.created', { param: param.id });
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

  public retrieveIRoleMenu(iRoleMenuId): void {
    this.iRoleMenuService()
      .find(iRoleMenuId)
      .then(res => {
        this.iRoleMenu = res;
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
    this.iMenuService()
      .retrieve()
      .then(res => {
        this.iMenus = res.data;
      });
  }
}
