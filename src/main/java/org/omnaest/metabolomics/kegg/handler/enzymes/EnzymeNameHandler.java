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
package org.omnaest.metabolomics.kegg.handler.enzymes;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolomics.kegg.model.KeggEnzyme;

public class EnzymeNameHandler extends AbstractEnzymeHandler
{

	public EnzymeNameHandler(KeggEnzyme keggEnzyme)
	{
		super(keggEnzyme);
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

			if (!this.keggEnzyme.hasName())
			{
				this.keggEnzyme.setName(cleanedLine);
			} else
			{
				this.keggEnzyme.addSynonym(cleanedLine);
			}
		}
	}
}