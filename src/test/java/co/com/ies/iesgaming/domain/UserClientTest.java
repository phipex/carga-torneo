package co.com.ies.iesgaming.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.iesgaming.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserClient.class);
        UserClient userClient1 = new UserClient();
        userClient1.setId(1L);
        UserClient userClient2 = new UserClient();
        userClient2.setId(userClient1.getId());
        assertThat(userClient1).isEqualTo(userClient2);
        userClient2.setId(2L);
        assertThat(userClient1).isNotEqualTo(userClient2);
        userClient1.setId(null);
        assertThat(userClient1).isNotEqualTo(userClient2);
    }
}
