package org.omnaest.metabolomics.kegg.handler.reactions;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolomics.kegg.model.KeggReaction;

public class ReactionDefinitionHandler extends AbstractReactionHandler
{
	public ReactionDefinitionHandler(KeggReaction keggReaction)
	{
		super(keggReaction);
	}

	@Override
	public String getEventKey()
	{
		return "DEFINITION";
	}

	@Override
	public void handle(String line)
	{
		if (StringUtils.isNotBlank(line))
		{
			String cleanedLine = StringUtils.trim(line);
			this.keggReaction.setDefinition(cleanedLine);
		}
	}
}