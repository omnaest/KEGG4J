package org.omnaest.metabolics.kegg.handler.reactions;

import org.omnaest.metabolics.kegg.handler.TextHandler;
import org.omnaest.metabolics.kegg.handler.TextHandler.Handler;
import org.omnaest.metabolics.kegg.model.KeggReaction;

public abstract class AbstractReactionHandler implements Handler
{
	protected KeggReaction keggReaction;

	public AbstractReactionHandler(KeggReaction keggReaction)
	{
		super();
		this.keggReaction = keggReaction;
	}

}