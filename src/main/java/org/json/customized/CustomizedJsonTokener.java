package org.json.customized;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/***
 *  Customizing the tokener which will assist in providing the next char values and assemble each property name
 *
 */
public class CustomizedJsonTokener extends JSONTokener {

	public CustomizedJsonTokener(String s) {
		super(s);
	}

	@Override
	public Object nextValue() throws JSONException {
		char c = this.nextClean();
		switch (c) {
		case '\"':
		case '\'':
			return this.nextString(c);
		case '[':
			this.back();
			return new JSONArray(this);
		case '{':
			this.back();
			// return our own customized object here which will handle duplication
			return new CustomizedJsonObject(this);
		default:
			StringBuffer sb;
			for (sb = new StringBuffer(); c >= 32 && ",:]}/\\\"[{;=#".indexOf(c) < 0; c = this.next()) {
				sb.append(c);
			}

			this.back();
			String string = sb.toString().trim();
			if ("".equals(string)) {
				throw this.syntaxError("Missing value");
			} else {
				return JSONObject.stringToValue(string);
			}
		}
	}

}
