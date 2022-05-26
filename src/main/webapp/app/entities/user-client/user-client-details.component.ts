import { Component, Vue, Inject } from 'vue-property-decorator';

import { IUserClient } from '@/shared/model/user-client.model';
import UserClientService from './user-client.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class UserClientDetails extends Vue {
  @Inject('userClientService') private userClientService: () => UserClientService;
  @Inject('alertService') private alertService: () => AlertService;

  public userClient: IUserClient = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.userClientId) {
        vm.retrieveUserClient(to.params.userClientId);
      }
    });
  }

  public retrieveUserClient(userClientId) {
    this.userClientService()
      .find(userClientId)
      .then(res => {
        this.userClient = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
