<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="userClient">
        <h2 class="jh-entity-heading" data-cy="userClientDetailsHeading">
          <span v-text="$t('pcargaApp.userClient.detail.title')">UserClient</span> {{ userClient.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pcargaApp.userClient.idOperator')">Id Operator</span>
          </dt>
          <dd>
            <span>{{ userClient.idOperator }}</span>
          </dd>
          <dt>
            <span v-text="$t('pcargaApp.userClient.idUserOperator')">Id User Operator</span>
          </dt>
          <dd>
            <span>{{ userClient.idUserOperator }}</span>
          </dd>
          <dt>
            <span v-text="$t('pcargaApp.userClient.idTorneo')">Id Torneo</span>
          </dt>
          <dd>
            <span>{{ userClient.idTorneo }}</span>
          </dd>
          <dt>
            <span v-text="$t('pcargaApp.userClient.acumulado')">Acumulado</span>
          </dt>
          <dd>
            <span>{{ userClient.acumulado }}</span>
          </dd>
          <dt>
            <span v-text="$t('pcargaApp.userClient.lastReport')">Last Report</span>
          </dt>
          <dd>
            <span v-if="userClient.lastReport">{{ $d(Date.parse(userClient.lastReport), 'long') }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="userClient.id"
          :to="{ name: 'UserClientEdit', params: { userClientId: userClient.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-client-details.component.ts"></script>
