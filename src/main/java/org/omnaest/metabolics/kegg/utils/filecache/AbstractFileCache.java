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

public abstract class AbstractFileCache<T>
{
	protected File			file;
	protected Provider<T>	provider;

	public static interface Provider<E>
	{
		public E get();
	}

	public AbstractFileCache(String fileName)
	{
		this(new File(fileName));
	}

	public AbstractFileCache(File file)
	{
		super();
		this.file = file;
	}

	public T get()
	{
		T retval = this.readElementFromFile();
		if (retval == null)
		{
			retval = this.provider.get();
			this.writeElementToFile(retval);
		}
		return retval;
	}

	protected abstract void writeElementToFile(T element);

	protected abstract T readElementFromFile();

	protected AbstractFileCache<T> setProvider(Provider<T> provider)
	{
		this.provider = provider;
		return this;
	}

}
