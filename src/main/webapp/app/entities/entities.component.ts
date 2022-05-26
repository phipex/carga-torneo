import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import UserClientService from './user-client/user-client.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('userClientService') private userClientService = () => new UserClientService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
