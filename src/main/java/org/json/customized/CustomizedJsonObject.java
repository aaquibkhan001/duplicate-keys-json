package org.json.customized;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomizedJsonObject extends JSONObject {

	public CustomizedJsonObject(CustomizedJsonTokener x) throws JSONException {
		{
			if (x.nextClean() != '{') {
				throw x.syntaxError("A JSONObject text must begin with '{'");
			} else {
				while (true) {
					// NOTE: since we cannot access getPrevious(), we are using next(). later the logic will be handled accordingly
					char prev = x.next();
					char c = x.nextClean();
					switch (c) {
					case '\u0000':
						throw x.syntaxError("A JSONObject text must end with '}'");
					case '[':
					case '{':
						if (prev == '{') {
							throw x.syntaxError(
									"A JSON Object can not directly nest another JSON Object or JSON Array.");
						}
						continue;
					case '}':
						return;
					default:
						x.back();
						String key = x.nextValue().toString();
						// NOTE: doing the nextClean() twice as we have considered the previous as next() above
						x.nextClean();
						c = x.nextClean();
						if (c != ':') {
							throw x.syntaxError("Expected a ':' after a key");
						}

						if (key != null) {
							handleMapUpdateWithCustomizedList(x, key);
						}

						switch (x.nextClean()) {
						case ',', ';' -> {
							if (x.nextClean() == '}') {
								return;
							}
							x.back();
						}
						case '}' -> {
							return;
						}
						default -> throw x.syntaxError("Expected a ',' or '}'");
						}
					}
				}
			}
		}
	}

	private void handleMapUpdateWithCustomizedList(CustomizedJsonTokener x, String key) {
		if (this.opt(key) != null) {
			JSONArray listObjects;
			if (!(this.opt(key) instanceof JSONArray)) {
				listObjects = new JSONArray();
				listObjects.put(this.opt(key));
			} else {
				listObjects = ((JSONArray) this.opt(key));
			}
			this.put(key, listObjects);
		}

		Object value = x.nextValue();
		if (value != null) {
			if (this.opt(key) != null) {
				var storedType = this.opt(key);
				if (storedType instanceof JSONArray) {
					value = ((JSONArray) storedType).put(value);
				}
			}
			this.put(key, value);
		}
	}

}

