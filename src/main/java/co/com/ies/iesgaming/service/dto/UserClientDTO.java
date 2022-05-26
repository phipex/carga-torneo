package co.com.ies.iesgaming.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.iesgaming.domain.UserClient} entity.
 */
public class UserClientDTO implements Serializable {

    private Long id;

    @NotNull
    private Long idOperator;

    @NotNull
    private String idUserOperator;

    @NotNull
    private Long idTorneo;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal acumulado;

    @NotNull
    private ZonedDateTime lastReport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOperator() {
        return idOperator;
    }

    public void setIdOperator(Long idOperator) {
        this.idOperator = idOperator;
    }

    public String getIdUserOperator() {
        return idUserOperator;
    }

    public void setIdUserOperator(String idUserOperator) {
        this.idUserOperator = idUserOperator;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public BigDecimal getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(BigDecimal acumulado) {
        this.acumulado = acumulado;
    }

    public ZonedDateTime getLastReport() {
        return lastReport;
    }

    public void setLastReport(ZonedDateTime lastReport) {
        this.lastReport = lastReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserClientDTO)) {
            return false;
        }

        UserClientDTO userClientDTO = (UserClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userClientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserClientDTO{" +
            "id=" + getId() +
            ", idOperator=" + getIdOperator() +
            ", idUserOperator='" + getIdUserOperator() + "'" +
            ", idTorneo=" + getIdTorneo() +
            ", acumulado=" + getAcumulado() +
            ", lastReport='" + getLastReport() + "'" +
            "}";
    }
}
