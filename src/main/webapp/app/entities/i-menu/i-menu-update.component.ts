import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import IRoleMenuService from '@/entities/i-role-menu/i-role-menu.service';
import { IIRoleMenu } from '@/shared/model/i-role-menu.model';

import { IIMenu, IMenu } from '@/shared/model/i-menu.model';
import IMenuService from './i-menu.service';

const validations: any = {
  iMenu: {
    url: {},
    menuName: {},
    parentId: {},
    createTime: {},
    updateTime: {},
    createUserId: {},
    updateUserId: {},
  },
};

@Component({
  validations,
})
export default class IMenuUpdate extends Vue {
  @Inject('iMenuService') private iMenuService: () => IMenuService;
  @Inject('alertService') private alertService: () => AlertService;

  public iMenu: IIMenu = new IMenu();

  @Inject('iRoleMenuService') private iRoleMenuService: () => IRoleMenuService;

  public iRoleMenus: IIRoleMenu[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iMenuId) {
        vm.retrieveIMenu(to.params.iMenuId);
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
    if (this.iMenu.id) {
      this.iMenuService()
        .update(this.iMenu)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iMenu.updated', { param: param.id });
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
      this.iMenuService()
        .create(this.iMenu)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iMenu.created', { param: param.id });
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

  public retrieveIMenu(iMenuId): void {
    this.iMenuService()
      .find(iMenuId)
      .then(res => {
        this.iMenu = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.iRoleMenuService()
      .retrieve()
      .then(res => {
        this.iRoleMenus = res.data;
      });
  }
}
