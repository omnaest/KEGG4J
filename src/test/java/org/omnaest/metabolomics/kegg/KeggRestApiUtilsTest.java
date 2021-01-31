package org.omnaest.metabolomics.kegg;

import org.junit.Ignore;
import org.junit.Test;
import org.omnaest.metabolomics.kegg.KeggRestApiUtils;
import org.omnaest.metabolomics.kegg.model.KeggPathway;
import org.omnaest.utils.JSONHelper;

public class KeggRestApiUtilsTest
{

    @Test
    @Ignore
    public void testGetPathway() throws Exception
    {
        KeggPathway pathway = KeggRestApiUtils.getPathway("ec00380" /* "hsa00760" */);
        System.out.println(JSONHelper.prettyPrint(pathway));
    }

}