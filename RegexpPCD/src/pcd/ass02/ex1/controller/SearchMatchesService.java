package pcd.ass02.ex1.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.view.RegexpView;

public class SearchMatchesService extends Thread {
	
	private final Path startPath;
	private final Pattern pattern;
	private final int maxDepth;
	private final BlockingQueue<SearchFileResult> queue;
	private final RegexpView view;
	
	public SearchMatchesService(final Path startPath, final Pattern pattern, final int maxDepth, final RegexpView view) {
		this.startPath = startPath;
		this.pattern = pattern;
		this.maxDepth = maxDepth;
		this.queue = new LinkedBlockingQueue<>();
		this.view = view;
	}
	
	@Override
	public void run() {
		final List<File> files = new ArrayList<>();
		try {
			Files.walkFileTree(this.startPath, Collections.emptySet(), this.maxDepth, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
					if(!attrs.isDirectory()) {
						files.add(path.toFile());
					}
					return FileVisitResult.CONTINUE;
			      }
			});
		} catch (IOException e) {
			// notify view
		}
		new Consumer(this.queue, this.view, files.size()).start();
		new Master(files, this.pattern, this.queue).compute();
	}

}
