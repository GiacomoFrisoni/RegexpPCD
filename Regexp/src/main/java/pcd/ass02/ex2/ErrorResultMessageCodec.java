package pcd.ass02.ex2;

import com.google.gson.Gson;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import pcd.ass02.ex1.model.SearchFileErrorResult;

/**
 * This class defines a custom message codec in order to send error results
 * objects across the event bus.
 *
 */
public class ErrorResultMessageCodec implements MessageCodec<SearchFileErrorResult, SearchFileErrorResult> {

	private static final int INT_BYTES_DIMENSION = 4;

	@Override
	public void encodeToWire(final Buffer buffer, final SearchFileErrorResult result) {
		// Encodes object to string
		final String jsonString = new Gson().toJson(result);

		// Length of JSON: is NOT characters count
		int length = jsonString.getBytes().length;

		// Writes data into given buffer
		buffer.appendInt(length);
		buffer.appendString(jsonString);
	}

	@Override
	public SearchFileErrorResult decodeFromWire(int position, final Buffer buffer) {
		// Length of JSON
		final int length = buffer.getInt(position);

		// Decodes object from JSON
		final SearchFileErrorResult result = new Gson().fromJson(
				buffer.getString(position += INT_BYTES_DIMENSION, position += length),
				SearchFileErrorResult.class);

		return result;
	}

	@Override
	public SearchFileErrorResult transform(final SearchFileErrorResult result) {
		return result;
	}

	@Override
	public String name() {
		/*
		 * Each codec must have a unique name. This is used to identify a codec when
		 * sending a message and for unregistering codecs.
		 */
		return this.getClass().getSimpleName();
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}
