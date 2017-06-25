/*

	Copyright 2017 Danny Kunz

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.


*/
package org.omnaest.metabolics.kegg;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.utils.RestHelper;

/**
 * @see KeggUtils
 * @author Omnaest
 */
public class KeggRestApiUtils
{
	private static String baseUrl = "http://rest.kegg.jp/";

	public static String getReaction(String reactionId)
	{
		return RestHelper.requestGet(baseUrl + "get/rn:" + reactionId);
	}

	public static List<String> getReactionsList()
	{
		return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/reaction"), "\r\n"));
	}

	public static String getEnzyme(String enzymeId)
	{
		return RestHelper.requestGet(baseUrl + "get/ec:" + enzymeId);
	}

	public static List<String> getEnzymesList()
	{
		return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/enzyme"), "\r\n"));
	}
}