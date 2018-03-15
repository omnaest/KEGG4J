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
package org.omnaest.metabolics.kegg.intermodel;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class IMEnzyme
{
    private String          id;
    private String          name;
    private Set<String>     synonyms  = Collections.emptySet();
    private String          ecNumber;
    private Set<IMReaction> reactions = new LinkedHashSet<>();
    private List<IMGene>    genes     = Collections.emptyList();
    private Set<IMOrganism> organisms = new LinkedHashSet<>();

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<String> getSynonyms()
    {
        return this.synonyms;
    }

    public void setSynonyms(Set<String> synonyms)
    {
        this.synonyms = synonyms;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEcNumber()
    {
        return this.ecNumber;
    }

    public void setEcNumber(String ecNumber)
    {
        this.ecNumber = ecNumber;
    }

    public Set<IMReaction> getReactions()
    {
        return this.reactions;
    }

    public void setReactions(Set<IMReaction> reactions)
    {
        this.reactions = reactions;
    }

    public List<IMGene> getGenes()
    {
        return this.genes;
    }

    public void setGenes(List<IMGene> genes)
    {
        this.genes = genes;
    }

    public Set<IMOrganism> getOrganisms()
    {
        return this.organisms;
    }

    public void setOrganisms(Set<IMOrganism> organisms)
    {
        this.organisms = organisms;
    }

}
