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
package org.omnaest.metabolics.kegg.wrapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.omnaest.metabolics.kegg.intermodel.IMEnzyme;

public class IMEnzymeTemplateWrapper
{
	private IMEnzyme enzyme;

	public IMEnzymeTemplateWrapper(IMEnzyme enzyme)
	{
		super();
		this.enzyme = enzyme;
	}

	public String getEcNumber()
	{
		return this.enzyme.getEcNumber();
	}

	public Set<IMReactionTemplateWrapper> getReactions()
	{
		return this.enzyme	.getReactions()
							.stream()
							.map((reaction) -> new IMReactionTemplateWrapper(reaction))
							.collect(Collectors.toSet());
	}

	public String getEnumEcNumber()
	{
		return this	.getEcNumber()
					.replaceAll("\\.", "_")
					.replaceAll("\\-", "X");
	}

	public String getId()
	{
		return this.enzyme.getId();
	}

}