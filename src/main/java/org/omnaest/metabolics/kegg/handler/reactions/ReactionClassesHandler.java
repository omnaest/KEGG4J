package org.omnaest.metabolics.kegg.handler.reactions;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.model.KeggReaction;

public class ReactionClassesHandler extends AbstractReactionHandler
{
	public ReactionClassesHandler(KeggReaction keggReaction)
	{
		super(keggReaction);
	}

	@Override
	public String getEventKey()
	{
		return "RCLASS";
	}

	@Override
	public void handle(String line)
	{
		if (StringUtils.isNotBlank(line))
		{
			this.keggReaction	.getReactionClasses()
								.add(line);
		}
	}
}