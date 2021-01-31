package org.omnaest.metabolomics.kegg.handler.reactions;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolomics.kegg.model.KeggReaction;

public class ReactionEnzymeHandler extends AbstractReactionHandler
{
	public ReactionEnzymeHandler(KeggReaction keggReaction)
	{
		super(keggReaction);
	}

	@Override
	public String getEventKey()
	{
		return "ENZYME";
	}

	@Override
	public void handle(String line)
	{
		if (StringUtils.isNotBlank(line))
		{
			String[] enzymes = StringUtils.split(line, " ");
			this.keggReaction	.getEnzymes()
								.addAll(Arrays.asList(enzymes));
		}
	}
}