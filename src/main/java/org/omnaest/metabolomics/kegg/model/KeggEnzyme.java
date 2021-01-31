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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omnaest.metabolomics.kegg.utils.JSONHelper;

public class KeggEnzyme
{
    private String                id;                                                                                                                                                                                                																																				 //e.g.: 6.2.1.1
    private String                name;
    private List<String>          synonoms       = new ArrayList<>();
    private List<String>          classes        = new ArrayList<>();
    private String                systemicName;
    private String                reaction;
    private List<String>          reactions      = new ArrayList<>();
    private List<String>          otherReactions = new ArrayList<>();
    private List<String>          substrates     = new ArrayList<>();
    private List<String>          products       = new ArrayList<>();
    private String                comment;
    private String                history;
    private List<KeggPublication> publications   = new ArrayList<>();
    private List<String>          pathway        = new ArrayList<>();
    private String                othology;
    private List<GeneReference>   genes          = new ArrayList<>();
    private Set<String>           organisms      = new HashSet<>();
    private List<String>          dbLinks        = new ArrayList<>();

    public static class GeneReference
    {
        private String organism;
        private String gene;
        private String location;

        public GeneReference(String organism, String gene, String location)
        {
            super();
            this.organism = organism;
            this.gene = gene;
            this.location = location;
        }

        public String getOrganism()
        {
            return this.organism;
        }

        public String getGene()
        {
            return this.gene;
        }

        public String getLocation()
        {
            return this.location;
        }

    }

    public String getId()
    {
        return this.id;
    }

    public KeggEnzyme setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getSynonoms()
    {
        return this.synonoms;
    }

    public void setSynonoms(List<String> synonoms)
    {
        this.synonoms = synonoms;
    }

    public List<String> getClasses()
    {
        return this.classes;
    }

    public void setClasses(List<String> classes)
    {
        this.classes = classes;
    }

    public String getSystemicName()
    {
        return this.systemicName;
    }

    public void setSystemicName(String systemicName)
    {
        this.systemicName = systemicName;
    }

    public String getReaction()
    {
        return this.reaction;
    }

    public void setReaction(String reaction)
    {
        this.reaction = reaction;
    }

    public List<String> getReactions()
    {
        return this.reactions;
    }

    public void setReactions(List<String> reactions)
    {
        this.reactions = reactions;
    }

    public List<String> getOtherReactions()
    {
        return this.otherReactions;
    }

    public void setOtherReactions(List<String> otherReactions)
    {
        this.otherReactions = otherReactions;
    }

    public List<String> getSubstrates()
    {
        return this.substrates;
    }

    public void setSubstrates(List<String> substrates)
    {
        this.substrates = substrates;
    }

    public List<String> getProducts()
    {
        return this.products;
    }

    public void setProducts(List<String> products)
    {
        this.products = products;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getHistory()
    {
        return this.history;
    }

    public void setHistory(String history)
    {
        this.history = history;
    }

    public List<KeggPublication> getPublications()
    {
        return this.publications;
    }

    public void setPublications(List<KeggPublication> publications)
    {
        this.publications = publications;
    }

    public List<String> getPathway()
    {
        return this.pathway;
    }

    public void setPathway(List<String> pathway)
    {
        this.pathway = pathway;
    }

    public String getOthology()
    {
        return this.othology;
    }

    public void setOthology(String othology)
    {
        this.othology = othology;
    }

    public List<GeneReference> getGenes()
    {
        return this.genes;
    }

    public void setGenes(List<GeneReference> genes)
    {
        this.genes = genes;
    }

    public List<String> getDbLinks()
    {
        return this.dbLinks;
    }

    public void setDbLinks(List<String> dbLinks)
    {
        this.dbLinks = dbLinks;
    }

    public boolean hasName()
    {
        return this.name != null;
    }

    public void addSynonym(String synonym)
    {
        this.synonoms.add(synonym);
    }

    public void addGene(String organism, String gene, String location)
    {
        this.genes.add(new GeneReference(organism, gene, location));
    }

    public void addOrganism(String organism)
    {
        this.organisms.add(organism);
    }

    public Set<String> getOrganisms()
    {
        return this.organisms;
    }

    @Override
    public String toString()
    {
        return JSONHelper.prettyPrint(this);
    }

}
