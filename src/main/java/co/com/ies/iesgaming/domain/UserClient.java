package co.com.ies.iesgaming.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserClient.
 */
@Entity
@Table(name = "user_client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_operator", nullable = false)
    private Long idOperator;

    @NotNull
    @Column(name = "id_user_operator", nullable = false)
    private String idUserOperator;

    @NotNull
    @Column(name = "id_torneo", nullable = false)
    private Long idTorneo;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "acumulado", precision = 21, scale = 2, nullable = false)
    private BigDecimal acumulado;

    @NotNull
    @Column(name = "last_report", nullable = false)
    private ZonedDateTime lastReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserClient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOperator() {
        return this.idOperator;
    }

    public UserClient idOperator(Long idOperator) {
        this.setIdOperator(idOperator);
        return this;
    }

    public void setIdOperator(Long idOperator) {
        this.idOperator = idOperator;
    }

    public String getIdUserOperator() {
        return this.idUserOperator;
    }

    public UserClient idUserOperator(String idUserOperator) {
        this.setIdUserOperator(idUserOperator);
        return this;
    }

    public void setIdUserOperator(String idUserOperator) {
        this.idUserOperator = idUserOperator;
    }

    public Long getIdTorneo() {
        return this.idTorneo;
    }

    public UserClient idTorneo(Long idTorneo) {
        this.setIdTorneo(idTorneo);
        return this;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public BigDecimal getAcumulado() {
        return this.acumulado;
    }

    public UserClient acumulado(BigDecimal acumulado) {
        this.setAcumulado(acumulado);
        return this;
    }

    public void setAcumulado(BigDecimal acumulado) {
        this.acumulado = acumulado;
    }

    public ZonedDateTime getLastReport() {
        return this.lastReport;
    }

    public UserClient lastReport(ZonedDateTime lastReport) {
        this.setLastReport(lastReport);
        return this;
    }

    public void setLastReport(ZonedDateTime lastReport) {
        this.lastReport = lastReport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserClient)) {
            return false;
        }
        return id != null && id.equals(((UserClient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserClient{" +
            "id=" + getId() +
            ", idOperator=" + getIdOperator() +
            ", idUserOperator='" + getIdUserOperator() + "'" +
            ", idTorneo=" + getIdTorneo() +
            ", acumulado=" + getAcumulado() +
            ", lastReport='" + getLastReport() + "'" +
            "}";
    }
}
