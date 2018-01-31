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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.omnaest.metabolics.kegg.model.KeggPathway;
import org.omnaest.metabolics.kegg.utils.RestHelper;
import org.omnaest.utils.XMLHelper;

/**
 * @see KeggUtils
 * @author Omnaest
 */
public class KeggRestApiUtils
{
    private static String baseUrl = "http://rest.kegg.jp/";

    public static String getReaction(String reactionId)
    {
        return RestHelper.requestGet(baseUrl + "get/rn:" + reactionId);
    }

    public static String getChemicalCompound(String compoundId)
    {
        return RestHelper.requestGet(baseUrl + "get/cpd:" + compoundId);
    }

    public static List<String> getReactionsList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/reaction"), "\r\n"));
    }

    public static String getEnzyme(String enzymeId)
    {
        return RestHelper.requestGet(baseUrl + "get/ec:" + enzymeId);
    }

    public static List<String> getEnzymesList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/enzyme"), "\r\n"));
    }

    /**
     * Special case of {@link #getGenesList(String)} with "hsa" as identifier
     *
     * @return
     */
    public static List<String> getHumanGenesList()
    {
        return getGenesList("hsa");
    }

    /**
     * Returns the gene list for a given genome id. For humans the genome id is "hsa"
     *
     * @see #getHumanGenesList()
     * @param genomeId
     * @return
     */
    public static List<String> getGenesList(String genomeId)
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/" + genomeId), "\r\n"));
    }

    public static List<String> getGenomeList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/genome"), "\r\n"));
    }

    public static List<String> getOrganismList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/organism"), "\r\n"));
    }

    public static List<String> getChemicalCompoundList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/compound"), "\r\n"));
    }

    public static List<String> getDrugList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/drugs"), "\r\n"));
    }

    public static List<String> getDrugGroupList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/dgroup"), "\r\n"));
    }

    public static List<String> getGlycanList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/glycan"), "\r\n"));
    }

    public static List<String> getDiseaseList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/disease"), "\r\n"));
    }

    public static List<String> getEnvironmentList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/environ"), "\r\n"));
    }

    public static List<String> getOrthologyList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/orthology"), "\r\n"));
    }

    public static List<String> getBriteList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/brite"), "\r\n"));
    }

    public static List<String> getModuleList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/module"), "\r\n"));
    }

    public static List<String> getPathwayList()
    {
        return Arrays.asList(StringUtils.split(RestHelper.requestGet(baseUrl + "list/pathway"), "\r\n"));
    }

    public static KeggPathway getPathway(String pathwayId)
    {
        return XMLHelper.parse(RestHelper.requestGet(baseUrl + "get/" + pathwayId + "/kgml"), KeggPathway.class);
    }
}
