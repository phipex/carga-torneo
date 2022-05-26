package co.com.ies.iesgaming.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserClientMapperTest {

    private UserClientMapper userClientMapper;

    @BeforeEach
    public void setUp() {
        userClientMapper = new UserClientMapperImpl();
    }
}
