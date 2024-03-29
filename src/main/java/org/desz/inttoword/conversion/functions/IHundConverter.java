package org.desz.inttoword.conversion.functions;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.desz.inttoword.language.IntWordMapping;

/*
 * responsible for converting hundredth to word.
 * parameterised with Exception subtype.
 */
@FunctionalInterface
public interface IHundConverter {

	/**
	 * 
	 * @param number      the number.
	 * @param langMapping
	 * @return the word for the hundredth
	 */
	Optional<String> hundredthToWord(String number, IntWordMapping langMapping);

	static boolean inRange(int i) {
		return IntStream.range(1, 100).boxed().collect(Collectors.toSet()).contains(i);
	}

}
