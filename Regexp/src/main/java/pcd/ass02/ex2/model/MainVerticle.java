package pcd.ass02.ex2.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import pcd.ass02.ex1.view.RegexpView;

public class MainVerticle extends AbstractVerticle {

	private final RegexpView view;
	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private int nLeastOneMatch;
	private double meanNumberOfMatches;
	private int nComputedFiles;
	
	public MainVerticle(final RegexpView view, final Path startingPath, final Pattern pattern, final int maxDepth) {
		Objects.requireNonNull(view);
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		this.view = view;
		this.startingPath = startingPath;
		this.pattern = pattern;
		this.maxDepth = maxDepth;
		this.nLeastOneMatch = 0;
		this.meanNumberOfMatches = 0;
		this.nComputedFiles = 0;
	}
	
    @Override
    public void start(final Future<Void> done) throws Exception {
    	analyzeFile(this.startingPath.toString(), this.maxDepth, handler -> {
    		if (handler.failed()) {
    			
    		} else {
    			
    		}
    	});
    }
    
    private void analyzeFile(final String pathDir, final int depth, final Handler<AsyncResult<Void>> handler) {
    	final Future<Void> future = Future.<Void>future().setHandler(handler);
    	final FileSystem fs = vertx.fileSystem();
    	final String startingPath = this.startingPath.toString();
    	fs.exists(startingPath, existsHandler -> {
    		if (existsHandler.failed()) {
    			// view notify existsHandler.cause()
    			future.fail(existsHandler.cause());
    		} else {
    			fs.open(startingPath, new OpenOptions(), openHandler -> {
    				if (openHandler.failed()) {
    					// view notify openHandler.cause()
    					future.fail(openHandler.cause());
    				} else {
    					fs.readDir(startingPath, readDirHandler -> {
    	    				if (readDirHandler.failed()) {
    	    					// view notify readHandler.cause()
    	    					future.fail(readDirHandler.cause());
    	    				} else {
        	    				final List<String> pathFiles = readDirHandler.result();
        	    				for (final String pathFile : pathFiles) {
        	    					fs.lprops(pathFile, propsHandler -> {
        	    						if (propsHandler.failed()) {
        	    							// view notify propsHandler.cause()
        	    							future.fail(propsHandler.cause());
        	    						} else {
        	    							final FileProps props = propsHandler.result();
        	    							if (props.isDirectory()) {
        	    								// call recursive
        	    							} else {
        	    								fs.readFile(pathFile, readFileHandler -> {
        	    									if (readFileHandler.failed()) {
        	    										future.fail(readFileHandler.cause());
        	    										this.view.showResult(pathFile, readFileHandler.cause().getMessage());
        	    									} else {
        	    										final Buffer buffer = readFileHandler.result();
        	    										final Matcher matcher = this.pattern.matcher(buffer.toString());
        	    										int nMatches = 0;
        	    								        while (matcher.find())
        	    								        	nMatches++;
        	    								        if (nMatches > 0) {
        	    								        	// Updates the number of files with least one match
        	    											this.nLeastOneMatch++;
        	    											// Updates the mean number of matches among files with matches
        	    											double tmp = this.meanNumberOfMatches;
        	    											this.meanNumberOfMatches += (nMatches - tmp) / this.nLeastOneMatch;
        	    										}
        	    								        this.view.showResult(pathFile, nMatches);
        	    								        // Shows statistics on view
        	    										this.view.showMeanNumberOfMatches(this.meanNumberOfMatches);
        	    										this.view.showLeastOneMatchPercentage((double)this.nLeastOneMatch / (double)nComputedFiles);
        	    										// Shows analysis progress on view
        	    										this.view.setNumberOfScannedFiles(nComputedFiles);
        	    									}
        	    								});
        	    							}
        	    						}
        	    					});
        	    				}
    	    				}
    	    			});
    				}
    			});
    		}
    	});
    }
    
}
