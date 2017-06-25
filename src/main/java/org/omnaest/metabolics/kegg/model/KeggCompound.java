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
package org.omnaest.metabolics.kegg.model;

import java.util.ArrayList;
import java.util.List;

public class KeggCompound
{
	private String			id;
	private String			name;
	private List<String>	synonyms	= new ArrayList<>();
	private String			formula;
	private String			exactMass;
	private String			molWeight;
	private List<String>	reactions	= new ArrayList<>();
	private List<String>	pathways	= new ArrayList<>();
	private List<String>	modules		= new ArrayList<>();
	private List<String>	enzymes		= new ArrayList<>();
	private String			brite;
	private List<String>	dbLinks		= new ArrayList<>();
	private List<String>	atom		= new ArrayList<>();
	private List<String>	bond		= new ArrayList<>();

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

	public List<String> getSynonyms()
	{
		return this.synonyms;
	}

	public void setSynonyms(List<String> synonyms)
	{
		this.synonyms = synonyms;
	}

	public String getFormula()
	{
		return this.formula;
	}

	public void setFormula(String formula)
	{
		this.formula = formula;
	}

	public String getExactMass()
	{
		return this.exactMass;
	}

	public void setExactMass(String exactMass)
	{
		this.exactMass = exactMass;
	}

	public String getMolWeight()
	{
		return this.molWeight;
	}

	public void setMolWeight(String molWeight)
	{
		this.molWeight = molWeight;
	}

	public List<String> getReactions()
	{
		return this.reactions;
	}

	public void setReactions(List<String> reactions)
	{
		this.reactions = reactions;
	}

	public List<String> getPathways()
	{
		return this.pathways;
	}

	public void setPathways(List<String> pathways)
	{
		this.pathways = pathways;
	}

	public List<String> getModules()
	{
		return this.modules;
	}

	public void setModules(List<String> modules)
	{
		this.modules = modules;
	}

	public List<String> getEnzymes()
	{
		return this.enzymes;
	}

	public void setEnzymes(List<String> enzymes)
	{
		this.enzymes = enzymes;
	}

	public String getBrite()
	{
		return this.brite;
	}

	public void setBrite(String brite)
	{
		this.brite = brite;
	}

	public List<String> getDbLinks()
	{
		return this.dbLinks;
	}

	public void setDbLinks(List<String> dbLinks)
	{
		this.dbLinks = dbLinks;
	}

	public List<String> getAtom()
	{
		return this.atom;
	}

	public void setAtom(List<String> atom)
	{
		this.atom = atom;
	}

	public List<String> getBond()
	{
		return this.bond;
	}

	public void setBond(List<String> bond)
	{
		this.bond = bond;
	}

}
