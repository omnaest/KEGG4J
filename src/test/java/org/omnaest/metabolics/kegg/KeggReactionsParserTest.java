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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.omnaest.metabolics.kegg.intermodel.IMChemical;
import org.omnaest.metabolics.kegg.intermodel.IMEnzyme;
import org.omnaest.metabolics.kegg.intermodel.IMReaction;
import org.omnaest.metabolics.kegg.model.KeggReaction;
import org.omnaest.metabolics.kegg.utils.JSONHelper;
import org.omnaest.metabolics.kegg.utils.filecache.TextListWithFileCache;
import org.omnaest.metabolics.kegg.wrapper.IMChemicalTemplateWrapper;
import org.omnaest.pandora.kegg.KeggReactionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeggReactionsParserTest
{
    private static Logger LOG = LoggerFactory.getLogger(KeggReactionsParserTest.class);

    public static class ChemicalMultiplierPrefixMapper implements Function<String, Stream<String>>
    {
        @Override
        public Stream<String> apply(String chemical)
        {
            List<String> retlist = new ArrayList<>();
            boolean hasMultiplierPrefix = Pattern.compile("^[0-9]+ ")
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

    private KeggReactionParser parser = new KeggReactionParser();

    @Test
    @Ignore
    public void testFileCache()
    {
        List<String> elements = new TextListWithFileCache("reactions.txt").setProvider(() -> Arrays.asList("a", "b", "cd"))
                                                                          .get();
        System.out.println(elements);
    }

    @Test
    @Ignore
    public void testIMReactionTemplateChemicalsParsing()
    {
        List<IMChemical> chemicals = this.convertToIMChemical(Arrays.asList(StringUtils.splitByWholeSeparator("Hydrogen peroxide + NADH + H+", " + ")));
        for (IMChemical chemical : chemicals)
        {
            IMChemicalTemplateWrapper chemicalTemplateWrapper = new IMChemicalTemplateWrapper(chemical);
            String compatibleName = chemicalTemplateWrapper.getEnumCompatibleName();
            System.out.println(compatibleName);
        }
    }

    @Test
    @Ignore
    public void testCodeGeneration() throws IOException
    {
        List<KeggReaction> reactions = new ArrayList<>();
        {
            Set<String> reactionIds = this.collectReactionIds();
            for (String reactionId : reactionIds.stream()
                                                // .limit(100)
                                                .collect(Collectors.toList()))
            {
                KeggReaction keggReaction = this.collectReaction(reactionId);
                System.out.println(JSONHelper.prettyPrint(keggReaction));

                reactions.add(keggReaction);
            }
        }

        //
        SortedMap<String, Set<KeggReaction>> enzymeToReactionsMap = new TreeMap<>();
        for (KeggReaction reaction : reactions)
        {
            Set<String> enzymes = reaction.getEnzymes()
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
        List<IMEnzyme> enzymes = new ArrayList<>();
        for (String enzymeName : enzymeToReactionsMap.keySet())
        {
            IMEnzyme imEnzyme = new IMEnzyme();
            {
                //
                imEnzyme.setId(enzymeName);
                imEnzyme.setEcNumber(enzymeName);

                //
                Set<KeggReaction> keggReactionsOfEnzyme = enzymeToReactionsMap.get(enzymeName);
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

    }

    private List<IMChemical> convertToIMChemical(List<String> rawChemicals)
    {
        return rawChemicals.stream()
                           .flatMap(new ChemicalMultiplierPrefixMapper())
                           .filter((rawChemical) -> StringUtils.isNotBlank(rawChemical))
                           .map((rawChemical) -> new IMChemical().setName(rawChemical)
                                                                 .setId(rawChemical))
                           .collect(Collectors.toList());
    }

    @Test
    @Ignore
    public void test()
    {
        Set<String> reactionIds = this.collectReactionIds();
        for (String reactionId : reactionIds)
        {
            KeggReaction keggReaction = this.collectReaction(reactionId);
            System.out.println(JSONHelper.prettyPrint(keggReaction));
        }

    }

    @Test
    @Ignore
    public void testDB()
    {
        List<IMEnzyme> enzymes = this.parser.parse();
        System.out.println(JSONHelper.prettyPrint(enzymes.stream()
                                                         .limit(10)
                                                         .collect(Collectors.toList())));

    }

    private KeggReaction collectReaction(String reactionId)
    {
        return KeggUtils.getReaction(reactionId);
    }

    private Set<String> collectReactionIds()
    {

        return KeggUtils.getReactionIds();
    }

}
