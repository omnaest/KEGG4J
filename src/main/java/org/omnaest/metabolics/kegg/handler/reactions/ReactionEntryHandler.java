package org.omnaest.metabolics.kegg.handler.reactions;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.model.KeggReaction;

public class ReactionEntryHandler extends AbstractReactionHandler
{

	public ReactionEntryHandler(KeggReaction keggReaction)
	{
		super(keggReaction);
	}

	@Override
	public String getEventKey()
	{
		return "ENTRY";
	}

	@Override
	public void handle(String line)
	{
		if (StringUtils.isNotBlank(line))
		{
			String cleanedLine = StringUtils.remove(line, "Reaction");
			this.keggReaction.setId(StringUtils.trim(cleanedLine));
		}
	}
}