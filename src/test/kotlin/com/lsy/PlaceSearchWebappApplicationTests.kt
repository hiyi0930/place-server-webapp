package com.lsy

import com.lsy.common.constant.TEST_PROFILE
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(TEST_PROFILE)
@SpringBootTest
class PlaceSearchWebappApplicationTests {

	@Test
	fun contextLoads() {
	}

}
