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
package org.omnaest.metabolics.kegg.utils.filecache;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextWithFileCache extends AbstractFileCache<String>
{
	private static final String	ENCODING	= "utf-8";
	private static Logger		LOG			= LoggerFactory.getLogger(TextWithFileCache.class);

	public static interface TextProvider extends Provider<String>
	{
	}

	public static interface TextListProvider extends Provider<List<String>>
	{
	}

	public TextWithFileCache(String fileName)
	{
		super(fileName);
	}

	public TextWithFileCache(File file)
	{
		super(file);
		this.file = file;
	}

	public TextWithFileCache setProvider(TextProvider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected String readElementFromFile()
	{
		String retval = null;
		if (this.file != null && this.file.exists() && this.file.isFile())
		{
			try
			{
				String content = FileUtils.readFileToString(this.file, ENCODING);
				retval = content;
			} catch (IOException e)
			{
				LOG.error("", e);
			}
		}
		return retval;
	}

	@Override
	protected void writeElementToFile(String element)
	{
		try
		{
			FileUtils.writeStringToFile(this.file, element, ENCODING);
		} catch (IOException e)
		{
			LOG.error("", e);
		}

	}

}
