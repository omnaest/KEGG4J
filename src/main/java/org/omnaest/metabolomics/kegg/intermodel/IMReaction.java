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
package org.omnaest.metabolomics.kegg.intermodel;

import java.util.ArrayList;
import java.util.List;

public class IMReaction
{
    private String           id;
    private boolean          reversible = false;
    private List<IMChemical> educts     = new ArrayList<>();
    private List<IMChemical> products   = new ArrayList<>();

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isReversible()
    {
        return this.reversible;
    }

    public void setReversible(boolean reversible)
    {
        this.reversible = reversible;
    }

    public List<IMChemical> getEducts()
    {
        return this.educts;
    }

    public void setEducts(List<IMChemical> educts)
    {
        this.educts = educts;
    }

    public List<IMChemical> getProducts()
    {
        return this.products;
    }

    public void setProducts(List<IMChemical> products)
    {
        this.products = products;
    }

}
