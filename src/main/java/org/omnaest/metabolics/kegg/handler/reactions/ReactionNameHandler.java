package org.omnaest.metabolics.kegg.handler.reactions;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.model.KeggReaction;

public class ReactionNameHandler extends AbstractReactionHandler
{

	public ReactionNameHandler(KeggReaction keggReaction)
	{
		super(keggReaction);
	}

	@Override
	public String getEventKey()
	{
		return "NAME";
	}

	@Override
	public void handle(String line)
	{
		if (StringUtils.isNotBlank(line))
		{
			String cleanedLine = StringUtils.trim(line);
			this.keggReaction.setName(cleanedLine);
		}
	}
}