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
package org.omnaest.metabolics.kegg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.handler.TextHandler;
import org.omnaest.metabolics.kegg.handler.compounds.CompoundNameHandler;
import org.omnaest.metabolics.kegg.handler.enzymes.EnzymeGenesHandler;
import org.omnaest.metabolics.kegg.handler.enzymes.EnzymeNameHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionClassesHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionDefinitionHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionEntryHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionEnzymeHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionEquationHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionNameHandler;
import org.omnaest.metabolics.kegg.handler.reactions.ReactionOrthologyHandler;
import org.omnaest.metabolics.kegg.model.KeggCompound;
import org.omnaest.metabolics.kegg.model.KeggEnzyme;
import org.omnaest.metabolics.kegg.model.KeggReaction;
import org.omnaest.metabolics.kegg.utils.filecache.TextListWithFileCache;
import org.omnaest.metabolics.kegg.utils.filecache.TextWithFileCache;

public class KeggUtils
{
	public static String CACHE_FOLDER = "kegg_data";

	public static KeggReaction getReaction(String reactionId)
	{
		String reactionText = new TextWithFileCache(CACHE_FOLDER + "/reactions/reaction" + reactionId
				+ ".txt")	.setProvider(() -> KeggRestApiUtils.getReaction(reactionId))
							.get();
		KeggReaction keggReaction = new KeggReaction();
		TextHandler textHandler = new TextHandler(	new ReactionEntryHandler(keggReaction), new ReactionNameHandler(keggReaction),
													new ReactionDefinitionHandler(keggReaction), new ReactionEquationHandler(keggReaction),
													new ReactionClassesHandler(keggReaction), new ReactionEnzymeHandler(keggReaction),
													new ReactionOrthologyHandler(keggReaction));
		textHandler.handle(reactionText);

		return keggReaction;
	}

	public static Set<String> getReactionIds()
	{
		Set<String> reactionIds = new LinkedHashSet<>();
		List<String> reactionLinesList = new TextListWithFileCache(CACHE_FOLDER
				+ "/reactions/reactions.txt")	.setProvider(() -> KeggRestApiUtils.getReactionsList())
												.get();
		for (String reactionLine : reactionLinesList)
		{
			Matcher matcher = Pattern	.compile("rn\\:([A-Za-z0-9]+)")
										.matcher(reactionLine);
			if (matcher.find())
			{
				String reactionId = matcher.group(1);
				if (StringUtils.isNotBlank(reactionId))
				{
					reactionIds.add(reactionId);
				}
			}
		}
		return reactionIds;
	}

	public static KeggEnzyme getEnzyme(String enzymeId)
	{
		String reactionText = new TextWithFileCache(CACHE_FOLDER + "/enzymes/enzyme" + enzymeId
				+ ".txt")	.setProvider(() -> KeggRestApiUtils.getEnzyme(enzymeId))
							.get();
		KeggEnzyme keggEnzyme = new KeggEnzyme().setId(enzymeId);
		TextHandler textHandler = new TextHandler(new EnzymeNameHandler(keggEnzyme), new EnzymeGenesHandler(keggEnzyme));
		textHandler.handle(reactionText);

		return keggEnzyme;
	}

	public static Set<String> getEnzymeIds()
	{
		Set<String> enzymeIds = new LinkedHashSet<>();
		List<String> reactionLinesList = new TextListWithFileCache(CACHE_FOLDER + "/enzymes/enzymes.txt")
																											.setProvider(() -> KeggRestApiUtils.getEnzymesList())
																											.get();
		for (String enzymeLine : reactionLinesList)
		{
			Matcher matcher = Pattern	.compile("ec\\:([A-Za-z0-9\\.]+)")
										.matcher(enzymeLine);
			if (matcher.find())
			{
				String enzymeId = matcher.group(1);
				if (StringUtils.isNotBlank(enzymeId))
				{
					enzymeIds.add(enzymeId);
				}
			}
		}
		return enzymeIds;
	}

	public static class OrganismReference
	{
		private String		id;
		private String		name;
		private Set<String>	groups;

		public OrganismReference(String id, String name, Set<String> groups)
		{
			super();
			this.id = id;
			this.name = name;
			this.groups = groups;
		}

		public String getId()
		{
			return this.id;
		}

		public String getName()
		{
			return this.name;
		}

		public Set<String> getGroups()
		{
			return this.groups;
		}

		@Override
		public String toString()
		{
			return "OrganismReference [id=" + this.id + ", name=" + this.name + "]";
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (this.getClass() != obj.getClass())
				return false;
			OrganismReference other = (OrganismReference) obj;
			if (this.id == null)
			{
				if (other.id != null)
					return false;
			}
			else if (!this.id.equals(other.id))
				return false;
			return true;
		}

	}

	public static Set<OrganismReference> getOrganisms()
	{
		Set<OrganismReference> organisms = new LinkedHashSet<>();
		List<String> lines = new TextListWithFileCache(CACHE_FOLDER + "/organisms.txt")	.setProvider(() -> KeggRestApiUtils.getOrganismList())
																						.get();

		for (String line : lines)
		{
			//T01001	hsa	Homo sapiens (human)	Eukaryotes;Animals;Vertebrates;Mammals
			Matcher matcher = Pattern	.compile(".*\t(.+)\t(.*)\t(.*)")
										.matcher(line);
			if (matcher.find())
			{
				String id = matcher.group(1);
				String name = matcher.group(2);
				String joinedGroups = matcher.group(3);
				if (StringUtils.isNotBlank(id))
				{
					Set<String> groups = Arrays	.asList(StringUtils.split(joinedGroups, ";"))
												.stream()
												.collect(Collectors.toSet());
					organisms.add(new OrganismReference(id, name, groups));
				}
			}
		}
		return organisms;
	}

	public static Stream<KeggEnzyme> getEnzymes()
	{
		return getEnzymeIds()	.parallelStream()
								.map(enzymeId -> getEnzyme(enzymeId));
	}

	public static Stream<KeggReaction> getReactions()
	{
		return getReactionIds()	.parallelStream()
								.map(reactionId -> getReaction(reactionId));
	}

	public static class ChemicalCompoundReference
	{
		private String			id;
		private List<String>	names;

		public ChemicalCompoundReference(String id, List<String> names)
		{
			super();
			this.id = id;
			this.names = names;
		}

		public String getId()
		{
			return this.id;
		}

		public List<String> getNames()
		{
			return this.names;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (this.getClass() != obj.getClass())
				return false;
			ChemicalCompoundReference other = (ChemicalCompoundReference) obj;
			if (this.id == null)
			{
				if (other.id != null)
					return false;
			}
			else if (!this.id.equals(other.id))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "ChemicalCompoundReference [id=" + this.id + ", names=" + this.names + "]";
		}

	}

	public static Set<ChemicalCompoundReference> getChemicalCompoundIds()
	{
		Set<ChemicalCompoundReference> compounds = new HashSet<>();
		List<String> lines = new TextListWithFileCache(CACHE_FOLDER + "/compounds/compounds.txt")
																									.setProvider(() -> KeggRestApiUtils.getChemicalCompoundList())
																									.get();

		for (String line : lines)
		{
			//cpd:C00002	ATP; Adenosine 5'-triphosphate
			Matcher matcher = Pattern	.compile("cpd\\:(.+)\t(.*)")
										.matcher(line);
			if (matcher.find())
			{
				String id = matcher.group(1);
				String joinedNames = matcher.group(2);

				if (StringUtils.isNotBlank(id))
				{
					Set<String> names = Arrays	.asList(StringUtils.split(joinedNames, ";"))
												.stream()
												.collect(Collectors.toSet());
					compounds.add(new ChemicalCompoundReference(id, names	.stream()
																			.collect(Collectors.toList())));
				}
			}
		}
		return compounds;
	}

	public static KeggCompound getChemicalCompound(String compoundId)
	{
		String text = new TextWithFileCache(CACHE_FOLDER + "/compounds/compound" + compoundId
				+ ".txt")	.setProvider(() -> KeggRestApiUtils.getChemicalCompound(compoundId))
							.get();
		KeggCompound keggCompound = new KeggCompound().setId(compoundId);
		TextHandler textHandler = new TextHandler(new CompoundNameHandler(keggCompound));
		textHandler.handle(text);

		return keggCompound;
	}

	public static Stream<KeggCompound> getChemicalCompounds()
	{
		return getChemicalCompoundIds()	.parallelStream()
										.map(compound -> compound.getId())
										.map(compoundId -> getChemicalCompound(compoundId));
	}

}
