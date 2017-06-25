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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.handler.TextHandler;
import org.omnaest.metabolics.kegg.handler.enzymes.EnzymeGenesHandler;
import org.omnaest.metabolics.kegg.handler.enzymes.EnzymeNameHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionClassesHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionDefinitionHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionEntryHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionEnzymeHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionEquationHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionNameHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionOrthologyHandler;
import org.omnaest.metabolics.kegg.model.KeggEnzyme;
import org.omnaest.metabolics.kegg.model.KeggReaction;
import org.omnaest.metabolics.kegg.utils.filecache.TextListWithFileCache;
import org.omnaest.metabolics.kegg.utils.filecache.TextWithFileCache;

public class KeggUtils
{
	public static String CACHE_FOLDER = "kegg_data";

	public static KeggReaction getReaction(String reactionId)
	{
		String reactionText = new TextWithFileCache(CACHE_FOLDER + "/reaction" + reactionId
				+ ".txt")	.setProvider(() -> KeggRestApiUtils.getReaction(reactionId))
							.get();
		KeggReaction keggReaction = new KeggReaction();
		TextHandler textHandler = new TextHandler(	new ReactionEntryHandler(keggReaction), new ReactionNameHandler(keggReaction),
													new ReactionDefinitionHandler(keggReaction), new ReactionEquationHandler(keggReaction),
													new ReactionClassesHandler(keggReaction), new ReactionEnzymeHandler(keggReaction),
													new ReactionOrthologyHandler(keggReaction));
		textHandler.handle(reactionText);

		return keggReaction;
	}

	public static Set<String> getReactionIds()
	{
		Set<String> reactionIds = new LinkedHashSet<>();
		List<String> reactionLinesList = new TextListWithFileCache(CACHE_FOLDER + "/reactions.txt")	.setProvider(() -> KeggRestApiUtils.getReactionsList())
																									.get();
		for (String reactionLine : reactionLinesList)
		{
			Matcher matcher = Pattern	.compile("rn\\:([A-Za-z0-9]+)")
										.matcher(reactionLine);
			if (matcher.find())
			{
				String reactionId = matcher.group(1);
				if (StringUtils.isNotBlank(reactionId))
				{
					reactionIds.add(reactionId);
				}
			}
		}
		return reactionIds;
	}

	public static KeggEnzyme getEnzyme(String enzymeId)
	{
		String reactionText = new TextWithFileCache(CACHE_FOLDER + "/reaction" + enzymeId + ".txt")	.setProvider(() -> KeggRestApiUtils.getEnzyme(enzymeId))
																									.get();
		KeggEnzyme keggEnzyme = new KeggEnzyme().setId(enzymeId);
		TextHandler textHandler = new TextHandler(new EnzymeNameHandler(keggEnzyme), new EnzymeGenesHandler(keggEnzyme));
		textHandler.handle(reactionText);

		return keggEnzyme;
	}

}
