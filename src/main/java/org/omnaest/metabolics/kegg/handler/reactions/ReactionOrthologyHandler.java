package org.omnaest.metabolics.kegg.handler.reactions;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.model.KeggReaction;

public class ReactionOrthologyHandler extends AbstractReactionHandler
{
	public ReactionOrthologyHandler(KeggReaction keggReaction)
	{
		super(keggReaction);
	}

	@Override
	public String getEventKey()
	{
		return "ORTHOLOGY";
	}

	@Override
	public void handle(String line)
	{
		if (StringUtils.isNotBlank(line))
		{
			this.keggReaction	.getOrthology()
								.add(line);
		}
	}
}