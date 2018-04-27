package pcd.ass02.ex2;

import com.google.gson.Gson;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import pcd.ass02.ex1.model.SearchFileSuccessfulResult;

public class SuccessfulResultMessageCodec implements MessageCodec<SearchFileSuccessfulResult, SearchFileSuccessfulResult> {

	private static final int INT_BYTES_DIMENSION = 4;

	@Override
	public void encodeToWire(final Buffer buffer, final SearchFileSuccessfulResult result) {
		// Encodes object to string
		final String jsonString = new Gson().toJson(result);
		
		// Length of JSON: is NOT characters count
	    int length = jsonString.getBytes().length;

	    // Writes data into given buffer
	    buffer.appendInt(length);
	    buffer.appendString(jsonString);
	}

	@Override
	public SearchFileSuccessfulResult decodeFromWire(int position, final Buffer buffer) {
	    // Length of JSON
	    final int length = buffer.getInt(position);

	    // Decodes object from JSON
	    final SearchFileSuccessfulResult result = new Gson().fromJson(
	    		buffer.getString(position+=INT_BYTES_DIMENSION, position+=length),
	    		SearchFileSuccessfulResult.class);
	    
	   return result;
	}

	@Override
	public SearchFileSuccessfulResult transform(final SearchFileSuccessfulResult result) {
		return result;
	}

	@Override
	public String name() {
		/*
		 * Each codec must have a unique name.
		 * This is used to identify a codec when sending a message and for unregistering codecs.
		 */
	    return this.getClass().getSimpleName();
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}
