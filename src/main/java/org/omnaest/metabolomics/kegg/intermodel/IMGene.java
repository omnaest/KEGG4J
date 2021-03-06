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
package org.omnaest.metabolomics.kegg.intermodel;

public class IMGene
{
	private String	name;
	private String	organism;
	private String	organismName;

	public IMGene(String name, String organism, String organismName)
	{
		super();
		this.name = name;
		this.organism = organism;
		this.organismName = organismName;
	}

	public String getName()
	{
		return this.name;
	}

	public String getOrganism()
	{
		return this.organism;
	}

	public String getOrganismName()
	{
		return this.organismName;
	}

	@Override
	public String toString()
	{
		return "IMGene [name=" + this.name + ", organism=" + this.organism + ", organismName=" + this.organismName + "]";
	}

}
