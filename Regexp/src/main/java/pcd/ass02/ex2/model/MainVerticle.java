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
    	this.view.setTotalFilesToScan(999);
    	analyzeFile(this.startingPath.toString(), this.maxDepth, handler -> {
    		if (handler.failed()) {
    			//this.view.showThreadException(handler.cause().getMessage(), null);
    			handler.cause().printStackTrace();
    		} else {
    			
    		}
    		this.view.setFinish();
    	});
    }
    
    private void analyzeFile(final String pathDir, final int depth, final Handler<AsyncResult<Void>> handler) {

    	final Future<Void> future = Future.<Void>future().setHandler(handler);
    	final FileSystem fs = vertx.fileSystem();
    	final String startingPath = this.startingPath.toString();
    	OpenOptions options = new OpenOptions();
    	options.setWrite(false);
    	options.setRead(true);
    	
    	
    	//Check if file/folder exists
    	/*fs.exists(startingPath, existsHandler -> {
    		if (existsHandler.failed()) {
    			future.fail(existsHandler.cause());
    		} else {			 	
    			
    			//Check if it's a folder or file
    			fs.lprops(startingPath, propsHandler -> {
    				if (propsHandler.failed()) {
    					future.fail(propsHandler.cause());
    				} else {
    					  			
    					System.out.println(pathDir);
    					System.out.print("Directory: " + propsHandler.result().isDirectory());
    					System.out.print(" | Regular file: " + propsHandler.result().isRegularFile());
    					System.out.print(" | Symbolic: " + propsHandler.result().isSymbolicLink());
    					System.out.print(" | Other: " + propsHandler.result().isOther());
    					System.out.println("");
    					
    					//It's a folder!					
	    				if (propsHandler.result().isDirectory()) {
	    					//Read subfolders and files only if I depth is not 0
	    					if (depth > 0) {
	    						fs.readDir(pathDir, readDirHandler -> {
	    							if (readDirHandler.failed()) {
	    								future.fail(readDirHandler.cause());
	    							} else {
	    								
	    								//Foreach folder or file I have here
	    								final List<String> pathFiles = readDirHandler.result();
	    								for (final String s : pathFiles) {
	    									analyzeFile(s, depth-1, handler);
	    								}
	    							}
	    						});
	    					}
    					}
    					
    					//It's a file -> must read!
    					if (propsHandler.result().isRegularFile()) {
    						fs.open(startingPath, new OpenOptions(), openHandler -> {
    							if (openHandler.failed()) {
    								future.fail(openHandler.cause());
    							} else {
    								
    								//Reading file
    								fs.readFile(startingPath, readHandler -> {
    									if (readHandler.failed()) {
        									//Oh no, failed to read
    										future.fail(readHandler.cause());
    										this.view.showResult(startingPath, readHandler.cause().getMessage());
    									} else {
    										//Yes, I made it!
    										final Buffer buffer = readHandler.result();
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
    								        this.view.showResult(startingPath, nMatches, 0);
    								        // Shows statistics on view
    										this.view.showMeanNumberOfMatches(this.meanNumberOfMatches);
    										this.view.showLeastOneMatchPercentage((double)this.nLeastOneMatch / (double)nComputedFiles);
    										// Shows analysis progress on view
    										this.view.setNumberOfScannedFiles(nComputedFiles);
    									}
    								});
    							}
    						});
    					}
    				}
    			});
    		}
    	});
    }*/
    	
    	
    	//Check if exists
    	fs.exists(startingPath, existsHandler -> {
    		if (existsHandler.failed()) {
    			System.out.println("Not exist " + startingPath);
    			future.fail(existsHandler.cause());
    		} else {
    			
    			//Try to read the directory
				fs.readDir(startingPath, readDirHandler -> {
    				if (readDirHandler.failed()) {
    					future.fail(readDirHandler.cause());
    				} else {
    					
    					//Get the list of files and folders
	    				final List<String> pathFiles = readDirHandler.result();
	    				
	    				//Foreach path check if it's a file or folder
	    				for (final String pathFile : pathFiles) {
	    					fs.lprops(pathFile, propsHandler -> {
	    						if (propsHandler.failed()) {
	    							future.fail(propsHandler.cause());
	    						} else {
	    							
	    							//Get the result
	    							final FileProps props = propsHandler.result();
	    							
	    							if (props.isDirectory()) {
		    							//Is a directory?
	    								analyzeFile(pathFile, depth-1, handler);
	    							} else {
	    								//Is a file?	    								
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
	    								        this.view.showResult(pathFile, nMatches, 0);
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
    	    				//}
    	    			//});
    				}
    			});
    		}
    	});
    }
    }
    

