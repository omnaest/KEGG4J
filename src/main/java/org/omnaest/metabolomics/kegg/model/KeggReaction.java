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
package org.omnaest.metabolomics.kegg.model;

import java.util.ArrayList;
import java.util.List;

public class KeggReaction
{
	private String			id;
	private String			name;
	private String			definition;
	private String			equation;
	private List<String>	reactionClasses	= new ArrayList<>();
	private List<String>	enzymes			= new ArrayList<>();
	private List<String>	orthology		= new ArrayList<>();

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDefinition()
	{
		return this.definition;
	}

	public void setDefinition(String definition)
	{
		this.definition = definition;
	}

	public String getEquation()
	{
		return this.equation;
	}

	public void setEquation(String equation)
	{
		this.equation = equation;
	}

	public List<String> getReactionClasses()
	{
		return this.reactionClasses;
	}

	public void setReactionClasses(List<String> reactionClasses)
	{
		this.reactionClasses = reactionClasses;
	}

	public List<String> getEnzymes()
	{
		return this.enzymes;
	}

	public void setEnzymes(List<String> enzymes)
	{
		this.enzymes = enzymes;
	}

	public List<String> getOrthology()
	{
		return this.orthology;
	}

	public void setOrthology(List<String> orthology)
	{
		this.orthology = orthology;
	}

}
