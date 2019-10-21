package org.desz.inttoword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
@AllArgsConstructor
@ToString(includeFieldNames = true)
@Data
public class IntToWordRequest {
	private int number;
	private String lang;

}
