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
package org.omnaest.pandora.kegg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.KeggUtils;
import org.omnaest.metabolics.kegg.handler.TextHandler.Handler;
import org.omnaest.metabolics.kegg.intermodel.IMChemical;
import org.omnaest.metabolics.kegg.intermodel.IMEnzyme;
import org.omnaest.metabolics.kegg.intermodel.IMGene;
import org.omnaest.metabolics.kegg.intermodel.IMReaction;
import org.omnaest.metabolics.kegg.model.KeggEnzyme;
import org.omnaest.metabolics.kegg.model.KeggReaction;
import org.omnaest.metabolics.kegg.utils.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeggReactionParser
{
	private static final Logger LOG = LoggerFactory.getLogger(KeggReactionParser.class);

	private final class ChemicalMultiplierPrefixMapper implements Function<String, Stream<String>>
	{
		@Override
		public Stream<String> apply(String chemical)
		{
			List<String> retlist = new ArrayList<>();
			boolean hasMultiplierPrefix = Pattern	.compile("^[0-9]+ ")
													.matcher(chemical)
													.find();
			if (hasMultiplierPrefix)
			{
				String[] multiplierAndChemical = chemical.split(" ", 2);
				if (multiplierAndChemical.length == 2)
				{
					int multiplier = Integer.valueOf(multiplierAndChemical[0]);
					String iChemical = multiplierAndChemical[1];
					for (int ii = 0; ii < multiplier; ii++)
					{
						retlist.add(iChemical);
					}
				}
			}
			else
			{
				retlist.add(chemical);
			}
			return retlist.stream();
		}
	}

	private static abstract class AbstractHandler implements Handler
	{
		protected KeggReaction keggReaction;

		public AbstractHandler(KeggReaction keggReaction)
		{
			super();
			this.keggReaction = keggReaction;
		}

	}

	private static class EntryHandler extends AbstractHandler
	{

		public EntryHandler(KeggReaction keggReaction)
		{
			super(keggReaction);
		}

		@Override
		public String getEventKey()
		{
			return "ENTRY";
		}

		@Override
		public void handle(String line)
		{
			if (StringUtils.isNotBlank(line))
			{
				String cleanedLine = StringUtils.remove(line, "Reaction");
				this.keggReaction.setId(StringUtils.trim(cleanedLine));
			}
		}
	}

	private class NameHandler extends AbstractHandler
	{

		public NameHandler(KeggReaction keggReaction)
		{
			super(keggReaction);
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
				this.keggReaction.setName(cleanedLine);
			}
		}
	}

	private class DefinitionHandler extends AbstractHandler
	{
		public DefinitionHandler(KeggReaction keggReaction)
		{
			super(keggReaction);
		}

		@Override
		public String getEventKey()
		{
			return "DEFINITION";
		}

		@Override
		public void handle(String line)
		{
			if (StringUtils.isNotBlank(line))
			{
				String cleanedLine = StringUtils.trim(line);
				this.keggReaction.setDefinition(cleanedLine);
			}
		}
	}

	private class EquationHandler extends AbstractHandler
	{
		public EquationHandler(KeggReaction keggReaction)
		{
			super(keggReaction);
		}

		@Override
		public String getEventKey()
		{
			return "EQUATION";
		}

		@Override
		public void handle(String line)
		{
			if (StringUtils.isNotBlank(line))
			{
				String cleanedLine = StringUtils.trim(line);
				this.keggReaction.setEquation(cleanedLine);
			}
		}
	}

	private class ReactionClassesHandler extends AbstractHandler
	{
		public ReactionClassesHandler(KeggReaction keggReaction)
		{
			super(keggReaction);
		}

		@Override
		public String getEventKey()
		{
			return "RCLASS";
		}

		@Override
		public void handle(String line)
		{
			if (StringUtils.isNotBlank(line))
			{
				this.keggReaction	.getReactionClasses()
									.add(line);
			}
		}
	}

	private class EnzymeHandler extends AbstractHandler
	{
		public EnzymeHandler(KeggReaction keggReaction)
		{
			super(keggReaction);
		}

		@Override
		public String getEventKey()
		{
			return "ENZYME";
		}

		@Override
		public void handle(String line)
		{
			if (StringUtils.isNotBlank(line))
			{
				String[] enzymes = StringUtils.split(line, " ");
				this.keggReaction	.getEnzymes()
									.addAll(Arrays.asList(enzymes));
			}
		}
	}

	private class OrthologyHandler extends AbstractHandler
	{
		public OrthologyHandler(KeggReaction keggReaction)
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

	public List<IMEnzyme> parse()
	{
		List<KeggReaction> reactions = new ArrayList<>();
		{
			Set<String> reactionIds = this.collectReactionIds();
			for (String reactionId : reactionIds.stream()
												.collect(Collectors.toList()))
			{
				KeggReaction keggReaction = this.collectReaction(reactionId);

				reactions.add(keggReaction);
			}
		}

		//
		SortedMap<String, Set<KeggReaction>> enzymeToReactionsMap = new TreeMap<>();
		for (KeggReaction reaction : reactions)
		{
			Set<String> enzymes = reaction	.getEnzymes()
											.stream()
											.collect(Collectors.toSet());
			for (String enzyme : enzymes)
			{
				Set<KeggReaction> reactionsOfEnzyme = enzymeToReactionsMap.get(enzyme);
				if (reactionsOfEnzyme == null)
				{
					reactionsOfEnzyme = new LinkedHashSet<>();
					enzymeToReactionsMap.put(enzyme, reactionsOfEnzyme);
				}

				reactionsOfEnzyme.add(reaction);
			}
		}

		//
		Map<String, KeggEnzyme> enzymeIdToEnzymeMap = this	.getEnzymes()
															.stream()
															.collect(Collectors.toMap(keggEnzyme -> keggEnzyme.getId(), keggEnzyme -> keggEnzyme));

		//
		List<IMEnzyme> enzymes = new ArrayList<>();
		for (String enzymeECNumber : enzymeToReactionsMap.keySet())
		{
			KeggEnzyme keggEnzyme = enzymeIdToEnzymeMap.get(enzymeECNumber);

			IMEnzyme imEnzyme = new IMEnzyme();
			{
				//
				imEnzyme.setId(enzymeECNumber);
				imEnzyme.setEcNumber(enzymeECNumber);

				if (keggEnzyme != null)
				{
					imEnzyme.setName(keggEnzyme.getName());
					imEnzyme.setSynonyms(keggEnzyme	.getSynonoms()
													.stream()
													.collect(Collectors.toSet()));

					//
					imEnzyme.setGenes(keggEnzyme.getGenes()
												.stream()
												.map(keggGene -> new IMGene(keggGene.getGene(), keggGene.getOrganism()))
												.collect(Collectors.toList()));
				}

				//
				Set<KeggReaction> keggReactionsOfEnzyme = enzymeToReactionsMap.get(enzymeECNumber);
				for (KeggReaction keggReactionOfEnzyme : keggReactionsOfEnzyme)
				{
					IMReaction imReaction = new IMReaction();
					{
						//
						imReaction.setId(keggReactionOfEnzyme.getId());

						//
						List<IMChemical> educts = imReaction.getEducts();
						List<IMChemical> products = imReaction.getProducts();

						List<String> rawEducts = new ArrayList<>();
						List<String> rawProducts = new ArrayList<>();
						String equation = keggReactionOfEnzyme.getDefinition();
						String[] eductsAndProducts = StringUtils.split(equation, "<=>");
						if (eductsAndProducts.length == 2)
						{
							String[] splittedEducts = StringUtils.splitByWholeSeparator(eductsAndProducts[0], " + ");
							String[] splittedProducts = StringUtils.splitByWholeSeparator(eductsAndProducts[1], " + ");

							for (String rawEduct : splittedEducts)
							{
								rawEducts.add(StringUtils.trim(rawEduct));
							}
							for (String rawProduct : splittedProducts)
							{
								rawProducts.add(StringUtils.trim(rawProduct));
							}
						}
						else
						{
							LOG.warn("Droped reaction: " + JSONHelper.prettyPrint(keggReactionOfEnzyme));
						}

						educts.addAll(this.convertToIMChemical(rawEducts));
						products.addAll(this.convertToIMChemical(rawProducts));
					}
					imEnzyme.getReactions()
							.add(imReaction);

				}
			}
			enzymes.add(imEnzyme);
		}

		return enzymes;
	}

	private List<KeggEnzyme> getEnzymes()
	{
		return KeggUtils.getEnzymes()
						.collect(Collectors.toList());
	}

	private KeggReaction collectReaction(String reactionId)
	{
		return KeggUtils.getReaction(reactionId);
	}

	private Set<String> collectReactionIds()
	{
		return KeggUtils.getReactionIds();
	}

	private List<IMChemical> convertToIMChemical(List<String> rawChemicals)
	{
		return rawChemicals	.stream()
							.flatMap(new ChemicalMultiplierPrefixMapper())
							.filter((rawChemical) -> StringUtils.isNotBlank(rawChemical))
							.map((rawChemical) -> new IMChemical()	.setName(rawChemical)
																	.setId(rawChemical))
							.collect(Collectors.toList());
	}

}
