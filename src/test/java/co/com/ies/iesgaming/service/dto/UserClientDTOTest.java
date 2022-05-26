package co.com.ies.iesgaming.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.iesgaming.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserClientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserClientDTO.class);
        UserClientDTO userClientDTO1 = new UserClientDTO();
        userClientDTO1.setId(1L);
        UserClientDTO userClientDTO2 = new UserClientDTO();
        assertThat(userClientDTO1).isNotEqualTo(userClientDTO2);
        userClientDTO2.setId(userClientDTO1.getId());
        assertThat(userClientDTO1).isEqualTo(userClientDTO2);
        userClientDTO2.setId(2L);
        assertThat(userClientDTO1).isNotEqualTo(userClientDTO2);
        userClientDTO1.setId(null);
        assertThat(userClientDTO1).isNotEqualTo(userClientDTO2);
    }
}
