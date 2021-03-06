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
package org.omnaest.metabolomics.kegg.handler.enzymes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolomics.kegg.model.KeggEnzyme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnzymeGenesHandler extends AbstractEnzymeHandler
{
    private final static Logger LOG = LoggerFactory.getLogger(EnzymeGenesHandler.class);

    public EnzymeGenesHandler(KeggEnzyme keggEnzyme)
    {
        super(keggEnzyme);
    }

    @Override
    public String getEventKey()
    {
        return "GENES";
    }

    @Override
    public void handle(String line)
    {
        if (StringUtils.isNotBlank(line))
        {
            String cleanedLine = StringUtils.trim(line);

            if (StringUtils.isNotBlank(cleanedLine))
            {
                //HSA: 55902(ACSS2) 84532(ACSS1)
                //FGR: FGSG_11925
                String[] organismAndGenesWithLoci = StringUtils.split(cleanedLine, ":");

                if (organismAndGenesWithLoci.length != 2)
                {
                    LOG.warn("Incorrect gene format for enzyme: " + this.keggEnzyme.getId() + ":" + this.keggEnzyme.getName() + " " + cleanedLine);
                }
                else
                {
                    String organism = organismAndGenesWithLoci[0];
                    String genesWithLoci = organismAndGenesWithLoci[1];

                    if (StringUtils.contains(genesWithLoci, "("))
                    {
                        String lociPattern = "([0-9_a-zA-Z\\.]+)";
                        String genePattern = "\\(([0-9a-zA-Z]*)\\)";
                        Matcher matcher = Pattern.compile(lociPattern + genePattern)
                                                 .matcher(genesWithLoci);
                        while (matcher.find())
                        {
                            String location = matcher.group(1);
                            String gene = matcher.group(2);
                            this.keggEnzyme.addGene(organism, gene, location);
                            this.keggEnzyme.addOrganism(organism);
                        }
                    }
                    else if (StringUtils.isNotBlank(organism))
                    {
                        this.keggEnzyme.addOrganism(organism);
                    }
                    else
                    {
                        LOG.warn("Missing organism: " + this.keggEnzyme.getId() + ":" + this.keggEnzyme.getName() + " " + cleanedLine);
                    }
                }
            }
        }
    }
}
