package org.desz.inttoword;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
public class TestSpringBootToWordFunction {

	@Autowired
	private WebTestClient client;

	@Test
	public void test() throws Exception {

		final IntToWordRequest req = new IntToWordRequest();
		req.setLang("UK");
		req.setNumber(22);

		client.post().uri("/myFunction").syncBody(req).header("content-type", "application/json").exchange()
				.expectStatus().isOk().expectBody(String.class);

	}

}
