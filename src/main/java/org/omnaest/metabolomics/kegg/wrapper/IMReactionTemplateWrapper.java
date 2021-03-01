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
/*

	Copyright 2017 Danny Kunz

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.


*/
package org.omnaest.metabolomics.kegg.wrapper;

import java.util.List;
import java.util.stream.Collectors;

import org.omnaest.metabolomics.kegg.intermodel.IMChemical;
import org.omnaest.metabolomics.kegg.intermodel.IMReaction;

public class IMReactionTemplateWrapper
{
	private IMReaction reaction;

	public IMReactionTemplateWrapper(IMReaction reaction)
	{
		super();
		this.reaction = reaction;
	}

	public String getId()
	{
		return this.reaction.getId();
	}

	public List<IMChemicalTemplateWrapper> getEducts()
	{
		return this.wrapChemicals(this.reaction.getEducts());
	}

	private List<IMChemicalTemplateWrapper> wrapChemicals(List<IMChemical> educts)
	{
		return educts	.stream()
						.map((chemical) -> new IMChemicalTemplateWrapper(chemical))
						.collect(Collectors.toList());
	}

	public List<IMChemicalTemplateWrapper> getProducts()
	{
		return this.wrapChemicals(this.reaction.getProducts());
	}

	public List<String> getEnumProducts()
	{
		return this.makeEnumCompatible(this.getProducts());
	}

	public List<String> getEnumEducts()
	{
		return this.makeEnumCompatible(this.getEducts());
	}

	private List<String> makeEnumCompatible(List<IMChemicalTemplateWrapper> list)
	{
		return list	.stream()
					.map((chemicalWrapper) -> chemicalWrapper.getEnumCompatibleName())
					.collect(Collectors.toList());
	}

}
