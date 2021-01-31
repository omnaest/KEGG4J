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
package org.omnaest.metabolomics.kegg;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolomics.kegg.handler.TextHandler;
import org.omnaest.metabolomics.kegg.handler.compounds.CompoundNameHandler;
import org.omnaest.metabolomics.kegg.handler.enzymes.EnzymeGenesHandler;
import org.omnaest.metabolomics.kegg.handler.enzymes.EnzymeNameHandler;
import org.omnaest.metabolomics.kegg.handler.reactions.ReactionClassesHandler;
import org.omnaest.metabolomics.kegg.handler.reactions.ReactionDefinitionHandler;
import org.omnaest.metabolomics.kegg.handler.reactions.ReactionEntryHandler;
import org.omnaest.metabolomics.kegg.handler.reactions.ReactionEnzymeHandler;
import org.omnaest.metabolomics.kegg.handler.reactions.ReactionEquationHandler;
import org.omnaest.metabolomics.kegg.handler.reactions.ReactionNameHandler;
import org.omnaest.metabolomics.kegg.handler.reactions.ReactionOrthologyHandler;
import org.omnaest.metabolomics.kegg.model.KeggCompound;
import org.omnaest.metabolomics.kegg.model.KeggEnzyme;
import org.omnaest.metabolomics.kegg.model.KeggReaction;
import org.omnaest.metabolomics.kegg.utils.filecache.TextListWithFileCache;
import org.omnaest.metabolomics.kegg.utils.filecache.TextWithFileCache;
import org.omnaest.utils.CollectorUtils;
import org.omnaest.utils.JSONHelper;
import org.omnaest.utils.MatcherUtils;
import org.omnaest.utils.PredicateUtils;
import org.omnaest.utils.element.bi.BiElement;
import org.omnaest.utils.element.cached.CachedElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see KeggRestApiUtils
 * @author Omnaest
 */
public class KeggUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(KeggUtils.class);

    public static String CACHE_FOLDER = "kegg_data";

    public static KeggReaction getReaction(String reactionId)
    {
        String reactionText = new TextWithFileCache(CACHE_FOLDER + "/reactions/reaction" + reactionId
                + ".txt").setProvider(() -> KeggRestApiUtils.getReaction(reactionId))
                         .get();
        KeggReaction keggReaction = new KeggReaction();
        TextHandler textHandler = new TextHandler(new ReactionEntryHandler(keggReaction), new ReactionNameHandler(keggReaction),
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
                + "/reactions/reactions.txt").setProvider(() -> KeggRestApiUtils.getReactionsList())
                                             .get();
        for (String reactionLine : reactionLinesList)
        {
            Matcher matcher = Pattern.compile("rn\\:([A-Za-z0-9]+)")
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
                + ".txt").setProvider(() -> KeggRestApiUtils.getEnzyme(enzymeId))
                         .get();
        KeggEnzyme keggEnzyme = new KeggEnzyme().setId(enzymeId);
        TextHandler textHandler = new TextHandler(new EnzymeNameHandler(keggEnzyme), new EnzymeGenesHandler(keggEnzyme));
        textHandler.handle(reactionText);

        return keggEnzyme;
    }

    public static Set<String> getEnzymeIds()
    {
        Set<String> enzymeIds = new LinkedHashSet<>();
        List<String> reactionLinesList = new TextListWithFileCache(CACHE_FOLDER + "/enzymes/enzymes.txt").setProvider(() -> KeggRestApiUtils.getEnzymesList())
                                                                                                         .get();
        for (String enzymeLine : reactionLinesList)
        {
            Matcher matcher = Pattern.compile("ec\\:([A-Za-z0-9\\.]+)")
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
        private String       id;
        private String       name;
        private List<String> groups;

        public OrganismReference(String id, String name, List<String> groups)
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

        public List<String> getGroups()
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
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (this.getClass() != obj.getClass())
            {
                return false;
            }
            OrganismReference other = (OrganismReference) obj;
            if (this.id == null)
            {
                if (other.id != null)
                {
                    return false;
                }
            }
            else if (!this.id.equals(other.id))
            {
                return false;
            }
            return true;
        }

    }

    public static Set<OrganismReference> getOrganisms()
    {
        Set<OrganismReference> organisms = new LinkedHashSet<>();
        List<String> lines = new TextListWithFileCache(CACHE_FOLDER + "/organisms.txt").setProvider(() -> KeggRestApiUtils.getOrganismList())
                                                                                       .get();

        for (String line : lines)
        {
            //T01001	hsa	Homo sapiens (human)	Eukaryotes;Animals;Vertebrates;Mammals
            Matcher matcher = Pattern.compile(".*\t(.+)\t(.*)\t(.*)")
                                     .matcher(line);
            if (matcher.find())
            {
                String id = matcher.group(1);
                String name = matcher.group(2);
                String joinedGroups = matcher.group(3);
                if (StringUtils.isNotBlank(id))
                {
                    List<String> groups = Arrays.asList(StringUtils.split(joinedGroups, ";"))
                                                .stream()
                                                .distinct()
                                                .collect(Collectors.toList());
                    organisms.add(new OrganismReference(id, name, groups));
                }
            }
        }
        return organisms;
    }

    public static Stream<KeggEnzyme> getEnzymes()
    {
        return getEnzymeIds().parallelStream()
                             .map(enzymeId -> getEnzyme(enzymeId));
    }

    public static Stream<KeggReaction> getReactions()
    {
        return getReactionIds().parallelStream()
                               .map(reactionId -> getReaction(reactionId));
    }

    public static class ChemicalCompoundReference
    {
        private String       id;
        private List<String> names;

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
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (this.getClass() != obj.getClass())
            {
                return false;
            }
            ChemicalCompoundReference other = (ChemicalCompoundReference) obj;
            if (this.id == null)
            {
                if (other.id != null)
                {
                    return false;
                }
            }
            else if (!this.id.equals(other.id))
            {
                return false;
            }
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
        List<String> lines = new TextListWithFileCache(CACHE_FOLDER + "/compounds/compounds.txt").setProvider(() -> KeggRestApiUtils.getChemicalCompoundList())
                                                                                                 .get();

        for (String line : lines)
        {
            //cpd:C00002	ATP; Adenosine 5'-triphosphate
            Matcher matcher = Pattern.compile("cpd\\:(.+)\t(.*)")
                                     .matcher(line);
            if (matcher.find())
            {
                String id = matcher.group(1);
                String joinedNames = matcher.group(2);

                if (StringUtils.isNotBlank(id))
                {
                    Set<String> names = Arrays.asList(StringUtils.split(joinedNames, ";"))
                                              .stream()
                                              .collect(Collectors.toSet());
                    compounds.add(new ChemicalCompoundReference(id, names.stream()
                                                                         .collect(Collectors.toList())));
                }
            }
        }
        return compounds;
    }

    public static KeggCompound getChemicalCompound(String compoundId)
    {
        String text = new TextWithFileCache(CACHE_FOLDER + "/compounds/compound" + compoundId
                + ".txt").setProvider(() -> KeggRestApiUtils.getChemicalCompound(compoundId))
                         .get();
        KeggCompound keggCompound = new KeggCompound().setId(compoundId);
        TextHandler textHandler = new TextHandler(new CompoundNameHandler(keggCompound));
        textHandler.handle(text);

        return keggCompound;
    }

    public static Stream<KeggCompound> getChemicalCompounds()
    {
        return getChemicalCompoundIds().parallelStream()
                                       .map(compound -> compound.getId())
                                       .map(compoundId -> getChemicalCompound(compoundId));
    }

    public static class ReactionIdToReversibleMap
    {
        @JsonProperty
        private Map<String, Boolean> map = new HashMap<>();

        public ReactionIdToReversibleMap()
        {
            super();
        }

        public ReactionIdToReversibleMap(Map<String, Boolean> map)
        {
            super();
            this.map.putAll(map);
        }

        public int size()
        {
            return this.map.size();
        }

        @JsonIgnore
        public boolean isEmpty()
        {
            return this.map.isEmpty();
        }

        public Boolean get(Object key)
        {
            return this.map.get(key);
        }

        public Set<String> keySet()
        {
            return this.map.keySet();
        }

        public Set<Entry<String, Boolean>> entrySet()
        {
            return this.map.entrySet();
        }

        public Boolean getOrDefault(Object key, Boolean defaultValue)
        {
            return this.map.getOrDefault(key, defaultValue);
        }

    }

    public static ReactionIdToReversibleMap getReactionIdToReversibleMap()
    {
        CachedElement<ReactionIdToReversibleMap> cache = CachedElement.of(() ->
        {
            Map<String, Boolean> map = KeggRestApiUtils.getPathwayList()
                                                       .stream()
                                                       .map(entry -> MatcherUtils.matcher()
                                                                                 .of(Pattern.compile("path\\:([0-9a-zA-Z]+)\\s+.*"))
                                                                                 .matchAgainst(entry)
                                                                                 .get()
                                                                                 .getGroups()
                                                                                 .get(1))
                                                       .filter(PredicateUtils.notNull())
                                                       .map(pathwayId -> pathwayId.replaceAll("^map", "ec"))
                                                       .peek(pathwayId -> LOG.info("Loading pathway: " + pathwayId))
                                                       .map(pathwayId -> KeggRestApiUtils.getPathway(pathwayId))
                                                       .filter(PredicateUtils.notNull())
                                                       .flatMap(pathway -> pathway.getReactions()
                                                                                  .stream()
                                                                                  .map(reaction -> BiElement.of(reaction.getName(), reaction.getType())))
                                                       .flatMap(reactionsAndType ->
                                                       {
                                                           Boolean reversible = StringUtils.equalsIgnoreCase("reversible", reactionsAndType.getSecond());
                                                           return org.omnaest.utils.StringUtils.splitToStream(reactionsAndType.getFirst(), " ")
                                                                                               .filter(PredicateUtils.notNull())
                                                                                               .map(reactionStr -> reactionStr.replaceAll("^rn\\:", "")
                                                                                                                              .trim())
                                                                                               .map(reactionId -> BiElement.of(reactionId, reversible));

                                                       })
                                                       .distinct()
                                                       .collect(CollectorUtils.toMapByBiElement((r1, r2) -> r1 || r2));
            return new ReactionIdToReversibleMap(map);
        })
                                                                      .withFileCache(new File(CACHE_FOLDER + "/reactionToReversible.json"),
                                                                                     JSONHelper.serializer(),
                                                                                     JSONHelper.deserializer(ReactionIdToReversibleMap.class));

        return cache.get();
    }

}
