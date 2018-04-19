package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Document;
import model.SearchFileResult;

public class SearchMatchesInFileTask implements Runnable {

	private final File file;
	private final Pattern pattern;
	private final BlockingQueue<SearchFileResult> queue;
	
	public SearchMatchesInFileTask(final File file, final Pattern pattern, final BlockingQueue<SearchFileResult> queue) {
		this.file = file;
		this.pattern = pattern;
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try {
			final Document document = new Document(this.file);
			final Matcher matcher = this.pattern.matcher(document.getContent());
			int nMatches = 0;
	        while (matcher.find())
	        	nMatches++;
			this.queue.add(new SearchFileResult(this.file.toPath(), nMatches));
		} catch (FileNotFoundException e) {
			// TODO
		}
	}

}
