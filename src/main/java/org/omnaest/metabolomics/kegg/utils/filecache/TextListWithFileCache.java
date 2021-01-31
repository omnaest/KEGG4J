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
package org.omnaest.metabolomics.kegg.utils.filecache;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.omnaest.metabolomics.kegg.utils.filecache.TextWithFileCache.TextListProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextListWithFileCache extends AbstractFileCache<List<String>>
{
	private static Logger		LOG			= LoggerFactory.getLogger(TextListWithFileCache.class);
	private static final String	ENCODING	= "utf-8";

	public TextListWithFileCache(File file)
	{
		super(file);
	}

	public TextListWithFileCache(String fileName)
	{
		super(fileName);
	}

	public TextListWithFileCache setProvider(TextListProvider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected List<String> readElementFromFile()
	{
		List<String> retval = null;
		if (this.file != null && this.file.exists() && this.file.isFile())
		{
			try
			{
				List<String> lines = FileUtils.readLines(this.file, ENCODING);
				retval = lines;

			} catch (IOException e)
			{
				LOG.error("", e);
			}
		}
		return retval;
	}

	@Override
	protected void writeElementToFile(List<String> list)
	{
		try
		{
			FileUtils.writeLines(this.file, ENCODING, list);
		} catch (IOException e)
		{
			LOG.error("", e);
		}

	}
}
