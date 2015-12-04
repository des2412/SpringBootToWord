/**
 * 
 */
package org.desz.integertoword.service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.logging.Logger;

import org.desz.integertoword.content.ContentContainer.PROV_LANG;
import org.desz.integertoword.exceptions.IntToWordServiceException;
import org.desz.integertoword.mapper.RecursiveConverter;
import org.desz.integertoword.repository.IntFreqRepoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author des Service Stereotype; update MongoDb and invokes the integer
 *         conversion to word.
 * 
 */
@Service
public final class IntToWordServiceImpl implements IFIntToWordService<BigInteger> {

	protected final Logger LOGGER = Logger.getLogger(IntToWordServiceImpl.class.getName());

	private final Optional<IntFreqRepoJpaRepository> optFreqRepo;

	@Autowired
	public IntToWordServiceImpl(Optional<IntFreqRepoJpaRepository> optFreqRepoSrv) {
		this.optFreqRepo = optFreqRepoSrv;
		LOGGER.info("Mongo Repo Service Present:" + optFreqRepo.isPresent());
	}

	@Override
	public String getWordInLang(PROV_LANG provLang, String num) throws IntToWordServiceException {
		if (provLang.equals(PROV_LANG.EMPTY))
			throw new IntToWordServiceException("Empty provisioned language specified");
		if (optFreqRepo.isPresent())
			optFreqRepo.get().saveOrUpdateFrequency(num);
		else
			LOGGER.info("repository unavailable - stats not collected");
		RecursiveConverter converter = new RecursiveConverter(provLang);
		try {
			return converter.convert(new StringBuilder(), Integer.valueOf(num));
		} catch (Exception e) {
			throw new IntToWordServiceException(e.getLocalizedMessage());
		}

	}

}
