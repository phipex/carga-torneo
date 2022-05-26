<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pcargaApp.userClient.home.createOrEditLabel"
          data-cy="UserClientCreateUpdateHeading"
          v-text="$t('pcargaApp.userClient.home.createOrEditLabel')"
        >
          Create or edit a UserClient
        </h2>
        <div>
          <div class="form-group" v-if="userClient.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="userClient.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pcargaApp.userClient.idOperator')" for="user-client-idOperator"
              >Id Operator</label
            >
            <input
              type="number"
              class="form-control"
              name="idOperator"
              id="user-client-idOperator"
              data-cy="idOperator"
              :class="{ valid: !$v.userClient.idOperator.$invalid, invalid: $v.userClient.idOperator.$invalid }"
              v-model.number="$v.userClient.idOperator.$model"
              required
            />
            <div v-if="$v.userClient.idOperator.$anyDirty && $v.userClient.idOperator.$invalid">
              <small class="form-text text-danger" v-if="!$v.userClient.idOperator.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.userClient.idOperator.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pcargaApp.userClient.idUserOperator')" for="user-client-idUserOperator"
              >Id User Operator</label
            >
            <input
              type="text"
              class="form-control"
              name="idUserOperator"
              id="user-client-idUserOperator"
              data-cy="idUserOperator"
              :class="{ valid: !$v.userClient.idUserOperator.$invalid, invalid: $v.userClient.idUserOperator.$invalid }"
              v-model="$v.userClient.idUserOperator.$model"
              required
            />
            <div v-if="$v.userClient.idUserOperator.$anyDirty && $v.userClient.idUserOperator.$invalid">
              <small class="form-text text-danger" v-if="!$v.userClient.idUserOperator.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pcargaApp.userClient.idTorneo')" for="user-client-idTorneo">Id Torneo</label>
            <input
              type="number"
              class="form-control"
              name="idTorneo"
              id="user-client-idTorneo"
              data-cy="idTorneo"
              :class="{ valid: !$v.userClient.idTorneo.$invalid, invalid: $v.userClient.idTorneo.$invalid }"
              v-model.number="$v.userClient.idTorneo.$model"
              required
            />
            <div v-if="$v.userClient.idTorneo.$anyDirty && $v.userClient.idTorneo.$invalid">
              <small class="form-text text-danger" v-if="!$v.userClient.idTorneo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.userClient.idTorneo.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pcargaApp.userClient.acumulado')" for="user-client-acumulado">Acumulado</label>
            <input
              type="number"
              class="form-control"
              name="acumulado"
              id="user-client-acumulado"
              data-cy="acumulado"
              :class="{ valid: !$v.userClient.acumulado.$invalid, invalid: $v.userClient.acumulado.$invalid }"
              v-model.number="$v.userClient.acumulado.$model"
              required
            />
            <div v-if="$v.userClient.acumulado.$anyDirty && $v.userClient.acumulado.$invalid">
              <small class="form-text text-danger" v-if="!$v.userClient.acumulado.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.userClient.acumulado.min" v-text="$t('entity.validation.min', { min: 0 })">
                This field should be at least 0.
              </small>
              <small class="form-text text-danger" v-if="!$v.userClient.acumulado.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pcargaApp.userClient.lastReport')" for="user-client-lastReport"
              >Last Report</label
            >
            <div class="d-flex">
              <input
                id="user-client-lastReport"
                data-cy="lastReport"
                type="datetime-local"
                class="form-control"
                name="lastReport"
                :class="{ valid: !$v.userClient.lastReport.$invalid, invalid: $v.userClient.lastReport.$invalid }"
                required
                :value="convertDateTimeFromServer($v.userClient.lastReport.$model)"
                @change="updateZonedDateTimeField('lastReport', $event)"
              />
            </div>
            <div v-if="$v.userClient.lastReport.$anyDirty && $v.userClient.lastReport.$invalid">
              <small class="form-text text-danger" v-if="!$v.userClient.lastReport.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.userClient.lastReport.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.userClient.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./user-client-update.component.ts"></script>
