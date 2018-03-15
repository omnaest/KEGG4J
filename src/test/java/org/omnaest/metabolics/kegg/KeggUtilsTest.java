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

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.omnaest.metabolics.kegg.KeggUtils.OrganismReference;
import org.omnaest.metabolics.kegg.KeggUtils.ReactionIdToReversibleMap;
import org.omnaest.metabolics.kegg.model.KeggEnzyme;
import org.omnaest.metabolics.kegg.utils.JSONHelper;
import org.omnaest.utils.CollectorUtils;

public class KeggUtilsTest
{

    @Test
    @Ignore
    public void testGetEnzyme() throws Exception
    {
        KeggEnzyme enzyme = KeggUtils.getEnzyme("6.2.1.1");

        System.out.println(enzyme);
    }

    @Test
    @Ignore
    public void testGetEnzymeIds() throws Exception
    {
        System.out.println(KeggUtils.getEnzymeIds());
    }

    @Test
    @Ignore
    public void testLoadAllEnzymes() throws Exception
    {
        KeggUtils.getEnzymeIds()
                 .stream()
                 .parallel()
                 .limit(10)
                 .map(enzymeId -> KeggUtils.getEnzyme(enzymeId))
                 .peek(System.out::println)
                 .count();
    }

    @Test
    @Ignore
    public void testLoadAllReactions() throws Exception
    {
        KeggUtils.getReactions()
                 .peek(reaction -> System.out.println(JSONHelper.prettyPrint(reaction)))
                 .count();
    }

    @Test
    @Ignore
    public void testLoadAllChemicals() throws Exception
    {
        KeggUtils.getChemicalCompounds()
                 .peek(compound -> System.out.println(JSONHelper.prettyPrint(compound)))
                 .count();
    }

    @Test
    @Ignore
    public void testGetOrganisms() throws Exception
    {
        Set<String> organisms = KeggUtils.getOrganisms()
                                         .stream()
                                         .filter(organism -> organism.getGroups()
                                                                     .contains("Saccharomycetes"))
                                         .map(organism -> organism.getId())
                                         .collect(Collectors.toSet());
        System.out.println(JSONHelper.prettyPrint(organisms));
    }

    @Test
    @Ignore
    public void testGetOrganisms2() throws Exception
    {
        Set<OrganismReference> organisms = KeggUtils.getOrganisms()
                                                    .stream()
                                                    //                                         .map(organism -> organism.getId())
                                                    .collect(Collectors.toSet());
        System.out.println(JSONHelper.prettyPrint(organisms));
    }

    @Test
    @Ignore
    public void testEnzymesOfSaccharomycetes() throws Exception
    {
        Set<String> organisms = KeggUtils.getOrganisms()
                                         .stream()
                                         .filter(organism -> organism.getGroups()
                                                                     .contains("Saccharomycetes"))
                                         .map(organism -> organism.getId())
                                         .collect(Collectors.toSet());
        List<KeggEnzyme> enzymesOfFungi = KeggUtils.getEnzymeIds()
                                                   .stream()
                                                   .map(enzymeId -> KeggUtils.getEnzyme(enzymeId))
                                                   .filter(enzyme -> enzyme.getGenes()
                                                                           .stream()
                                                                           .anyMatch(gene -> organisms.contains(StringUtils.lowerCase(gene.getOrganism()))))
                                                   .collect(Collectors.toList());

        enzymesOfFungi.stream()
                      .map(enzyme -> enzyme.getId())
                      .map(id -> ".withEnzymes(Enzymes.EC_" + id.replaceAll("[\\.]", "_") + ")")
                      .collect(Collectors.toList())
                      .forEach(System.out::println);

    }

    @Test
    @Ignore
    public void testGetEnzyme2721() throws Exception
    {
        KeggEnzyme enzyme = KeggUtils.getEnzyme("2.7.2.1");

        //        System.out.println(enzyme);

        assertTrue(enzyme.getGenes()
                         .stream()
                         .anyMatch(gene -> gene.getOrganism()
                                               .equals("FPR")));
    }

    @Test
    @Ignore
    public void testEnzymesOfFaecalibacterium() throws Exception
    {
        Set<String> organisms = KeggUtils.getOrganisms()
                                         .stream()
                                         .filter(organism -> organism.getGroups()
                                                                     .contains("Faecalibacterium"))
                                         .map(organism -> organism.getId())
                                         .map(String::toLowerCase)
                                         .collect(Collectors.toSet());
        System.out.println(organisms);
        List<String> enzymesOfFaecalibacterium = KeggUtils.getEnzymeIds()
                                                          .stream()
                                                          .map(enzymeId -> KeggUtils.getEnzyme(enzymeId))
                                                          .peek(enzyme ->
                                                          {
                                                              if (enzyme.getId()
                                                                        .equals("2.7.2.1"))
                                                              {
                                                                  System.out.println(enzyme);
                                                              }
                                                          })
                                                          .filter(enzyme -> enzyme.getGenes()
                                                                                  .stream()
                                                                                  .anyMatch(gene -> organisms.contains(StringUtils.lowerCase(gene.getOrganism()))))
                                                          .map(enzyme -> enzyme.getId())
                                                          .collect(Collectors.toList());

        System.out.println(enzymesOfFaecalibacterium);

    }

    @Test
    @Ignore
    public void testGetReactionIdToReversibleMap() throws Exception
    {
        ReactionIdToReversibleMap reversibleMap = KeggUtils.getReactionIdToReversibleMap();

        System.out.println(JSONHelper.prettyPrint(reversibleMap.entrySet()
                                                               .stream()
                                                               .collect(CollectorUtils.appendToMap(new TreeMap<>()))));
    }

}
