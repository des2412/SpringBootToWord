package org.desz.integertoword.spring.config;

import java.math.BigInteger;
import java.util.Optional;
import java.util.logging.Logger;

import org.desz.integertoword.repository.IntFreqRepoJpaRepository;
import org.desz.integertoword.service.IFIntToWordService;
import org.desz.integertoword.service.IntToWordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = { IntFreqRepoConfig.class })
public class IntToWordServiceConfig {
	protected final Logger LOGGER = Logger.getLogger(IntToWordServiceConfig.class.getName());

	@Autowired
	private IntFreqRepoConfig intFreqRepoConfig;

	@Bean
	public IFIntToWordService<BigInteger> intToWordService() {

		Optional<IntFreqRepoJpaRepository> opt = Optional.empty();
		try {
			if (intFreqRepoConfig.intFreqRepo() == null)
				return new IntToWordServiceImpl(opt);

			opt = Optional.of(intFreqRepoConfig.intFreqRepo());
		} catch (Exception e) {
			LOGGER.severe(e.getMessage());

		}
		return new IntToWordServiceImpl(opt);
	}

}
