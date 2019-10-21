package org.desz.inttoword;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames = true)
@Data
public class IntToWordRequest {
	private int number;
	private String lang;

}
