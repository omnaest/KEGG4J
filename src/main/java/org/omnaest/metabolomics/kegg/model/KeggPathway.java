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
package org.omnaest.metabolomics.kegg.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pathway")
@XmlAccessorType(XmlAccessType.FIELD)
public class KeggPathway
{
    @XmlAttribute
    private String name;

    @XmlAttribute(name = "org")
    private String organism;

    @XmlAttribute(name = "number")
    private String id;

    @XmlAttribute
    private String title;

    @XmlElement(name = "entry")
    private List<Object> entries = new ArrayList<>();

    @XmlElement(name = "relation")
    private List<Object> relations = new ArrayList<>();

    @XmlElement(name = "reaction")
    private List<Reaction> reactions = new ArrayList<>();

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Reaction
    {
        @XmlAttribute
        private String id;
        @XmlAttribute
        private String name;
        @XmlAttribute
        private String type;

        @XmlElement
        private Compound substrate;
        private Compound product;

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class Compound
        {
            @XmlAttribute
            private String id;

            @XmlAttribute
            private String name;

            public String getId()
            {
                return this.id;
            }

            public String getName()
            {
                return this.name;
            }

            @Override
            public String toString()
            {
                return "Compound [id=" + this.id + ", name=" + this.name + "]";
            }

        }

        public String getId()
        {
            return this.id;
        }

        public String getName()
        {
            return this.name;
        }

        public String getType()
        {
            return this.type;
        }

        public Compound getSubstrate()
        {
            return this.substrate;
        }

        public Compound getProduct()
        {
            return this.product;
        }

        @Override
        public String toString()
        {
            return "Reaction [id=" + this.id + ", name=" + this.name + ", type=" + this.type + ", substrate=" + this.substrate + ", product=" + this.product
                    + "]";
        }

    }

    public String getName()
    {
        return this.name;
    }

    public String getOrganism()
    {
        return this.organism;
    }

    public String getId()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public List<Object> getEntries()
    {
        return this.entries;
    }

    public List<Object> getRelations()
    {
        return this.relations;
    }

    public List<Reaction> getReactions()
    {
        return this.reactions;
    }

    @Override
    public String toString()
    {
        return "KeggPathway [name=" + this.name + ", organism=" + this.organism + ", id=" + this.id + ", title=" + this.title + ", entries=" + this.entries
                + ", relations=" + this.relations + ", reactions=" + this.reactions + "]";
    }

}
