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
import java.util.regex.Pattern;

import org.omnaest.metabolics.kegg.intermodel.IMChemical;

public class IMChemicalTemplateWrapper
{
	private IMChemical chemical;

	public IMChemicalTemplateWrapper(IMChemical chemical)
	{
		super();
		this.chemical = chemical;
	}

	public String getName()
	{
		return this.chemical.getName();
	}

	public Set<String> getSynonyms()
	{
		return this.chemical.getSynonyms();
	}

	public String getId()
	{
		return this.chemical.getId();
	}

	public String getEnumCompatibleName()
	{
		return this	.getName()
					.trim()
					.replaceAll(Pattern.quote("+"), "_Positive")
					.replaceAll(Pattern.quote("-") + "$", "_Negative")
					.replaceAll("(^[0-9])", "_$1")
					.replaceAll("[\\(\\)]", "__")
					.replaceAll("[^a-zA-Z0-9]", "_")
					.replaceAll("___", "__");
	}

}
