package org.omnaest.metabolics.kegg.handler.reactions;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.model.KeggReaction;

public class ReactionEquationHandler extends AbstractReactionHandler
{
	public ReactionEquationHandler(KeggReaction keggReaction)
	{
		super(keggReaction);
	}

	@Override
	public String getEventKey()
	{
		return "EQUATION";
	}

	@Override
	public void handle(String line)
	{
		if (StringUtils.isNotBlank(line))
		{
			String cleanedLine = StringUtils.trim(line);
			this.keggReaction.setEquation(cleanedLine);
		}
	}
}