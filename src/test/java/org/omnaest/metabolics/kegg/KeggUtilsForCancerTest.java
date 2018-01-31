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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.omnaest.utils.csv.CSVUtils;

public class KeggUtilsForCancerTest
{

	@Test
	public void testGetData() throws Exception
	{
		List<Map<String, String>> data = CSVUtils	.parse(	new File("C:\\Z\\databases\\icgc\\DO8264\\exp_seq.tsv"), CSVFormat.TDF.withFirstRecordAsHeader(),
															StandardCharsets.UTF_8)
													.collect(Collectors.toList());

		double average = data	.stream()
								.map(row -> row.get("raw_read_count"))
								.mapToInt(countStr -> NumberUtils.toInt(countStr))
								.average()
								.getAsDouble();

		double averageFactor = 4;
		Set<String> genes = data.stream()
								.filter(row -> NumberUtils.toInt(row.get("raw_read_count")) > averageFactor * average)
								.map(row -> row.get("gene_id"))
								.collect(Collectors.toSet());

		String organism = "hsa";
		Set<String> targetGenes = genes;
		Map<String, String> enzymeIds = KeggUtils	.getEnzymes()
													.filter(enzyme -> enzyme.getGenes()
																			.stream()
																			.anyMatch(gene -> StringUtils.equalsIgnoreCase(organism, gene.getOrganism())
																					&& targetGenes.contains(gene.getGene())))
													.collect(Collectors.toMap(enzyme -> enzyme.getId(), enzyme -> enzyme.getGenes()
																														.stream()
																														.map(gene -> gene.getGene())
																														.filter(gene -> genes.contains(gene))
																														.findAny()
																														.get()));
		System.out.println(enzymeIds.keySet()
									.stream()
									.map(id -> "\"" + id + "\"")
									.collect(Collectors.joining(",")));

		Map<String, Double> geneToExpression = data	.stream()
													.collect(Collectors.toMap(	row -> row.get("gene_id"),
																				row -> NumberUtils.toInt(row.get("raw_read_count")) / average, (g1, g2) -> g1));

		enzymeIds	.entrySet()
					.stream()
					.forEach(entry ->
					{
						System.out.println(".put(\"" + entry.getKey() + "\"," + geneToExpression.get(entry.getValue()) + ")");
					});
	}

}
