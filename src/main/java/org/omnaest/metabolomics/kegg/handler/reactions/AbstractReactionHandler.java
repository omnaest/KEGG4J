package org.omnaest.metabolomics.kegg.handler.reactions;

import org.omnaest.metabolomics.kegg.handler.TextHandler;
import org.omnaest.metabolomics.kegg.handler.TextHandler.Handler;
import org.omnaest.metabolomics.kegg.model.KeggReaction;

public abstract class AbstractReactionHandler implements Handler
{
	protected KeggReaction keggReaction;

	public AbstractReactionHandler(KeggReaction keggReaction)
	{
		super();
		this.keggReaction = keggReaction;
	}

}