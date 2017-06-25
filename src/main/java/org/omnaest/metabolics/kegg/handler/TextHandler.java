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
package org.omnaest.metabolics.kegg.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class TextHandler
{
	private List<Handler> handlers;

	public TextHandler(Handler... handlers)
	{
		super();
		this.handlers = Arrays.asList(handlers);
	}

	public static interface Handler
	{
		public String getEventKey();

		public void handle(String line);
	}

	public void handle(String text)
	{
		//
		Map<String, Handler> eventKeyToHandlerMap = generateEventKeyToHandlerMap(this.handlers);

		//
		String[] lines = StringUtils.split(text, "\r\n");
		String currentKey = null;
		for (String line : lines)
		{
			if (StringUtils.startsWith(line, " "))
			{
				String[] keyAndBody = StringUtils.split(line, " ", 1);
				if (keyAndBody.length >= 1)
				{
					handleValue(eventKeyToHandlerMap, currentKey, keyAndBody[0]);
				}
			} else if (StringUtils.startsWith(line, "///"))
			{
				break;
			} else
			{
				String[] keyAndBody = StringUtils.split(line, " ", 2);
				if (keyAndBody.length >= 1)
				{
					currentKey = keyAndBody[0];
				}
				if (keyAndBody.length >= 2)
				{
					handleValue(eventKeyToHandlerMap, currentKey, keyAndBody[1]);
				}
			}
		}
	}

	private static void handleValue(Map<String, Handler> eventKeyToHandlerMap, String currentKey, String body)
	{
		if (eventKeyToHandlerMap.containsKey(currentKey))
		{
			Handler handler = eventKeyToHandlerMap.get(currentKey);
			handler.handle(body);
		}
	}

	private static Map<String, Handler> generateEventKeyToHandlerMap(Collection<Handler> handlers)
	{
		Map<String, Handler> retmap = new HashMap<>();
		for (Handler handler : handlers)
		{
			retmap.put(handler.getEventKey(), handler);
		}
		return retmap;
	}
}
