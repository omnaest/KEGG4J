/*******************************************************************************
 * Copyright 2021 Danny Kunz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.omnaest.metabolomics.kegg.handler.reactions;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolomics.kegg.model.KeggReaction;

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
