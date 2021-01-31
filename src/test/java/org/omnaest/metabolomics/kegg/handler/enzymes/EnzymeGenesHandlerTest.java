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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.omnaest.metabolomics.kegg.handler.enzymes.EnzymeGenesHandler;
import org.omnaest.metabolomics.kegg.model.KeggEnzyme;

public class EnzymeGenesHandlerTest
{

	@Test
	public void testHandle() throws Exception
	{
		KeggEnzyme keggEnzyme = new KeggEnzyme();
		new EnzymeGenesHandler(keggEnzyme).handle("HSA: 55902(ACSS2) 84532(ACSS1)");

		assertEquals(2, keggEnzyme	.getGenes()
									.size());
		assertEquals("HSA", keggEnzyme	.getGenes()
										.get(0)
										.getOrganism());
		assertEquals("ACSS2", keggEnzyme.getGenes()
										.get(0)
										.getGene());
		assertEquals("55902", keggEnzyme.getGenes()
										.get(0)
										.getLocation());
		assertEquals("HSA", keggEnzyme	.getGenes()
										.get(1)
										.getOrganism());
		assertEquals("ACSS1", keggEnzyme.getGenes()
										.get(1)
										.getGene());
		assertEquals("84532", keggEnzyme.getGenes()
										.get(1)
										.getLocation());
	}

}
