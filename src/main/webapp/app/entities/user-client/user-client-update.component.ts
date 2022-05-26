import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, decimal, minValue } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import { IUserClient, UserClient } from '@/shared/model/user-client.model';
import UserClientService from './user-client.service';

const validations: any = {
  userClient: {
    idOperator: {
      required,
      numeric,
    },
    idUserOperator: {
      required,
    },
    idTorneo: {
      required,
      numeric,
    },
    acumulado: {
      required,
      decimal,
      min: minValue(0),
    },
    lastReport: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class UserClientUpdate extends Vue {
  @Inject('userClientService') private userClientService: () => UserClientService;
  @Inject('alertService') private alertService: () => AlertService;

  public userClient: IUserClient = new UserClient();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.userClientId) {
        vm.retrieveUserClient(to.params.userClientId);
      }
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
    if (this.userClient.id) {
      this.userClientService()
        .update(this.userClient)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pcargaApp.userClient.updated', { param: param.id });
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
      this.userClientService()
        .create(this.userClient)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('pcargaApp.userClient.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.userClient[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.userClient[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.userClient[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.userClient[field] = null;
    }
  }

  public retrieveUserClient(userClientId): void {
    this.userClientService()
      .find(userClientId)
      .then(res => {
        res.lastReport = new Date(res.lastReport);
        this.userClient = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
