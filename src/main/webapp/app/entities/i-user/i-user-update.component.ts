import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import IUserRoleService from '@/entities/i-user-role/i-user-role.service';
import { IIUserRole } from '@/shared/model/i-user-role.model';

import { IIUser, IUser } from '@/shared/model/i-user.model';
import IUserService from './i-user.service';

const validations: any = {
  iUser: {
    ip: {},
    username: {},
    nikename: {},
    password: {},
    sex: {},
    emaile: {},
    avatar: {},
    createTime: {},
    updateTime: {},
    createUserId: {},
    updateUserId: {},
    birthday: {},
    company: {},
    phone: {},
  },
};

@Component({
  validations,
})
export default class IUserUpdate extends Vue {
  @Inject('iUserService') private iUserService: () => IUserService;
  @Inject('alertService') private alertService: () => AlertService;

  public iUser: IIUser = new IUser();

  @Inject('iUserRoleService') private iUserRoleService: () => IUserRoleService;

  public iUserRoles: IIUserRole[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.iUserId) {
        vm.retrieveIUser(to.params.iUserId);
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
    if (this.iUser.id) {
      this.iUserService()
        .update(this.iUser)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iUser.updated', { param: param.id });
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
      this.iUserService()
        .create(this.iUser)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('blogNewApp.iUser.created', { param: param.id });
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

  public retrieveIUser(iUserId): void {
    this.iUserService()
      .find(iUserId)
      .then(res => {
        this.iUser = res;
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
  }
}
