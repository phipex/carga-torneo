package co.com.ies.iesgaming.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link co.com.ies.iesgaming.domain.UserClient} entity. This class is used
 * in {@link co.com.ies.iesgaming.web.rest.UserClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class UserClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter idOperator;

    private StringFilter idUserOperator;

    private LongFilter idTorneo;

    private BigDecimalFilter acumulado;

    private ZonedDateTimeFilter lastReport;

    private Boolean distinct;

    public UserClientCriteria() {}

    public UserClientCriteria(UserClientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idOperator = other.idOperator == null ? null : other.idOperator.copy();
        this.idUserOperator = other.idUserOperator == null ? null : other.idUserOperator.copy();
        this.idTorneo = other.idTorneo == null ? null : other.idTorneo.copy();
        this.acumulado = other.acumulado == null ? null : other.acumulado.copy();
        this.lastReport = other.lastReport == null ? null : other.lastReport.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserClientCriteria copy() {
        return new UserClientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getIdOperator() {
        return idOperator;
    }

    public LongFilter idOperator() {
        if (idOperator == null) {
            idOperator = new LongFilter();
        }
        return idOperator;
    }

    public void setIdOperator(LongFilter idOperator) {
        this.idOperator = idOperator;
    }

    public StringFilter getIdUserOperator() {
        return idUserOperator;
    }

    public StringFilter idUserOperator() {
        if (idUserOperator == null) {
            idUserOperator = new StringFilter();
        }
        return idUserOperator;
    }

    public void setIdUserOperator(StringFilter idUserOperator) {
        this.idUserOperator = idUserOperator;
    }

    public LongFilter getIdTorneo() {
        return idTorneo;
    }

    public LongFilter idTorneo() {
        if (idTorneo == null) {
            idTorneo = new LongFilter();
        }
        return idTorneo;
    }

    public void setIdTorneo(LongFilter idTorneo) {
        this.idTorneo = idTorneo;
    }

    public BigDecimalFilter getAcumulado() {
        return acumulado;
    }

    public BigDecimalFilter acumulado() {
        if (acumulado == null) {
            acumulado = new BigDecimalFilter();
        }
        return acumulado;
    }

    public void setAcumulado(BigDecimalFilter acumulado) {
        this.acumulado = acumulado;
    }

    public ZonedDateTimeFilter getLastReport() {
        return lastReport;
    }

    public ZonedDateTimeFilter lastReport() {
        if (lastReport == null) {
            lastReport = new ZonedDateTimeFilter();
        }
        return lastReport;
    }

    public void setLastReport(ZonedDateTimeFilter lastReport) {
        this.lastReport = lastReport;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserClientCriteria that = (UserClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idOperator, that.idOperator) &&
            Objects.equals(idUserOperator, that.idUserOperator) &&
            Objects.equals(idTorneo, that.idTorneo) &&
            Objects.equals(acumulado, that.acumulado) &&
            Objects.equals(lastReport, that.lastReport) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idOperator, idUserOperator, idTorneo, acumulado, lastReport, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserClientCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idOperator != null ? "idOperator=" + idOperator + ", " : "") +
            (idUserOperator != null ? "idUserOperator=" + idUserOperator + ", " : "") +
            (idTorneo != null ? "idTorneo=" + idTorneo + ", " : "") +
            (acumulado != null ? "acumulado=" + acumulado + ", " : "") +
            (lastReport != null ? "lastReport=" + lastReport + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
