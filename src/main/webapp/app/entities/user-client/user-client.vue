<template>
  <div>
    <h2 id="page-heading" data-cy="UserClientHeading">
      <span v-text="$t('pcargaApp.userClient.home.title')" id="user-client-heading">User Clients</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pcargaApp.userClient.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'UserClientCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-user-client"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pcargaApp.userClient.home.createLabel')"> Create a new User Client </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && userClients && userClients.length === 0">
      <span v-text="$t('pcargaApp.userClient.home.notFound')">No userClients found</span>
    </div>
    <div class="table-responsive" v-if="userClients && userClients.length > 0">
      <table class="table table-striped" aria-describedby="userClients">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('idOperator')">
              <span v-text="$t('pcargaApp.userClient.idOperator')">Id Operator</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'idOperator'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('idUserOperator')">
              <span v-text="$t('pcargaApp.userClient.idUserOperator')">Id User Operator</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'idUserOperator'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('idTorneo')">
              <span v-text="$t('pcargaApp.userClient.idTorneo')">Id Torneo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'idTorneo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('acumulado')">
              <span v-text="$t('pcargaApp.userClient.acumulado')">Acumulado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'acumulado'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('lastReport')">
              <span v-text="$t('pcargaApp.userClient.lastReport')">Last Report</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastReport'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="userClient in userClients" :key="userClient.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UserClientView', params: { userClientId: userClient.id } }">{{ userClient.id }}</router-link>
            </td>
            <td>{{ userClient.idOperator }}</td>
            <td>{{ userClient.idUserOperator }}</td>
            <td>{{ userClient.idTorneo }}</td>
            <td>{{ userClient.acumulado }}</td>
            <td>{{ userClient.lastReport ? $d(Date.parse(userClient.lastReport), 'short') : '' }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'UserClientView', params: { userClientId: userClient.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'UserClientEdit', params: { userClientId: userClient.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(userClient)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="pcargaApp.userClient.delete.question" data-cy="userClientDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-userClient-heading" v-text="$t('pcargaApp.userClient.delete.question', { id: removeId })">
          Are you sure you want to delete this User Client?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-userClient"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeUserClient()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="userClients && userClients.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-client.component.ts"></script>
