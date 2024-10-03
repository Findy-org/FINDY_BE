package org.findy.findy_be.common;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(properties = {
	"jwt.secret= my_super_secret_key_that_is_long_enough_for_jwt"
})
@Ignore
public class MockTest {
}
